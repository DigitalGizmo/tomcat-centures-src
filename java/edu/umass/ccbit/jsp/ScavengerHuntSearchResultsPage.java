
/**
 * Title:        ScavengerHuntSearchResultsPage<p>
 * Description:  base class for scavenger search result page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ScavengerHuntSearchResultsPage extends BasicSearchResultsPage
{
  public String scavText_;
  public int scavID_;
  public int scavItem_;
  public String scavImg_;

  /**
   * parse the request parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
  }

  /**
   * load the information from the session and initialize the jsp
   */
  public void load(HttpSession session)
   throws SQLException, CkcException
  {
    super.load(session);
    scavID_ = JspSession.getInt(session, "scav_id", 1);
    scavItem_ = JspSession.getInt(session, "scav_item", 0);
    scavImg_ = (String) JspSession.load(session, "scavImg");
  }

  /**
   * text to return to the search page with the previous search
   */
  protected String refineSearchURL()
  {
    return super.refineSearchURL("searchindex.jsp")+"&scav_item="+scavItem_;
  }

  /**
   * the scavenger hunt text with image and links to display while searching
   */
  public String scavText()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("You're on a Scavenger Hunt looking for: ");
    buf.append(scavengerImageTag()).append("<br>");
    buf.append(emptyReturnLink());
    return buf.toString();
  }

  /**
   * the image tag for the particular item being searched for
   */
  protected String scavengerImageTag()
  {
    return "<img src="+scavImg_+">";
  }

  /**
   * the link to return without any item
   */
  protected String emptyReturnLink()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<a href=\"scavenger.jsp?scav_id=").append(scavID_);
    buf.append("\">return to Hunt Image</a>");
    return buf.toString();
  }

  public String itemPageBaseUrl()
  {
    return "itempage.jsp";
  }

}
