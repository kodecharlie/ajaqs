/*
 * Create the Ajaqs database.
 */

CREATE DATABASE @db.ajaqs.name@;
GRANT ALL ON @db.ajaqs.name@.* TO '@db.user.logon@'@'@db.user.host@' IDENTIFIED BY '@db.user.password@';
FLUSH PRIVILEGES;
