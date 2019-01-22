
/**
 * Title:        InteractiveActivityPage<p>
 * Description:  base class for flash jsp page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.InteractiveActivity;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.html.HtmlUtils;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;

public abstract class InteractiveActivityPage extends HttpDbJspBase
{
  // reference itemid
  protected int itemID_;
  // activity
  protected InteractiveActivity activity_;

  /**
   * load the information from the database
   */
  public void load() throws SQLException
  {
    activity_ = new InteractiveActivity();
    activity_.load(connection_, getServletContext(), itemID_);
  }

  /**
   * parse the parameters from the url
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    itemID_=servletParams_.getInt("itemid", 0);
  }

  /**
   * number of activities for itemid
   */
  protected int numItems()
  {
    return activity_.numItems();
  }

  /**
   * activity name
   */
  protected String getActivityName(int index)
  {
    return activity_.activityName(index);
  }

  /**
   * activity description
   */
  protected String getActivityDescription(int index)
  {
    String desc = activity_.activityDescription(index);
    if(desc != null)
      return desc;
    return "";
  }

  /**
   * nonflash version link
   */
  protected String getActivityNonflashLink(int index, String message)
  {
    if(activity_.nonflashPath(index)!=null)
      return HtmlUtils.anchor(activity_.nonflashPath(index), message);
    return "";
  }

  /**
   * get filename
   */
  private String getFilename(int index)
  {
    return activity_.activityPath(index);
  }

  /**
   * height
   */
  private int getHeight(int index)
  {
    return activity_.height(index);
  }

  /**
   * width
   */
  private int getWidth(int index)
  {
    return activity_.width(index);
  }

  /**
   * get flash tag
   */
  protected String getFlashTag(int index)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"");
    buf.append(" codebase=\"http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0\"");
    buf.append(" ID=").append(getActivityName(index));
    buf.append(" WIDTH=").append(getWidth(index));
    buf.append(" HEIGHT=").append(getHeight(index)).append(">");
    buf.append("<PARAM NAME=movie VALUE=\"").append(getFilename(index)).append("\">");
    buf.append("<PARAM NAME=quality VALUE=high> <PARAM NAME=bgcolor VALUE=#FFFFFF>");
    buf.append("<EMBED src=\"").append(getFilename(index)).append("\"");
    buf.append(" quality=high bgcolor=#FFFFFF");
    buf.append(" WIDTH=").append(getWidth(index));
    buf.append(" HEIGHT=").append(getHeight(index));
    buf.append(" TYPE=\"application/x-shockwave-flash\"");
    buf.append(" PLUGINSPAGE=\"http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\"></EMBED></OBJECT>");
    return buf.toString();
  }
}
