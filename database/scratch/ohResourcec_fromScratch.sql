USE centuriesForum;

/* DROP TABLE OhResource;  */
CREATE TABLE OhResource (
   ID INT NOT NULL IDENTITY (1,1),
   shortName VARCHAR (32) NOT NULL,
   title VARCHAR (64) NULL,
   linkedTitle CHAR (1) DEFAULT 'f' NOT NULL,
   titleUrl VARCHAR (128) NULL,
   author VARCHAR (64) NULL,
   source VARCHAR (64) NULL,
   sourceLinkText VARCHAR (128) NULL,
   sourceLinkUrl VARCHAR (128) NULL,
   status INT default 0 NOT NULL,
   
   CONSTRAINT OhResource_PK PRIMARY KEY(ID)
);


