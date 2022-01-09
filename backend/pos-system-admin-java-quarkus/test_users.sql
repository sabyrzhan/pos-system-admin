-- Add test users
insert into pos_users(username, email, password, salt, role) values('admin', 'admin@test.com', '123123', '123', 'ADMIN');
insert into pos_users(username, email, password, salt, role) values('user', 'user@test.com', '123123', '123', 'USER');

