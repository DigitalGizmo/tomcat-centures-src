
/**
 * Title:        TranscriptionSearchResults<p>
 * Description:  finding transcriptions for a particular item that match the search query<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.database.MainCollectionItem;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ckc.util.StringUtils;
import java.util.Vector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TranscriptionSearchResults extends DbLoadable
{
  protected Vector scripts_;
  protected MainCollectionItem item_;
  protected String searchMode_;

  /**
   * load the data
   */
  public void load(Connection conn, int itemID, String searchText, MainCollectionItem item, String searchMode) throws SQLException
  {
    item_ = item;
    searchMode_ = searchMode;
    if(searchText!=null)
      if(searchText.length()>0)
        load(conn, query(itemID, searchText));
    else
      scripts_ = new Vector();
  }

  /**
   * free text search predicate
   */
  public boolean containsTextSearch()
  {
    return searchMode_.compareTo("on")==0;
  }

  /**
   * text search operator - 'contains' if fundamental text search, 'freetext' otherwise
   */
  protected String textSearchOperator()
  {
    return containsTextSearch() ? "contains" : "freetext";
  }

  /**
   * query
   */
  public String query(int itemID, String searchText)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(TranscriptionList.TranscriptionView_);
    buf.append(" WHERE itemid=").append(itemID);
    buf.append(" and ready=1");
    if(containsTextSearch())
        buf.append(" and ").append(textSearchOperator()).append("(TranscriptionPage.*, ").append(querySearchText(searchText)).append(")");
    else
        buf.append(" and ").append(textSearchOperator()).append("(TranscriptionPage.*, ").append(querySearchText(searchText)).append(")");
    return buf.toString();
  }

  /**
   * the modified, correctly "quoted" search text to avoid search quoting syntax errors
   */
  private String querySearchText(String searchText)
  {
    String text = searchText;
    text = SearchParameters.removeChar(text, '\"');
    text = StringUtils.substitute(text, "'", "''");
    StringBuffer buf = new StringBuffer();
    if(containsTextSearch()) // CONTAINS -- exact phrase OR boolean expression
    {
      String [] boolWords = {" or "," OR "," and "," AND "};
      for(int i=0; i<boolWords.length; i++)
      {
        String search = boolWords[i];
        int the = text.indexOf(search);
        text = StringUtils.substitute(text, boolWords[i], "\" "+boolWords[i]+" \"");
      }
      buf.append("'\"");
      buf.append(text);
      buf.append("\"'");
    }
    else                     // FREETEXT -- string of words, no phrases
    {
      buf.append("'");
      buf.append(text);
      buf.append("'");
    }
    String something = buf.toString();
    return buf.toString();
  }

  /**
   * initialize data in this object based on contents of result set
   */
  protected void initFromResult(ResultSet result) throws SQLException
  {
    scripts_=new Vector();
    String tempPage;
    while (result.next())
    {
      tempPage=getPageName(result);
      if(!scripts_.contains(tempPage))
        scripts_.add(tempPage);
    }
  }

  /**
   * clear
   */
  public void clear()
  {
    scripts_ = new Vector();
  }

  /**
   * link to itempage with transcription for selected page
   */
  public int imgIndex(String pageName)
  {
    String actual="";
    String test="";
    for(int i=0; i<item_.imageMaxIndex();i++)
    {
      actual = StringUtils.removeChars(" ", item_.imagePageName(i).toLowerCase());
      test = StringUtils.removeChars(" ", pageName.toLowerCase());
      if(actual.compareToIgnoreCase(test)==0)
        return i;
    }
    return -1;
  }

  /**
   * size
   */
  public int size()
  {
    if(scripts_!=null)
      return scripts_.size();
    return 0;
  }

  /**
   * element at
   */
  public String elementAt(int i)
  {
    return (String) scripts_.elementAt(i);
  }

  /**
   * get the pagename from the result set
   */
  private String getPageName(ResultSet result) throws SQLException
  {
    return result.getString("PageName");
  }
}
