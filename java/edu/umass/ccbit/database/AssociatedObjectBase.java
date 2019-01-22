/*
 * Title:        AssociatedObjectBase.java<p>
 * Description:  base class for people/place/event objects<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: AssociatedObjectBase.java,v 1.21 2003/04/18 12:57:52 keith Exp $
 *
 *
 * $Id: AssociatedObjectBase.java,v 1.21 2003/04/18 12:57:52 keith Exp $
 *
 * $Log: AssociatedObjectBase.java,v $
 * Revision 1.21  2003/04/18 12:57:52  keith
 * overloaded objectUrl method to allow objectID to be explicitly specified when two people have same name
 *
 * Revision 1.20  2002/04/12 14:07:08  pbrown
 * fixed copyright info
 *
 * Revision 1.19  2001/04/23 14:48:44  pbrown
 * added idsentence for p&p
 *
 * Revision 1.18  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.17  2000/06/27 22:01:20  pbrown
 * fixed links to p/p in associated item dropdown
 *
 * Revision 1.16  2000/06/27 16:13:44  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.15  2000/06/14 18:41:12  pbrown
 * added alt text parameters
 *
 * Revision 1.14  2000/06/02 21:52:49  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.13  2000/05/30 21:34:24  pbrown
 * removed throw of CkcException from new_PeoplePlacesQueryItem
 *
 * Revision 1.12  2000/05/26 20:51:47  pbrown
 * added methods for generating image tags
 *
 * Revision 1.11  2000/05/25 22:25:06  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.10  2000/05/18 19:05:13  pbrown
 * added code to objectimage method
 *
 * Revision 1.9  2000/05/18 02:23:45  pbrown
 * added method to return string representation of item type
 *
 * Revision 1.8  2000/05/16 16:35:48  pbrown
 * added sorting by date for people/places
 *
 * Revision 1.7  2000/05/15 04:01:35  pbrown
 * added methods for displaying people/places object info on item page
 *
 * Revision 1.6  2000/05/14 03:31:48  pbrown
 * some changes to initialization due to new PeoplePlacesViewQuery class
 *
 * Revision 1.5  2000/05/13 03:55:02  pbrown
 * changed class factory and initialization methods
 *
 * Revision 1.4  2000/05/12 22:47:52  pbrown
 * many changes, rewritten for sprinta db driver
 *
 * Revision 1.3  2000/05/09 17:03:11  pbrown
 * made class factory method static
 *
 * Revision 1.2  2000/05/09 17:02:51  pbrown
 * started class factory method
 *
 * Revision 1.1  2000/05/09 13:21:11  pbrown
 * no change
 */

package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import edu.umass.ckc.util.CkcException;
import edu.umass.ccbit.jsp.PeoplePlacesItemPage;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.database.CollectionPerson;
import edu.umass.ccbit.database.CollectionPlace;
import edu.umass.ccbit.database.CollectionEvent;
import edu.umass.ccbit.database.CollectionObjectProducer;
import edu.umass.ckc.html.HtmlUtils;

public abstract class AssociatedObjectBase implements InstanceFromResult
{
  public String name_;
  public String accessionNumber_;
  public int itemID_;
  public int objectID_;
  public String fileName_;
  public String client_;
  public String idSentence_;

  public interface AssociatedItemType
  {
    public static final int Unknown_=0;
    public static final int People_=1;
     public static final int Place_=2;
     public static final int Event_=3;
     public static final int Producer_=4;
   }

   /**
    * initialize common fields in people/places object
    */
   protected final void base_init(ResultSet result) throws SQLException
   {
     name_           =result.getString(PeoplePlacesItems.fields.Name_);
     accessionNumber_=result.getString(PeoplePlacesItems.fields.AccessionNumber_);
     itemID_         =result.getInt(   PeoplePlacesItems.fields.ItemID_);
     fileName_       =result.getString(PeoplePlacesItems.fields.Filename_);
     client_         =result.getString(PeoplePlacesItems.fields.Client_);
     idSentence_     =result.getString(PeoplePlacesItems.fields.IdSentence_);
   }

   /**
    * initialize values from values returned by people/places view
    */
   public void PeoplePlacesItems_init(ResultSet result) throws SQLException
   {
     base_init(result);
     objectID_=result.getInt(PeoplePlacesItems.fields.ObjectID_);
   }

   /**
    * init single object from values in result set
    */
   public void init(ResultSet result) throws SQLException
   {
     base_init(result);
   }

