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
        $products = $this->apiClient->getProducts();
        return view('createorder', ['products' => $products]);
    }

    public function getOrdersPage() {
        return view('orders');
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
        if (!$response) {
            return redirect()->route('add_order_page')->with('error', 'Internal Server Error. Please try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('add_order_page')->with('error', $response['error']);
        }

        return redirect()->route('get_orders_page')->with('success', 'Order created successfully');
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
}
