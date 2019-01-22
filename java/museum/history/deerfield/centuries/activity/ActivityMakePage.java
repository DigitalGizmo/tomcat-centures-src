package museum.history.deerfield.centuries.activity;

import java.lang.reflect.Array;
import java.util.Vector;
import java.util.Iterator;
import java.sql.SQLException;
import edu.umass.ccbit.jsp.HttpDbJspBase;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import edu.umass.ccbit.database.CollectionItemInfo;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.database.Item;
import museum.history.deerfield.centuries.database.CollectionItemInfoList;
import museum.history.deerfield.centuries.util.VectorUtil;

/**
 * Class from which makeActivity.jsp is extended.  This class and its JSP are modelled in
 * part on the CCBIT architecture, in which every JSP is backed by a servlet, rather than
 * the Struts/Torque architecture underpinning the rest of the Activities module.  
 *
 * The Activities module, which needs to store as well as present data, relies on a separate
 * database from the one behind the rest of the site, which is read-only.  However, rather
 * the store duplicate information about activity items in both databases, the Activities
 * module uses classes that extend HttpDbJspBase to query the read-only database.  
 * ActivityMakePage is such a class.
 */
public abstract class ActivityMakePage extends HttpDbJspBase {
  
  /**
   * Read the list of activity itemIDs from the DTO, get the corresponding display
   * info about those items from the main database, construct Item beans from the 
   * database, and put the list of beans into the activityMakeForm for display.
   */ 
  protected void load( HttpServletRequest request, HttpSession session ) {
    
    try {
      ActivityDTO activityDTO = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
      int[]       itemIDs     = activityDTO.getItemIDs();
      int         numItems    = Array.getLength( itemIDs );
      Vector      items       = new Vector( numItems );
      
      CollectionItemInfoList itemInfoList = new CollectionItemInfoList();    
      if (numItems > 0) itemInfoList.load( connection_, itemIDs );

      for (int i=0; i<numItems; i++) {
        int itemID  = itemIDs[i];
        String name = itemInfoList.get( itemID ).itemName();
        items.add( new Item( itemID, name ) );
      }
      
      ActivityForm activityMakeForm = (ActivityForm) request.getAttribute( "activityMakeForm" );
      activityMakeForm.setItems( items );
      
    } catch (SQLException e){
      System.out.println( "ActivityListPage load:  SQLException thrown by itemInfoList.load()" );
    }
  }
}