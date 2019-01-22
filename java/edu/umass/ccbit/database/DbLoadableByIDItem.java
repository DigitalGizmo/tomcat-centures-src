/**
 * Title:        DbLoadableItemFromID<p>
 * Description:  single item which can be loaded by id<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadableByIDItem.java,v 1.4 2003/12/03 21:01:01 keith Exp $
 *
 * $Log: DbLoadableByIDItem.java,v $
 * Revision 1.4  2003/12/03 21:01:01  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.3  2002/04/12 14:07:18  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/06/06 14:33:48  pbrown
 * many changes for searching
 *
 * Revision 1.1  2000/06/02 21:52:52  pbrown
 * many changes for searching..more to come
 *
 */
package edu.umass.ccbit.database;
//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DbLoadableByIDItem extends DbLoadableItem
{
  protected int itemID_;
  /**
   * view to load from
   */
  protected abstract String view();
  
  /**
   * load
   */
  public void load(Connection conn, int itemID) throws SQLException
  {
    itemID_=itemID;
    load(conn, DbQueries.selectByItemID(view(), itemID, null));
  }
}

