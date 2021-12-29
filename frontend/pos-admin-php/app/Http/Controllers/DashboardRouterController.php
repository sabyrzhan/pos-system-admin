<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Auth;

class DashboardRouterController extends Controller
{
    public function index() {
        if (Auth::user() && Auth::user()->isAdmin()) {
            return redirect('/admin_dashboard');
        } else {
            return redirect('/user_dashboard');
        }
    }
}
