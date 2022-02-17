<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;

class AdminDashboardController extends Controller
{
    private $apiClient;

    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }

    public function index() {
        $dashboardInfo = $this->apiClient->getDashboardInfo();
        return view('admin_dashboard', ['info' => $dashboardInfo]);
    }
}
