
/**
 * Title:        MainCollectionOrderItemsPager<p>
 * Description:  orders the items based on alpha or date<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import javax.servlet.http.HttpSession;
import edu.umass.ccbit.util.IntegerMap;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.Parameters;
import edu.umass.ckc.html.HtmlUtils;
import museum.history.deerfield.centuries.database.MyCollection;

public class MainCollectionOrderedItemsPager extends MainCollectionItemsPager
{
  // mapping of item id's to index of item in result set...this is necessary
  // because query does not return items in the same order as the itemid's in
  // the "WHERE ItemID in (....)" clause
  private IntegerMap itemIndexMapping_;
  private int order_;
  private static final String Order_ = "order";

  public MainCollectionOrderedItemsPager()
  {
    previousPage_=-1;
    nextPage_=-1;
  }

  /**
   * link to previous page
   */
  public String previousPageLink(String url, String text)
  {
    return orderedPageLink(previousPage_,url, text);
  }

  /**
   * link to next page
   */
  public String nextPageLink(String url, String text)
  {
    return orderedPageLink(nextPage_, url, text);
  }

  /**
   * link to next page
   */
  public String orderedPageLink(int page, String url, String text)
  {
    // if page doesn't exist, return text without link
    if (page==-1)
      return text;
    else
    {
      StringBuffer link=new StringBuffer();
      link.append(url);
      HtmlUtils.addToLink(PageNumber_, page, true, link);
      HtmlUtils.addToLink(Order_, order_, false, link);
      return HtmlUtils.anchor(link.toString(), text);
    }
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
      order_ = JspSession.getInt(session, MyCollection.myOrder_, 0);
      load(conn, query(items));
      sortBy(order_);
      if(order_ == 0)
        getItemIndexMapping(items);
      }
  }

  /**
   * formulate query to load items
   */
  protected String query(DbLoadableItemIDList items)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("SELECT * from ").append(CollectionItemInfo.view_);
    buf.append(" WHERE ").append(CollectionItemInfo.fields.ItemID_);
    buf.append(" IN (");
    for (int i=0; i <= items.getCount()-1; i++)
    {
      buf.append(items.itemID(i));
      if (i < items.getCount()-1)
        buf.append(", ");
    }
    buf.append(")");
    return buf.toString();
  }

  /**
   * get item at index
   */
  protected CollectionItemInfo getItemInfo(int index)
  {
    if(order_ == 0)
    {
      Integer val=itemIndexMapping_.get(index);
      if (val != null)
        return (CollectionItemInfo) get(val.intValue());
      else
        return null;
    }
    else
    {
      try
      {
        return (CollectionItemInfo) get(index);
      }
      catch(Exception e)
      { System.err.println(e.toString()); }
      return null;
    }
  }

  /**
   * sort by
   */
  public void sortBy(int order)
  {
    if(order > 0)
      sort(new MainCollectionItemsDateComparator());
    if(order < 0)
      sort(new MainCollectionItemsAlphaComparator());
  }

  /**
   * get item id mapping..
   * this sets up a mapping so that we may order results
   * read from the database in the same order as the item id list
   * which was used to read the results...
   * the order of results of a query like "SELECT * where itemid in (...)"
   * may not match the order of the items given in (...), so this is
   * necessary
   * (from super class...might need modifications for this class)
   */
  private void getItemIndexMapping(DbLoadableItemIDList items)
  {
    IntegerMap itemIDmap=new IntegerMap();
    // map: itemID --> its index in result set
    for (int i=0; i < getCount(); i++)
    {
      CollectionItemInfo itemInfo=(CollectionItemInfo) get(i);
      itemIDmap.put(itemInfo.itemID_, i);
    }
    itemIndexMapping_=new IntegerMap();
    // original index of itemID --> itemID --> index in results
    for (int i=0; i <= getCount(); i++)
    {
      Integer ival=itemIDmap.get(items.itemID(i));
      if (ival != null)
      {
        itemIndexMapping_.put((-1)*(i-getCount()+1), ival.intValue());
      }
    }
  }
}
