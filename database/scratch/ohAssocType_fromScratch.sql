USE centuriesForum;

DROP TABLE OhAssocType; /* */
CREATE TABLE OhAssocType (
  ID INT DEFAULT 0 NOT NULL,
  assocName varchar(32) NOT NULL,
  associate varchar(16) NOT NULL,
  assocWith varchar(16) NOT NULL,

  CONSTRAINT OhAssocType_PK PRIMARY KEY(ID)
);

/*  SQL server instert syntax different, do it by hand for now  
INSERT INTO OhAssocType VALUES 
(1,'clipStory', 'clip', 'story');
(2,'cappicStory', 'cappic', 'story');
(3,'resourceStory', 'resource', 'story');
*/

