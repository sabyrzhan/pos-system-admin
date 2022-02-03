-- USER and ADMIN test users
insert into pos_users(username, email, password, salt, role) values('admin', 'admin@test.com', '123123', '123', 'ADMIN');
insert into pos_users(username, email, password, salt, role) values('user', 'user@test.com', '123123', '123', 'USER');

-- Other test users
insert into pos_users(username, email, password, salt, role) values('test1', 'test1@test.com', '123123', '123', 'USER');
insert into pos_users(username, email, password, salt, role) values('test2', 'test2@test.com', '123123', '123', 'USER');
insert into pos_users(username, email, password, salt, role) values('test3', 'test3@test.com', '123123', '123', 'USER');
insert into pos_users(username, email, password, salt, role) values('test4', 'test4@test.com', '123123', '123', 'USER');


-- Initial categories
insert into pos_categories(name) values('category1');
insert into pos_categories(name) values('category2');
insert into pos_categories(name) values('category3');
insert into pos_categories(name) values('category4');
insert into pos_categories(name) values('category5');

-- Test products
INSERT INTO pos_products (name, category_id, purchase_price, sale_price, stock, description, images, created)
                  VALUES ('Test product 1', 1, 10, 100, 10, 'Test product 1 description', null, current_timestamp);
INSERT INTO pos_products (name, category_id, purchase_price, sale_price, stock, description, images, created)
                  VALUES ('Test product 2', 2, 20, 200, 20, 'Test product 2 description', null, current_timestamp);
INSERT INTO pos_products (name, category_id, purchase_price, sale_price, stock, description, images, created)
                  VALUES ('Test product 3', 3, 30, 300, 30, 'Test product 3 description', null, current_timestamp);
INSERT INTO pos_products (name, category_id, purchase_price, sale_price, stock, description, images, created)
                  VALUES ('Test product 4', 4, 40, 400, 40, 'Test product 4 description', null, current_timestamp);
INSERT INTO pos_products (name, category_id, purchase_price, sale_price, stock, description, images, created)
                  VALUES ('Test product 5', 5, 50, 500, 50, 'Test product 5 description', null, current_timestamp);

-- Test configs
INSERT INTO pos_store_configs (config_key, config_value, created, updated)
                VALUES ('TAX_PERCENT', '5', current_timestamp, current_timestamp);