<?php

use App\Http\Controllers\Api\CategoriesController;
use App\Http\Controllers\Api\OrdersController;
use App\Http\Controllers\Api\ProductsController;
use App\Http\Controllers\Api\UsersController;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware(['auth:api'])->group(function() {
    Route::delete('/users/{userId}', [UsersController::class, 'deleteUser'])->middleware('authRole:ADMIN');
    Route::delete('/categories/{catId}', [CategoriesController::class, 'deleteCategory'])->middleware('authRole:ADMIN');
    Route::delete('/products/{id}', [ProductsController::class, 'deleteProduct'])->middleware('authRole:ADMIN');
    Route::delete('/orders/{id}', [OrdersController::class, 'cancelOrder']);
});
