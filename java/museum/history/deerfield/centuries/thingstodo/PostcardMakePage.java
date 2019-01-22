package museum.history.deerfield.centuries.thingstodo;

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

import org.apache.struts.action.DynaActionForm;
import edu.umass.ccbit.database.MainCollectionItem;
import edu.umass.ccbit.database.Glossary;
import edu.umass.ccbit.database.PeoplePlacesItems;

/**
 * based on activity/ActivityViewPage
 * 
 * Class from which postcardMake.jsp is extended.  This class and its JSP are modelled in
 * part on the CCBIT architecture, in which every JSP is backed by a servlet, rather than
 * the Struts/Torque architecture underpinning the rest of the Activities module.  
 * per KU notes in activity/ActivityViewPage etc. 
 *
 * I'm diverging from Keith's model here in that I'm using DynaActionForms, and not DTOs
 * Getting the itemid here directly from the form
 */
public abstract class PostcardMakePage extends HttpDbJspBase {
  
  /**
   * Get the itemid from the form, get the corresponding display
   * info about that item from the main database, 
   * get name and imgTagParms from the database and put these into the form for display.
   * 
   * Even though there's only one item in this case, use Keith's itemInfoList
   * so as not to reinvent all of the machinery.
   */ 

  /** now copying from ccbit/jsp/ImagePage
   */
  protected MainCollectionItem item_;
  // glossary
  protected Glossary glossary_=null;
  // people and places
  protected PeoplePlacesItems ppitems_=null;

  /** Get a single item image, label etc.
   * Patterned after Keith's ActivityList
   * A bit of a hack to use CollectionItemInfoList to get the info for just one imageTagParm
   * but it doesn't seem worth writing a version of that class that just gets one.
   */
  protected void load( HttpServletRequest request, HttpSession session ) {
    // use dynaForm directly
    DynaActionForm  dynaForm        = (DynaActionForm)request.getAttribute( "postcardForm" );

    // int itemID  = Integer.parseInt(request.getParameter("itemid"));
    // get itemID from form
    int itemID  = ((Integer)dynaForm.get( "itemid" )).intValue();
    // System.out.println("**** postcardMakePage itemid:" + itemID);
    
    // if we need imageTagParams - we'd use Keith's machinery
    // create an array of one to use the infolist class

    //int[]  itemIDs  = {itemID};
    //int    numItems = Array.getLength( itemIDs );
   // Vector items    = new Vector( numItems );
    
    try {
      /*
      CollectionItemInfoList itemInfoList = new CollectionItemInfoList();    
      if (numItems > 0) itemInfoList.load( connection_, itemIDs );

      try {
        CollectionItemInfo itemInfo = itemInfoList.get( itemID );
        CollectionImage    img      = itemInfo.itemImage_;
        img.initImageInfo();
      
        Hashtable imgTagParams = img.getImgTagParams( 500,500 );

        dynaForm.set("imgTagParams", imgTagParams);
        //items.add( new Item( itemID, name, imgTagParams ) );
        
      } catch (NullPointerException e) {
        System.out.println( "ActivityViewPage load:  NullPointerException thrown by item with itemID " + itemID );
      }
      */
      // other than imgTagParams we just need text. Get it more directly the ccbit item page way
      // based on ItemPage.java
      // need item object (may overlap with itemInfo)
      item_= new MainCollectionItem();
      item_.load(connection_, itemID);
      // get label text - omit the ImagePage step of parsing for glossary & people terms.
      // String label = parseLabel(item_.webLabel());
      String label = item_.webLabel();
      String name  = item_.itemName_;
      // hmm, the ItemPage approach gives a whole tag. I'd rather get just the imgTagParams
      String imageTag = item_.objectImage(400, 400, 0);

      dynaForm.set("label", label);
      dynaForm.set("name", name);
      dynaForm.set("imageTag", imageTag);
    
    } catch (SQLException e) {
      System.err.println( "ActivityViewPage load:  SQLException thrown by itemInfoList.load()" );
    }

   }
}