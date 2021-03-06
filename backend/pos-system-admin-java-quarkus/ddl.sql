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

-- Create categories table
create table pos_categories
(
    id serial constraint pos_categories_pk primary key,
    name varchar(255) not null
);

-- Create products table
create table pos_products
(
    id serial constraint post_products_pk primary key,
    name varchar(100) not null,
    category_id int not null,
    purchase_price float not null,
    sale_price float not null,
    stock int not null,
    description text not null,
    images text,
    created timestamp without time zone not null default current_timestamp
);

-- Create invoices table
create table pos_orders
(
    id varchar(255) constraint pos_order_pk primary key,
    customer_name varchar(255) not null,
    subtotal float not null,
    tax float not null,
    discount float not null,
    total float not null,
    paid float not null,
    due float not null,
    payment_type varchar(100) not null,
    status varchar(100) not null,
    created timestamp without time zone default CURRENT_TIMESTAMP not null
);

-- Create invoice details table

create table pos_order_items
(
    id serial constraint pos_order_details_pk primary key,
    order_id varchar(255) not null,
    product_id int not null,
    product_name varchar(100) not null,
    quantity int not null,
    price float not null,
    created timestamp without time zone default CURRENT_TIMESTAMP not null
);

-- Create store configs table

create table pos_store_configs
(
    id serial constraint pos_store_configs_pk primary key,
    config_key varchar(100) not null,
    config_value varchar(100) not null,
    created timestamp without time zone default CURRENT_TIMESTAMP not null,
    updated timestamp without time zone default CURRENT_TIMESTAMP not null
);