/**
 * Title:        BrowseSubcategoryItems<p>
 * Description:  list of items in browse subcategory<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: BrowseSubcategoryItems.java,v 1.3 2002/04/12 14:07:11 pbrown Exp $
 *
 * $Log: BrowseSubcategoryItems.java,v $
 * Revision 1.3  2002/04/12 14:07:11  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/06/12 16:38:43  pbrown
 * some changes for better site navigation
 *
 * Revision 1.1  2000/06/12 14:20:01  pbrown
 * many changes for implementation of browse
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.jsp.MainBrowsePage;
import edu.umass.ccbit.jsp.Navigator;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.Parameters;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class BrowseSubcategoryItems extends DbLoadableItemIDList
{
  private static final String browseSubcategorySessionAttribute_="browse_items";
  protected int subcategoryID_;
  protected BrowseCategoryInfo browseInfo_;
  
  /**
   * subcategory id
   */
  public int subcategoryID()
  {
    return subcategoryID_;
  }

  /**
   * attribute name for session
   */
  protected String attributeName()
  {
    return browseSubcategorySessionAttribute_;
  }
  
  /**
   * subcategory name
   */
  public String subcategoryName()
  {
    return browseInfo_.subcategory_;
  }

  /**
   * subcategory name
   */
  public String categoryName()
  {
    return browseInfo_.category_;
  }

  /**
   * category id
   */
  public int categoryID()
  {
    return browseInfo_.categoryID_;
  }
  
  /**
   * get subcategory id from parameters
   */
  protected boolean getSubcategoryParam(Parameters param)
  {
    if (param.containsKey(MainBrowsePage.BrowseSubcategoryID_))
    {
      subcategoryID_=param.getInt(MainBrowsePage.BrowseSubcategoryID_, -1);
      return true;
    }
    else
      return false;
  }

  /**
   * pager link
   */
  public String pagerLink(String mainUrl, String resultsUrl, int pageNumber)
  {
    Navigator nav=new Navigator();
    nav.add(mainUrl, "Browse");
    nav.add(MainBrowsePage.categoryUrl(mainUrl, categoryID()), categoryName());
    nav.add(MainCollectionItemsPager.pageLink(pageNumber, resultsUrl, subcategoryName()));
    return nav.toString();
  }

  /**
   * query
   */
  protected String query(int subcategoryID)
  {
    StringBuffer query=new StringBuffer();
    query.append("SELECT ItemID FROM CollectionBrowseItems WHERE BrowseSubcategoryID=");
    query.append(subcategoryID);
    return query.toString();
  }

  /**
   * get from session
   */
  public void getFromSession(HttpSession session)
  {
    super.getFromSession(session);
    browseInfo_=new BrowseCategoryInfo();
    browseInfo_.getFromSession(session);
  }

  /**
   * save to session
   */
  public void saveToSession(HttpSession session)
  {
    super.saveToSession(session);
    browseInfo_.saveToSession(session);
  }

  /**
   * get subcategory items, either from database, or from items previously
   * cached in session object
   * @param conn database connection
   * @param session the http session object
   * @param param request parameters (containing id of the subcategory)
   */
  public void load(Connection conn, HttpSession session, Parameters param) throws SQLException
  {
    if (getSubcategoryParam(param))
    {
      load(conn, query(subcategoryID_));
      browseInfo_=new BrowseCategoryInfo();
      browseInfo_.load(connection_, subcategoryID_);
      JspSession.setItemPageViewMode(session, JspSession.itemPageViewModes.Browse_);
      saveToSession(session);
    }
    else
    {
      getFromSession(session);
    }
  }
}
