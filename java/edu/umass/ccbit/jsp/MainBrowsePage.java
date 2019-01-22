/**
 * Title:        MainBrowsePage<p>
 * Description:  base class for main browse jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: MainBrowsePage.java,v 1.5 2002/04/12 14:07:59 pbrown Exp $
 *
 * $Log: MainBrowsePage.java,v $
 * Revision 1.5  2002/04/12 14:07:59  pbrown
 * fixed copyright info
 *
 * Revision 1.4  2001/09/28 15:05:45  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.3  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.2  2000/06/12 14:21:09  pbrown
 * implemented browse
 *
 * Revision 1.1  2000/06/08 02:48:36  pbrown
 * preliminary version of browse
 *
 */
package edu.umass.ccbit.jsp;
import edu.umass.ccbit.database.BrowseCategories;
import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.io.IOException;
import java.lang.Integer;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public abstract class MainBrowsePage extends HttpDbJspBase
{
  // number of items per row in table
  protected static final int numPerRow_=4;
  // browse categories object
  protected BrowseCategories browseCategories_;
  // category id
  protected int browseCategoryID_;
  protected int browseSubcategoryID_;
  // parameter name for categoryid
  public static final String BrowseCategoryID_="category";
  public static final String BrowseSubcategoryID_="subcategoryid";
  // browse results url
  protected String browseResultsUrl_;
  // init parameter for browse results url
  private static final String BrowseResultsUrl_="browse.resultsurl";
  
  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    //itemPageBaseURL_=initParams_.getString(ItemPageBaseURL_, "");
    browseResultsUrl_=initParams_.getString(BrowseResultsUrl_, "");
  }
  
  /**
   * get servlet request parameters into ServletParams object
   * this calls parseRequestParameters in superclass, then sets data members associated with
   * servlet parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    browseCategoryID_=servletParams_.getInt(BrowseCategoryID_, -1);
  }

  /**
   * load
   */
  public void load() throws SQLException
  {
    browseCategories_=new BrowseCategories();
    browseCategories_.load(connection_, browseCategoryID_);
  }

  /**
   * true iff this is subcategory page
   */
  private boolean isSubcategoryPage()
  {
    return browseCategoryID_ > 0;
  }

  /**
   * number of items
   */
  protected int numItems()
  {
    return browseCategories_.getCount();
  }

  /**
   * begin item...make new table row if necessary
   */
  protected String beginItem(int nitem)
  {
    return (nitem % numPerRow_ == 0) ? "<tr>" : "";
  }

  /**
   * end item..end current row if necessary
   */
  protected String endItem(int nitem)
  {
    return ((nitem + 1 % numPerRow_ == 0) || nitem == numItems() - 1) ? "</tr>" : "";
  }

  /**
   * browse category name
   */
  protected String browseCategory()
  {
    if (isSubcategoryPage() && numItems() > 0)
      return browseCategories_.categoryName(0);
    else
      return "";
  }

  /**
   * browse link
   */
  protected String browseLink()
  {
    return HtmlUtils.anchor(URI_, "Browse:");
  }

  /**
   * browse page title
   */
  protected String browsePageTitle()
  {
    StringBuffer buf=new StringBuffer();
    if (isSubcategoryPage())
    {
      buf.append("Highlights: ");
      buf.append(browseCategory());
    }
    else
    {
      buf.append("Highlights of the Digital Collection");
    }
    return buf.toString();
  }

  /**
   * subcategory url
   */
  public static String subcategoryUrl(String url, int subcategory)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(url);
    HtmlUtils.addToLink(BrowseSubcategoryID_, Integer.toString(subcategory), true, buf);
    return buf.toString();
  }

  /**
   * category url
   */
  public static String categoryUrl(String url, int category)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(url);
    HtmlUtils.addToLink(BrowseCategoryID_, Integer.toString(category), true, buf);
    return buf.toString();
  }

  /**
   * browse url
   */
  protected String browseUrl(int nitem)
  {
    if (isSubcategoryPage())
      return subcategoryUrl(browseResultsUrl_, browseCategories_.browseSubcategoryID(nitem));
    else
      return categoryUrl(URI_, browseCategories_.browseCategoryID(nitem));
  }

  /**
   * navigation
   */
  protected String navigation()
  {
    Navigator nav=new Navigator();
    String text="Highlights";
    if (isSubcategoryPage())
    {
      nav.add(URI_, text);
      nav.add(browseCategory());
    }
    else
      nav.add(text);
    return nav.toString();
  }

  /**
   * browse link text
   */
  protected String browseLinkText(int nitem)
  {
    if (isSubcategoryPage())
      return browseCategories_.subcategoryName(nitem);
    else
      return browseCategories_.categoryName(nitem);
  }

  /**
   * browse item image
   */
  protected String itemImageTag(int width, int height, int index)
  {
    CollectionImage img=browseCategories_.itemImage(index);
    if (img != null)
    {
      img.initImageInfo();
      return img.scaledImageTag(width, height, MrSidImage.SCALE_IMAGE);
    }
    else
      return "";
  }
  
  /**
   * browse item image link
   */
  protected String browseItemImageLink(int width, int height, int index)
  {
    return HtmlUtils.anchor(browseUrl(index), itemImageTag(width, height, index));
  }
}
