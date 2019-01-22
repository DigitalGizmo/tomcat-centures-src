
/**
 * Title:        ChronologyEvents<p>
 * Description:  chronologies read from the database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;

public class ChronologyEvents extends DbLoadableItemList
{
  public static final String tableName_="ChronologyItems";
  public static final String linkTable_="ChronologyLink";

  protected class ChronologyEvent implements InstanceFromResult
  {
    // event data members
    public int eventID_;
    public int year_;
    public String description_;
    public String imagePath_;

    public void init(ResultSet result) throws SQLException
    {
      // initialize data members from result set
      eventID_ = result.getInt("ChronologyItemID");
      year_ = result.getInt("Year");
      description_ = result.getString("Description");
      imagePath_ = result.getString("ImagePath");
    }
  }

  /**
   * new instance
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    // default body of method
    ChronologyEvent event = new ChronologyEvent();
    event.init(result);
    return event;
  }

  /**
   * get event id
   */
  public int getEventID(int index)
  {
    return getEvent(index).eventID_;
  }

  /**
   * get year of event
   */
  public int getEventYear(int index)
  {
    return getEvent(index).year_;
  }

  /**
   * get description of event
   */
  public String getEventDesc(int index)
  {
    return getEvent(index).description_;
  }

  /**
   * get image path for associated image
   */
  public String getEventImagePath(int index)
  {
    return getEvent(index).imagePath_;
  }

  /**
   * get a chronology event from the vector
   */
  private ChronologyEvent getEvent(int index)
  {
    return (ChronologyEvent) get(index);
  }

  /**
   * construct a query using the chronologyID to retrieve the relevant events
   */
  private String query(int chronologyID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(tableName_);
    buf.append(" WHERE ChronologyItemID IN (SELECT ChronologyItemID FROM ").append(linkTable_);
    buf.append(" WHERE ChronologyID=").append(chronologyID).append(") ORDER BY YEAR");
    return buf.toString();
  }

  /**
   * load the chronology's events
   * @param conn the connection to the db
   * @param context the servlet context
   * @param chronologyID the id of the chronology
   */
  public void load(Connection conn, int chronologyID) throws SQLException
  {
    load(conn, query(chronologyID));
  }
}
