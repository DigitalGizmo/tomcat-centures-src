/*
 * Title:        CollectionEvent.java<p>
 * Description:  collection event<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CollectionEvent.java,v 1.13 2003/12/29 19:14:00 keith Exp $
 *
 *
 * $Log: CollectionEvent.java,v $
 * Revision 1.13  2003/12/29 19:14:00  keith
 * fixed view_, which got changed erroneously
 *
 * Revision 1.12  2003/12/03 21:00:59  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.11  2002/04/12 14:07:12  pbrown
 * fixed copyright info
 *
 * Revision 1.10  2000/06/27 16:13:44  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.9  2000/06/02 21:52:49  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.8  2000/05/19 21:41:59  pbrown
 * added classes for item pages
 *
 * Revision 1.7  2000/05/18 02:32:51  pbrown
 * changes due to moved packages
 *
 * Revision 1.6  2000/05/16 16:35:49  pbrown
 * added sorting by date for people/places
 *
 * Revision 1.5  2000/05/15 04:01:35  pbrown
 * added methods for displaying people/places object info on item page
 *
 * Revision 1.4  2000/05/14 03:31:48  pbrown
 * some changes to initialization due to new PeoplePlacesItems class
 *
 * Revision 1.3  2000/05/13 03:55:03  pbrown
 * changed class factory and initialization methods
 *
 * Revision 1.2  2000/05/12 22:47:53  pbrown
 * many changes, rewritten for sprinta db driver
 *
 * Revision 1.1  2000/05/09 13:25:15  pbrown
 * fixed collection places and added collection event
 *
 *
 *
 */

package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.util.CollectionDate;

public class CollectionEvent extends AssociatedObjectBase
{
  public CollectionDate date_;
  public String description_;

  protected static final String view_="Web_HistoricalEventsView";
  /* fields in this view which are not included in people/places view */
  public interface fields
  {
    static final String Description_="Description";
  }

  /**
   * item type
   */
  protected int itemType()
  {
    return AssociatedObjectBase.AssociatedItemType.Event_;
  }

  /**
   * table/view name
   */
  protected String table()
  {
    return view_;
  }

  /**
   * object date
   */
  public String objectDate()
  {
    return date_.toString();
  }

  /**
   * object year
   */
   public int objectYear()
   {
     return date_.comparisonYear();
   }

  /**
   * initialize date
   */
  protected void initDate(ResultSet result) throws SQLException
  {
    date_=new CollectionDate(PeoplePlacesItems.fields.ItemDate_, result);
  }

  /**
   * init values for single object
   */
  public void init(ResultSet result) throws SQLException
  {
    super.init(result);
    initDate(result);
    description_=result.getString(fields.Description_);
  }

  /**
   * web text
   */
  public String webText()
  {
    return description_;
  }

  /**
   * initialize values from people/places view
   */
  public void PeoplePlacesItems_init(ResultSet result) throws SQLException
  {
    super.PeoplePlacesItems_init(result);
    initDate(result);
  }
}

