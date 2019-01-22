
/**
 * Title:        BasicSearchResults<p>
 * Description:  base class for basic form search results<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ItemIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ckc.util.Parameters;
import edu.umass.ckc.util.CkcException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class BasicSearchResults extends SearchResults {
  // search parameters
  SearchParameters searchParams_;

  /**
   * get search parameters
   */
  protected void getSearchParameters(Parameters param) {
    searchParams_=new SearchParameters();
    searchParams_.getParameters(param);
  }

  /**
   * save to session
   */
  public void saveToSession(HttpSession session) {
    super.saveToSession(session);
    JspSession.setItemPageViewMode(session, JspSession.itemPageViewModes.Search_);
    JspSession.save(session, SearchText_, searchParams_.searchText());
  }

  /**
   * search summary
   */
  public String searchSummary(HttpSession session) {
    return searchParams_.searchSummary();
  }

  /**
   * search text
   */
  public String searchText() {
    return searchParams_.searchText();
  }

  /**
   * check request parameters to see if this is a new search
   */
  protected boolean isNewSearch(Parameters param) {
    // assume new search if search text parameter was passed
    return param.containsKey(SearchParameters.SearchText_);
  }

  /**
   * checks request parameters to see if this is a lexicon aided search
   */
  protected boolean isNewLexiconSearch(Parameters param) throws CkcException {
    if(param.getInt(SearchParameters.Lexicon_, 0)==1 && param.getString(SearchParameters.SearchText_, "").length()>0)
      return true;
    return false;
  }

  /**
   * show thumbnails
   */
  public boolean noThumbnails() {
    return searchParams_.noThumbnails();
  }

  /**
   * get search results, either from database or from results previously
   * cached in session object
   * @param conn database connection
   * @param param servlet parameters (contains search criteria etc)
   */
  public void load(Connection conn, HttpSession session, Parameters param) throws SQLException, CkcException {
    if (isNewLexiconSearch(param)) {
      String searchText = param.getString(SearchParameters.SearchText_);
      String contains = param.getString(SearchParameters.ContainsTextSearch_);
      searchParams_=SearchParameters.loadFromSession(session);
      searchParams_.put(SearchParameters.SearchText_, searchText);
      searchParams_.put(SearchParameters.ContainsTextSearch_, contains);
      load(conn, searchParams_.textSearchQueryVector());
      //load(conn, searchParams_.textSearchQuery());
      saveToSession(session);
      searchParams_.saveToSession(session);
      
    } else {
      if (isNewSearch(param)) {
        getSearchParameters(param);
        load(conn, searchParams_.textSearchQueryVector());
        //load(conn, searchParams_.textSearchQuery());
        saveToSession(session);
        searchParams_.saveToSession(session);
      } else {
        searchParams_=SearchParameters.loadFromSession(session);
        getFromSession(session);
      }
    }
  }
}
