<?php

use App\Http\Controllers\AdminDashboardController;
use App\Http\Controllers\AuthController;
use App\Http\Controllers\DashboardRouterController;
use App\Http\Controllers\UserDashboardController;
use App\Http\Controllers\CategoriesController;
use App\Http\Controllers\UsersController;
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
Route::get('logout', [AuthController::class, 'logout']);

Route::middleware([Authenticate::class])->group(function() {
    Route::get('/', [DashboardRouterController::class, 'index'])->name('home');
    Route::get('/admin_dashboard', [AdminDashboardController::class, 'index'])->middleware('authRole:ADMIN');
    Route::get('/user_dashboard', [UserDashboardController::class, 'index'])->middleware('authRole:USER');
    Route::get('/categories', [CategoriesController::class, 'categoriesPage'])->name('categories');
    Route::get('/users/add', [UsersController::class, 'addUserPage'])->name('add_user_page');
    Route::post('/users/add', [UsersController::class, 'addUser'])->name('add_user');
    Route::get('/change_password', [UsersController::class, 'changePasswordPage'])->name('changePasswordPage');
    Route::post('/change_password', [UsersController::class, 'changePassword']);
});
