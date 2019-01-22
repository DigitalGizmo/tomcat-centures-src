package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.BasicNewssearchResults;
import java.sql.SQLException;
import edu.umass.ckc.util.CkcException;
import javax.servlet.http.HttpSession;

public abstract class BasicNewssearchResultsPage extends NewssearchResultsPage {
  /**
   * load search results
   */
  protected void load(HttpSession session) throws SQLException, CkcException {
    results_ = new BasicNewssearchResults();
    results_.load( connection_, session, servletParams_ );
    load( session, results_ );
  }
}
