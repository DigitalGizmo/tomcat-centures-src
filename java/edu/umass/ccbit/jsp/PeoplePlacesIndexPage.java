/*
 * Title:        PeoplePlacesIndexPage<p>
 * Description:  base class for people/places/events main page...mainly for counting items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: PeoplePlacesIndexPage.java,v 1.4 2002/04/12 14:08:05 pbrown Exp $
 *
 * $Log: PeoplePlacesIndexPage.java,v $
 * Revision 1.4  2002/04/12 14:08:05  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2000/06/27 16:14:40  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.2  2000/06/02 21:52:56  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.1  2000/05/18 02:32:53  pbrown
 * changes due to moved packages
 *
 */

package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.AssociatedObjectBase;
import edu.umass.ckc.util.Parameters;
import java.lang.StringBuffer;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;
import edu.umass.ckc.util.CkcException;
import java.lang.Exception;
import java.io.IOException;

public abstract class PeoplePlacesIndexPage extends HttpDbJspBase
{
  protected static final String view_="Web_PeoplePlacesItemCountView";
  Parameters itemCount_;

  /**
   * query to count people/places items
   */
  public String query()
  {
    StringBuffer query=new StringBuffer();
    // select * from Web_PeoplePlacesItemCountView
    /*
      CREATE VIEW dbo.Web_PeoplePlacesItemCountView
      AS
      SELECT ItemType, COUNT(ItemType) AS NumItems
      FROM Web_PeoplePlacesView
      WHERE Ready = 1
      GROUP BY ItemType
    */
    query.append("SELECT * FROM ").append(view_);
    return query.toString();
  }

  /**
   * number of people (and obj producer entries)
   */
  public int numPeople()
  {
    return itemCount_.getInt(AssociatedObjectBase.itemTypeKey(AssociatedObjectBase.AssociatedItemType.People_), 0) +
    itemCount_.getInt(AssociatedObjectBase.itemTypeKey(AssociatedObjectBase.AssociatedItemType.Producer_), 0);
  }

  /**
   * number of places
   */
  public int numPlaces()
  {
    return itemCount_.getInt(AssociatedObjectBase.itemTypeKey(AssociatedObjectBase.AssociatedItemType.Place_), 0);
  }

  /**
   * number of events
   */
  public int numEvents()
  {
    return itemCount_.getInt(AssociatedObjectBase.itemTypeKey(AssociatedObjectBase.AssociatedItemType.Event_), 0);
  }

  /**
   * total
   */
  public int total()
  {
    return numPeople() + numPlaces() + numEvents();
  }

  /**
   * gets list of people/places/events from database
   */
  protected void load() throws CkcException, Exception
  {
    Statement stmt=connection_.createStatement();
    ResultSet result=stmt.executeQuery(query());
    itemCount_=new Parameters();
    while (result.next())
    {
      itemCount_.put(AssociatedObjectBase.itemTypeKey(result.getInt(1)), result.getString(2));
    }
  }
}
