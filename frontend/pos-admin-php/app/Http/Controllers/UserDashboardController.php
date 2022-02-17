<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;

class UserDashboardController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function index() {
        $dashboardInfo = $this->apiClient->getDashboardInfo();
        return view('user_dashboard', ['info' => $dashboardInfo]);
    }
}
