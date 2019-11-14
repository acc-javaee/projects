DROP TABLE post_tags;
DROP TABLE tags;
DROP TABLE mentions;
DROP TABLE following;
DROP TABLE comments;
DROP TABLE posts;
DROP TABLE users;
DROP TABLE profiles;

CREATE TABLE profiles (
    firstname VARCHAR(20),
    lastname VARCHAR(30),
    email VARCHAR(100),
    timezone VARCHAR(50),
    biography VARCHAR(512),
    avatar BLOB(200K),
    mime VARCHAR(30),
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_profiles PRIMARY KEY (id)
);

CREATE TABLE users (
    username VARCHAR(20) NOT NULL,
    passhash CHAR(55) NOT NULL,
    joined   DATE DEFAULT CURRENT_DATE,
    profile  INTEGER NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (username),
    CONSTRAINT fk_user_profile FOREIGN KEY (profile) REFERENCES profiles(id)
);

CREATE TABLE posts (
    content VARCHAR(140) NOT NULL,
    author VARCHAR(20) NOT NULL,
    posted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_posts PRIMARY KEY (id),
    CONSTRAINT fk_post_author FOREIGN KEY (author) REFERENCES users(username)
);

CREATE TABLE comments (
    author VARCHAR(20) NOT NULL,
    target INTEGER NOT NULL,
    comment VARCHAR(70) NOT NULL,
    commented TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_comments PRIMARY KEY (id),
    CONSTRAINT fk_comment_author FOREIGN KEY (author) REFERENCES users(username),
    CONSTRAINT fk_comment_target FOREIGN KEY (target) REFERENCES posts(id)
);

CREATE TABLE following (
    follower VARCHAR(20) NOT NULL,
    followee VARCHAR(20) NOT NULL,
    CONSTRAINT pk_following PRIMARY KEY (follower,followee),
    CONSTRAINT fk_follower_user FOREIGN KEY (follower) REFERENCES users(username),
    CONSTRAINT fk_followee_user FOREIGN KEY (followee) REFERENCES users(username)
);

CREATE TABLE mentions (
    post INTEGER NOT NULL,
    subject VARCHAR(20) NOT NULL,
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_mentions PRIMARY KEY (id),
    CONSTRAINT fk_mention_post FOREIGN KEY (post) REFERENCES posts(id),
    CONSTRAINT fk_mention_subject FOREIGN KEY (subject) REFERENCES users(username)
);

CREATE TABLE tags (
    tagname VARCHAR(30) NOT NULL,
    creator VARCHAR(20) NOT NULL,
    created TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    CONSTRAINT pk_tags PRIMARY KEY (tagname),
    CONSTRAINT fk_creator_user FOREIGN KEY (creator) REFERENCES users(username)
);

/* LINK TABLE FOR MANY-TO-MANY Post/Tag RELATIONSHIP */
CREATE TABLE post_tags (
    post INTEGER NOT NULL,
    tag  VARCHAR(30) NOT NULL,
    id  INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_post_tags PRIMARY KEY (id),
    CONSTRAINT fk_tag_tagname FOREIGN KEY (tag) REFERENCES tags(tagname),
    CONSTRAINT fk_tag_post FOREIGN KEY (post) REFERENCES posts(id)
);

INSERT INTO profiles (firstname, lastname, email, timezone, biography) VALUES
    ('John', 'Doe', 'johndoe@morgue.org', NULL, NULL),
    ('Jane', 'Doe', NULL, NULL, 'I sometimes hang with @johndoe.'),
    (NULL, NULL, 'jjack@pailowater.net', 'America/Los_Angeles', NULL);


INSERT INTO users (username, passhash, profile) VALUES
    ('johndoe', '$5$JI3G06zK$S.ZZCjQFhTYEdPfcGenqkK.s8nIr0KW4jXsqDEwBCX8', 1),
    ('janedoe', '$5$JI3G06zK$S.ZZCjQFhTYEdPfcGenqkK.s8nIr0KW4jXsqDEwBCX8', 2),
    ('jilljack', '$5$JI3G06zK$S.ZZCjQFhTYEdPfcGenqkK.s8nIr0KW4jXsqDEwBCX8', 3);

INSERT INTO posts (author, posted, content) VALUES
    ('johndoe', '2017-05-09 08:23:47.110', 'My first Hubbub post! #JavaRules #J2EERocks'),
    ('johndoe', '2017-06-02 19:00:05.965', 'I''ve invited @janedoe to join.'),
    ('janedoe', '2018-01-13 06:30:45.888', 'Alright, I''ve signed up. Now what?'),
    ('jilljack', '2019-11-11 23:09:28.027', 'Hey @janedoe and @johndoe! Whazzup in #Hubbub? #JavaRules');

INSERT INTO comments (author, target, comment) VALUES
    ('janedoe', 2, 'I''m here, @johndoe!');

INSERT INTO following VALUES
    ('johndoe', 'janedoe'),
    ('janedoe', 'johndoe'),
    ('janedoe', 'jilljack');

INSERT INTO mentions (post,subject) VALUES 
    (2, 'janedoe'),
    (4, 'janedoe'),
    (4, 'johndoe');

INSERT INTO tags VALUES
    ('JavaRules', 'johndoe', '2017-05-09 08:23:47.110'),
    ('J2EERocks', 'johndoe', '2017-05-09 08:23:47.110'),
    ('Hubbub', 'jilljack', '2019-11-11 23:09:28.027');

INSERT INTO post_tags (tag, post) VALUES
    ('JavaRules', 1),
    ('J2EERocks', 1),
    ('Hubbub', 4),
    ('JavaRules', 4);