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
        $id = request('id');
        $product = null;
        if ($id) {
            $product = $this->apiClient->getProduct($id);
        }

        $categories = $this->apiClient->getCategories();
        return view('addproduct', ['categories' => $categories, 'product' => $product]);
    }

    public function addUpdateProduct() {
        $id = request('id');
        $image = request('image');
        $params = request(['id', 'name', 'categoryId', 'purchasePrice', 'salePrice', 'stock', 'description']);
        if ($id) {
            $response = $this->apiClient->updateProduct($params);
        } else {
            $response = $this->apiClient->addProduct($params);
        }

        if (!$response) {
            return redirect()->route('add_product_page')->with('error', 'Error adding/updating category. Try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('add_product_page')->with('error', $response['error']);
        }

        if ($id) {
            $message = 'Product updated!';
        } else {
            $message = 'Product added';
        }

        return redirect()->route('add_product_page')->with('success', $message);
    }

    public function getProductsPage() {
        return view('productlist', ['products' => []]);
    }
}
