
/**
 * Title:        Chronologies<p>
 * Description:  a list of chronologies from the db<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.util.Enumeration;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;

public class Chronologies extends DbLoadableItemList
{
  protected class Chronology implements InstanceFromResult
  {
    // chronology data members
    public String chronName_;
    public int chronID_;

    public void init(ResultSet result) throws SQLException
    {
      // initialize data members
      chronName_ = result.getString("ChronologyName");
      chronID_ = result.getInt("ChronologyID");
    }
  }

  /**
   * get name of chronology
   */
  public String getChronName(int chronNum)
  {
    return getChron(chronNum).chronName_;
  }

  /**
   * get id of chronology
   */
  public int getChronID(int chronNum)
  {
    return getChron(chronNum).chronID_;
  }

  /**
   * get the name of a chronology based on its chronid
   */
  public String getName(int chronID)
  {
    Enumeration e = items_.elements();
    while(e.hasMoreElements())
    {
      Chronology chron = (Chronology) e.nextElement();
      if(chron.chronID_ == chronID)
        return chron.chronName_;
    }
    return null;
  }

  /**
   * get a chronology from the vector
   */
  private Chronology getChron(int chronNum)
  {
    return (Chronology) get(chronNum);
  }

  /**
   * load the chronologies
   * @param conn the connection to the db
   * @param context the servlet context
   */
  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT ChronologyName, ChronologyID FROM Chronologies WHERE Ready=1 ORDER BY ChronologyName");
  }

  /**
   * new instance
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    Chronology chrono = new Chronology();
    chrono.init(result);
    return chrono;
  }
}
