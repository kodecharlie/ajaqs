/*
 * Insert test-data into tables.
 */

/* Set up users. */
INSERT INTO faq_user(logon, password, email, create_date, last_login, state)
  VALUES ("cread", "cread123", "cread@mukuju.com", NOW(), NOW(), 1),
         ("ender", "ender123", "ender@game.com", NOW(), NOW(), 1),
         ("daedelus", "daedelus123", "daedelus@joyce.com", NOW(), NOW(), 1),
         ("admin", "admin123", "admin@mukuju.com", NOW(), NOW(), 1),
         ("user0_", "user0_123", "user0_@mukuju.com", NOW(), NOW(), 1),
         ("user1_", "user1_123", "user1_@mukuju.com", NOW(), NOW(), 1),
         ("user2_", "user2_123", "user2_@mukuju.com", NOW(), NOW(), 1),
         ("user3_", "user3_123", "user3_@mukuju.com", NOW(), NOW(), 1),
         ("user4_", "user4_123", "user4_@mukuju.com", NOW(), NOW(), 1),
         ("user5_", "user5_123", "user5_@mukuju.com", NOW(), NOW(), 1),
         ("user6_", "user6_123", "user6_@mukuju.com", NOW(), NOW(), 1),
         ("user7_", "user7_123", "user7_@mukuju.com", NOW(), NOW(), 1),
         ("user8_", "user8_123", "user8_@mukuju.com", NOW(), NOW(), 1),
         ("user9_", "user9_123", "user9_@mukuju.com", NOW(), NOW(), 1),
         ("user10_", "user10_123", "user10_@mukuju.com", NOW(), NOW(), 1),
         ("user11_", "user11_123", "user11_@mukuju.com", NOW(), NOW(), 1),
         ("user12_", "user12_123", "user12_@mukuju.com", NOW(), NOW(), 1),
         ("user13_", "user13_123", "user13_@mukuju.com", NOW(), NOW(), 1),
         ("user14_", "user14_123", "user14_@mukuju.com", NOW(), NOW(), 1),
         ("user15_", "user15_123", "user15_@mukuju.com", NOW(), NOW(), 1),
         ("user16_", "user16_123", "user16_@mukuju.com", NOW(), NOW(), 1),
         ("user17_", "user17_123", "user17_@mukuju.com", NOW(), NOW(), 1),
         ("user18_", "user18_123", "user18_@mukuju.com", NOW(), NOW(), 1),
         ("user19_", "user19_123", "user19_@mukuju.com", NOW(), NOW(), 1),
         ("user20_", "user20_123", "user20_@mukuju.com", NOW(), NOW(), 1),
         ("user21_", "user21_123", "user21_@mukuju.com", NOW(), NOW(), 1),
         ("user22_", "user22_123", "user22_@mukuju.com", NOW(), NOW(), 1),
         ("user23_", "user23_123", "user23_@mukuju.com", NOW(), NOW(), 1),
         ("user24_", "user24_123", "user24_@mukuju.com", NOW(), NOW(), 1),
         ("user25_", "user25_123", "user25_@mukuju.com", NOW(), NOW(), 1),
         ("user26_", "user26_123", "user26_@mukuju.com", NOW(), NOW(), 1),
         ("user27_", "user27_123", "user27_@mukuju.com", NOW(), NOW(), 1),
         ("user28_", "user28_123", "user28_@mukuju.com", NOW(), NOW(), 1),
         ("user29_", "user29_123", "user29_@mukuju.com", NOW(), NOW(), 1),
         ("user30_", "user30_123", "user30_@mukuju.com", NOW(), NOW(), 1),
         ("user31_", "user31_123", "user31_@mukuju.com", NOW(), NOW(), 1),
         ("user32_", "user32_123", "user32_@mukuju.com", NOW(), NOW(), 1),
         ("user33_", "user33_123", "user33_@mukuju.com", NOW(), NOW(), 1),
         ("user34_", "user34_123", "user34_@mukuju.com", NOW(), NOW(), 1),
         ("user35_", "user35_123", "user35_@mukuju.com", NOW(), NOW(), 1);

