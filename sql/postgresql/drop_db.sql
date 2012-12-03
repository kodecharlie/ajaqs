/*
 * Drop the Ajaqs database.
 */

REVOKE ALL ON DATABASE @db.ajaqs.name@ FROM @db.user.logon@;
DROP DATABASE @db.ajaqs.name@;
