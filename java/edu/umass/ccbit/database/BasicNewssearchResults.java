package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ArticleIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.NewssearchParameters;
import edu.umass.ckc.util.Parameters;
import edu.umass.ckc.util.CkcException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class BasicNewssearchResults extends NewssearchResults {
  // search parameters
  NewssearchParameters searchParams_;

  /**
   * get search parameters
   */
  protected void getSearchParameters( Parameters param ) {
    searchParams_ = new NewssearchParameters();
    searchParams_.getParameters( param );
  }

  /**
   * save to session
   */
  public void saveToSession(HttpSession session) {
    super.saveToSession(session);
    JspSession.save( session, SearchText_, searchParams_.searchText() );
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
    return param.containsKey( NewssearchParameters.SearchText_ );
  }

  /**
   * get search results, either from database or from results previously
   * cached in session object
   * @param conn database connection
   * @param param servlet parameters (contains search criteria etc)
   */
  public void load( Connection conn, HttpSession session, Parameters param ) throws SQLException, CkcException {
    if (isNewSearch( param )) {
      getSearchParameters( param );
      load( conn, searchParams_.textSearchQueryVector() );
      //load(conn, searchParams_.textSearchQuery());
      saveToSession( session );
      searchParams_.saveToSession(session);
    } else {
      searchParams_ = NewssearchParameters.loadFromSession( session );
      getFromSession( session );
    }
  }
}
