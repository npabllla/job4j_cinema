insert into account(username, email, phone) values ('Kirill', 'kirill@gmail.com', '+79653248496');
insert into account(username, email, phone) values ('Ivan', 'ivan@gmail.com', '+79872753471');

insert into ticket(row, col, price, account_id) values (1, 1, 500, null);
insert into ticket(row, col, price, account_id) values (1, 2, 550, null);
insert into ticket(row, col, price, account_id) values (1, 3, 500, null);
insert into ticket(row, col, price, account_id) values (2, 1, 600, null);
insert into ticket(row, col, price, account_id) values (2, 2, 650, null);
insert into ticket(row, col, price, account_id) values (2, 3, 600, null);
insert into ticket(row, col, price, account_id) values (3, 1, 650, null);
insert into ticket(row, col, price, account_id) values (3, 2, 700, null);
insert into ticket(row, col, price, account_id) values (3, 3, 650, null);

update ticket set account_id = 1 where id = 5;
update ticket set account_id = 1 where id = 6;
update ticket set account_id = 2 where id = 2;