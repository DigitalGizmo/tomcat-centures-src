
/**
 * Title:        AdvancedSearchResults<p>
 * Description:  database search results; advanced<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.jsp.Navigator;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.AdvancedSearchParameters;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.Parameters;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class AdvancedSearchResults extends SearchResults
{
  // search parameters
  AdvancedSearchParameters advSearchParams_;

  /**
   * get search parameters
   */
  protected void getSearchParameters(Parameters param)
  {
    advSearchParams_=new AdvancedSearchParameters();
    advSearchParams_.getParameters(param);
  }

  /**
   * save to session
   */
  public void saveToSession(HttpSession session)
  {
    super.saveToSession(session);
    JspSession.setItemPageViewMode(session, JspSession.itemPageViewModes.AdvSearch_);
    JspSession.save(session, SearchText_, advSearchParams_.searchText());
  }

  /**
   * search summary
   */
  public String searchSummary(HttpSession session)
  {
    return advSearchParams_.searchSummary(session);
  }

  /**
   * search text
   */
  public String searchText()
  {
    return advSearchParams_.searchText();
  }


  /**
   * attribute name for session
   */
  protected String attributeName()
  {
    return searchResultsSessionAttribute_;
  }

  /**
   * check request parameters to see if this is a new search
   */
  protected boolean isNewSearch(Parameters param)
  {
    // assume new search if search text parameter was passed
    return param.containsKey(AdvancedSearchParameters.SearchText_);
  }

  /**
   * checks request parameters to see if this is a lexicon aided search
   */
  protected boolean isLexiconSearch(Parameters param) throws CkcException
  {
    if(param.getInt(AdvancedSearchParameters.Lexicon_, 0)==1)
      return true;
    return false;
  }

  /**
   * show thumbnails
   */
  public boolean noThumbnails()
  {
    return advSearchParams_.noThumbnails();
  }

  /**
   * load the data...don't load if query is null
   */
  public void load(Connection conn, String query) throws SQLException
  {
    if (query != null)
      super.load(conn, query);
  }

  /**
   * get search results, either from database or from results previously
   * cached in session object
   * @param conn database connection
   * @param param servlet parameters (contains search criteria etc)
   */
  public void load(Connection conn, HttpSession session, Parameters param) throws SQLException, CkcException
  {
    if (isNewSearch(param))
    {
      if (isLexiconSearch(param))
      {
        String searchText = param.getString(AdvancedSearchParameters.SearchText_);
        String contains = param.getString(AdvancedSearchParameters.ContainsTextSearch_);
        advSearchParams_=AdvancedSearchParameters.loadAdvFromSession(session);
        advSearchParams_.put(AdvancedSearchParameters.SearchText_, searchText);
        advSearchParams_.put(AdvancedSearchParameters.ContainsTextSearch_, contains);
      }
      else
      {
        getSearchParameters(param);
      }
      //load(conn, advSearchParams_.searchMainByTextDateQuery());
      load(conn, advSearchParams_.textSearchQueryVector());
      load(conn, advSearchParams_.searchAccessionQuery());
      load(conn, advSearchParams_.searchItemNameQuery());
      load(conn, advSearchParams_.searchCollectionPlaces());
      load(conn, advSearchParams_.searchCollectionPeople());
      load(conn, advSearchParams_.searchMainLinkTablesQuery());
      saveToSession(session);
      advSearchParams_.saveToSession(session);
    }
    else
    {
      advSearchParams_=AdvancedSearchParameters.loadAdvFromSession(session);
      getFromSession(session);
    }
  }
}
