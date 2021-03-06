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
            return redirect()->route('add_product_page', ['id' => $id])->with('error', 'Error adding/updating category. Try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('add_product_page', ['id' => $id])->with('error', $response['error']);
        }

        if ($id) {
            $message = 'Product updated!';
        } else {
            $message = 'Product added';
        }

        return redirect()->route('add_product_page', ['id' => $id])->with('success', $message);
    }

    public function getProductsPage() {
        $page = request('page');
        if (!$page) {
            $page = 1;
        }
        $products = $this->apiClient->getProducts($page);
        return view('productlist', ['products' => $products]);
    }

    public function viewProduct($productId) {
        $product = $this->apiClient->getProduct($productId);
        if (!$product) {
            return redirect()->route('products_page')->with('error', 'Product not found');
        }
        return view('viewproduct', ['product' => $product]);
    }
}
