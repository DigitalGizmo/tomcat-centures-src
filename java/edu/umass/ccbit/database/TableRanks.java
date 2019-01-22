package edu.umass.ccbit.database;

/**
 * Title:        TableRanks
 * Description:  a list of tables and associated ranking values
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 * import edu.umass.ccbit.c
 *
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContext;

public class TableRanks extends DbLoadableItemList
{
  protected class TableRank implements InstanceFromResult
  {
    public String tableName_;
    public int tableValue_;

    public void init(ResultSet result) throws SQLException
    {
      tableName_=result.getString("tablename");
      if(tableName_!=null)
        tableName_ = tableName_.trim();
      tableValue_=result.getInt("value");
    }
  }

  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    TableRank rank = new TableRank();
    rank.init(result);
    return rank;
  }

  public String getTableName(int index)
  {
    return ((TableRank)get(index)).tableName_;
  }

  public int getTableValue(int index)
  {
    return ((TableRank)get(index)).tableValue_;
  }

  public int getTableValue(String tableName)
  {
    Enumeration e = items_.elements();
    TableRank rank;
    while(e.hasMoreElements())
    {
      rank = (TableRank) e.nextElement();
      if(rank.tableName_.equalsIgnoreCase(tableName))
        return rank.tableValue_;
    }
    return 0;
  }

  public int numItems()
  {
    return items_.size();
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT * from TableRanking");
  }
}
