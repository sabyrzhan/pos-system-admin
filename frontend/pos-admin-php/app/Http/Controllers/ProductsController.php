<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;

class ProductsController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function addProductPage() {
        $categories = $this->apiClient->getCategories();
        return view('addproduct', ['categories' => $categories]);
    }
}
