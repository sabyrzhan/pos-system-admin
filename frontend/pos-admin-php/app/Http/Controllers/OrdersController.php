<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;

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
        $invoice = request(['customerName', 'paid', 'discount', 'paymentType']);
        $invoiceDetailsItems = request(['productId', 'qty']);
        $invoice['items'] = $invoiceDetailsItems;
        $response = $this->$this->apiClient->createOrder($invoice);
        if (!$response) {
            return redirect()->route('add_order_page')->with('error', 'Internal Server Error. Please try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('add_order_page')->with('error', $response['error']);
        }

        return redirect()->route('get_orders_page')->with('success', 'Order created successfully');
    }
}
