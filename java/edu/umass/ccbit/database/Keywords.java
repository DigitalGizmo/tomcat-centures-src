/**
 * Title:        Keywords<p>
 * Description:  keywords, read from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: Keywords.java,v 1.6 2002/04/12 14:07:28 pbrown Exp $
 *
 * $Log: Keywords.java,v $
 * Revision 1.6  2002/04/12 14:07:28  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/07/24 20:45:33  tarmstro
 * changes for new dropdown lists
 *
 * Revision 1.4  2001/03/12 17:20:52  pbrown
 * added keywords to curatorial info
 *
 * Revision 1.3  2001/02/13 21:30:54  tarmstro
 * added method for number of keywords
 *
 * Revision 1.2  2000/11/02 16:12:18  pbrown
 * changed query so that only keywords associated to items in database are returned
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
import java.lang.StringBuffer;
import java.util.Enumeration;

public class Keywords extends DbLoadableItemList
{
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    Keyword kwd=new Keyword();
    kwd.init(result);
    return kwd;
  }

  public String getKeyword(int index)
  {
    return ((Keyword)get(index)).keyword_;
  }

  public int getKeywordID(int index)
  {
    return ((Keyword)get(index)).keywordID_;
  }

  public int numItems()
  {
    return items_.size();
  }

  /**
   * get the keyword
   * @param key the id to find the keyword by
   * @return the keyword
   */
  public String getKeywordByID(int key)
  {
    Enumeration e = items_.elements();
    Keyword item;
    while(e.hasMoreElements())
    {
      item=(Keyword) e.nextElement();
      if(item.keywordID_ == key)
        return item.keyword_;
    }
    return "";
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    /*
      CREATE VIEW dbo.Web_SearchableKeywordsView
      AS
      SELECT DISTINCT
          KeywordLink.KeywordID, Keywords.Keyword
      FROM KeywordLink INNER JOIN
          Keywords ON
          KeywordLink.KeywordID = Keywords.KeywordID INNER JOIN
          Main ON KeywordLink.ItemID = Main.ItemID
      WHERE (Keywords.FilemakerPro = 1) AND (Main.Ready = 1)
     */
    load(conn, context, "SELECT * from Web_SearchableKeywordsView ORDER BY Keyword");
  }
}






