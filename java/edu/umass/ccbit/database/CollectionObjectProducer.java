/*
 * Title:        CollectionObjectProducer<p>
 * Description:  collection object producer<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version  $Id: CollectionObjectProducer.java,v 1.10 2002/04/12 14:07:14 pbrown Exp $
 *
 * $Log: CollectionObjectProducer.java,v $
 * Revision 1.10  2002/04/12 14:07:14  pbrown
 * fixed copyright info
 *
 * Revision 1.9  2000/06/27 16:13:45  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.8  2000/06/02 21:52:50  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.7  2000/05/18 02:32:52  pbrown
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
 * Revision 1.1  2000/05/09 13:22:42  pbrown
 * added files for people/places
 *
 *
 */

package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ckc.util.CkcException;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.util.CollectionDate;

public class CollectionObjectProducer extends AssociatedObjectBase
{
  public int date_;
  public String description_;

  /**
   * item type
   */
  protected int itemType()
  {
    return AssociatedObjectBase.AssociatedItemType.Producer_;
  }

  /**
   * display date
   */
  public String objectDate()
  {
    return CollectionDate.formatYear(date_);
  }

  /**
   * object year
   */
   public int objectYear()
   {
     return date_;
   }

  /**
   * init date
   */
  protected void initDate(ResultSet result) throws SQLException
  {
    date_=result.getInt(PeoplePlacesItems.fields.CircaDate_);
  }

  /**
   * init from people/places view
   */
  public void PeoplePlacesItems_init(ResultSet result) throws SQLException
  {
    super.PeoplePlacesItems_init(result);
    initDate(result);
  }

  protected static final String view_="Web_ObjectProducerView";

  /**
   * table/view name
   */
  protected String table()
  {
    return view_;
  }

  /* fields in view which are not included in people/places view */
  public interface fields
  {
    static final String Description_="Description";
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
}

