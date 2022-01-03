<?php

namespace App\Http\Controllers;

use App\Providers\APIClientProvider;

class UsersController extends Controller
{
    private $apiClient;

    /**
     * @param $apiClient
     */
    public function __construct(APIClientProvider $apiClient)
    {
        $this->apiClient = $apiClient;
    }


    public function addUserPage() {
        return view('add_user');
    }

    public function changePasswordPage() {
        return view('changepassword');
    }

    public function changePassword() {
        $data = request(['oldPassword', 'newPassword', 'newPassword', 'newPasswordConfirm']);
        $isPasswordCorrect = $this->apiClient->authenticate(auth()->user()['username'], $data['oldPassword']);
        if (!$isPasswordCorrect) {
            return redirect()->route('changePasswordPage')->with('error', 'Old password is invalid');
        }

        $newPassword = $data['newPassword'];
        $newPasswordConfirm = $data['newPasswordConfirm'];
        if (trim($newPassword) == '' || $newPassword != $newPasswordConfirm) {
            return redirect()->route('changePasswordPage')->with('error', "New passwords don't match");
        }

        $isUpdated = $this->apiClient->changePassword(auth()->user()['id'], $newPassword);
        if (!$isUpdated) {
            return redirect() -> route('changePasswordPage')->with('error', 'Error while updating password. Please try again');
        }

        return redirect()->route('changePasswordPage')->with('success', 'Password has been updated successfully!');
    }
}
