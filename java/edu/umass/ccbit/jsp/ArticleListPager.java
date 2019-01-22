/**
 * Title:        ArticleListPager<p>
 * Description:  base class for paging through a list of news articles<p>
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.DbLoadableArticleIDList;
import edu.umass.ccbit.database.MainCollectionArticlesPager;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.util.Vector;
import java.net.URLEncoder;
import javax.servlet.http.HttpSession;

public abstract class ArticleListPager extends HttpDbJspBase {
  // key names for parameters
  public static final String PageNumber_="page";
  // article pager object
  protected MainCollectionArticlesPager pager_;
  // parameter values
  protected int pageNumber_;

  /**
   * number of results displayed on page
   */
  public int numDisplayedResults() {
    return pager_.getCount();
  }

  /**
   * link to previous search results page
   */
  protected String previousPageLink() {
    return pager_.previousPageLink( URI_, "Previous page" );
  }

  /**
   * link to next search results page
   */
  protected String nextPageLink() {
    return pager_.nextPageLink( URI_, "Next page" );
  }

  /**
   * description of search results on this page, e.g., 'articles xx-yy'
   */
  protected String resultsPageDescription() {
    return pager_.description();
  }

  /**
   * result text
   */
  protected String resultText( int nArticle ) {
    return pager_.description( nArticle );
  }

  /**
   * article publication
   */
  protected String publication( int nArticle ) {
    return pager_.publication( nArticle );
  }

  /**
   * load
   */
  protected void load( HttpSession session, DbLoadableArticleIDList articleList ) throws SQLException {
    pager_ = new MainCollectionArticlesPager();
    pager_.load( connection_, session, servletParams_, articleList );
  }
}
