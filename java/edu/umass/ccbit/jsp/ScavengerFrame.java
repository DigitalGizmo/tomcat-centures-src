
/**
 * Title:        ScavengerFrame<p>
 * Description:  base class for searching frames<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.jasper.runtime.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.image.MrSidImage;

public abstract class ScavengerFrame extends HttpDbJspBase
{
  public int scavItem_;
  public int scavID_;
  public static String view_="Web_ScavengerHuntView";
  //coordinates of the cropped section
  public int x1_;
  public int y1_;
  public int x2_;
  public int y2_;
  public int centerX_;
  public int centerY_;
  public int width_;
  public int height_;
  public int imageLevel_;
  public String client_;
  public String filename_;
  public MrSidImage itemImage_;

  /**
   * parse the request parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    scavItem_=servletParams_.getInt("scav_item");
  }

  /**
   * parameters for nothing found return
   */
  protected String notFoundParameters()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("?scav_id=").append(scavID_);
    return buf.toString();
  }

  /**
   * load the scav_item into a session
   */
  private void load(HttpSession session)
  {
    JspSession.setInt(session, "scav_item", scavItem_);
    JspSession.setInt(session, "scav_id", scavID_);
    session.setAttribute("scavImg", itemImage_.imageRef(imageLevel_, centerX_, centerY_, width_, height_));
    session.setAttribute("hunt", new Boolean(true));
  }

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
   * load the context parameters
   */
  protected void loadContextParams(HttpSession session)
  {
    scavID_ = JspSession.getInt(session, "scav_id", 1);
    client_ = (String) JspSession.load(session, "Client");
    filename_ = (String) JspSession.load(session, "Filename");
  }

  /**
   * scaled image tag of the hotspot to show what they are looking for
   */
  protected String imgTag()
  {
    return itemImage_.imageRef(imageLevel_, centerX_, centerY_, width_, height_);
  }
}
