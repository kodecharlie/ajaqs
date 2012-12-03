/*
 * Drop the Ajaqs database.
 */

REVOKE ALL ON @db.ajaqs.name@.* FROM '@db.user.logon@'@'@db.user.host@';
DROP DATABASE @db.ajaqs.name@;
FLUSH PRIVILEGES;
