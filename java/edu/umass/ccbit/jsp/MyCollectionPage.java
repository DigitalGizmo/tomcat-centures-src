/**
 * Title:        MyCollectionPage<p>
 * Description:  base class for the user's MyCollection page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
 
/* NOTE:  20031015 KRU
 *
 * MyCollectionPage originally used a pager to display large collections.  Once the interface changed 
 * to accomodate persistent collections, it was decided that a pager would be confusing.  (For example,
 * what does "Tag All Items" mean for a multipage collection?  Tag all items on this page, or on
 * every page?)  However, since the pager provides needed functionality -- like sorting -- I kept in
 * the code, but made it transparent to the user by setting the page size arbitrarily large.  In other
 * words, no matter how big a collection gets, it will always fit on a single page.
 */
package edu.umass.ccbit.jsp;

import java.util.Vector;
import java.util.Hashtable;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.database.MainCollectionItemsPager;
import museum.history.deerfield.centuries.database.MyCollection;
import museum.history.deerfield.centuries.database.Item;
import museum.history.deerfield.centuries.mycollection.MyCollectionForm;

public abstract class MyCollectionPage extends ItemOrderedListPager {
	
  private MyCollection collection_;
  private int itemID_;
  private int order_;
  private static final String ItemPageBaseUrl_ = "my.itempagebaseurl";
  private static final int orderDefault_ = -1;
  private String itemPageBaseUrl_;
  public static final String jsp_="mycollection.jsp";

  protected String itemPageBaseUrl() {
    return itemPageBaseUrl_;
  }

  protected String orderByLink( int mode ) {
  	
    StringBuffer buf = new StringBuffer();
    String linkText = "Order Collected";
    String itemText = "Item Name";
    String dateText = "Date";
    if(mode<0)
      linkText = itemText;
    if(mode>0)
      linkText = dateText;
    HtmlUtils.addToLink("order", mode, true, buf);
    return HtmlUtils.anchor(jsp_+buf.toString(), linkText);
  }

  /**
   * returns itemID for item at specified index
   */
  protected int getItemID( int nItem ) {
    return (pager_.itemID( nItem ));
  }
  
  protected int numItems() {
    return collection_.numItems();
  }

  protected int lowItemIndex() {
    return pager_.lowerBound();
  }

  protected int highItemIndex() {
    return pager_.upperBound();
  }

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit() {
    super.jspInit();
    itemPageBaseUrl_=initParams_.getString(ItemPageBaseUrl_, "");
  }

  /**
   * load the collection from the session and perform any operations
   */
  protected void load( HttpServletRequest request, HttpSession session ) throws SQLException {
    collection_ = (MyCollection) session.getAttribute( MyCollection.AttributeName_ );
    if (collection_ == null) {
    	collection_ = new MyCollection();
      session.setAttribute( MyCollection.AttributeName_, collection_ );
    }
    
    session.setAttribute( MyCollection.myOrder_, new Integer( order_ ) );
    // kludge:  MyCollection does not page, but it's easiest to keep using a pager.
    // So we just temporarily set the number of items per page to an arbitrarily large number.
    session.setAttribute( MainCollectionItemsPager.PerPage_, new Integer( 100000 ));
    load( session, collection_ );
    session.removeAttribute( MainCollectionItemsPager.PerPage_);
    session.setAttribute( MyCollection.myOrder_, new Integer( 0 ) );
    
    // If the collection's not empty, put an ActionForm into the request so that
    // the action for the My Collection page can use it to populate the page.
    if (numItems() > 0) {
      MyCollectionForm myCollectionForm = getMyCollectionForm();
      request.setAttribute( "myCollectionForm", myCollectionForm );
    }
  }

  /**
   * Creates, populates, and returns a form bean containing all items in the collection.
   * Collection must be non-empty.
   */
  private MyCollectionForm getMyCollectionForm() {
    MyCollectionForm myCollectionForm = new MyCollectionForm();
    
    Vector items = new Vector();
    CollectionImage img;
    Hashtable imgTagParams = null;
    int itemID;
    String url, name, date, accessionNumber, text = null;
    
    for (int i=lowItemIndex(); i<=highItemIndex(); i++) {
      img             = pager_.imageObj( i );
      imgTagParams    = img.getImgTagParams( 150,150 );      	
      url             = resultURL( i );
      name            = itemName( i );      	
      itemID          = getItemID( i );      	      	
      date            = resultDate( i );
      accessionNumber = resultAccessionNumber( i );
      text            = resultText( i );

      items.add( new Item( itemID, name, imgTagParams, url, date, accessionNumber, text ) );
    }
 
    myCollectionForm.setItems( items );
    return (myCollectionForm);
  }
  
  /**
   * parse the parameters for required information
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException {
  	
    super.parseRequestParameters(request);
    itemID_ = servletParams_.getInt( "itemid", -1 );
    order_  = servletParams_.getInt( "order",   0 );
  }
}