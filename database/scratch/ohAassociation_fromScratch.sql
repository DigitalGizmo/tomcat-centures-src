USE centuriesForum;

/*DROP TABLE OhAssociation;  */
CREATE TABLE OhAssociation (
  ID INT NOT NULL IDENTITY (1,1),
  assocTypeID INT DEFAULT 0 NOT NULL,
  associateID INT DEFAULT 0 NOT NULL,
  assocWithID INT DEFAULT 0 NOT NULL,
  ordinal     SMALLINT DEFAULT 0 NOT NULL,

  CONSTRAINT OhAssociation_PK PRIMARY KEY(ID)
);

/*  SQL server instert syntax different, do it by hand for now  
INSERT INTO OhAssociation VALUES 

(1, 1, 1, 1, 2),
(2, 1, 1, 2, 1),
(3, 1, 2, 3, 1);
*/
