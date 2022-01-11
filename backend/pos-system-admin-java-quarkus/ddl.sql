-- Create database
create database pos_system;

-- Create tables
-- Create user table
create table pos_users
(
    id serial constraint pos_users_pk primary key,
    username varchar(200) not null,
    email varchar(200) not null,
    password varchar(200) not null,
    salt varchar(200) not null,
    role varchar(200) not null
);

create unique index pos_users_id_uindex on pos_users (id);

-- Create categories table
create table pos_categories
(
    id serial constraint pos_categories_pk primary key,
    name varchar(255) not null
);

create unique index pos_categories_id_uindex on pos_categories (id);

--

