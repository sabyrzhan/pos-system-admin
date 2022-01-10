<?php

namespace App\Http\Middleware;

use Closure;

class Authenticate
{
    public function handle($request, Closure $next, $route)
    {
        $user = NULL;
        try {
            $user = auth()->userOrFail();
        } catch (\Tymon\JWTAuth\Exceptions\UserNotDefinedException $e) {
        }

        if (!$user) {
            if ($route == 'api') {
                if (env('APP_ENV') == 'local') {
                    return $next($request);
                } else {
                    return response()->json(['error' => 'Authenticate'], 401);
                }
            } else {
                return redirect('login');
            }
        }

        return $next($request);
    }
}
