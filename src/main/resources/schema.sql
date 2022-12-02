CREATE TABLE tickets(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    personName VARCHAR(255),
    gender VARCHAR(255),
    age INT,
    tickets INT,
    price FLOAT,
    viewZone VARCHAR(255)
);

CREATE TABLE sec_user(
    userId INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    userName VARCHAR(255),
    encrPassword VARCHAR(255)
);

CREATE TABLE user_role(
    userId INT NOT NULL,
    roleId INT NOT NULL AUTO_INCREMENT,
    roleName VARCHAR(255),
    Primary Key(userId, roleId),
    FOREIGN KEY (userId) REFERENCES sec_user(userId)
);