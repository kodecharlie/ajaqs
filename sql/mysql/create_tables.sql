/*
 * Create tables used by Ajaqs.
 *
 * See mapping.xml to see how these tables are mapped
 * to Java classes in the source code.
 *
 * NOTE: several INDEX directives below refer to the same
 * index names (Eg, user_ind or proj_ind). This does not
 * cause problems in MySQL.
 */

/* The ajaqs database is where we store everything. */
USE @db.ajaqs.name@;

CREATE TABLE project(
  proj_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  create_date DATETIME,
  state TINYINT,
  UNIQUE (name))
  TYPE=INNODB;

CREATE TABLE faq_user(
  logon VARCHAR(32) NOT NULL PRIMARY KEY,
  password VARCHAR(32) NOT NULL,
  email VARCHAR(128),
  create_date DATETIME,
  last_login DATETIME,
  state TINYINT)
  TYPE=INNODB;

CREATE TABLE role(
  role_name VARCHAR(32) NOT NULL PRIMARY KEY)
  TYPE=INNODB;

/*
 * This tables manages the N-N relationship between
 * role and faq_user tables.
 */
CREATE TABLE user_role(
  logon VARCHAR(32) NOT NULL,
  role_name VARCHAR(32) NOT NULL,
  PRIMARY KEY(logon, role_name),
  INDEX user_ind (logon),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE,
  INDEX role_ind (role_name),
  FOREIGN KEY(role_name) REFERENCES role(role_name) ON DELETE CASCADE)
  TYPE=INNODB;

/*
 * This table manages the N-N relationship between
 * project and faq_user tables.
 */
CREATE TABLE user_project(
  logon VARCHAR(32) NOT NULL,
  proj_id INT UNSIGNED NOT NULL,
  PRIMARY KEY(logon, proj_id),
  INDEX user_ind (logon),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE,
  INDEX proj_ind (proj_id),
  FOREIGN KEY(proj_id) REFERENCES project(proj_id) ON DELETE CASCADE)
  TYPE=INNODB;

CREATE TABLE faq(
  faq_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  create_date DATETIME,
  state TINYINT,
  proj_id INT UNSIGNED NOT NULL,
  INDEX proj_ind (proj_id),
  FOREIGN KEY(proj_id) REFERENCES project(proj_id) ON DELETE CASCADE,
  UNIQUE (name, proj_id)) /* No duplicate FAQs per project. */
  TYPE=INNODB;

/*
 * Note: we do not enforce that the question is not duplicated
 * for some FAQ.
 */
CREATE TABLE question(
  quest_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  question BLOB NOT NULL,  /* Allows for 64 Kb */
  create_date DATETIME,
  faq_id INT UNSIGNED NOT NULL,
  logon VARCHAR(32) NOT NULL,
  INDEX faq_ind (faq_id),
  FOREIGN KEY(faq_id) REFERENCES faq(faq_id) ON DELETE CASCADE,
  INDEX user_ind (logon),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE)
  TYPE=INNODB;

CREATE TABLE answer(
  answer_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  answer BLOB NOT NULL,  /* Allows for 64 Kb */
  create_date DATETIME,
  quest_id INT UNSIGNED NOT NULL,
  logon VARCHAR(32) NOT NULL,
  INDEX quest_ind (quest_id),
  FOREIGN KEY(quest_id) REFERENCES question(quest_id) ON DELETE CASCADE,
  INDEX user_ind (logon),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE)
  TYPE=INNODB;

CREATE TABLE attachment(
  attach_id INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  attachment MEDIUMBLOB NOT NULL, /* Allows for 16 Mb */
  descr VARCHAR(128),
  create_date DATETIME,
  file_name VARCHAR(128) NOT NULL,
  file_type VARCHAR(64),
  answer_id INT UNSIGNED,
  logon VARCHAR(32),
  INDEX answer_ind (answer_id),
  FOREIGN KEY(answer_id) REFERENCES answer(answer_id) ON DELETE CASCADE,
  INDEX user_ind (logon),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE)
  TYPE=INNODB;
