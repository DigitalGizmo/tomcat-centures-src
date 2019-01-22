package edu.umass.ccbit.database;

import edu.umass.ccbit.util.IntegerMap;
import edu.umass.ccbit.util.ArticleIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.Parameters;
import java.lang.Math;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class MainCollectionArticlesPager extends DbLoadableArticleList {
  // from script parameters
  protected int perPage_;
  protected int pageNumber_;
  // instance vars set by getPageArticleRange
  protected int firstArticle_;
  protected int lastArticle_;
  protected int previousPage_;
  protected int nextPage_;
  // key names for script parameters
  public static final String PerPage_="perpage";
  public static final String PageNumber_="pagenum";
  protected static final int perPageDefault_=10;
  // mapping of article id's to index of article in result set...this is necessary
  // because query does not return articles in the same order as the articleid's in
  // the "WHERE ArticleID IN (....)" clause
  private IntegerMap articleIndexMapping_;

  /**
   * constructor
   */
  public MainCollectionArticlesPager() {
    previousPage_=-1;
    nextPage_=-1;
  }

  /**
   * get parameters
   */
  protected void getParameters(HttpSession session, Parameters param) {
    pageNumber_=param.getInt(PageNumber_, 0);
    perPage_=JspSession.getInt(session, PerPage_, perPageDefault_);
  }

  /**
   * get per page
   */
  public int getPerPage() {
    return perPageDefault_;
  }

  public int upperBound() {
    return lastArticle_;
  }

  public int lowerBound() {
    return firstArticle_;
  }

  /**
   * get article range for page, adjusting pageNumber_ if necessary, and
   * setting values for first/last articles on current page, and page numbers
   * of next and previous pages
   */
  protected void getPageArticleRange( int numArticles ) {
    //if numArticles % perPage_ = 0 then there will be an extra results page...
    int numPages;
    if((numArticles % perPage_)==0)
      numPages=(numArticles / perPage_);
    else
      numPages=(numArticles / perPage_) + 1;
    // adjust page number so that it falls within
    // valid range of pages..page numbers are zero-based
    if (pageNumber_ < 0 || pageNumber_ >= numPages) {
      pageNumber_=0;
    }
    firstArticle_ = pageNumber_ * perPage_;
    lastArticle_  = Math.min( firstArticle_+perPage_, numArticles ) - 1;
    // set values of next/previous pages..values of -1 indicate that
    // there is no next page (i.e., already on last page) or no previous
    // page (already on first page)..note that in the latter case, all we
    // have to do is subtract 1 :-)
    previousPage_ = pageNumber_ - 1;
    nextPage_     = pageNumber_ < numPages - 1 ? pageNumber_ + 1 : -1;
  }

  /**
   * page number for given article index and number of articles..
   * this is used to create a link from an article page in the search
   * result set back to search results page containing the article
   */
  /*public static int resultsPageNumber(HttpSession session, int narticle) {
    int perPage=JspSession.getInt(session, PerPage_, perPageDefault_);
    return narticle / perPage;
  }*/

  /**
   * link to next page
   */
  public static String pageLink(int page, String url, String text) {
    // if page doesn't exist, return text without link
    if (page==-1)
      return text;
    else {
      StringBuffer link=new StringBuffer();
      link.append(url);
      HtmlUtils.addToLink(PageNumber_, page, true, link);
      return HtmlUtils.anchor(link.toString(), text);
    }
  }

  /**
   * link to next page
   */
  public String nextPageLink(String url, String text) {
    return pageLink(nextPage_, url, text);
  }

  /**
   * link to previous page
   */
  public String previousPageLink(String url, String text) {
    return pageLink(previousPage_, url, text);
  }

  /**
   * get article id mapping..
   * this sets up a mapping so that we may order results
   * read from the database in the same order as the article id list
   * which was used to read the results...
   * the order of results of a query like "SELECT * WHERE ArticleID IN (...)"
   * may not match the order of the articles given in (...), so this is
   * necessary
   */
  private void getArticleIndexMapping( DbLoadableArticleIDList articles ) {
    IntegerMap articleIDmap=new IntegerMap();
    
    // map: articleID --> its index in result set
    for (int i=0; i < getCount(); i++) {
      CollectionArticleInfo articleInfo=(CollectionArticleInfo) get(i);
      articleIDmap.put( articleInfo.articleID_, i );
    }
    
    articleIndexMapping_=new IntegerMap();
    
    // original index of articleID --> articleID --> index in results
    for (int i=0; i <= getCount(); i++) {
      Integer ival=articleIDmap.get(articles.articleID( i + firstArticle_ ));
      if (ival != null) {
        articleIndexMapping_.put(i, ival.intValue());
      }
    }
  }

  /**
   * formulate query to load articles
   */
  protected String query( DbLoadableArticleIDList articles ) {
    StringBuffer buf=new StringBuffer();
    buf.append( "SELECT * FROM NewsIndex WHERE " );
    buf.append( CollectionArticleInfo.fields.ArticleID_ );
    buf.append( " IN (" );
    for (int i=firstArticle_; i <= lastArticle_; i++) {
      buf.append(articles.articleID(i));
      if (i < lastArticle_)
        buf.append(", ");
    }
    buf.append(")");
    return buf.toString();
  }

  /**
   * create new instance from result set
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException {
    CollectionArticleInfo info=new CollectionArticleInfo();
    info.init(result);
    return info;
  }

  /**
   * load
   */
  public void load(Connection conn, HttpSession session, Parameters param, DbLoadableArticleIDList articles)
  throws SQLException
  {
    if (articles != null && articles.getCount() > 0)
    {
      getParameters(session, param);
      getPageArticleRange(articles.getCount());
      load(conn, query(articles));
      getArticleIndexMapping(articles);
    }
  }

  /**
   * get article at index
   */
  protected CollectionArticleInfo getArticleInfo(int index)
  {
    Integer val=articleIndexMapping_.get(index);
    if (val != null)
      return (CollectionArticleInfo) get(val.intValue());
    else
      return null;
  }

  /**
   * description of articles on page
   */
  public String description() {
    if (getCount() > 0 && firstArticle_ < lastArticle_) {
      StringBuffer buf=new StringBuffer();
      buf.append("articles ");
      // articles are zero-based, so add 1
      buf.append(firstArticle_ + 1).append(" - ").append(lastArticle_ + 1);
      return buf.toString();
    }
    else
      return "";
  }

  /**
   * article primary topic at index
   */
  public String primarySubject(int index) {
    return getArticleInfo(index).primarySubject_;
  }

  /**
   * article secondary topic at index
   */
  public String secondarySubject(int index) {
    return getArticleInfo(index).secondarySubject_;
  }

  /**
   * article tertiary topic at index
   */
  public String tertiarySubject(int index) {
    return getArticleInfo(index).tertiarySubject_;
  }

  /**
   * article publication at index
   */
  public String publication(int index) {
    return getArticleInfo(index).publication_;
  }

  /**
   * article id
   */
  public int articleID( int index ) {
    return getArticleInfo( index ).articleID_;
  }

  /**
   * article description at index
   */
  public String description(int index) {
    return getArticleInfo(index).description_;
  }
}