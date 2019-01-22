/**
 * Title:        MainCollectionItemsPager<p>
 * Description:  class w/methods for paging through list of items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: MainCollectionItemsPager.java,v 1.15 2003/12/03 21:01:06 keith Exp $
 *
 * $Log: MainCollectionItemsPager.java,v $
 * Revision 1.15  2003/12/03 21:01:06  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.14  2003/08/01 14:41:07  keith
 * minor readability changes
 *
 * Revision 1.13  2003/02/05 20:25:15  keith
 * many changes related to implementing subthemeid URL param
 *
 * Revision 1.12  2002/04/12 14:07:31  pbrown
 * fixed copyright info
 *
 * Revision 1.11  2001/08/24 19:20:03  tarmstro
 * changes for searching
 *
 * Revision 1.10  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.9  2000/11/01 20:33:28  tarmstro
 * added accessor methods for ordering properly
 *
 * Revision 1.8  2000/10/20 19:23:07  tarmstro
 * added method to return date info
 *
 * Revision 1.7  2000/08/10 16:38:09  tarmstro
 * removed debugging output
 *
 * Revision 1.6  2000/07/25 16:16:53  tarmstro
 * fixed modulo numPerPage problem
 *
 * Revision 1.5  2000/06/27 16:13:47  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.4  2000/06/12 14:20:01  pbrown
 * many changes for implementation of browse
 *
 * Revision 1.3  2000/06/08 02:51:57  pbrown
 * added code maintain correct order of search results loaded from database
 *
 * Revision 1.2  2000/06/06 14:33:49  pbrown
 * many changes for searching
 *
 * Revision 1.1  2000/06/02 21:52:53  pbrown
 * many changes for searching..more to come
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.util.IntegerMap;
import edu.umass.ccbit.util.ItemIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.Parameters;
import java.lang.Math;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class MainCollectionItemsPager extends DbLoadableItemList
{
  // from script parameters
  protected int perPage_;
  protected int pageNumber_;
  // instance vars set by getPageItemRange
  protected int firstItem_;
  protected int lastItem_;
  protected int previousPage_;
  protected int nextPage_;
  // key names for script parameters
  public static final String PerPage_="perpage";
  public static final String PageNumber_="pagenum";
  protected static final int perPageDefault_=10;
  // mapping of item id's to index of item in result set...this is necessary
  // because query does not return items in the same order as the itemid's in
  // the "WHERE ItemID in (....)" clause
  private IntegerMap itemIndexMapping_;

  /**
   * constructor
   */
  public MainCollectionItemsPager()
  {
    previousPage_=-1;
    nextPage_=-1;
  }

  /**
   * get parameters
   */
  protected void getParameters(HttpSession session, Parameters param)
  {
    pageNumber_=param.getInt(PageNumber_, 0);
    perPage_=JspSession.getInt(session, PerPage_, perPageDefault_);
  }

  public int upperBound()
  {
    return lastItem_;
  }

  public int lowerBound()
  {
    return firstItem_;
  }

  /**
   * get item range for page, adjusting pageNumber_ if necessary, and
   * setting values for first/last items on current page, and page numbers
   * of next and previous pages
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
    firstItem_=pageNumber_ * perPage_;
    lastItem_=Math.min(firstItem_+perPage_, numItems) - 1;
    // set values of next/previous pages..values of -1 indicate that
    // there is no next page (i.e., already on last page) or no previous
    // page (already on first page)..note that in the latter case, all we
    // have to do is subtract 1 :-)
    previousPage_ = pageNumber_ - 1;
    nextPage_ = pageNumber_ < numPages - 1 ? pageNumber_ + 1 : -1;
  }

  /**
   * page number for given item index and number of items..
   * this is used to create a link from an item page in the search
   * result set back to search results page containing the item
   */
  public static int resultsPageNumber(HttpSession session, int nitem)
  {
    int perPage=JspSession.getInt(session, PerPage_, perPageDefault_);
    return nitem / perPage;
  }

  /**
   * link to next page
   */
  public static String pageLink(int page, String url, String text)
  {
    // if page doesn't exist, return text without link
    if (page==-1)
      return text;
    else
    {
      StringBuffer link=new StringBuffer();
      link.append(url);
      HtmlUtils.addToLink(PageNumber_, page, true, link);
      return HtmlUtils.anchor(link.toString(), text);
    }
  }

  /**
   * link to next page
   */
  public static String pageLink(int page, String url, String text, int lexicon)
  {
    // if page doesn't exist, return text without link
    if (page==-1)
      return text;
    else
    {
      StringBuffer link=new StringBuffer();
      link.append(url);
      HtmlUtils.addToLink(PageNumber_, page, true, link);
      HtmlUtils.addToLink(SearchParameters.Lexicon_, lexicon, false, link);
      return HtmlUtils.anchor(link.toString(), text);
    }
  }

  /**
   * link to next page
   */
  public String nextPageLink(String url, String text)
  {
    return pageLink(nextPage_, url, text);
  }

  /**
   * link to next page
   */
  public String nextPageLink(String url, String text, int lexicon)
  {
    return pageLink(nextPage_, url, text, lexicon);
  }

  /**
   * link to previous page
   */
  public String previousPageLink(String url, String text)
  {
    return pageLink(previousPage_, url, text);
  }

  /**
   * link to previous page
   */
  public String previousPageLink(String url, String text, int lexicon)
  {
    return pageLink(previousPage_, url, text, lexicon);
  }

  /**
   * get item id mapping..
   * this sets up a mapping so that we may order results
   * read from the database in the same order as the item id list
   * which was used to read the results...
   * the order of results of a query like "SELECT * where itemid in (...)"
   * may not match the order of the items given in (...), so this is
   * necessary
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
      Integer ival=itemIDmap.get(items.itemID(i + firstItem_));
      if (ival != null)
      {
        itemIndexMapping_.put(i, ival.intValue());
      }
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
    for (int i=firstItem_; i <= lastItem_; i++)
    {
      buf.append(items.itemID(i));
      if (i < lastItem_)
        buf.append(", ");
    }
    buf.append(")");
    return buf.toString();
  }

  /**
   * create new instance from result set
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    CollectionItemInfo info=new CollectionItemInfo();
    info.init(result);
    return info;
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
      getItemIndexMapping(items);
    }
  }

  /**
   * get item at index
   */
  protected CollectionItemInfo getItemInfo(int index)
  {
    Integer val=itemIndexMapping_.get(index);
    if (val != null)
      return (CollectionItemInfo) get(val.intValue());
    else
      return null;
  }

  /**
   * description of items on page
   */
  public String description()
  {
    if (getCount() > 0 && firstItem_ < lastItem_)
    {
      StringBuffer buf=new StringBuffer();
      buf.append("items ");
      // items are zero-based, so add 1
      buf.append(firstItem_ + 1).append(" - ").append(lastItem_ + 1);
      return buf.toString();
    }
    else
      return "";
  }

  /**
   * accession number at index
   */
  public String accessionNumber(int index)
  {
    return getItemInfo(index).accessionNumber_;
  }

  /**
   * item name at index
   */
  public String itemName(int index)
  {
    return getItemInfo(index).itemName_;
  }

  /**
   * item date at index
   */
  public String itemDate(int index)
  {
    return getItemInfo(index).itemDate();
  }

  /**
   * get item date comparator info
   */
  public int[] getDateInfo(int index)
  {
    int array[] = {getItemInfo(index).date_.comparisonMonth(), getItemInfo(index).date_.comparisonYear()};
    return array;
  }

  /**
   * item id
   */
  public int itemID(int index)
  {
    return getItemInfo(index).itemID_;
  }

  /**
   * image at index
   */
  public String image(int index, int width, int height)
  {
    CollectionImage img=getItemInfo(index).itemImage_;
    img.initImageInfo();
    return img.scaledImageTag(width, height, MrSidImage.SCALE_IMAGE);
  }

  /**
   * image object at index (added by KRU)
   */
  public CollectionImage imageObj( int index ) {
    CollectionImage img = getItemInfo(index).itemImage_;
    img.initImageInfo();
    return (img);
  }

  /**
   * id sentence at index
   */
  public String idSentence(int index)
  {
    return getItemInfo(index).idSentence();
  }
}
