DROP TABLE IF EXISTS GOODS;
CREATE TABLE GOODS(ID int auto_increment primary key, CODE int unique, NAME varchar(20) not null, PRICE int, STATUS varchar(10) not null);