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
create table pos_invoices
(
    id serial constraint pos_invoice_pk primary key,
    customer_name varchar(255) not null,
    subtotal float not null,
    tax float not null,
    discount float not null,
    total float not null,
    paid float not null,
    due float not null,
    payment_type varchar(100) not null,
    created timestamp without time zone default CURRENT_TIMESTAMP not null
);

-- Create invoice details table

create table pos_invoice_details
(
    id serial constraint pos_invoice_details_pk primary key,
    invoice_id int not null,
    product_id int not null,
    product_name varchar(100) not null,
    quantity int not null,
    price float not null,
    created timestamp without time zone default CURRENT_TIMESTAMP not null
);
