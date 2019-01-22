package edu.umass.ccbit.database;

import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.AdvancedNewssearchParameters;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.Parameters;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class AdvancedNewssearchResults extends NewssearchResults {
  // search parameters
  AdvancedNewssearchParameters advSearchParams_;

  /**
   * get search parameters
   */
  protected void getSearchParameters(Parameters param) {
    advSearchParams_ = new AdvancedNewssearchParameters();
    advSearchParams_.getParameters(param);
  }

  /**
   * save to session
   */
  public void saveToSession(HttpSession session) {
    super.saveToSession(session);
    JspSession.setItemPageViewMode(session, JspSession.itemPageViewModes.AdvSearch_);
    JspSession.save(session, SearchText_, advSearchParams_.searchText());
  }

  /**
   * search summary
   */
  public String searchSummary(HttpSession session) {
    return advSearchParams_.searchSummary(session);
  }

  /**
   * search text
   */
  public String searchText() {
    return advSearchParams_.searchText();
  }

  /**
   * attribute name for session
   */
  protected String attributeName() {
    return searchResultsSessionAttribute_;
  }

  /**
   * check request parameters to see if this is a new search
   */
  protected boolean isNewSearch(Parameters param) {
    // assume new search if search text parameter was passed
    return param.containsKey(AdvancedNewssearchParameters.SearchText_);
  }

  /**
   * load the data...don't load if query is null
   */
  public void load(Connection conn, String query) throws SQLException {
    if (query != null) super.load(conn, query);
  }

  /**
   * get search results, either from database or from results previously
   * cached in session object
   * @param conn database connection
   * @param param servlet parameters (contains search criteria etc)
   */
  public void load(Connection conn, HttpSession session, Parameters param) throws SQLException, CkcException {
    if (isNewSearch(param)) {
      getSearchParameters(param);
      load(conn, advSearchParams_.textSearchQueryVector());
      load(conn, advSearchParams_.searchMainLinkTablesQuery());
      saveToSession(session);
      advSearchParams_.saveToSession(session);
      
    } else {
      advSearchParams_=AdvancedNewssearchParameters.loadAdvFromSession(session);
      getFromSession(session);
    }
  }
}
