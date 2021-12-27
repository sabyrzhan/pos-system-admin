<?php

namespace App\Providers;

use App\Models\User;
use Illuminate\Auth\GenericUser;
use Illuminate\Contracts\Auth\Authenticatable;
use Illuminate\Contracts\Auth\UserProvider;
use Illuminate\Support\ServiceProvider;

class JWTUserProvider extends ServiceProvider implements UserProvider
{
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

    public function retrieveById($identifier)
    {
        $user = new User();
        $user['id'] = 2;
        $user['username'] = 'sabyrzhan';
        $user['role'] = 'ADMIN';
        return $user;
    }

    public function retrieveByToken($identifier, $token)
    {
        // TODO: Implement retrieveByToken() method.
        var_dump($token);exit;
    }

    public function updateRememberToken(Authenticatable $user, $token)
    {
        // TODO: Implement updateRememberToken() method.
    }

    public function retrieveByCredentials(array $credentials)
    {
        $user = new User();
        $user['id'] = 1;
        $user['username'] = 'sabyrzhan';
        $user['role'] = 'ADMIN';

        return $user;
    }

    public function validateCredentials(Authenticatable $user, array $credentials)
    {
        return true;
    }


}
