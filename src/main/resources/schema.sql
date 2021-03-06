DROP TABLE IF EXISTS person;
DROP TABLE IF EXISTS question;

CREATE TABLE person (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        username VARCHAR(250) NOT NULL,
                        password_hash VARCHAR(250) NOT NULL

);

CREATE TABLE question (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         text VARCHAR(250) NOT NULL,
                         owner VARCHAR(250) NOT NULL,
                         FOREIGN KEY (owner) REFERENCES person(id)

);
