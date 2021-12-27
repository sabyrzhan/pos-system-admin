<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
use Tymon\JWTAuth\Contracts\Providers\Auth;

class JWTAuthProvider extends ServiceProvider implements Auth
{
    public function __construct($app)
    {
        parent::__construct($app);
    }

    /**
     * Register services.
     *
     * @return void
     */
    public function register()
    {
        //
    }

    /**
     * Bootstrap services.
     *
     * @return void
     */
    public function boot()
    {
        //
    }

    public function byCredentials(array $credentials)
    {
    }

    public function byId($id)
    {
        // TODO: Implement byId() method.
    }

    public function user()
    {
        // TODO: Implement user() method.
    }

}
