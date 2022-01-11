<?php

namespace App\Http\Controllers;

class CategoriesController extends Controller
{
    public function categoriesPage() {
        return view('categories', ['categories' => []]);
    }

    public function addCategory() {
        return view('categories');
    }
}
