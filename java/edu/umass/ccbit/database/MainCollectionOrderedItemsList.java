
/**
 * Title:        MainCollectionOrderedItemsList<p>
 * Description:  order the items as a list by time for chronological order<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import edu.umass.ckc.util.Parameters;

public class MainCollectionOrderedItemsList extends MainCollectionOrderedItemsPager
{
  public void MainCollectionOrderdItemsList()
  {
    previousPage_ = -1;
    nextPage_ = -1;
  }

  /**
   * load
   */
  public void load(Connection conn, HttpSession session, Parameters param, DbLoadableItemIDList items)
  throws SQLException
  {
    if (items != null && items.getCount() > 0)
    {
      getParameters(session, param);
      getPageItemRange(items.getCount());
      load(conn, query(items));
      sortBy(1);
    }
  }

  /**
   * get item at index
   */
  protected CollectionItemInfo getItemInfo(int index)
  {
    try
    {
      return (CollectionItemInfo) get(index);
    }
    catch(Exception e)
    {
      System.err.println(e.toString());
    }
    return null;
  }

  /**
   * override the methods which limit the results on a page...
   */
  protected void getPageItemRange(int numItems)
  {
    //if numItems % perPage_ = 0 then there will be an extra results page...
    int numPages;
    if((numItems % perPage_)==0)
      numPages=(numItems / perPage_);
    else
      numPages=(numItems / perPage_) + 1;
    // adjust page number so that it falls within
    // valid range of pages..page numbers are zero-based
    if (pageNumber_ < 0 || pageNumber_ >= numPages)
    {
      pageNumber_=0;
    }
    firstItem_ = 0;
    lastItem_ = numItems - 1;
    // set values of next/previous pages..values of -1 indicate that
    // there is no next page (i.e., already on last page) or no previous
    // page (already on first page)..note that in the latter case, all we
    // have to do is subtract 1 :-)
    previousPage_ = pageNumber_ - 1;
    nextPage_ = pageNumber_ < numPages - 1 ? pageNumber_ + 1 : -1;
  }
}
