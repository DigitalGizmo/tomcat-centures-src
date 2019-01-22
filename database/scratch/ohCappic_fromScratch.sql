USE centuriesForum;

DROP TABLE OhCappic;  
CREATE TABLE OhCappic (
   ID INT NOT NULL IDENTITY (1,1),
   shortName VARCHAR (32) NOT NULL,
   altTag VARCHAR (128) NULL,
   width INT default 99 NOT NULL,
   height INT default 99 NOT NULL,
   status INT default 0 NOT NULL,
   htmltext TEXT NULL,
   
   CONSTRAINT OhCappic_PK PRIMARY KEY(ID)
);

