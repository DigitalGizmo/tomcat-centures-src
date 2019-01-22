/**
 * Title:        SearchResults<p>
 * Description:  database search results<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: SearchResults.java,v 1.20 2002/04/12 14:07:40 pbrown Exp $
 *
 * $Log: SearchResults.java,v $
 * Revision 1.20  2002/04/12 14:07:40  pbrown
 * fixed copyright info
 *
 * Revision 1.19  2001/07/25 20:50:22  tarmstro
 * many changes for lexicon/searching/priority
 *
 * Revision 1.18  2001/07/24 20:45:33  tarmstro
 * changes for new dropdown lists
 *
 * Revision 1.17  2001/05/30 19:45:13  pbrown
 * changes for advanced search fixes etc.
 *
 * Revision 1.16  2001/04/05 19:29:01  tarmstro
 * changes for search output
 *
 * Revision 1.15  2001/01/25 18:47:59  tarmstro
 * saved search text to session
 *
 * Revision 1.14  2001/01/23 18:11:29  tarmstro
 * changes for displaying search criterion
 *
 * Revision 1.13  2001/01/18 18:31:34  tarmstro
 * changed method to save to session
 *
 * Revision 1.12  2000/10/24 19:34:53  pbrown
 * changed thumbnail option due to reversed sense (check to disable thumbnails)
 * on new search pages
 *
 * Revision 1.11  2000/09/25 18:12:12  tarmstro
 * many changes for searching and storing results
 *
 * Revision 1.10  2000/07/31 13:19:21  tarmstro
 * added method calls to new search queries
 *
 * Revision 1.9  2000/06/27 16:13:48  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.8  2000/06/12 16:38:44  pbrown
 * some changes for better site navigation
 *
 * Revision 1.7  2000/06/12 14:20:01  pbrown
 * many changes for implementation of browse
 *
 * Revision 1.6  2000/06/08 02:52:52  pbrown
 * added code for item page/search results interface
 *
 * Revision 1.5  2000/06/06 14:33:49  pbrown
 * many changes for searching
 *
 * Revision 1.4  2000/06/02 21:52:54  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.3  2000/06/01 15:58:53  pbrown
 * removed..victim of class reorganization..replaced by PeoplePlacesItems
 *
 * Revision 1.2  2000/06/01 14:51:35  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.1  2000/05/30 21:37:08  pbrown
 * new base class for items loadable from db, and beginnings of search/search
 * results
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.jsp.Navigator;
import edu.umass.ccbit.util.ItemIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.Parameters;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class SearchResults extends DbLoadableItemIDRankedIntersection
{
  protected static final String searchResultsSessionAttribute_="search_results";
  public static final String SearchText_="search.text";

  /**
   * attribute name for session
   */
  protected String attributeName()
  {
    return searchResultsSessionAttribute_;
  }

  /**
   * pager link
   */
  public String pagerLink(String mainUrl, String resultsUrl, int pageNumber)
  {
    Navigator nav=new Navigator();
    nav.add(mainUrl, "Search");
    nav.add(MainCollectionItemsPager.pageLink(pageNumber, resultsUrl, "Results"));
    return nav.toString();
  }

  /**
   * save to session
   */
  public void saveToSession(HttpSession session)
  {
    if(itemIDs_==null)
      itemIDs_ = new ItemIDList();
    super.saveToSession(session);
  }

  /**
   * search summary
   */
  public abstract String searchSummary(HttpSession session);

  /**
   * search text
   */
  public abstract String searchText();

  /**
   * get search parameters
   */
  protected abstract void getSearchParameters(Parameters param);

  /**
   * check request parameters to see if this is a new search
   */
  protected abstract boolean isNewSearch(Parameters param);

  /**
   * show thumbnails
   */
  public abstract boolean noThumbnails();

  /**
   * get search results, either from database or from results previously
   * cached in session object
   * @param conn database connection
   * @param param servlet parameters (contains search criteria etc)
   */
  public abstract void load(Connection conn, HttpSession session, Parameters param) throws SQLException, CkcException;
}
