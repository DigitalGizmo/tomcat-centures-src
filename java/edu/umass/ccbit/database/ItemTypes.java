/**
 * Title:        ItemTypes<p>
 * Description:  item types, read from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ItemTypes.java,v 1.3 2002/04/12 14:07:27 pbrown Exp $
 *
 * $Log: ItemTypes.java,v $
 * Revision 1.3  2002/04/12 14:07:27  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2001/07/24 20:45:33  tarmstro
 * changes for new dropdown lists
 *
 * Revision 1.1  2000/06/27 16:13:46  pbrown
 * update for searching, navigation bar
 *
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import java.io.IOException;

public class ItemTypes extends DbLoadableItemList
{
  protected class ItemType implements InstanceFromResult
  {
    public String type_;
    public void init(ResultSet result) throws SQLException
    {
      type_=result.getString("Category");
    }
  }

  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    ItemType itemType=new ItemType();
    itemType.init(result);
    return itemType;
  }

  public String getItemType(int index)
  {
    return ((ItemType)get(index)).type_;
  }

  public int numItems()
  {
    return items_.size();
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT DISTINCT Category FROM Nomenclature where Category is not null order by Category");
  }
}
