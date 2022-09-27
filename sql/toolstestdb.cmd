cd %~dp0
mysql -vvv --batch --user=root --password=root --host=localhost < createdb_toolstest.sql
mysql toolstest -vvv --batch --user=root --password=root --host=localhost < sample.sql

pause
