/**
 * Title:        ItemKeywords<p>
 * Description:  keywords associated with an item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pb
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemKeywords extends DbLoadableByID
{
  /**
   * accession number
   * @param index the index of the accession number in the vector of accession numbers
   * @return the accession number
   */
  public String getKeyword(int index)
  {
    return ((Keyword)get(index)).keyword_;
  }

  /**
   * new accession number object
   * @param result the result set
   * @return AccessionNumber
   */
  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    Keyword keyword = new Keyword();
    keyword.init(result);
    return keyword;
  }

  /**
   * view name
   * CREATE VIEW dbo.Web_ItemKeywordView
   * AS
   * SELECT KeywordLink.KeywordID, KeywordLink.ItemID, 
   *     Keywords.Keyword
   * FROM KeywordLink INNER JOIN
   *     Keywords ON KeywordLink.KeywordID = Keywords.KeywordID
   */
  protected String view()
  {
    return "Web_ItemKeywordView";
  }

  /**
   * format list of keywords as text, separated by <BR> for inclusion in
   * html table
   */
  public String formatKeywords()
  {
    StringBuffer buf=new StringBuffer();
    for (int i=0; i < getCount(); i++)
    {
      if (i > 0)
      {
        buf.append("<br>");
      }
      buf.append(getKeyword(i));
    }
    return buf.toString();
  }

  /**
   * order by
   */
  protected String orderBy()
  {
    return "Keyword";
  }
}
