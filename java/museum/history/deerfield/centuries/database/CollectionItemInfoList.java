package museum.history.deerfield.centuries.database;

import java.lang.reflect.Array;
import java.util.Hashtable;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import edu.umass.ccbit.database.DbLoadable;
import edu.umass.ccbit.database.CollectionItemInfo;
import edu.umass.ccbit.database.InstanceFromResult;

public class CollectionItemInfoList extends DbLoadable {

  protected Hashtable itemInfos_;

  /**
   * instantiates object from database result
   */
  protected InstanceFromResult newInstance( ResultSet result ) throws SQLException {
    CollectionItemInfo itemInfo = new CollectionItemInfo();
    itemInfo.init( result );
    return (itemInfo);
  }
  
  /**
   * load data from database
   */
  public void load( Connection conn, int[] itemIDs ) throws SQLException {
    load( conn, query( itemIDs ) );
  }

  /**
   * initialize data in this object based on contents of result set
   */
  protected void initFromResult( ResultSet result ) throws SQLException {
    itemInfos_ = new Hashtable();
    
    while (result.next()) {
    	CollectionItemInfo itemInfo = (CollectionItemInfo) newInstance( result );
      itemInfos_.put( new Integer( itemInfo.itemID() ), itemInfo );
    }
  }

  protected String query( int[] itemIDs )  {

    StringBuffer buf = new StringBuffer();
    buf.append( "SELECT * FROM " ).append( CollectionItemInfo.view_ );
    buf.append( " WHERE " ).append( CollectionItemInfo.fields.ItemID_ );
    buf.append( " IN (" );
  	int numItems = Array.getLength( itemIDs );
    
    for (int i=0; i<numItems; i++) {
      buf.append( itemIDs[i] );
      if (i < numItems-1) buf.append( ", " );
    }
    buf.append( ")" );
    return buf.toString();
  }
  
  public CollectionItemInfo get( int itemID ) {
  	CollectionItemInfo itemInfo = (CollectionItemInfo) itemInfos_.get( new Integer( itemID ) );
  	return (itemInfo);
  }
  
}
