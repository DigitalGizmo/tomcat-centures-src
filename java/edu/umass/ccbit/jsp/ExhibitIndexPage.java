
/**
 * Title:        ExhibitIndexPage<p>
 * Description:  base class for main page for the exhibit matrix<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import edu.umass.ccbit.database.ExhibitMatrixQuery;
import edu.umass.ccbit.database.ExhibitObject;
import edu.umass.ccbit.database.CollectionMatrixItem;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.util.Vector;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.io.IOException;

public abstract class ExhibitIndexPage extends HttpDbJspBase
{
  protected ExhibitMatrixQuery query_;
  public int xMax_;
  public int yMax_;
  public int exhibitID_;
  public static final String exhibitView_ = "Web_ExhibitMatrixView";
  public static final String XMax_="exhibit.max";
  public static final String YMax_="exhibit.ymax";
  public static final String ExhibitID_="exhibit.id";

  /**
   * load session information
   * @param config the servlet config
   */
  public void load(ServletConfig config, HttpSession session) throws SQLException
  {
    ServletContext context = config.getServletContext();
    query_ = new ExhibitMatrixQuery();
    query_.execute(connection_ , exhibitID_);
    JspSession.setInt(session,XMax_,xMax());
    JspSession.setInt(session,YMax_,yMax());
    JspSession.setInt(session,ExhibitID_,exhibitID_);
  }

  /**
   * checks for a null string...returns '' if null
   * @param string the string to check the nullity of
   */
  public String isNull(String string)
  {
    if(string == null)
      return "Text not available";
    return string;
  }

  /**
   * title of the exhibit
   */
  public String title()
  {
    return isNull(query_.exhibitName_);
  }

  /**
   * image tag for jsp page
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  public String imgTag(int x, int y, int a, int b, int c, int d)
  {
    return item(x,y).imgTag();
  }

  /**
   * get an ExhibitObject from the data structure containing the query result
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  private CollectionMatrixItem item(int x, int y)
  {
    return query_.item(x, y);
  }

  /**
   * the theme and era ids to pass as parameters
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  public String parameters(int x, int y)
  {
    return query_.item(x, y).parameters();
  }

  /**
   * the maximum xPos, the width of the table
   */
  public int xMax()
  {
    return query_.eras_.size();
  }

  /**
   * the maximum yPos, the depth of the table
   */
  public int yMax()
  {
    //return ((Vector) query_.eras_.elementAt(0)).size();
    int max = ((CollectionMatrixItem) ((Vector) query_.eras_.elementAt(0)).elementAt(0)).yMax_;
    return max;
  }

  /**
   * x axis theme name
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  public String xThemeName(int x, int y)
  {
    return isNull(((CollectionMatrixItem) ((Vector) query_.eras_.elementAt(x)).elementAt(y)).xThemeName_);
  }

  /**
   * x axis theme parameters to pass to intro.jsp
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  public int xThemeParameter(int x, int y)
  {
    return ((CollectionMatrixItem) ((Vector) query_.eras_.elementAt(x)).elementAt(y)).xThemeID_;
  }

  /**
   * y axis theme name
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  public String yThemeName(int x, int y)
  {
    return isNull(((CollectionMatrixItem) ((Vector) query_.eras_.elementAt(x)).elementAt(y)).yThemeName_);
  }

  /**
   * y axis theme parameter to pass to intro.jsp
   * @param x the x axis theme id
   * @param y the y axis theme id
   */
  public int yThemeParameter(int x, int y)
  {
    return ((CollectionMatrixItem) ((Vector) query_.eras_.elementAt(x)).elementAt(y)).yThemeID_;
  }

  /**
   * parse url parameters
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
      exhibitID_ = servletParams_.getInt("id", 1);
  }
}
