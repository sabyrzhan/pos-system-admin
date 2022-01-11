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
        return view('categories');
    }
}
