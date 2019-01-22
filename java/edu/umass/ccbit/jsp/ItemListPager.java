/**
 * Title:        ItemListPager<p>
 * Description:  base class for paging through a list of items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ItemListPager.java,v 1.9 2003/12/03 21:01:09 keith Exp $
 *
 * $Log: ItemListPager.java,v $
 * Revision 1.9  2003/12/03 21:01:09  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.8  2003/09/23 19:06:52  keith
 * deleted commented-out method
 *
 * Revision 1.7  2003/09/23 19:06:15  keith
 * moved removeLink() down to MyCollectionPage.java
 *
 * Revision 1.6  2002/04/12 14:07:56  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/09/28 15:05:45  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.4  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.3  2000/10/11 20:02:49  tarmstro
 * added method to remove an item from a collection
 *
 * Revision 1.2  2000/06/27 16:14:39  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.1  2000/06/12 14:21:33  pbrown
 * class for paging through list of items
 *
 */
package edu.umass.ccbit.jsp;
import edu.umass.ccbit.database.DbLoadableItemIDList;
import edu.umass.ccbit.database.MainCollectionItemsPager;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.util.Vector;
import java.net.URLEncoder;
import javax.servlet.http.HttpSession;

public abstract class ItemListPager extends HttpDbJspBase
{
  // key names for parameters
  public static final String PageNumber_="page";
  // item pager object
  protected MainCollectionItemsPager pager_;
  // parameter values
  protected int pageNumber_;
  // init parameter for item page url - NOTE: this doesn't work yet because
  // <jsp-file> tag in jakarta is broken..using servlet context parameter for now
  private static final String ItemPageBaseURL_="itempagebaseurl";
  // temporary servlet context parameter for item page base url
  private static final String ContextItemPageBaseURL_="search.itempagebaseurl";

  /** url for item page */
  protected abstract String itemPageBaseUrl();

  /**
   * number of results displayed on page
   */
  public int numDisplayedResults()
  {
    return pager_.getCount();
  }

  /**
   * link to previous search results page
   */
  protected String previousPageLink()
  {
    return pager_.previousPageLink(URI_, "Previous page");
  }

  /**
   * link to next search results page
   */
  protected String nextPageLink()
  {
    return pager_.nextPageLink(URI_, "Next page");
  }

  /**
   * description of search results on this page, e.g., 'items xx-yy'
   */
  protected String resultsPageDescription()
  {
    return pager_.description();
  }

  /**
   * image link to search result item
   */
  protected String resultImageLink(int nitem, int width, int height)
  {
    String img=pager_.image(nitem, width, height);
    if (img != null && img.length() > 0) {
      return HtmlUtils.anchor(resultURL(itemPageBaseUrl(), nitem), img);
    } else {
      return "";
    }
  }

  protected String resultImage( int nitem, int width, int height ) {
  	return pager_.image( nitem, width, height );
  }

  /**
   * item name from index
   */
  protected String itemName(int nitem)
  {
    return pager_.itemName(nitem);
  }

  /**
   * link to search result item
   */
  protected String resultLink(int nitem)
  {
    return HtmlUtils.anchor(resultURL(itemPageBaseUrl(), nitem), itemName(nitem));
  }

  /**
   * result text
   */
  protected String resultText(int nitem)
  {
    return pager_.idSentence(nitem);
  }

  /**
   * result date
   */
  protected String resultDate(int nitem)
  {
    return pager_.itemDate(nitem);
  }

  /**
   * url for result item
   */
  private String resultURL( String baseurl, int nitem )
  {
    StringBuffer url=new StringBuffer();
    url.append(baseurl);
    HtmlUtils.addToLink(ItemPage.ItemID_, pager_.itemID(nitem), true, url);
    return url.toString();
  }

  /**
   * url for result item
   */
  protected String resultURL( int nitem ) {
    return resultURL( itemPageBaseUrl(), nitem );
  }

  /**
   * accession number for result
   */
  protected String resultAccessionNumber(int nitem)
  {
    return pager_.accessionNumber(nitem);
  }

  /**
   * load
   */
  protected void load(HttpSession session, DbLoadableItemIDList itemList) throws SQLException
  {
    pager_=new MainCollectionItemsPager();
    pager_.load(connection_, session, servletParams_, itemList);
  }

  /**
   * create parameter string consisting of
   * itemid=nnn&title=yyyyy&itemid=nnnn&title=yyyy...for all items in list
   */
  public String itemIDParameterString()
  {
    StringBuffer buf=new StringBuffer();
    for (int i=0; i<pager_.getCount(); i++)
    {
      if (i > 0)
      {
        buf.append("&");
      }
      buf.append("itemid=").append(pager_.itemID(i));
      buf.append("&");
      buf.append("title=").append(URLEncoder.encode(itemName(i)));
    }
    return buf.toString();
  }
}
