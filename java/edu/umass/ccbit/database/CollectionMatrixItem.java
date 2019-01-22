
/**
 * Title:        CollectionExhibitItem<p>
 * Description:  base class for a matrix position<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.database.ExhibitObject;
//import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.lang.StringBuffer;

public class CollectionMatrixItem
{
  public int xPos_;
  public int yPos_;
  public int xMax_;
  public int yMax_;
  public int xThemeID_;
  public int yThemeID_;
  public int exhibitID_;
  public String xThemeURL_;
  public String yThemeURL_;
  public String xThemeName_;
  public String yThemeName_;
  public String imgPath_;

  /**
   * takes a row of the query and puts it into an object
   */
  public static CollectionMatrixItem new_ExhibitQuery(ResultSet result)
   throws SQLException
  {
    CollectionMatrixItem obj = new CollectionMatrixItem();
    obj.init_ExhibitQuery(result);
    return obj;
  }

  /**
   * initialize an exhibit query, overridden in derived class
   */
  public void init_ExhibitQuery(ResultSet result)
   throws SQLException
  {
    base_init(result);
  }

  /**
   * initialize the variables for an object
   */
  protected void base_init(ResultSet result) throws SQLException
  {
    xPos_=result.getInt(ExhibitMatrixQuery.fields.xPos_);
    yPos_=result.getInt(ExhibitMatrixQuery.fields.yPos_);
    xMax_=result.getInt(ExhibitMatrixQuery.fields.xMax_);
    yMax_=result.getInt(ExhibitMatrixQuery.fields.yMax_);
    exhibitID_=result.getInt(ExhibitMatrixQuery.fields.exhibitID_);
    xThemeID_=result.getInt(ExhibitMatrixQuery.fields.xThemeID_);
    yThemeID_=result.getInt(ExhibitMatrixQuery.fields.yThemeID_);
    //xThemeURL_=result.getString(ExhibitMatrixQuery.fields.xThemeURL_);
    //yThemeURL_=result.getString(ExhibitMatrixQuery.fields.yThemeURL_);
    xThemeName_=result.getString(ExhibitMatrixQuery.fields.xThemeName_);
    yThemeName_=result.getString(ExhibitMatrixQuery.fields.yThemeName_);
    /*
    try
    {
      String tempPath = result.getString(ExhibitMatrixQuery.fields.imgPath_);
      imgPath_= tempPath!=null ? tempPath.trim() : "(Image not available)";
    }
    catch(Exception e)
    {
      imgPath_="(Image not available)";
    }
    */
    imgPath_=result.getString(ExhibitMatrixQuery.fields.imgPath_);
  }

  /**
   * the tag for the image
   */
  public String imgTag()
  {
    if(imgPath_==null)
      return "(Image not available)";
    StringBuffer buf = new StringBuffer();
    buf.append("<img src=\"").append(imgPath_).append("\"");
    buf.append(" height=120 width=120>");
    return buf.toString();
  }

  /**
   * create the parameters for the URL to pass to theme.jsp
   */
  public String parameters()
  {
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink("x", xThemeID_, true, buf);
    HtmlUtils.addToLink("y", yThemeID_, false, buf);
    return buf.toString();
  }

  /**
   * description displayed on mouse rollover
   */
  protected String description()
  {
    StringBuffer buf = new StringBuffer();
    buf.append(xThemeName_).append(" ").append(yThemeName_);
    return buf.toString();
  }
}