   /**
    * item title
    */
   public String name()
   {
     return name_;
   }

  /**
   * id sentence
   */
  public String idSentence()
  {
    return idSentence_ != null ? idSentence_ : "";
  }

   /**
    * item type
    */
   protected abstract int itemType();

   /**
    * accession number
    */
   public String accessionNumber()
   {
     if (accessionNumber_ != null && accessionNumber_.length()>0)
     {
       return "#" + accessionNumber_;
     }
     else
     {
       return "";
     }
   }

   /**
    * item id
    */
   public int itemID()
   {
     return itemID_;
   }

   /**
    * object date
    * this will be overridden in derived classes
    */
   public abstract String objectDate();

   /**
    * object year
    * this is the year which is used to order objects chronologically
    */
    public abstract int objectYear();

   /**
    * link to object's item page
    */
   public String objectUrl() {
     StringBuffer url=new StringBuffer();
     url.append(PeoplePlacesItemPage.Jsp_);
     HtmlUtils.addToLink(PeoplePlacesItemPage.ItemType_, itemType(), true, url);
     HtmlUtils.addToLink(PeoplePlacesItemPage.ObjectID_, objectID_, false, url);
     return url.toString();
   }

   /**
    * link to object's item page, with object explicitly specified by objectID
    * (This method is needed when two people cannot be distinguished by their name.)
    */
   public String objectUrl( String objectID ) {
     StringBuffer url=new StringBuffer();
     url.append(PeoplePlacesItemPage.Jsp_);
     HtmlUtils.addToLink(PeoplePlacesItemPage.ItemType_, itemType(), true, url);
     HtmlUtils.addToLink(PeoplePlacesItemPage.ObjectID_, objectID, false, url);
     return url.toString();
   }

  /*
   * class factory..create object of desired type
   */
  public static AssociatedObjectBase newInstance(int type)
  {
    switch (type)
    {
      case AssociatedItemType.People_:
        return new CollectionPerson();
      case AssociatedItemType.Place_:
        return new CollectionPlace();
      case AssociatedItemType.Event_:
        return new CollectionEvent();
      case AssociatedItemType.Producer_:
        return new CollectionObjectProducer();
      default:
        return null;
    }
  }

  /*
   * returns string for use as text key to itemtype
   */
  public static String itemTypeKey(int type)
  {
    switch (type)
    {
      case AssociatedItemType.People_:
        return "Person";
      case AssociatedItemType.Place_:
        return "Place";
      case AssociatedItemType.Event_:
        return "Event";
      case AssociatedItemType.Producer_:
        return "Producer";
      default:
        return "";
    }
  }

  /*
   * class factory method...create new AssociatedObject from
   * a row in a database result set from people/places view
   */
  public static AssociatedObjectBase new_PeoplePlacesItemsObject(ResultSet result)
  throws SQLException
  {
    int type=result.getInt(PeoplePlacesItems.fields.ItemType_);
    AssociatedObjectBase obj=newInstance(type);
    if (obj!=null)
    {
      obj.PeoplePlacesItems_init(result);
    }
    return obj;
  }

  /**
   * table/view containing the object
   */
  protected abstract String table();

  /**
   * query to load individual object from database
   */
  protected String query(int objectID)
  {
    StringBuffer query=new StringBuffer();
    query.append("SELECT * FROM ").append(table());
    query.append(" WHERE ").append(PeoplePlacesItems.fields.ObjectID_);
    query.append("=").append(objectID);
    return query.toString();
  }

  /**
   * descriptive text about object to appear on web page
   */
  abstract public String webText();

  /**
   * object image
   */
  protected MrSidImage objectImage()
  {
    return new MrSidImage(client_, fileName_);
  }

  /**
   * bounded object image
   */
  public String boundedObjectImageTag(int width, int height)
  {
    return objectImage().scaledImageTag(width, height, MrSidImage.INSIDE_BOUNDING_BOX, name());
  }

  /**
   * scaled image tag
   */
  public String scaledObjectImageTag(int width, int height)
  {
    return objectImage().scaledImageTag(width, height, MrSidImage.SCALE_IMAGE, name());
  }

  /*
   * load an individual object from the database
   */
  public void load(Connection conn, int objectID) throws SQLException
  {
    Statement stmt=conn.createStatement();
    ResultSet result=stmt.executeQuery(query(objectID));
    if (result.next())
      init(result);
  }
}
