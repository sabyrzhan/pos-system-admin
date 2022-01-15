<?php

namespace App\Http\Controllers;

class ProductsController extends Controller
{
    public function addProductPage() {
        $categories = [];
        return view('addproduct', ['categories' => $categories]);
    }
}
