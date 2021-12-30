<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Crypt;

class AuthController extends Controller
{
    public function loginPage() {
        return view('login');
    }

    public function authenticate() {
        $credentials = request(['email', 'password']);
        if (!$token = auth()->attempt($credentials)) {
            return redirect('/login?error=invalid_credentials');
        }

        return redirect('/')->cookie('token', Crypt::encrypt($token));
    }

    public function logout() {
        auth()->logout(true);

        return redirect('/');
    }
}
