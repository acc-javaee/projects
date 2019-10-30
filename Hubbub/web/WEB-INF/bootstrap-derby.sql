DROP TABLE posts;
DROP TABLE users;

CREATE TABLE users (
    username VARCHAR(20) NOT NULL,
    passhash CHAR(55) NOT NULL,
    joined   DATE DEFAULT CURRENT_DATE,
    CONSTRAINT pk_users PRIMARY KEY (username)
);

CREATE TABLE posts (
    content VARCHAR(140) NOT NULL,
    author VARCHAR(20) NOT NULL,
    posted TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY,
    CONSTRAINT pk_posts PRIMARY KEY (id),
    CONSTRAINT fk_post_author FOREIGN KEY (author) REFERENCES users(username)
);

INSERT INTO users (username, passhash) VALUES
    ('johndoe', '$5$JI3G06zK$S.ZZCjQFhTYEdPfcGenqkK.s8nIr0KW4jXsqDEwBCX8'),
    ('janedoe', '$5$JI3G06zK$S.ZZCjQFhTYEdPfcGenqkK.s8nIr0KW4jXsqDEwBCX8');

INSERT INTO posts (author, posted, content) VALUES
    ('johndoe', '2017-05-09 08:23:47.110', 'My first Hubbub post! #JavaRules #J2EERocks'),
    ('johndoe', '2017-06-02 19:00:05.965', 'I''ve invited Jane to join.'),
    ('janedoe', '2018-01-13 06:30:45.888', 'Alright, I''ve signed up. Now what?');
