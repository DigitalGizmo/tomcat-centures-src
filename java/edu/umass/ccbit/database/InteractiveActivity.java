
/**
 * Title:        InteractiveActivity<p>
 * Description:  a flash file and description associated with an item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.ServletContext;

public class InteractiveActivity extends DbLoadableItemList
{
  public static final String ActivityView_ = "Web_InteractiveActivityView";
  protected ActivityItem activityItem_;

  protected class ActivityItem implements InstanceFromResult
  {
    public int activityID_;
    public int itemID_;
    public String activityName_;
    public String activityDescription_;
    public String filename_;
    public int height_;
    public int width_;
    public String nonflashPath_;

    public void init(ResultSet result) throws SQLException
    {
      activityID_=result.getInt("ActivityID");
      itemID_=result.getInt("ItemID");
      activityName_=result.getString("ActivityName");
      activityDescription_=result.getString("ActivityDescription");
      filename_=result.getString("ActivityPath");
      height_=result.getInt("Height");
      width_=result.getInt("Width");
      nonflashPath_=result.getString("nonflashpath");
     }
  }

  /**
   * load the data from the database
   * @param conn the database connection manager
   * @param context the servlet context to save the list of activities to
   * @param itemID the id of the item to search for activities with
   */
  public void load(Connection conn, ServletContext context, int itemID) throws SQLException
  {
    // remove attribute because of DbLoadableItemList check
    context.removeAttribute(getClass().getName());
    load(conn, context, query(itemID));
  }

  /**
   * construct a query
   * @param itemID id to search by
   * @return query string
   */
  protected String query(int itemID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(ActivityView_);
    buf.append(" where ItemID=").append(itemID);
    return buf.toString();
  }

  /**
   * instance from resultset
   * @param result the result set generated from the query
   * @return ActivityItem
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    ActivityItem activityItem = new ActivityItem();
    activityItem.init(result);
    return activityItem;
  }

  /**
   * checks to see if there is an activity for an item within the vector
   * @param itemID id of item
   * @return whether or not there is an activity for that item
   */
  public boolean hasActivity(int itemID)
  {
    ActivityItem actItem;
    Enumeration e = items_.elements();
    while(e.hasMoreElements())
    {
      actItem = (ActivityItem) e.nextElement();
      if(actItem.itemID_==itemID)
        return true;
    }
    return false;
  }

  /**
   * activity name
   * @param i the index of the activity in the vector of activities
   * @return the name of the activity
   */
  public String activityName(int i)
  {
    return ((ActivityItem)get(i)).activityName_;
  }

  /**
   * activity description
   * @param i the index of the activity in the vector of activities
   * @return the description of the activity
   */
  public String activityDescription(int i)
  {
    return ((ActivityItem)get(i)).activityDescription_;
  }

  /**
   * activity path
   */
  public String activityPath(int i)
  {
    return ((ActivityItem)get(i)).filename_;
  }

  /**
   * nonflash activity path
   */
  public String nonflashPath(int i)
  {
    return ((ActivityItem)get(i)).nonflashPath_;
  }

  /**
   * activity height
   */
  public int height(int i)
  {
    return ((ActivityItem)get(i)).height_;
  }

  /**
   * activity width
   */
  public int width(int i)
  {
    return ((ActivityItem)get(i)).width_;
  }

  /**
   * activity itemid
   */
  public int itemID(int i)
  {
    if(!items_.isEmpty() && items_.elementAt(i)!=null)
      return ((ActivityItem)get(i)).itemID_;
    else
      return -1;
  }

  /**
   * number of activities
   */
  public int numItems()
  {
    return items_.size();
  }
}
