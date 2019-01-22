package edu.umass.ccbit.database;

import edu.umass.ccbit.jsp.Navigator;
import edu.umass.ccbit.util.ArticleIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.Parameters;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class NewssearchResults extends DbLoadableArticleIDRankedIntersection {
  protected static final String searchResultsSessionAttribute_="search_results";
  public static final String SearchText_="search.text";

  /**
   * attribute name for session
   */
  protected String attributeName() {
    return searchResultsSessionAttribute_;
  }

  /**
   * pager link
   */
  public String pagerLink( String mainUrl, String resultsUrl, int pageNumber ) {
    Navigator nav=new Navigator();
    nav.add(mainUrl, "Search");
    nav.add(MainCollectionItemsPager.pageLink(pageNumber, resultsUrl, "Results"));
    return nav.toString();
  }

  /**
   * save to session
   */
  public void saveToSession( HttpSession session ) {
    if (articleIDs_ == null)
      articleIDs_ = new ArticleIDList();
    super.saveToSession(session);
  }

  /**
   * search summary
   */
  public abstract String searchSummary( HttpSession session );

  /**
   * search text
   */
  public abstract String searchText();

  /**
   * get search parameters
   */
  protected abstract void getSearchParameters( Parameters param );

  /**
   * check request parameters to see if this is a new search
   */
  protected abstract boolean isNewSearch( Parameters param );

  /**
   * get search results, either from database or from results previously
   * cached in session object
   * @param conn database connection
   * @param param servlet parameters (contains search criteria etc)
   */
  public abstract void load( Connection conn, HttpSession session, Parameters param ) throws SQLException, CkcException;
}
