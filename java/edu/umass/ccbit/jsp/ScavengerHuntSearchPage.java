
/**
 * Title:        ScavengerHuntSearchPage<p>
 * Description:  base class for search page in scavenger hunt<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.image.MrSidImage;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public abstract class ScavengerHuntSearchPage extends MainSearchPage
{
  public String scavText_;
  public int scavID_;
  public int x1_;
  public int y1_;
  public int x2_;
  public int y2_;
  public int imageLevel_;
  public int centerX_;
  public int centerY_;
  public int width_;
  public int height_;
  public int scavItem_;
  public String filename_;
  public String client_;
  public MrSidImage itemImage_;

  /**
   * load info from the database and initialize variables
   */
  protected void load(HttpSession session, ServletConfig config) throws SQLException
  {
    ServletContext context = config.getServletContext();
    Statement st = connection_.createStatement();
    ResultSet result = st.executeQuery(query());
    result.next();
    x1_=result.getInt("ImageX");
    y1_=result.getInt("ImageY");
    x2_=result.getInt("ImageWidth");
    y2_=result.getInt("ImageHeight");
    imageLevel_=result.getInt("ImageLevel");
    filename_=result.getString("Filename");
    client_=result.getString("Client");
    itemImage_ = new MrSidImage(client_, filename_);
    centerX_=4*(x1_ + x2_)/2;
    centerY_=4*(y1_ + y2_)/2;
    width_=((x1_ + x2_)/2 - x1_)*2;
    height_=((y1_ + y2_)/2 - y1_)*2;
    load(session);
    result.close();
    st.close();
  }

  /**
   * load the scav_item into a session
   */
  public void load(HttpSession session) throws SQLException
  {
    super.load(session);
    scavID_ = JspSession.getInt(session, "scav_id", 1);
    JspSession.setInt(session, "scav_item", scavItem_);
    //JspSession.setInt(session, "scav_id", scavID_);
    session.setAttribute("scavImg", itemImage_.imageRef(imageLevel_, centerX_, centerY_, width_, height_));
  }

  /**
   * load the context parameters
   */
  protected void loadContextParams(HttpSession session)
  {
    scavID_ = JspSession.getInt(session, "scav_id", 1);
    client_ = (String) JspSession.load(session, "Client");
    filename_ = (String) JspSession.load(session, "Filename");
  }

  /**
   * parse the request parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    scavItem_=servletParams_.getInt("scav_item", 0);
  }

  /**
   * construct a query
   */
  protected String query()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM Web_ScavengerHuntView");
    buf.append(" WHERE ScavengerHuntItemID=").append(scavItem_);
    return buf.toString();
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
    return "<img src="+imgTag()+">";
  }

  /**
   * scaled image tag of the hotspot to show what they are looking for
   */
  protected String imgTag()
  {
    return itemImage_.imageRef(imageLevel_, centerX_, centerY_, width_, height_);
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
