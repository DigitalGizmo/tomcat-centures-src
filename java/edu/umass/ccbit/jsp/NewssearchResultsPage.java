package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.NewssearchResults;
import edu.umass.ccbit.util.NewssearchParameters;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.CkcException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class NewssearchResultsPage extends ArticleListPager {
  // instance vars for searching/paging
  protected NewssearchResults results_;

  /**
   * text used in the search
   */
  public String searchSummary(HttpSession session) {
    String summary = results_.searchSummary(session);
    return summary;
  }

  /**
   * link to next search results page
   */
  protected String nextPageLink() {
    return pager_.nextPageLink( URI_, "Next page" );
  }

  /**
   * link to previous search results page
   */
  protected String previousPageLink() {
    return pager_.previousPageLink( URI_, "Previous page" );
  }

  /**
   * number of results
   */
  public int numResults() {
    return results_.getCount();
  }

  /**
   * url to refine search (i.e., search page with previous parameters filled in)...
   * appends parameters to baseurl
   * @param baseurl url to which parameters are attached
   */
  protected String refineSearchURL() {
    return refineSearchURL( "index.jsp" );
  }

  /**
   * refine search url
   */
  public static String refineSearchURL( String baseurl ) {
    StringBuffer url=new StringBuffer();
    url.append(baseurl);
    HtmlUtils.addToLink( MainSearchPage.RefineSearch_, 1, true, url );
    return url.toString();
  }

  /**
   * navigation
   */
  protected String navigation() {
    Navigator nav=new Navigator();
    nav.add(refineSearchURL(), "Search");
    nav.add("Results");
    return nav.toString();
  }

  /**
   * description of results using parameters
   */
  protected String resultsDescription( String noResults, String oneResult, String xResults ) {
    StringBuffer buf=new StringBuffer();
    if (numResults() > 1)
      buf.append(numResults()).append(" ").append(xResults);
    else if (numResults()==1)
      buf.append(oneResult);
    else
      buf.append(noResults);
    return buf.toString();
  }

  /**
   * load search results
   */
  protected abstract void load(HttpSession session) throws SQLException, CkcException;
}