/* Set up roles. */
INSERT INTO role(role_name)
  VALUES ("ajaqs-admin"),
         ("ajaqs-user");

/* Map roles to users. */
INSERT INTO user_role(logon, role_name)
  VALUES ("cread", "ajaqs-admin"),
         ("cread", "ajaqs-user"),
         ("ender", "ajaqs-user"),
         ("daedelus", "ajaqs-user");

/*
 * Set up data for Ajaqs project.
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Ajaqs", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id()),
         ("ender", last_insert_id()),
         ("daedelus", last_insert_id());

INSERT INTO faq(faq_id, name, create_date, state, proj_id)
  VALUES (1, "Origins", NOW(), 1, last_insert_id()),
         (2, "Software Requirements", NOW(), 1, last_insert_id()),
         (3, "End-user Help", NOW(), 1, last_insert_id());

/* FAQ #1 */
INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (1, "Why was Ajaqs written?", NOW(), 1, "cread"),
         (2, "Who are the authors?", NOW(), 1, "ender"),
         (3, "When was it first released?", NOW(), 1, "daedelus"),
         (4, "What was the initial price?", NOW(), 1, "ender"),
         (5, "Where was it first sold?", NOW(), 1, "cread");

/* FAQ #2 */
INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (6, "What platforms are supported?", NOW(), 2, "ender"),
         (7, "What additional .jar files that are needed?", NOW(), 2, "ender"),
         (8, "Is Ajaqs upwardly compatible with Tomcat 4.5?",
          NOW(), 2, "daedelus");

/* FAQ #3 */
INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (9, "How is it installed?", NOW(), 3, "ender"),
         (10, "How can you modify a .jsp page?", NOW(), 3, "ender"),
         (11, "How do you upgrade to version 1.7?", NOW(), 3, "daedelus"),
         (12, "What jar files are required?", NOW(), 3, "daedelus");

/* Answers for FAQ #1. */
INSERT INTO answer(answer_id, answer, create_date, quest_id, logon)
  VALUES (1, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 1, "cread"),
         (2, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 1, "ender"),
         (3, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 1, "ender"),
         (4, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 2, "daedelus"),
         (5, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 2, "ender"),
         (6, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 2, "daedelus"),
         (7, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 3, "ender"),
         (8, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 3, "cread"),
         (9, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 3, "cread"),
         (10, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 4, "ender"),
         (11, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 4, "cread"),
         (12, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 4, "cread"),
         (13, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 5, "ender"),
         (14, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 5, "cread"),
         (15, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 5, "cread");

/* Answers for FAQ #2. */
INSERT INTO answer(answer_id, answer, create_date, quest_id, logon)
  VALUES (16, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 6, "cread"),
         (17, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 6, "ender"),
         (18, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 6, "ender"),
         (19, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 7, "daedelus"),
         (20, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 7, "ender"),
         (21, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 7, "daedelus"),
         (22, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 8, "ender"),
         (23, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 8, "cread"),
         (24, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 8, "cread");

/* Answers for FAQ #3. */
INSERT INTO answer(answer_id, answer, create_date, quest_id, logon)
  VALUES (25, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 9, "cread"),
         (26, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 9, "ender"),
         (27, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 9, "ender"),
         (28, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 10, "daedelus"),
         (29, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 10, "ender"),
         (30, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 10, "daedelus"),
         (31, "It solved one of the outstanding problems posed by Hilbert in 1909.", NOW(), 11, "ender"),
         (32, "It was also written to demonstrate deployment of servlets onto the (now obsolete) Apache-Jserv module.", NOW(), 11, "cread"),
         (33, "In 2000, there was a recognized need for an online message board.  This was thought out and ultimately delivered by Bearded Iris Computing.  The initial release was on freshmeat.", NOW(), 12, "cread");

/*
 * Set up data for WebKiosk project.
 */
INSERT INTO project(name, create_date, state)
  VALUES ("WebKiosk", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id()),
         ("daedelus", last_insert_id());

