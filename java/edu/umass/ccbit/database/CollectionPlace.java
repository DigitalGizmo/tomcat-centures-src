/*
 * Title:        CollectionPlace<p>
 * Description:  collection place<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CollectionPlace.java,v 1.11 2002/04/12 14:07:15 pbrown Exp $
 *
 * $Id: CollectionPlace.java,v 1.11 2002/04/12 14:07:15 pbrown Exp $
 *
 * $Log: CollectionPlace.java,v $
 * Revision 1.11  2002/04/12 14:07:15  pbrown
 * fixed copyright info
 *
 * Revision 1.10  2000/06/27 16:13:45  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.9  2000/06/02 21:52:51  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.8  2000/05/18 02:32:52  pbrown
 * changes due to moved packages
 *
 * Revision 1.7  2000/05/16 16:35:50  pbrown
 * added sorting by date for people/places
 *
 * Revision 1.6  2000/05/15 04:01:36  pbrown
 * added methods for displaying people/places object info on item page
 *
 * Revision 1.5  2000/05/14 03:31:49  pbrown
 * some changes to initialization due to new PeoplePlacesItems class
 *
 * Revision 1.4  2000/05/13 03:55:03  pbrown
 * changed class factory and initialization methods
 *
 * Revision 1.3  2000/05/12 22:47:53  pbrown
 * many changes, rewritten for sprinta db driver
 *
 * Revision 1.2  2000/05/09 13:24:38  pbrown
 * removed filesystem.attributes
 *
 * Revision 1.1  2000/05/09 13:22:43  pbrown
 * added files for people/places
 *
 *
 */

package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.util.CollectionDate;

public class CollectionPlace extends AssociatedObjectBase
{
  public int date_;
  public String history_;
  public String relevance_;

  /**
   * item type
   */
  protected int itemType()
  {
    return AssociatedObjectBase.AssociatedItemType.Place_;
  }

  /*
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
   * initialize date
   */
  protected void initDate(ResultSet result) throws SQLException
  {
    date_=result.getInt(PeoplePlacesItems.fields.CircaDate_);
  }

  /**
   * initialize from people/places view
   */
  public void PeoplePlacesItems_init(ResultSet result) throws SQLException
  {
    super.PeoplePlacesItems_init(result);
    initDate(result);
  }

  protected static final String view_="Web_CollectionPlacesView";

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
    static final String History_="History";
    static final String Relevance_="Relevance";
  }

  /**
   * init values for single object
   */
  public void init(ResultSet result) throws SQLException
  {
    super.init(result);
    initDate(result);
    history_=result.getString(fields.History_);
    relevance_=result.getString(fields.Relevance_);
  }

  /**
   * web text
   */
  public String webText()
  {
    return history_;
  }
}
