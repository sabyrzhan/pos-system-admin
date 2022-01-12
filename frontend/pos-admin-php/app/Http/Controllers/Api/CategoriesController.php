<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Providers\APIClientProvider;

class CategoriesController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function deleteCategory($catId) {
        // TODO: Category must be deleted if no associated products exist. Otherwise products will refer to non
        // existing category
        return response()->json([
            'error' => 'Delete category not implemented yet!'
        ], 501);
    }
}
