/**
 * Title:        NavigationBar<p>
 * Description:  reads navigation items from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: NavigationBar.java,v 1.2 2002/04/12 14:07:34 pbrown Exp $
 *
 * $Log: NavigationBar.java,v $
 * Revision 1.2  2002/04/12 14:07:34  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/27 16:13:47  pbrown
 * update for searching, navigation bar
 *
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import java.io.IOException;
import edu.umass.ckc.html.HtmlUtils;

public class NavigationBar extends DbLoadableItemList
{
  protected static final String separator_=" | ";
  
  protected class NavigationBarItem implements InstanceFromResult
  {
    public String text_;
    public String url_;
    
    public void init(ResultSet result) throws SQLException
    {
      text_=result.getString("Text");
      url_=result.getString("URL");
    }

    public String link()
    {
      return HtmlUtils.anchor(url_, text_);
    }
  }

  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    NavigationBarItem item=new NavigationBarItem();
    item.init(result);
    return item;
  }

  /**
   * return navigation bar
   */
  public String toString()
  {
    StringBuffer buf=new StringBuffer();
    for (int i=0; i < getCount(); i++)
    {
      if (i > 0)
        buf.append(separator_);
      buf.append(((NavigationBarItem)get(i)).link());
    }
    return buf.toString();
  }
  
  public void load(Connection conn) throws SQLException
  {
    load(conn, "SELECT * FROM NavigationBarItems");
  }
}

  

  
      
  


