package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.AdvancedNewssearchResults;
import edu.umass.ckc.util.CkcException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class AdvancedNewssearchResultsPage extends NewssearchResultsPage {
  /**
   * load search results
   */
  protected void load(HttpSession session) throws SQLException, CkcException {
    results_ = new AdvancedNewssearchResults();
    results_.load(connection_, session, servletParams_);
    load(session, results_);
  }
}
