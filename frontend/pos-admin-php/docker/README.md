# Docker used by sail
## What are the changes?
This is the copy of `vendor/laravel/sail/runtime/8.1` with modified
Dockerfile which supports Apple M1 architecture and updated `docker-compose`. 
The changes are:
- using image `ubuntu:21.10` - which contains php8 libraries
- replace `php8.0-*` libraries with `php-` images since ubuntu package repository contains
packages with generic names
- `docker-compose.yml` points to `docker` directory instead of `vendor/laravel/sail/runtime/8.1`

## How to run?
From root directory:
- To run `./vendon/bin/sail up`
- To build `./vendon/bin/sail build`
