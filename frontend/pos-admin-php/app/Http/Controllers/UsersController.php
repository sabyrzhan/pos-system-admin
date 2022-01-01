<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class UsersController extends Controller
{
    public function addUserPage() {
        return view('add_user');
    }
}
