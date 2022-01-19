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
}
