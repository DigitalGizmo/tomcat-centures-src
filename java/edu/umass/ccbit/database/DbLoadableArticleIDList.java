package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ArticleIDList;
import javax.servlet.http.HttpSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

public abstract class DbLoadableArticleIDList extends DbLoadable {
  protected ArticleIDList articleIDs_;

  // field name for articleid field
  public static final String ArticleID_="ArticleID";

  /**
   * get number of article ids in list
   */
  public int getCount() {
    return articleIDs_ != null ? articleIDs_.size() : 0;
  }

  /**
   * article id at index
   * returns -1 if index was invalid
   */
  public int articleID(int narticle) {
    if (narticle >= 0 && narticle < getCount())
      return articleIDs_.getValue(narticle);
    else
      return -1;
  }

  /**
   * finds index for article
   * returns -1 if not found
   */
  public int indexForArticleID(int articleID) {
    for (int i=0; i<getCount(); i++) {
      if (articleID(i)==articleID)
        return i;
    }
    return -1;
  }

  /**
   * attribute name for saving to session
   */
  protected abstract String attributeName();

  /**
   * save results in user session
   */
  public void saveToSession(HttpSession session) {
    session.setAttribute(attributeName(), articleIDs_);
  }

  /**
   * link to page in article pager
   */
  public abstract String pagerLink( String mainUrl, String resultsUrl, int pageNumber );

  /**
   * get results from user session
   */
  public void getFromSession(HttpSession session) {
    try {
      articleIDs_ = (ArticleIDList) session.getAttribute(attributeName());
    } catch (Exception e) {
      articleIDs_=new ArticleIDList();
    }
  }

  /**
   * throw away article ids which are not in idList
   */
  public void retainAll(ArticleIDList idList) {
    articleIDs_.retainAll( idList );
  }

  /**
   * init
   */
  public void initFromResult(ResultSet result) throws SQLException {
    articleIDs_ = new ArticleIDList();
    initFromResult(articleIDs_, result);
  }

  /**
   * init article list from result set
   */
  public static void initFromResult(ArticleIDList articles, ResultSet result) throws SQLException {
    while (result.next()) {
      articles.add(result.getInt( ArticleID_ ));
    }
  }
}
