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

    public function updateCategory($params) {
        $response = $this->client->put('/api/v1/dict/categories/' . $params['id'], $params);

        if ($response->status() != 200 && !isset($response['error'])) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getCategory($id) {
        $response = $this->client->get('/api/v1/dict/categories/' . $id);

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

    public function getProduct($id) {
        $response = $this->client->get('/api/v1/products/' . $id);

        if ($response->status() != 200) {
            return null;
        } else {
            return $response->json();
        }
    }

    public function getProducts($page = 1) {
        $response = $this->client->get('/api/v1/products?page=' . $page);

        if ($response->status() != 200) {
            return null;
        } else {
            return $response->json();
        }
    }

    public function addProduct($params) {
        $response = $this->client->post('/api/v1/products', $params);
        if ($response->status() != 200 && !isset($response['error'])) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function updateProduct($params) {
        $response = $this->client->put('/api/v1/products/' . $params['id'], $params);
        if ($response->status() != 200 && !isset($response['error'])) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function createOrder($params) {
        $response = $this->client->post('/api/v1/orders', $params);
        if ($response->status() != 200 && !isset($response['error'])) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getOrders($page = 1) {
        $response = $this->client->get('/api/v1/orders?page=' . $page);

        if ($response->status() != 200) {
            return null;
        } else {
            return $response->json();
        }
    }

    public function getOrderById($id) {
        $response = $this->client->get('/api/v1/orders/' . $id);

        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function cancelOrder($id) {
        $response = $this->client->delete('/api/v1/orders/' . $id);
        return $response->status() == 200;
    }

    public function generateInvoice($orderId, $type = 'STANDARD') {
        $response = $this->client->post('/api/v1/orders/' . $orderId . '/invoice?type=' . $type);
        dd($response->json());
        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getPaymentTypes() {
        $response = $this->client->get('/api/v1/dict/paymentTypes');
        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }

    public function getDashboardInfo() {
        $response = $this->client->get('/api/v1/dashboard/info');
        if ($response->status() != 200) {
            return false;
        } else {
            return $response->json();
        }
    }


}
