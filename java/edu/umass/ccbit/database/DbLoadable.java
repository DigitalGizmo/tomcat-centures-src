/**
 * Title:        DbLoadable<p>
 * Description:  base class for objects which load items from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadable.java,v 1.7 2002/04/12 14:07:17 pbrown Exp $
 *
 * $Log: DbLoadable.java,v $
 * Revision 1.7  2002/04/12 14:07:17  pbrown
 * fixed copyright info
 *
 * Revision 1.6  2001/03/08 22:31:27  pbrown
 * changes for advanced search
 *
 * Revision 1.5  2000/06/14 18:40:59  pbrown
 * closes result set and statement at end of load method
 *
 * Revision 1.4  2000/06/06 14:33:48  pbrown
 * many changes for searching
 *
 * Revision 1.2  2000/06/01 14:51:33  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.1  2000/05/30 21:37:07  pbrown
 * new base class for items loadable from db, and beginnings of search/search
 * results
 *
 */

package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Connection;

public abstract class DbLoadable
{
  protected Connection connection_;
  /**
   * initialize data in this object based on contents of result set
   */
  protected abstract void initFromResult(ResultSet result) throws SQLException;

  /**
   * load the data
   */
  public void load(Connection conn, String query) throws SQLException
  {
    connection_=conn;
    Statement stmt=conn.createStatement();
    stmt.setQueryTimeout(60);
    ResultSet result=stmt.executeQuery(query);
    initFromResult(result);
    result.close();
    stmt.close();
  }
}
