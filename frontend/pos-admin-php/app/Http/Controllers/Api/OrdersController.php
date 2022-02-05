<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Providers\APIClientProvider;

class OrdersController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function cancelOrder($id) {
        $response = $this->apiClient->cancelOrder($id);
        if ($response) {
            return response()->json(null, 200);
        } else {
            return response()->json([
                'error' => 'Server error'
            ], 500);
        }
    }
}
