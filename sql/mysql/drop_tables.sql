/*
 * Drop tables.
 */

/* The ajaqs database is where we store everything. */
USE @db.ajaqs.name@;

/*
 * Drop tables in reverse-order of creation.
 */
DROP TABLE attachment;
DROP TABLE answer;
DROP TABLE question;
DROP TABLE faq;
DROP TABLE user_project;
DROP TABLE user_role;
DROP TABLE role;
DROP TABLE faq_user;
DROP TABLE project;
