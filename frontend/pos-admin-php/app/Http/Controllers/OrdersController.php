<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;
use DateTime;

class OrdersController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function addOrderPage() {
        $copyFromId = request('copyFrom');
        $otherOrder = null;
        if ($copyFromId) {
            $otherOrder = $this->apiClient->getOrderById($copyFromId);
            if ($otherOrder) {
                $otherOrder['created'] = $this->formatDate($otherOrder['created']);
            }
        }
        $products = $this->apiClient->getProducts();
        return view('createorder', ['products' => $products, 'otherOrder' => $otherOrder]);
    }

    public function getOrdersPage() {
        $page = request('page');
        if (!$page) {
            $page = 1;
        }
        $orders = $this->apiClient->getOrders($page);
        return view('orders', ['orders' => $orders]);
    }

    public function getOrderDetails($orderId) {
        $order = $this->apiClient->getOrderById($orderId);
        if (!$order) {
            return redirect()->route('get_orders_page')->with('error', 'Order not found!');
        }

        $order['created'] = $this->formatDate($order['created']);
        $products = $this->apiClient->getProducts();
        $paymentTypes = $this->apiClient->getPaymentTypes();

        return view('vieworder', ['products' => $products, 'order' => $order, 'paymentTypes' => $paymentTypes]);
    }

    public function addOrder() {
        $order = request(['customerName', 'subtotal', 'tax', 'discount', 'total', 'paid', 'due', 'paymentType', 'created']);
        $orderItems = $this->prepareItems(request(['productId', 'quantity']));
        if (!$orderItems) {
            return redirect()->route('add_order_page')->with('error', 'Products to order were not specified!');
        }
        $order['items'] = $orderItems;
        $order = $this->cleanOrderRequest($order);

        $response = $this->apiClient->createOrder($order);

        $routeParams = [];
        $copyFromId = request('copyFrom');
        if ($copyFromId) {
            $routeParams['copyFrom'] = $copyFromId;
        }

        if (!$response) {
            return redirect()->route('add_order_page', $routeParams)->with('error', 'Internal Server Error. Please try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('add_order_page', $routeParams)->with('error', $response['error']);
        }

        return redirect()->route('get_orders_page')->with('success', 'Order created successfully');
    }

    public function generateInvoice($orderId) {
        $type = request('type');
        if (!$type) {
            $type = 'STANDARD';
        }
        $invoice = $this->apiClient->generateInvoice($orderId, $type);
        if (!$invoice) {
            return redirect()->route('get_orders_page')->with('error', 'Error while generating invoice. Please try again!');
        }

        if (isset($invoice['url'])) {
            return redirect($invoice['url']);
        } else {
            return redirect()->route('get_orders_page')->with('error', 'Error while trying to show invoice. Did not receive invoice URL.');
        }
    }

    private function cleanOrderRequest($requestOrder) {
        foreach($requestOrder as $key => $value) {
            if ($value == null) {
                unset($requestOrder[$key]);
            }
        }
        foreach ($requestOrder['items'] as $key => $value) {
            if ($value == null) {
                unset($requestOrder['items'][$key]);
            }
        }

        if (isset($requestOrder['created'])) {
            $requestOrder['created'] = DateTime::createFromFormat("d.m.Y", $requestOrder['created'])->format('c');
        }

        return $requestOrder;
    }

    private function prepareItems($requestItems) {
        $result = array();
        $productId = [];
        if (isset($requestItems['productId']) && count($requestItems['productId']) > 0) {
            $productId = $requestItems['productId'];
        }
        $quantity = [];
        if (isset($requestItems['quantity']) && count($requestItems['quantity']) > 0) {
            $quantity = $requestItems['quantity'];
        }

        if (count($productId) == 0 || count($productId) != count($quantity)) {
            return false;
        }

        for($i = 0; $i < count($productId); $i++) {
            $result[] = array('productId' => $productId[$i], 'quantity' => $quantity[$i]);
        }

        return $result;
    }

    private function formatDate($utcDate) : String {
        // First try parsing UTC time with microseconds (example: 2022-02-17T18:58:18.564245Z)
        $result = DateTime::createFromFormat("Y-m-d\TH:i:s.uZ", $utcDate);
        if (!$result) {
            // Second try parsing UTC time with seconds (example: 2022-02-17T22:47:42Z)
            $result = DateTime::createFromFormat("Y-m-d\TH:i:sZ", $utcDate);
        }

        return $result->format('d.m.Y');
    }
}
