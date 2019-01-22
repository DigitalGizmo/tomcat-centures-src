USE centuriesForum;

/* DROP TABLE OhPerson;  */
CREATE TABLE OhPerson (
   ID INT NOT NULL IDENTITY (1,1),
   shortName VARCHAR (32) NOT NULL,
   ordinal INT NOT NULL,
   title VARCHAR (64) NOT NULL,
   subtitle VARCHAR (128) NULL,
   shortDescription VARCHAR (255) NULL,
   description TEXT NULL,
   status INT default 0 NOT NULL,
   
   CONSTRAINT OhPerson_PK PRIMARY KEY(ID)
);


