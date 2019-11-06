DROP TABLE emails;

CREATE TABLE emails (
    address VARCHAR(100) NOT NULL PRIMARY KEY,
    added TIMESTAMP DEFAULT CURRENT TIMESTAMP,
    subscribed BOOLEAN DEFAULT TRUE
);

INSERT INTO emails (address) VALUES ('johndoe@morgue.org');