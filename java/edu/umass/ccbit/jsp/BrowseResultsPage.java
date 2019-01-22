/**
 * Title:        BrowseResultsPage<p>
 * Description:  page through browse items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: BrowseResultsPage.java,v 1.3 2002/04/12 14:07:50 pbrown Exp $
 *
 * $Log: BrowseResultsPage.java,v $
 * Revision 1.3  2002/04/12 14:07:50  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/06/27 22:04:04  pbrown
 * changed title on  result page
 *
 * Revision 1.1  2000/06/12 14:21:09  pbrown
 * implemented browse
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.BrowseSubcategoryItems;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ccbit.jsp.MainBrowsePage;

public abstract class BrowseResultsPage extends ItemListPager
{
  protected BrowseSubcategoryItems browseItems_;
  // url for item page
  protected String itemPageBaseURL_;
  // init parameter for item page url
  private static final String ItemPageBaseURL_="browse.res.itempageurl";

  protected String itemPageBaseUrl()
  {
    return itemPageBaseURL_;
  }

  /**
   * number of items on page
   */
  public int numItems()
  {
    return browseItems_.getCount();
  }

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    itemPageBaseURL_=initParams_.getString(ItemPageBaseURL_, "");
  }

  /**
   * navigation bar
   */
  protected String navigation()
  {
    Navigator nav=new Navigator();
    String link="index.jsp";
    nav.add(link, "Highlights");
    nav.add(MainBrowsePage.categoryUrl(link, browseItems_.categoryID()), browseItems_.categoryName());
    nav.add(browseItems_.subcategoryName());
    return nav.toString();
  }

  /**
   * category/subcategory description
   */
  protected String resultsPageTitle()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("Highlights : ");
    buf.append(browseItems_.categoryName());
    buf.append(" : ");
    buf.append(browseItems_.subcategoryName());
    return buf.toString();
  }

  /**
   * description of results
   */
  protected String resultsDescription()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("Subcategory ").append(browseItems_.subcategoryName());
    buf.append(" contains ").append(numItems()).append(" item(s).");
    return buf.toString();
  }

  /**
   * load search results
   */
  protected void load(HttpSession session) throws SQLException
  {
    browseItems_=new BrowseSubcategoryItems();
    browseItems_.load(connection_, session, servletParams_);
    load(session, browseItems_);
  }
}

  
