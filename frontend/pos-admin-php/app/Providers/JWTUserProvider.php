<?php

namespace App\Providers;

use App\Models\User;
use Illuminate\Contracts\Auth\Authenticatable;
use Illuminate\Contracts\Auth\UserProvider;
use Illuminate\Support\ServiceProvider;

class JWTUserProvider extends ServiceProvider implements UserProvider
{
    private $apiClient;

    public function __construct($app, APIClientProvider $apiClient)
    {
        parent::__construct($app);
        $this->apiClient = $apiClient;
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

    public function retrieveById($identifier)
    {
        $payload = auth()->payload();
        $user = new User();
        $user['id'] = $payload['sub'];
        $user['username'] = $payload['username'];
        $user['role'] = $payload['role'];
        return $user;
    }

    public function retrieveByToken($identifier, $token)
    {
        // TODO: Implement retrieveByToken() method.
    }

    public function updateRememberToken(Authenticatable $user, $token)
    {
        // TODO: Implement updateRememberToken() method.
    }

    public function retrieveByCredentials(array $credentials)
    {
        $response = $this->apiClient->authenticate($credentials['email'], $credentials['password']);
        if (!$response) {
            return NULL;
        }

        $user = new User();
        $user['id'] = $response['id'];
        $user['username'] = $response['username'];
        $user['role'] = $response['role'];

        return $user;
    }

    public function validateCredentials(Authenticatable $user, array $credentials)
    {
        return true;
    }


}
