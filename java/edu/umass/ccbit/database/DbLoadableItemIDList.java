/**
 * Title:        DbLoadableItemIDList<p>
 * Description:  base class for loading list of item ids from database...can be passed to pager class for display<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadableItemIDList.java,v 1.10 2002/04/12 14:07:20 pbrown Exp $
 *
 * $Log: DbLoadableItemIDList.java,v $
 * Revision 1.10  2002/04/12 14:07:20  pbrown
 * fixed copyright info
 *
 * Revision 1.9  2001/05/30 19:45:13  pbrown
 * changes for advanced search fixes etc.
 *
 * Revision 1.8  2000/09/25 18:12:12  tarmstro
 * many changes for searching and storing results
 *
 * Revision 1.7  2000/07/25 16:14:58  tarmstro
 * added mechanism to continuing adding items to an itemidlist
 *
 * Revision 1.6  2000/06/27 16:13:45  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.5  2000/06/12 16:38:44  pbrown
 * some changes for better site navigation
 *
 * Revision 1.4  2000/06/12 14:20:01  pbrown
 * many changes for implementation of browse
 *
 * Revision 1.3  2000/06/08 02:49:11  pbrown
 * checks for null before trying to count elts
 *
 * Revision 1.2  2000/06/06 14:33:49  pbrown
 * many changes for searching
 *
 * Revision 1.1  2000/06/02 21:52:52  pbrown
 * many changes for searching..more to come
 *
 */

package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ItemIDList;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

public abstract class DbLoadableItemIDList extends DbLoadable
{
  protected ItemIDList itemIDs_;

  // field name for itemid field
  public static final String ItemID_="ItemID";

  /**
   * get number of items ids in list
   */
  public int getCount()
  {
    return itemIDs_ != null ? itemIDs_.size() : 0;
  }

  /**
   * item id at index
   * returns -1 if index was invalid
   */
  public int itemID(int nitem)
  {
    if (nitem >= 0 && nitem < getCount())
      return itemIDs_.getValue(nitem);
    else
      return -1;
  }

  /**
   * finds index for item
   * returns -1 if not found
   */
  public int indexForItemID(int itemID)
  {
    for (int i=0; i<getCount(); i++)
    {
      if (itemID(i)==itemID)
        return i;
    }
    return -1;
  }

  /**
   * attribute name for saving to session
   */
  protected abstract String attributeName();

  /**
   * save results in user session
   */
  public void saveToSession(HttpSession session)
  {
    session.setAttribute(attributeName(), itemIDs_);
  }

  /**
   * link to page in item pager
   */
  public abstract String pagerLink(String mainUrl, String resultsUrl, int pageNumber);

  /**
   * get results from user session
   */
  public void getFromSession(HttpSession session)
  {
    try
    {
      itemIDs_=(ItemIDList) session.getAttribute(attributeName());
    }
    catch (Exception e)
    {
      itemIDs_=new ItemIDList();
    }
  }

  /**
   * throw away item ids which are not in idList
   */
  public void retainAll(ItemIDList idList)
  {
    itemIDs_.retainAll(idList);
  }

  /**
   * init
   */
  public void initFromResult(ResultSet result) throws SQLException
  {
    itemIDs_=new ItemIDList();
    initFromResult(itemIDs_, result);
  }

  /**
   * init item list from result set
   */
  public static void initFromResult(ItemIDList items, ResultSet result) throws SQLException
  {
    while (result.next())
    {
      items.add(result.getInt(ItemID_));
    }
  }
}
