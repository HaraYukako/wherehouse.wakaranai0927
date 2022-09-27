DROP TABLE IF EXISTS STOCK;
CREATE TABLE STOCK(ID int auto_increment primary key, GOODS_CODE int unique, QUANTITY int, STATUS varchar(10) not null);