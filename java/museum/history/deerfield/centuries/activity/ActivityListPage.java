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
import museum.history.deerfield.centuries.database.CollectionItemInfoList;
import museum.history.deerfield.centuries.util.VectorUtil;

/**
 * Class from which activityList.jsp is extended.  This class and its JSP are modelled in
 * part on the CCBIT architecture, in which every JSP is backed by a servlet, rather than
 * the Struts/Torque architecture underpinning the rest of the Activities module.  
 *
 * The Activities module, which needs to store as well as present data, relies on a separate
 * database from the one behind the rest of the site, which is read-only.  However, rather
 * the store duplicate information about activity items in both databases, the Activities
 * module uses classes that extend HttpDbJspBase to query the read-only database.  
 * ActivityListPage is such a class.
 */
public abstract class ActivityListPage extends HttpDbJspBase {
  
  /**
   * For each DTO, get one itemID from the activity's list of itemIDs.  Then get the corresponding 
   * display info that item from the main database (imgTagParams), and put the imgTagParams into the 
   * Form that matches the DTO.
   */ 
  protected void load( HttpServletRequest request, HttpSession session ) {

    ActivityListDTO  activityListDTO  = (ActivityListDTO)  request.getAttribute( Constants.ACTIVITY_LIST_DTO );
    ActivityListForm activityListForm = (ActivityListForm) session.getAttribute( "activityListForm" );
    Vector           activityDTOs     = activityListDTO. getActivityDTOs();
    Vector           activityForms    = activityListForm.getActivityForms();
    int              numActivities    = activityForms.size();
    Vector           firstItemIDs     = new Vector( numActivities );    // first item of each activity, if activity has items
    Vector           activityIDs      = new Vector( numActivities );    // IDs of each activity
    Hashtable        itemIDsTable     = new Hashtable( numActivities ); // maps activityID -> firstItemID, if activity has items 
    Hashtable        formsTable       = new Hashtable( numActivities ); // maps activityID -> activityForm

    try {
      // System.out.println("numActivities in listPage: " +  numActivities);
      for (int i=0; i<numActivities; i++) {
        ActivityDTO activityDTO = (ActivityDTO) activityDTOs.get( i );
        int[]       itemIDs     = activityDTO.getItemIDs();

        // If the activity has any items, build the vectors and hashtables we'll use to gather
        // the info needed to display the activity's first item.
        if (Array.getLength( itemIDs ) > 0) {
          Integer      firstItemID  = new Integer( itemIDs[0] );
          ActivityForm activityForm = (ActivityForm) activityForms.get( i );
          Integer      activityID   = new Integer( activityForm.getActivityID() );
          firstItemIDs.add( firstItemID );
          activityIDs. add( activityID );
          itemIDsTable.put( activityID, firstItemID );
          formsTable.  put( activityID, activityForm );
        }
      }
  
      // Create a list of CollectionItemInfo's, one for each activity's first item (if there is one).
      CollectionItemInfoList itemInfoList = new CollectionItemInfoList();
      itemInfoList.load( connection_, VectorUtil.vectorToArray( firstItemIDs ) );
      
      Iterator iter = activityIDs.iterator();
      while (iter.hasNext()) {
        Integer            activityID   = (Integer) iter.next();
        ActivityForm       activityForm = (ActivityForm)       formsTable.  get( activityID );
        Integer            firstItemID  = (Integer)            itemIDsTable.get( activityID );
        CollectionItemInfo itemInfo     = (CollectionItemInfo) itemInfoList.get( firstItemID.intValue() );        

        try {
          CollectionImage img = itemInfo.itemImage_;
        
          // Initialize each first item, and store the item's image tag parameters in the activity's
          // form, from which they'll be read when it's time to build an HTML IMG tag.
          img.initImageInfo();
          Hashtable imgTagParams = img.getImgTagParams( 120,120 );
          activityForm.setFirstImgTagParams( imgTagParams );
          
        } catch (NullPointerException e) {
          System.out.println( "ActivityListPage load:  NullPointerException thrown by item with itemID " + firstItemID );
        }
      }
      
    } catch (SQLException e){
      System.out.println( "ActivityListPage load:  SQLException thrown by itemInfoList.load()" );
    }
  }
}