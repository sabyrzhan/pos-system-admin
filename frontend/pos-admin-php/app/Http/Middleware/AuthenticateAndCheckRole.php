<?php

namespace App\Http\Middleware;

use Closure;

class AuthenticateAndCheckRole
{
    public function handle($request, Closure $next, $role)
    {
        $authUserRole = auth()->user()['role'];
        if ($authUserRole != $role) {
            return redirect('/');
        }

        return $next($request);
    }
}
