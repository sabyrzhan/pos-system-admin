<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;

class CategoriesController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function categoriesPage() {
        $page = request('page');
        $id = request('id');
        if (!$page) {
            $page = 1;
        }

        $category = null;
        if ($id) {
            $category = $this->apiClient->getCategory($id);
        }

        $categories = $this->apiClient->getCategories($page);
        return view('categories', ['categories' => $categories, 'category' => $category]);
    }

    public function addOrEditCategory() {
        $params = request(['id', 'name']);
        if (isset($params['id'])) {
            $response = $this->apiClient->updateCategory($params);
        } else {
            $response = $this->apiClient->addCategory($params);
        }
        if (!$response) {
            return redirect()->route('categories_page')->with('error', 'Error adding/updating category. Try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('categories_page')->with('error', $response['error']);
        }

        if ($params['id']) {
            $message = 'Category updated!';
        } else {
            $message = 'Category added';
        }

        return redirect()->route('categories_page')->with('success', $message);
    }
}
