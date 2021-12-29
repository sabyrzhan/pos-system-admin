<?php

namespace App\Providers;

use Illuminate\Support\ServiceProvider;
use Illuminate\Support\Facades\Http;

class APIClientProvider extends ServiceProvider
{
    private $client;

    public function __construct($app)
    {
        parent::__construct($app);
        $this->client = Http::baseUrl(env('API_BASE_URL'));
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
    }

    public function authenticate($username, $password) {
        $response = $this->client->post('/api/v1/users/validate', [
            'username' => $username,
            'password' => $password,
        ]);

        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }
}
