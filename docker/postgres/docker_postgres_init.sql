CREATE USER shop WITH PASSWORD 'Test1234' CREATEDB;
CREATE DATABASE shop
    WITH
    OWNER = shop
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;


-- insert into documentfolder ("id", "name") values (1, '/')