
/**
 * Title:        ScavengerHuntItemPage<p>
 * Description:  base class for the item page that displays hunt information<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.html.HtmlUtils;
import java.io.IOException;
import java.sql.SQLException;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ScavengerHuntItemPage extends ItemPage
{
  public String scavText_;
  public int scavID_;
  public int scavItem_;
  public String scavImg_;
  public String filename_;
  public String client_;

  /**
   * load the context parameters
   * @param session the http session
   */
  protected void loadContextParams(HttpSession session)
  {
    scavID_ = JspSession.getInt(session, "scav_id", 1);
    client_ = (String) JspSession.load(session, "Client");
    filename_ = (String) JspSession.load(session, "Filename");
    scavImg_ = (String) JspSession.load(session, "scavImg");
  }

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
   * the jsp page base url as a method to be called from methods which construct
   * urls so it will return right value for derived classes
   */
  protected String jsp()
  {
    return URI_;
  }

  /**
   * load the information from the session and initialize the jsp
   */
  public void load(HttpSession session)
   throws SQLException, CkcException, Exception
  {
    super.load(session);
    loadContextParams(session);
  }

  /**
   * object viewer link
   * @param relPath the relative path to the viewer jsp page
   * @param text the text displayed as the link
   */
  public String objectViewer(String relPath, String text)
  {
    String params="";
    StringTokenizer toks = new StringTokenizer(super.objectViewer(), "?");
    while(toks.hasMoreTokens())
    {
      params = toks.nextToken();
    }
    StringBuffer buf = new StringBuffer();
    buf.append("<A HREF=\"");
    buf.append(relPath).append("/").append(ObjectViewerPage.Jsp_).append("?");
    buf.append(params).append("\">").append(text).append("</A>");
    return buf.toString();
  }

  /**
   * object viewer link
   * @param relPath the relative path to the viewer jsp page
   */
  public String objectViewer(String relPath)
  {
    String params="";
    StringTokenizer toks = new StringTokenizer(super.objectViewer(), "?");
    while(toks.hasMoreTokens())
    {
      params = toks.nextToken();
    }
    StringBuffer buf = new StringBuffer();
    buf.append("<A HREF=\"");
    buf.append(relPath).append("/").append(ObjectViewerPage.Jsp_).append("?");
    buf.append(params).append("\">");
    buf.append("<img src=\"").append(relPath).append("/images/viewerIcon.gif\" width=\"36\" height=\"35\" border=\"0\">");
    buf.append("</A>");
    return buf.toString();
  }

  /**
   * the scavenger hunt text with image and links to display while searching
   */
  public String scavText()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<font size=\"5\">You're on a Scavenger Hunt looking for: </font>");
    buf.append(scavengerImageTag()).append("<br>");
    buf.append(emptyReturnLink()).append("<br>");
    buf.append(returnLink());
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
   * the link to return with an item
   */
  protected String returnLink()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<a href=\"scavenger.jsp?scav_id=").append(scavID_);
    buf.append("&itemid=").append(itemID_).append("\">I found it!</a>(will return you to Hunt Image)");
    return buf.toString();
  }

  /**
   * the link to return with a message parameter
   */
  protected String returnLink(String message)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<a href=\"scavenger.jsp?scav_id=").append(scavID_);
    buf.append("&itemid=").append(itemID_).append("\">").append(message).append("</a>");
    return buf.toString();
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

}
