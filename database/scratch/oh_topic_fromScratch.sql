USE centuriesForum;

/*DROP TABLE OhTopic;  */
CREATE TABLE OhTopic (
   ID INT NOT NULL IDENTITY (1,1),
   person CHAR (1) DEFAULT 'f' NOT NULL,
   gender CHAR(1) DEFAULT 'm' NOT NULL
   shortName VARCHAR (32) NOT NULL,
   ordinal INT DEFAULT 0 NOT NULL,
   title VARCHAR (128) NULL,
   subtitle VARCHAR (128) NULL,
   shortDescription VARCHAR (255) NULL,
   description TEXT NULL,
   status INT default 0 NOT NULL,
   
   CONSTRAINT OhTopic_PK PRIMARY KEY(ID)
);