INSERT INTO faq(faq_id, name, create_date, state, proj_id)
  VALUES (4, "Origins", NOW(), 1, last_insert_id()),
         (5, "Software Requirements", NOW(), 1, last_insert_id()),
         (6, "End-user Help", NOW(), 1, last_insert_id()),
         (7, "Upgrades", NOW(), 1, last_insert_id());

INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (13, "Why was WebKiosk written?", NOW(), 4, "cread"),
         (14, "Who are the authors?", NOW(), 4, "ender"),
         (15, "When was it first released?", NOW(), 4, "daedelus"),
         (16, "What was the initial price?", NOW(), 4, "ender"),
         (17, "Where was it first sold?", NOW(), 4, "cread");

INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (18, "What platforms are supported?", NOW(), 5, "ender"),
         (19, "What additional .jar files that are needed?",
          NOW(), 5, "ender"),
         (20, "Is WebKiosk upwardly compatible with Tomcat 4.5?",
          NOW(), 5, "daedelus");

INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (21, "How is it installed?", NOW(), 6, "ender"),
         (22, "How can you modify a .jsp page?", NOW(), 6, "ender"),
         (23, "How do you upgrade to version 1.7?", NOW(), 6, "daedelus"),
         (24, "What jar files are required?", NOW(), 6, "daedelus");

INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (25, "How much is the basic upgrade?", NOW(), 7, "ender"),
         (26, "When will an upgrade for version 2.3 be released?",
          NOW(), 7, "ender"),
         (27, "Can we download the upgrade?", NOW(), 7, "daedelus");

/*
 * Set up data for xdelimitarea project.
 */
INSERT INTO project(name, create_date, state)
  VALUES ("xdelimitarea", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());

INSERT INTO faq(faq_id, name, create_date, state, proj_id)
  VALUES (8, "Configuration", NOW(), 1, last_insert_id()),
         (9, "Customer service", NOW(), 1, last_insert_id()),
         (10, "Versions / Releases", NOW(), 1, last_insert_id());


