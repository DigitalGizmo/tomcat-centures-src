package museum.history.deerfield.centuries.activity;

import java.lang.reflect.Array;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import edu.umass.ccbit.jsp.HttpDbJspBase;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.database.CollectionItemInfo;
import edu.umass.ccbit.image.CollectionImage;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.database.Item;
import museum.history.deerfield.centuries.database.CollectionItemInfoList;

/**
 * Class from which activityView.jsp is extended.  This class and its JSP are modelled in
 * part on the CCBIT architecture, in which every JSP is backed by a servlet, rather than
 * the Struts/Torque architecture underpinning the rest of the Activities module.  
 *
 * The Activities module, which needs to store as well as present data, relies on a separate
 * database from the one behind the rest of the site, which is read-only.  However, rather
 * the store duplicate information about activity items in both databases, the Activities
 * module uses classes that extend HttpDbJspBase to query the read-only database.  
 * ActivityViewPage is such a class.
 */
public abstract class ActivityViewPage extends HttpDbJspBase {
  
  /**
   * Read the list of activity itemIDs from the DTO, get the corresponding display
   * info about those items from the main database, construct Item beans from the 
   * database, and put the list of beans into the activityMakeForm for display.
   */ 
  protected void load( HttpServletRequest request, HttpSession session ) {

    String       mode             = (String) request.getAttribute( "mode" );
    ActivityForm activityViewForm = (ActivityForm) request.getAttribute( "activityViewForm" );
    ActivityDTO  activityDTO      = null;

    if (("preview").equals( mode )) {
      activityDTO = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_MAKE_DTO ); 

    } else if (("view").equals( mode )) {
      activityDTO = (ActivityDTO) request.getAttribute( Constants.ACTIVITY_VIEW_DTO );

    } else { 
      System.out.println( "ActivityViewPage load:  invalid mode [" + mode + "]" );
    }
    
    int[]  itemIDs  = activityDTO.getItemIDs();
    int    numItems = Array.getLength( itemIDs );
    Vector items    = new Vector( numItems );
    
    try {
      CollectionItemInfoList itemInfoList = new CollectionItemInfoList();    
      if (numItems > 0) itemInfoList.load( connection_, itemIDs );

      for (int i=0; i<numItems; i++) {
        int itemID = itemIDs[i];
        
      	try {
          CollectionItemInfo itemInfo = itemInfoList.get( itemID );
          CollectionImage    img      = itemInfo.itemImage_;
          img.initImageInfo();
        
          String    name         = itemInfo.itemName();
          Hashtable imgTagParams = img.getImgTagParams( 150,150 );

          items.add( new Item( itemID, name, imgTagParams ) );
          
        } catch (NullPointerException e) {
          System.out.println( "ActivityViewPage load:  NullPointerException thrown by item with itemID " + itemID );
        }
      }
    } catch (SQLException e) {
      System.err.println( "ActivityViewPage load:  SQLException thrown by itemInfoList.load()" );
    }

    activityViewForm.setItems( items );

    /** Don Oct 22. Since we now save before the preview we have no way to ensure that
     * ACTIVITY_MAKE_DTO is cleared other than to clear it now.
     * (it used to be cleared by ActivitySaveAction which was triggered by the links
     * at the bottom of the preview page
     * But now we need to create a short-lived version of the DTO to be used by
     * ActivitySaveStatusAction.
     */
    session.removeAttribute( Constants.ACTIVITY_MAKE_DTO );
    session.setAttribute( Constants.ACTIVITY_STATUS_DTO, activityDTO );
  }
}