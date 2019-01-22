package museum.history.deerfield.centuries.database.om;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.BasePeer;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import edu.umass.ccbit.util.ItemIDList;

/** 
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Wed Oct 01 09:32:54 EDT 2003]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class CollectionItemPeer extends BaseCollectionItemPeer {

  private Log log = LogFactory.getLog( this.getClass() );


  public static ItemIDList selectItemList( Visitor visitor ) throws TorqueException {
    
    ItemIDList itemList = new ItemIDList();
    Criteria   criteria = new Criteria();
    criteria.add( VISITORID, visitor.getVisitorID() );
    List resultSet = doSelect( criteria );
    Iterator iter  = resultSet.iterator();

    while (iter.hasNext()) {
      CollectionItem collectionItem = (CollectionItem) iter.next();
      int itemID = collectionItem.getItemID();
      itemList.add( itemID );
    }
    
    return (itemList);
  }
  

  public static void deleteItemList( Visitor visitor, int[] itemIDs ) throws TorqueException {
  	
    // 20031022 KRU I tried using doDelete, but it didn't work, for reasons described here:
    // http://www.mail-archive.com/turbine-user@jakarta.apache.org/msg05982.html
    
    StringBuffer buf = new StringBuffer();
    
    buf.append( "DELETE FROM ").append( TABLE_NAME );
    buf.append( " WHERE " ).append( VISITORID );
    buf.append( " = " ).append( visitor.getVisitorID() );
    buf.append( " AND " ).append( ITEMID );
    buf.append(" IN (");
    
    int nItems = Array.getLength( itemIDs );

    for (int i=0; i < nItems; i++) {
      buf.append( itemIDs[i] );
      if (i < nItems-1) buf.append( ", " );
    }
    buf.append( ")" );
    String query = buf.toString();
    
    Torque.getDefaultDB();
    int nRowsDeleted = BasePeer.executeStatement( query, DATABASE_NAME );
  }


  public static void insertItem( Visitor visitor, int itemID ) throws TorqueException {

    Criteria criteria = new Criteria();
    criteria.add( VISITORID, visitor.getVisitorID() );
    criteria.add( ITEMID,    itemID );
    doInsert( criteria );
  }
}
