<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Providers\APIClientProvider;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Session;

class UsersController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function deleteUser($userId) {
        $response = $this->apiClient->deleteUser($userId);
        if ($response) {
            return response()->json(null, 200);
        } else {
            return response()->json([
                'error' => 'Server error'
            ], 500);
        }
    }
}
