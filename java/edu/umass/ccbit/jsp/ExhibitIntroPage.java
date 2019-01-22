
/**
 * Title:        ExhibitIntroPage<p>
 * Description:  base class for intro.jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.runtime.*;

public abstract class ExhibitIntroPage extends HttpDbJspBase
{
  public int x_;
  public int y_;
  public int introImage_;
  public String exhibitView_ = "Web_ExhibitMatrixView";
  public String introText_;
  public String themeName_;

  /**
   * title of the page
   */
  public String title()
  {
    switch (x_)
    {
      case 0:
        return "Theme Intro";
      default:
        return "Era Intro";
    }
  }

  /**
   * load the information from the database
   */
  public void load() throws SQLException
  {
    Statement st = connection_.createStatement();
    ResultSet result = st.executeQuery(query());
    result.next();
    switch (x_)
    {
      case 0:
        introText_ = result.getString("YThemeIntroText");
        introImage_ = result.getInt("YThemeIntroImage");
        themeName_ = result.getString("YThemeName");
        break;
      default:
        introText_ = result.getString("XThemeIntroText");
        introImage_ = result.getInt("XThemeIntroImage");
        themeName_ = result.getString("XThemeName");
    }
    result.close();
    st.close();
  }

  /**
   * construct a query for a single item
   */
  public String query()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(exhibitView_);
    switch (x_)
    {
      case 0:
        buf.append(" WHERE YThemeID=").append(y_);
        break;
      default:
        buf.append(" WHERE XThemeID=").append(x_);
    }
    return buf.toString();
  }

  /**
   * the name of the particular theme
   */
  public String name()
  {
    return themeName_;
  }

  /**
   * the text given for the body of the intro page
   */
  public String introText()
  {
    return introText_;
  }

  /**
   * parse the parameters from the url
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    x_ = servletParams_.getInt("x");
    y_ = servletParams_.getInt("y");
  }
}
