
  public static String getOrdinal(int ID) {   
      OhAssociation   ohAssociation   = null;
      try {
          ohAssociation  = retrieveByPK( ID );
      } catch( TorqueException e ) {
        System.out.println( "Torque exception in om/OhAssociationPeer getOrdinal: " + e.getMessage() );
        return (null);
      }
              return ohAssociation.getOrdinal();
   }

  public String getAssociationOrdinal() {
  String associationOrdinal = null;
  associationOrdinal = OhAssociationPeer.getOrdinal(getAssocWithID());

  return associationOrdinal;
}

