cd %~dp0
mysql -vvv --batch --user=root --password=root --host=localhost < createdb.sql
mysql warehouse -vvv --batch --user=root --password=root --host=localhost < goods_schema.sql
mysql warehouse -vvv --batch --user=root --password=root --host=localhost < goods_data.sql
mysql warehouse -vvv --batch --user=root --password=root --host=localhost < stock_schema.sql
mysql warehouse -vvv --batch --user=root --password=root --host=localhost < stock_data.sql
mysql warehouse -vvv --batch --user=root --password=root --host=localhost < receiving_shipment_order_log_schema.sql

pause
