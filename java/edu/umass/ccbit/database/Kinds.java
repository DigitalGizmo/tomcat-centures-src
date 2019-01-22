package edu.umass.ccbit.database;

/**
 * Title:        Kinds<p>
 * Description:  kinds, read from database<p>
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

public class Kinds extends DbLoadableItemList
{
  protected class Kind implements InstanceFromResult
  {
    public String kind_;
    public int kindID_;

    public void init(ResultSet result) throws SQLException
    {
      kind_=result.getString("Kind");
      kindID_=result.getInt("KindID");
    }
  }

  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    Kind essence = new Kind();
    essence.init(result);
    return essence;
  }

  public String getKind(int index)
  {
    return ((Kind)get(index)).kind_;
  }

  public int getKindID(int index)
  {
    return ((Kind)get(index)).kindID_;
  }

  public int numItems()
  {
    return items_.size();
  }

  /**
   * get the kind
   * @param key the id to find the kind by
   * @return the kind
   */
  public String getKindByID(int key)
  {
    Enumeration e = items_.elements();
    Kind item;
    while(e.hasMoreElements())
    {
      item=(Kind) e.nextElement();
      if(item.kindID_ == key)
        return item.kind_;
    }
    return "";
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT nomenclatureid as kindid, subcategory as kind from Nomenclature where nomenclatureid in (select nomenclatureid from main where ready=1) and subcategory is not null order by kind");
  }
}
