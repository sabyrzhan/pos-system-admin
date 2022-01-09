-- Create database
create database pos_system;

-- Create tables
create table pos_users
(
    id serial,
    username varchar(200) not null,
    email varchar(200) not null,
    password varchar(200) not null,
    salt varchar(200) not null,
    role varchar(200) not null
);

create unique index pos_users_id_uindex
    on pos_users (id);

alter table pos_users
    add constraint pos_users_pk
        primary key (id);
--


