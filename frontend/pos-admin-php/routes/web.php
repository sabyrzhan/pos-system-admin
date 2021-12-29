<?php

use App\Http\Controllers\AdminDashboardController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\DashboardRouterController;
use App\Http\Controllers\UserDashboardController;
use App\Http\Middleware\Authenticate;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('login', [AuthController::class, 'loginPage'])->name('login');
Route::post('login', [AuthController::class, 'authenticate']);

Route::middleware([Authenticate::class])->group(function() {
    Route::get('/', [DashboardRouterController::class, 'index']);
    Route::get('/admin_dashboard', [AdminDashboardController::class, 'index']);
    Route::get('/user_dashboard', [UserDashboardController::class, 'index']);
});
