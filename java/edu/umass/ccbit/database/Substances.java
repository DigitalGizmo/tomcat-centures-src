package edu.umass.ccbit.database;

/**
 * Title:        Substances<p>
 * Description:  substances, read from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
  */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import java.util.Enumeration;

public class Substances extends DbLoadableItemList
{
  protected class Substance implements InstanceFromResult
  {
    public String substance_;

    public void init(ResultSet result) throws SQLException
    {
      substance_=result.getString("Substance");
    }
  }

  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    Substance stuff = new Substance();
    stuff.init(result);
    return stuff;
  }

  public String getSubstance(int index)
  {
    return ((Substance)get(index)).substance_;
  }

  public int numItems()
  {
    return items_.size();
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT distinct webmat as substance from processmaterials where processmaterialid in (select processmaterialid from main where ready=1) and webmat is not null order by substance");
  }
}
