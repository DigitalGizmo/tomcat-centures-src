/**
 * Title:        DbLoadableByID<p>
 * Description:  base class for objects which load items from database based on id<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadableByID.java,v 1.5 2003/12/03 21:00:59 keith Exp $
 *
 * $Log: DbLoadableByID.java,v $
 * Revision 1.5  2003/12/03 21:00:59  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.4  2002/04/12 14:07:18  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2000/06/02 21:52:51  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.2  2000/05/30 21:35:45  pbrown
 * extends new base class DbLoadable
 *
 * Revision 1.1  2000/05/24 06:11:28  pbrown
 * first implementation of item pages with images and object viewer
 *
 * 
 */

package edu.umass.ccbit.database;
//import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;
import java.lang.StringBuffer;

public abstract class DbLoadableByID extends DbLoadableItemList
{
  protected int itemID_;
  
  /**
   * query to get items from the database
   */
  protected String query(int itemID)
  {
    return DbQueries.selectByItemID(view(), itemID, orderBy());
  }

  /**
   * view name
   */
  protected abstract String view();

  /**
   * order by
   */
  protected String orderBy()
  {
    return null;
  }
  
  /**
   * load data from database
   */
  public void load(Connection conn, int itemID) throws SQLException
  {
    // save connection in instance var for use by derived classes
    connection_=conn;
    itemID_=itemID;
    load(connection_, query(itemID));
  }
}
