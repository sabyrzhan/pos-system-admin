<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
use Illuminate\Support\Facades\Http;

class APIClientProvider extends ServiceProvider
{
    private $client;
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
        $this->client = Http::baseUrl('http://localhost:8080');
    }

    public function authenticate($username, $password) {
        $response = Http::post('/api/v1/users/validate', [
            'username' => $username,
            'password' => $password,
        ]);

        var_dump($response);
        exit;
    }
}
