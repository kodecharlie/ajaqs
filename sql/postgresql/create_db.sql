/*
 * Create the Ajaqs database.
 */

CREATE DATABASE @db.ajaqs.name@ WITH OWNER=@db.user.logon@;
GRANT ALL ON DATABASE @db.ajaqs.name@ TO @db.user.logon@;
