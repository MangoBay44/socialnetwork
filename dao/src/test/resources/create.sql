DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS grp;
DROP TABLE IF EXISTS friendship;

CREATE TABLE account (
  id INT(11) NOT NULL AUTO_INCREMENT,
  FirstName VARCHAR(45) NULL DEFAULT NULL,
  MiddleName VARCHAR(45) NULL DEFAULT NULL,
  LastName VARCHAR(45) NULL DEFAULT NULL,
  DateOfBirth DATE NULL DEFAULT NULL,
  WorkPhone VARCHAR(45) NULL DEFAULT NULL,
  PersonalPhone VARCHAR(45) NULL DEFAULT NULL,
  HomeAddress VARCHAR(45) NULL DEFAULT NULL,
  WorkAddress VARCHAR(45) NULL DEFAULT NULL,
  Email VARCHAR(45) NULL DEFAULT NULL,
  Password VARCHAR(45) NULL DEFAULT NULL,
  Male VARCHAR(45) NULL DEFAULT NULL,
  Skype VARCHAR(45) NULL DEFAULT NULL,
  AdditionalInformation VARCHAR(200) NULL DEFAULT NULL,
  Photo LONGBLOB NULL DEFAULT NULL,
  PRIMARY KEY (id));

CREATE TABLE friendship (
  AccountIdFrom INT(11) NOT NULL,
  AccountIdTo INT(11) NOT NULL,
  Status VARCHAR(45) NOT NULL);

CREATE TABLE grp (
  id INT(11) NOT NULL AUTO_INCREMENT,
  Name VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (id));

INSERT INTO account (id, FirstName, LastName, WorkPhone, PersonalPhone, HomeAddress)
VALUES(1, 'Иван', 'Григорьевский', '123-324', '89123348491', 'SPB');

INSERT INTO account (id, FirstName, LastName, WorkPhone, PersonalPhone, HomeAddress)
VALUES(2, 'Амир', 'Мунипов', '956-231', '89123410414', 'SPB');

INSERT INTO account (id, FirstName, LastName, WorkPhone, PersonalPhone, HomeAddress)
VALUES(3, 'Гульназ', 'Гайсина', '993-113', '89112319595', 'СПб');

INSERT INTO grp (id, Name) VALUES (1, 'Music');
INSERT INTO grp (id, Name) VALUES (2, 'Art');
INSERT INTO grp (id, Name) VALUES (3, 'Programming');

INSERT INTO friendship (AccountIdFrom, AccountIdTo, Status) VALUES (1, 2, 'FRIEND');
INSERT INTO friendship (AccountIdFrom, AccountIdTo, Status) VALUES (2, 3, 'FRIEND');
