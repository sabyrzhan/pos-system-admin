<?php

namespace App\Http\Controllers;

class OrdersController extends Controller
{
    public function addOrderPage() {
        return view('createorder');
    }

    public function getOrdersPage() {
        return view('orders');
    }
}
