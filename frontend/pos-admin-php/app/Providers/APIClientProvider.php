<?php

namespace App\Providers;

use Illuminate\Support\Facades\Http;

class APIClientProvider
{
    private $client;

    public function __construct()
    {
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

    public function changePassword($userId, $newPassword) {
        $response = $this->client->post('/api/v1/users/' . $userId . '/change_password', [
            'password' => $newPassword
        ]);

        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getUserRoles() {
        $response = $this->client->get('/api/v1/dict/roles');

        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getUsers($page = 1) {
        $response = $this->client->get("/api/v1/users?page=" . $page);

        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getCategories($page = 1) {
        $response = $this->client->get("/api/v1/dict/categories?page=" . $page);

        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function addUser($params) {
        $response = $this->client->post('/api/v1/users', $params);

        if ($response->status() != 200 && !isset($response['error'])) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function addCategory($params) {
        $response = $this->client->post('/api/v1/dict/categories', $params);

        if ($response->status() != 200 && !isset($response['error'])) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function deleteUser($userId) {
        $response = $this->client->delete('/api/v1/users/' . $userId);
        return $response->status() == 202;
    }
}
