USE centuriesForum;

DROP TABLE OhStory; /* */
CREATE TABLE OhStory (
   ID INT NOT NULL IDENTITY (1,1),
   interview CHAR (1) DEFAULT 'f' NOT NULL,
   topicID INT DEFAULT 0 NOT NULL,
   ordinal INT DEFAULT 0 NOT NULL,
   shortName VARCHAR (32) NOT NULL,
   dateRange VARCHAR (32) NULL,
   title VARCHAR (128) NULL,
   subtitle VARCHAR (128) NULL,
   shortDescription VARCHAR (255) NULL,
   description TEXT NULL,
   status INT default 0 NOT NULL,
   
   CONSTRAINT OhStory_PK PRIMARY KEY(ID),
   CONSTRAINT OhStory_FK_1 FOREIGN KEY (topicID)
    REFERENCES OhTopic (ID)
);

