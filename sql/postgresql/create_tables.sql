/*
 * Create tables used by Ajaqs.
 *
 * See mapping.xml to see how these tables are mapped
 * to Java classes in the source code.
 */

CREATE SEQUENCE project_seq;
CREATE TABLE project (
  proj_id INTEGER DEFAULT NEXTVAL('project_seq') PRIMARY KEY,
  name VARCHAR(64) NOT NULL UNIQUE,
  create_date TIMESTAMP,
  state SMALLINT
);

CREATE TABLE faq_user (
  logon VARCHAR(32) PRIMARY KEY,
  password VARCHAR(32) NOT NULL,
  email VARCHAR(128),
  create_date TIMESTAMP,
  last_login TIMESTAMP,
  state SMALLINT
);

CREATE TABLE role (
  role_name VARCHAR(32) PRIMARY KEY
);

/*
 * This tables manages the N-N relationship between
 * role and faq_user tables.
 */
CREATE TABLE user_role (
  logon VARCHAR(32) NOT NULL,
  role_name VARCHAR(32) NOT NULL,
  PRIMARY KEY(logon, role_name),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE,
  FOREIGN KEY(role_name) REFERENCES role(role_name) ON DELETE CASCADE
);
CREATE INDEX user_role_logon_idx ON user_role(logon);
CREATE INDEX user_role_role_name_idx ON user_role(role_name);

/*
 * This table manages the N-N relationship between
 * project and faq_user tables.
 */
CREATE TABLE user_project (
  logon VARCHAR(32) NOT NULL,
  proj_id INTEGER NOT NULL,
  PRIMARY KEY(logon, proj_id),
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE,
  FOREIGN KEY(proj_id) REFERENCES project(proj_id) ON DELETE CASCADE
);
CREATE INDEX user_project_logon_idx ON user_project(logon);
CREATE INDEX user_project_proj_id_idx ON user_project(proj_id);

CREATE SEQUENCE faq_seq;
CREATE TABLE faq (
  faq_id INTEGER DEFAULT NEXTVAL('faq_seq') PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  create_date TIMESTAMP,
  state SMALLINT,
  proj_id INTEGER NOT NULL,
  FOREIGN KEY(proj_id) REFERENCES project(proj_id) ON DELETE CASCADE,
  UNIQUE (name, proj_id)  /* No duplicate FAQs per project. */
);
CREATE INDEX faq_proj_id ON faq(proj_id);

/*
 * Note: we do not enforce that the question is not duplicated
 * for some FAQ.
 */
CREATE SEQUENCE question_seq;
CREATE TABLE question (
  quest_id INTEGER DEFAULT NEXTVAL('question_seq') PRIMARY KEY,
  question OID NULL,  /* Not sure how large this can be. */
  create_date TIMESTAMP,
  faq_id INTEGER NOT NULL,
  logon VARCHAR(32) NOT NULL,
  FOREIGN KEY(faq_id) REFERENCES faq(faq_id) ON DELETE CASCADE,
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE
);
CREATE INDEX question_faq_id_idx ON question(faq_id);
CREATE INDEX question_logon_idx ON question(logon);

CREATE SEQUENCE answer_seq;
CREATE TABLE answer (
  answer_id INTEGER DEFAULT NEXTVAL('answer_seq') PRIMARY KEY,
  answer OID NULL,  /* Not sure how large this can be. */
  create_date TIMESTAMP,
  quest_id INTEGER NOT NULL,
  logon VARCHAR(32) NOT NULL,
  FOREIGN KEY(quest_id) REFERENCES question(quest_id) ON DELETE CASCADE,
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE
);
CREATE INDEX answer_quest_id_idx ON answer(quest_id);
CREATE INDEX answer_logon_idx ON answer(logon);

CREATE SEQUENCE attachment_seq;
CREATE TABLE attachment (
  attach_id INTEGER DEFAULT NEXTVAL('attachment_seq') PRIMARY KEY,
  attachment OID NULL, /* Not sure how large this can be. */
  descr VARCHAR(128),
  create_date TIMESTAMP,
  file_name VARCHAR(128) NOT NULL,
  file_type VARCHAR(64),
  answer_id INTEGER,
  logon VARCHAR(32),
  FOREIGN KEY(answer_id) REFERENCES answer(answer_id) ON DELETE CASCADE,
  FOREIGN KEY(logon) REFERENCES faq_user(logon) ON DELETE CASCADE
);
CREATE INDEX attachment_answer_id_idx ON attachment(answer_id);
CREATE INDEX attachment_logon_idx ON attachment(logon);
