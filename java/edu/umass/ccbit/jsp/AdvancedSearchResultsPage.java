
/**
 * Title:        AdvancedSearchResultsPage<p>
 * Description:  base class for adv results page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.AdvancedSearchResults;
import edu.umass.ccbit.database.Keywords;
import edu.umass.ccbit.database.Materials;
import edu.umass.ckc.util.CkcException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class AdvancedSearchResultsPage extends SearchResultsPage
{
  /**
   * load search results
   */
  protected void load(HttpSession session) throws SQLException, CkcException
  {
    results_ = new AdvancedSearchResults();
    results_.load(connection_, session, servletParams_);
    load(session, results_);
  }
}
