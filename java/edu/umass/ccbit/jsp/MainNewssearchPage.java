package edu.umass.ccbit.jsp;

import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.NewssearchParameters;
import java.io.IOException;
import java.lang.Integer;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

public abstract class MainNewssearchPage extends HttpDbJspBase {
  NewssearchParameters searchParams_;
  // request params
  public static final String RefineSearch_="redo";
  // request values
  protected boolean refineSearch_;

  public void load(HttpSession session) throws SQLException {
    refineSearch_=servletParams_.containsKey( RefineSearch_ );
    
    if (refineSearch_) {
      searchParams_=NewssearchParameters.loadFromSession(session);
    } else {
      searchParams_=new NewssearchParameters();
    }
  }

  /**
   * search text
   */
  protected String searchText() {
    return searchParams_.searchText();
  }

  /**
   * search text input with variable size
   */
  protected void writeSearchTextInput( JspWriter out, int size ) throws IOException {
    JspUtil.writeTextInput(out, NewssearchParameters.SearchText_, JspUtil.formatEntryText(searchText()), size, 200);
  }

  /**
   * contains checkbox
   */
  protected void writeContainsCheckbox( JspWriter out ) throws IOException {
    JspUtil.writeCheckbox(out, NewssearchParameters.ContainsTextSearch_, searchParams_.containsTextSearch());
  }
}

