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
        if (!$page) {
            $page = 1;
        }
        $categories = $this->apiClient->getCategories($page);
        return view('categories', ['categories' => $categories]);
    }

    public function addCategory() {
        $params = request(['name']);
        $response = $this->apiClient->addCategory($params);
        if (!$response) {
            return redirect()->route('categories_page')->with('error', 'Error adding category. Try again!');
        }

        if (isset($response['error'])) {
            return redirect()->route('categories_page')->with('error', $response['error']);
        }

        return redirect()->route('categories_page')->with('success', 'Category added!');
    }
}
