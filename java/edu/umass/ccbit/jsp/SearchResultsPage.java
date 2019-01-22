/**
 * Title:        SearchResultsPage<p>
 * Description:  base class for search results jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.Keywords;
import edu.umass.ccbit.database.Materials;
import edu.umass.ccbit.database.SearchResults;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.CkcException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class SearchResultsPage extends ItemListPager
{
  // init parameter for item page url - NOTE: this doesn't work yet because
  // <jsp-file> tag in jakarta is broken..using servlet context parameter for now
  protected static final String ItemPageBaseURL_="itempagebaseurl";
  // temporary servlet context parameter for item page base url
  protected static final String ContextItemPageBaseURL_="search.itempagebaseurl";

  // instance vars for searching/paging
  protected SearchResults results_;
  // url for item page
  protected String itemPageBaseURL_;
  // for search summary
  protected Keywords keywords_;
  protected Materials materials_;

  protected String itemPageBaseUrl()
  {
    return itemPageBaseURL_;
  }

  /**
   * text used in the search
   */
  public String searchSummary(HttpSession session)
  {
    String summary = results_.searchSummary(session);
    if(servletParams_.getInt(SearchParameters.Lexicon_, 0)!=0)
      return summary + ", using lexicon";
    return summary;
  }

  /**
   * lexicon display
   */
  public String lexiconDisplay()
  {
    String searchText = results_.searchText();
    if(servletParams_.getInt(SearchParameters.Lexicon_, 0)==0)
      return MainSearchPage.lexicon_.lexiconDisplay(searchText);
    return "";
  }

  /**
   * link to next search results page
   */
  protected String nextPageLink()
  {
    return pager_.nextPageLink(URI_, "Next page", servletParams_.getInt(SearchParameters.Lexicon_, 0));
  }

  /**
   * link to previous search results page
   */
  protected String previousPageLink()
  {
    return pager_.previousPageLink(URI_, "Previous page", servletParams_.getInt(SearchParameters.Lexicon_, 0));
  }


  /**
   * number of results
   */
  public int numResults()
  {
    return results_.getCount();
  }

  /**
   * image link to search result item
   */
  protected String resultImageLink(int nitem, int width, int height)
  {
    if (!results_.noThumbnails())
      return super.resultImageLink(nitem, width, height);
    else
      return "";
  }

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    //itemPageBaseURL_=initParams_.getString(ItemPageBaseURL_, "");
    itemPageBaseURL_=initParams_.getString(ContextItemPageBaseURL_, "");
  }

  /**
   * url to refine search (i.e., search page with previous parameters filled in)...
   * appends parameters to baseurl
   * @param baseurl url to which parameters are attached
   */
  protected String refineSearchURL()
  {
    return refineSearchURL("index.jsp");
  }

  /**
   * refine search url
   */
  public static String refineSearchURL(String baseurl)
  {
    StringBuffer url=new StringBuffer();
    url.append(baseurl);
    HtmlUtils.addToLink(MainSearchPage.RefineSearch_, 1, true, url);
    return url.toString();
  }

  /**
   * navigation
   */
  protected String navigation()
  {
    Navigator nav=new Navigator();
    nav.add(refineSearchURL(), "Search");
    nav.add("Results");
    return nav.toString();
  }

  /**
   * description of results
   */
  protected String resultsDescription()
  {
    StringBuffer buf=new StringBuffer();
    if (numResults() > 1)
      buf.append(numResults()).append(" items have been found that match your search request.");
    else if (numResults()==1)
      buf.append("One item has been found that matches your search request.");
    else
      buf.append("No items were found that match your search request.");
    return buf.toString();
  }

  /**
   * description of results using parameters
   */
  protected String resultsDescription(String noResults, String oneResult, String xResults)
  {
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
