package museum.history.deerfield.centuries.database;

import java.lang.reflect.Array;
import edu.umass.ccbit.database.DbLoadableItemIDList;
import edu.umass.ccbit.jsp.Navigator;
import edu.umass.ccbit.util.ItemIDList;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.database.om.CollectionItemPeer;
import museum.history.deerfield.centuries.database.om.CollectionItem;
import museum.history.deerfield.centuries.database.om.Visitor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.torque.TorqueException;

public class MyCollection extends DbLoadableItemIDList {

  // attribute name under which an instance of MyCollection is stored in a session
  public static final String AttributeName_ = "myCollection";
  public static final String myOrder_       = "myorder";
  private Visitor visitor_                  = null;  // becomes non-null only when MyCollection becomes persistent

  private Log log = LogFactory.getLog( this.getClass() );
  
  /**
   * Permanently associates this collection with a visitor, i.e., stores 
   * the collection in the database.
   *
   * If the visitor already has a persistent collection, it is merged with 
   * the one in the session.  The merged collection is stored in both the 
   * database and the session, and any changes to the session collection must 
   * be immediately made to the database collection as well.
   *
   * Persistence ends only when the visitor gets logged out, at which time 
   * the session is invalidated and the session collection is trashed. 
   * (The visitor can explicitly log himself out, or his session can expire,
   * which has the same effect.)
   */
  public void persist( Visitor visitor ) {
  	visitor_ = visitor;
  	
  	try {
    	ItemIDList persistentItemIDs = CollectionItemPeer.selectItemList( visitor_ );
    	ItemIDList transientItemIDs  = itemIDs_;
  	
  	  // Flush the transient collection into the database.
      for (int i=0; i < transientItemIDs.size(); i++) {
        int transientItemID = transientItemIDs.getValue( i );
      
        if (!persistentItemIDs.contains( new Integer( transientItemID ) )) {
          CollectionItem permanentItem = new CollectionItem();
          permanentItem.setVisitor( visitor_ );
          permanentItem.setItemID( transientItemID );
          permanentItem.save();
        }      
      }
    
      // Refresh the transient collection, so that it matches the persistent one.
      itemIDs_ = CollectionItemPeer.selectItemList( visitor_ );
      
    } catch (TorqueException e) {
    	log.error( "MyCollection persist:  error getting item list from database", e );
    	
    } catch (Exception e) {
    	log.error( "MyCollection persist:  error inserting item into database", e );    	
    }    
  }

  public String attributeName() {
    return AttributeName_;
  }

  /**
   * create an empty collection
   */
  public MyCollection() {
    itemIDs_ = new ItemIDList();
  }
  
  /**
   * return size of the collection
   */
  public int numItems() {
    return itemIDs_.size();
  }

  /**
   * add a single item to the collection
   */
  public void addItem( int itemID ) {
    if (!itemIDs_.contains( new Integer( itemID )) ) {
    	// add it to the transient collection...
      itemIDs_.add( itemID );
      
      // ...and to the persistent collection.
      if (isPersistent()) {
        try {
        	CollectionItemPeer.insertItem( visitor_, itemID );
        } catch (Exception e) {
    	    log.error( "MyCollection addItem:  error inserting item into database", e );
    	  }
    	}
    }
  }

  /**
   * remove multiple items from the collection
   */
  public void removeItems( int[] itemIDs ) {
    // remove them from the transient collection...
    int nItems = Array.getLength( itemIDs );

    for (int i=0; i<nItems; i++) {
      int itemID = itemIDs[i];
      itemIDs_.remove( new Integer( itemID ) );
    }
    
    // ...and from the persistent collection.
    if (isPersistent()) {
      try {
      	CollectionItemPeer.deleteItemList( visitor_, itemIDs );
      } catch (TorqueException e) {
       log.error( "MyCollection removeItems:  error deleting items from database", e );
      }
    }
  }
  
  /**
   * pager link [unused; must be implemented because superclass declares it abstract]
   */
  public String pagerLink( String mainUrl, String resultsUrl, int pageNumber ) {
    Navigator nav=new Navigator();
    // this does not have intuitive breadcrumbs...
    return nav.toString();
  }

  /**
   * Is this collection persistent, i.e., should all changes be saved to the database?
   */
  public boolean isPersistent() {
  	return (visitor_ != null);
  }
}