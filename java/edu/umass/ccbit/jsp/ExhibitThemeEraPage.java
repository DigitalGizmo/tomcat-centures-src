
/**
 * Title:        ExhibitThemeEraPage<p>
 * Description:  base class for theme jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import edu.umass.ccbit.database.ExhibitThemeQuery;
import edu.umass.ccbit.database.CollectionThemeItem;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.sql.Connection;
import java.io.IOException;
import java.util.Vector;
import java.sql.ResultSet;

public abstract class ExhibitThemeEraPage extends HttpDbJspBase
{
  public int xThemeID_;
  public int yThemeID_;
  public int xMax_;
  public int yMax_;
  public int exhibitID_;
  protected ExhibitThemeQuery query_;
  public String themeEraPage_;
  public static final String ThemeEraPage_="exhibit.themeerapage";
  protected Vector queries_;

  /**
   * load the information from the database
   * @param config the servlet config
   */
  public void load(ServletConfig config, HttpSession session) throws SQLException
  {
    ServletContext context = config.getServletContext();
    xMax_ = JspSession.getInt(session, ExhibitIndexPage.XMax_, 0);
    yMax_ = JspSession.getInt(session, ExhibitIndexPage.YMax_, 0);
    exhibitID_ = JspSession.getInt(session, ExhibitIndexPage.ExhibitID_, 1);

    queries_ = new Vector();
    ExhibitThemeQuery q;
    for (int nEra=1; nEra<=3; nEra++) {
	q = new ExhibitThemeQuery();
	q.execute( connection_, nEra, yThemeID_);
	queries_.add( q );
    }

    query_ = (ExhibitThemeQuery)queries_.get( xThemeID_-1 );
  }

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    themeEraPage_=initParams_.getString(ThemeEraPage_, "");
  }

  /**
   * get the exhibit id
   */
  public int getExhibitID()
  {
    return exhibitID_;
  }

  /**
   * parses parameters
   * @param request the servlet request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    xThemeID_=servletParams_.getInt("x");
    yThemeID_=servletParams_.getInt("y");
  }

  /**
   * get the max x
   */
  public int getXMax()
  {
    return xMax_;
  }

  /**
   * next turn
   */
  public String nextTurn(String message)
  {
    if(xThemeID_+1 > xMax_)
      return "";
    return turnThemeLink(1, message);
  }

  /**
   * previous turn
   */
  public String previousTurn(String message)
  {
    if(xThemeID_-1 <= 0)
      return "";
    return turnThemeLink(-1, message);
  }

  /**
   * link to the next era in the exhibit for this theme
   */
  private String turnThemeLink(int shift, String message)
  {
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink("x", xThemeID_+shift, true, buf);
    HtmlUtils.addToLink("y", yThemeID_, false, buf);
    return HtmlUtils.anchor(themeEraPage_+buf.toString(), message);
  }

  /**
   * checks for a null string...returns '' if null
   * @param string the string to check the nullity of
   */
  public String isNull(String string)
  {
    return isNull(string, "Text not available");
  }

  /**
   * checks for a null string...returns '' if null
   * @param string the string to check the nullity of
   * @param alt the alternatice string to display
   */
  public String isNull(String string, String alt)
  {
    if(string == null)
      return alt;
    return string;
  }

  /**
   * The theme's main image information
   * &quot;Name, Place&quot; by Creator, Year.<br>
   * Dimensions X&quot; x Y&quot;
   */
  public String themeImgInfo()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("&quot;").append(isNull(query_.themeItemName_)).append("&quot;");
    buf.append(", by ").append(isNull(query_.themeCreator_));
    buf.append(", c.").append(isNull(query_.themeCircaYear_, "Year not available")).append(".");
    return buf.toString();
  }

  /**
   * theme image tag for the image representing the whole theme
   */
  public String themeImgTag(int width, int height, String style)
  {
    if(query_.numItems()!=0)
      return query_.item(0).themeSpecialImageTag(width, height, style);
    return "";
  }

  /**
   * image tag for a subtheme representative image
   * @param i the index of the item in a vector
   */
  public String imgTag(int i, int height, int width)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<IMG SRC=\"").append(query_.item(i).imgPath_);
    buf.append("\" ALT=\"").append(query_.item(i).subthemeName_).append("\">");

    return buf.toString();
  }

  /**
   * number of items returned by the query
   */
  public int numItems()
  {
    return query_.numItems();
  }

  /**
   * title of the page
   * formatted per the mockup
   */
  public String title()
  {
    return theme()+" - "+era();
  }

  /**
   * the subtheme name for display on the jsp page
   * @param i the index of the item in the vector
   */
  public String subtheme(int i)
  {
    return isNull(query_.item(i).subthemeName_);
  }

  /**
   * get the alt name for a subtheme item
   */
  protected String getAltName(int i)
  {
    return isNull(query_.item(i).altName_);
  }

  /**
   * link to interactive activity if one exists
   * @param i the index of the item in the vector
   */
  public String activityLink(int i)
  {
    return query_.item(i).activityLink();
  }

  /**
   * the theme name
   */
  public String theme()
  {
    return isNull(query_.yThemeName_);
  }

  /**
   * returns the name of one of the three eras
   * @param xThemeID index of the era
   * */
  public String era( int xThemeID ) {
      ExhibitThemeQuery q = (ExhibitThemeQuery)queries_.get( xThemeID-1 );
      return (q.xThemeName_);
  }

  /**
   * the era name
   */
  public String era()
  {
    return isNull(query_.xThemeName_);
  }

  /**
   * label...is this ever used???
   * @param i the index of the item
   */
  public String label(int i)
  {
    return isNull(query_.item(i).label_);
  }

  /**
   * label title; the description of an item
   * in the context of a particular theme and era
   * @param i the index of the item
   */
  public String labelTitle(int i)
  {
    return isNull(query_.item(i).labelTitle_);
  }

  /**
   * item parameters
   * appends ?ItemID=_____&subtheme=_____ to the URL depending on the index of the item
   * @param i the index of the item that we are linking to
   */
  public String parameters(int i)
  {
    return query_.item(i).parameters();
  }

  /**
   * Returns parameter string "?x=___&y=___" for an era with the same theme.
   * @param xThemeID index of the era
   */
  public String jumpEraParams( int xThemeID ) {
    return ("?x=" + xThemeID + "&y=" + yThemeID());
  }

  /**
   * theme interpretation
   */
  public String themeInterpretation()
  {
    return isNull(query_.themeInterpretation_);
  }

  /**
   * the era id
   */
  public int xThemeID()
  {
    return xThemeID_;
  }

   /**
   * the theme id
   */
  public int yThemeID()
  {
    return yThemeID_;
  }

}