/*
 * Project #0
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #0", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());

INSERT INTO faq(faq_id, name, create_date, state, proj_id)
  VALUES (11, "FAQ #11", NOW(), 1, last_insert_id()),
         (12, "FAQ #12", NOW(), 1, last_insert_id()),
         (13, "FAQ #13", NOW(), 1, last_insert_id()),
         (14, "FAQ #14", NOW(), 1, last_insert_id()),
         (15, "FAQ #15", NOW(), 1, last_insert_id()),
         (16, "FAQ #16", NOW(), 1, last_insert_id()),
         (17, "FAQ #17", NOW(), 1, last_insert_id()),
         (18, "FAQ #18", NOW(), 1, last_insert_id()),
         (19, "FAQ #19", NOW(), 1, last_insert_id()),
         (20, "FAQ #20", NOW(), 1, last_insert_id()),
         (21, "FAQ #21", NOW(), 1, last_insert_id()),
         (22, "FAQ #22", NOW(), 1, last_insert_id()),
         (23, "FAQ #23", NOW(), 1, last_insert_id()),
         (24, "FAQ #24", NOW(), 1, last_insert_id()),
         (25, "FAQ #25", NOW(), 1, last_insert_id()),
         (26, "FAQ #26", NOW(), 1, last_insert_id()),
         (27, "FAQ #27", NOW(), 1, last_insert_id()),
         (28, "FAQ #28", NOW(), 1, last_insert_id()),
         (29, "FAQ #29", NOW(), 1, last_insert_id()),
         (30, "FAQ #30", NOW(), 1, last_insert_id()),
         (31, "FAQ #31", NOW(), 1, last_insert_id()),
         (32, "FAQ #32", NOW(), 1, last_insert_id()),
         (33, "FAQ #33", NOW(), 1, last_insert_id()),
         (34, "FAQ #34", NOW(), 1, last_insert_id()),
         (35, "FAQ #35", NOW(), 1, last_insert_id()),
         (36, "FAQ #36", NOW(), 1, last_insert_id()),
         (37, "FAQ #37", NOW(), 1, last_insert_id()),
         (38, "FAQ #38", NOW(), 1, last_insert_id()),
         (39, "FAQ #39", NOW(), 1, last_insert_id()),
         (40, "FAQ #40", NOW(), 1, last_insert_id()),
         (41, "FAQ #41", NOW(), 1, last_insert_id()),
         (42, "FAQ #42", NOW(), 1, last_insert_id()),
         (43, "FAQ #43", NOW(), 1, last_insert_id()),
         (44, "FAQ #44", NOW(), 1, last_insert_id());

/* FAQ #11 */
INSERT INTO question(quest_id, question, create_date, faq_id, logon)
  VALUES (28, "Question #28?", NOW(), 11, "cread"),
         (29, "Question #29?", NOW(), 11, "ender"),
         (30, "Question #30?", NOW(), 11, "daedelus"),
         (31, "Question #31?", NOW(), 11, "ender"),
         (32, "Question #32?", NOW(), 11, "cread"),
         (33, "Question #33?", NOW(), 11, "cread"),
         (34, "Question #34?", NOW(), 11, "cread"),
         (35, "Question #35?", NOW(), 11, "cread"),
         (36, "Question #36?", NOW(), 11, "cread"),
         (37, "Question #37?", NOW(), 11, "cread"),
         (38, "Question #38?", NOW(), 11, "cread"),
         (39, "Question #39?", NOW(), 11, "cread"),
         (40, "Question #40?", NOW(), 11, "cread"),
         (41, "Question #41?", NOW(), 11, "cread"),
         (42, "Question #42?", NOW(), 11, "cread"),
         (43, "Question #43?", NOW(), 11, "cread"),
         (44, "Question #44?", NOW(), 11, "cread"),
         (45, "Question #45?", NOW(), 11, "cread"),
         (46, "Question #46?", NOW(), 11, "cread"),
         (47, "Question #47?", NOW(), 11, "cread"),
         (48, "Question #48?", NOW(), 11, "cread"),
         (49, "Question #49?", NOW(), 11, "cread"),
         (50, "Question #50?", NOW(), 11, "cread"),
         (51, "Question #51?", NOW(), 11, "cread"),
         (52, "Question #52?", NOW(), 11, "cread"),
         (53, "Question #53?", NOW(), 11, "cread"),
         (54, "Question #54?", NOW(), 11, "cread"),
         (55, "Question #55?", NOW(), 11, "cread"),
         (56, "Question #56?", NOW(), 11, "cread"),
         (57, "Question #57?", NOW(), 11, "cread"),
         (58, "Question #58?", NOW(), 11, "cread"),
         (59, "Question #59?", NOW(), 11, "cread"),
         (60, "Question #60?", NOW(), 11, "cread"),
         (61, "Question #61?", NOW(), 11, "cread"),
         (62, "Question #62?", NOW(), 11, "cread"),
         (63, "Question #63?", NOW(), 11, "cread"),
         (64, "Question #64?", NOW(), 11, "cread"),
         (65, "Question #65?", NOW(), 11, "cread"),
         (66, "Question #66?", NOW(), 11, "cread"),
         (67, "Question #67?", NOW(), 11, "cread");


/*
 * Project #1
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #1", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #2
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #2", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #3
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #3", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #4
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #4", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #5
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #5", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #6
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #6", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #7
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #7", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #8
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #8", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #9
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #9", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #10
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #10", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #11
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #11", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #12
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #12", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #13
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #13", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #14
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #14", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #15
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #15", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #16
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #16", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #17
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #17", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #18
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #18", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #19
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #19", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #20
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #20", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #21
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #21", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #22
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #22", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #23
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #23", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #24
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #24", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #25
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #25", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #26
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #26", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #27
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #27", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #28
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #28", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #29
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #29", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #30
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #30", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #31
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #31", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #32
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #32", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #33
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #33", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #34
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #34", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #35
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #35", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #36
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #36", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());


/*
 * Project #37
 */
INSERT INTO project(name, create_date, state)
  VALUES ("Project #37", NOW(), 1);

INSERT INTO user_project(logon, proj_id)
  VALUES ("cread", last_insert_id());
