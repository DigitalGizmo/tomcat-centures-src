USE centuriesForum;

DROP TABLE OhClip; /* */
CREATE TABLE OhClip (
   ID INT NOT NULL IDENTITY (1,1),
   shortName VARCHAR (32) NOT NULL,
   title VARCHAR (128) NULL,
   subtitle VARCHAR (128) NULL,
   transcription TEXT NULL,
   status INT default 0 NOT NULL,
   
   CONSTRAINT OhClip_PK PRIMARY KEY(ID)
);

