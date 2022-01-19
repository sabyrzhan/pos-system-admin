<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Providers\APIClientProvider;

class ProductsController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function deleteProduct($id) {
        // TODO: Product must be removed if no associated orders exist. Otherwise products will refer to unprocessed orders
        return response()->json([
            'error' => 'Delete product not implemented yet!'
        ], 501);
    }
}
