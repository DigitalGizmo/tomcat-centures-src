package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.Newstopics;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.AdvancedNewssearchParameters;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

public abstract class AdvancedNewssearchPage extends MainNewssearchPage
{
  // search param saved names
  public static final String Topics_ = "topics";
  // request params
  public static final String RefineSearch_="redo";
  //default text input length
  public static final int TextInputDefaultLength_=50;
  // request values
  protected boolean refineSearch_;
  public static Newstopics topics_;

  // adv. parameters
  AdvancedNewssearchParameters searchAdvParams_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit() {
    super.jspInit();
    
    try {
      getDbConnection();
      topics_=new Newstopics();
      topics_.load(connection_, getServletContext());
      releaseDbConnection();
      
    } catch (Exception e) {
      System.out.println("WARNING: Unable to load data needed for advanced newssearch page!");
      System.out.println(e.toString());
    }
  }

  /**
   * load from session
   */
  public void load(HttpSession session) throws SQLException {
    refineSearch_=servletParams_.containsKey(RefineSearch_);

    if (refineSearch_)
      searchAdvParams_=AdvancedNewssearchParameters.loadAdvFromSession(session);
    else
      searchAdvParams_=new AdvancedNewssearchParameters();
  }

  /**
   * search text
   */
  protected String searchText() {
    return searchAdvParams_.searchText();
  }

  /**
   * topic dropdown with parameter
   */
  protected void writeTopicDropdown( JspWriter out, int nTopic ) throws IOException {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append( AdvancedNewssearchParameters.Topic_ ).append( nTopic ).append( " size=1>\n" );
    buf.append("<OPTION value=0>pick a topic...\n");
    
    for (int i=0; i<topics_.getCount(); i++) {
      buf.append("<OPTION value=").append(topics_.getTopicID(i));
      if (topics_.getTopicID(i)==searchAdvParams_.topic( nTopic ))
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(topics_.getTopic(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * contains checkbox
   */
  protected void writeContainsCheckbox(JspWriter out) throws IOException {
    JspUtil.writeCheckbox(out, AdvancedNewssearchParameters.ContainsTextSearch_, searchAdvParams_.containsTextSearch());
  }
}
