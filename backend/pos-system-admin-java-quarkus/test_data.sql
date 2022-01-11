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