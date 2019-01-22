
/**
 * Title:        CollectionThemeItem<p>
 * Description:  base class for a single item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.database.ExhibitObject;
import edu.umass.ccbit.database.ExhibitThemeQuery;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.sql.ResultSet;

public class CollectionThemeItem
{
  public int itemID_;
  public int exhibitID_;
  public int xThemeID_;
  public int yThemeID_;
  public int subthemeID_;
  public String fileName_;
  public String client_;
  public String labelTitle_;
  public String label_;
  public String intLabel_;
  public String begLabel_;
  public String subthemeName_;
  public String xThemeName_;
  public String yThemeName_;
  public String itemName_;
  public String objectDate_;
  public String description_;
  public String accessionNumber_;
  public String creator_;
  public String themeClient_;
  public String themeFilename_;
  public String material_;
  public String objectType_;
  public String lastName_;
  public String imgPath_;
  public String altName_;
  public boolean activity_;

  public static final String SubthemeID_="subthemeid";

  /**
   * I'm not sure how I am going to use this or
   * how they indend it to be used
   */
  public boolean primaryItem_;

  /**
   * initialize the variables for the theme/era page
   * @param result the result set generated from the query
   */
  protected void base_init(ResultSet result) throws SQLException
  {
    itemID_=result.getInt(ExhibitThemeQuery.fields.itemID_);
    exhibitID_=result.getInt(ExhibitThemeQuery.fields.exhibitID_);
    xThemeID_=result.getInt(ExhibitThemeQuery.fields.xThemeID_);
    yThemeID_=result.getInt(ExhibitThemeQuery.fields.yThemeID_);
    subthemeID_=result.getInt(ExhibitThemeQuery.fields.subthemeID_);
    fileName_=(result.getString(ExhibitThemeQuery.fields.fileName_)).trim();
    client_=(result.getString(ExhibitThemeQuery.fields.client_)).trim();
    labelTitle_=result.getString(ExhibitThemeQuery.fields.labelTitle_);
    label_=result.getString(ExhibitThemeQuery.fields.label_);
    intLabel_=result.getString(ExhibitThemeQuery.fields.intLabel_);
    begLabel_=result.getString(ExhibitThemeQuery.fields.begLabel_);
    subthemeName_=result.getString(ExhibitThemeQuery.fields.subthemeName_);
    xThemeName_=result.getString(ExhibitThemeQuery.fields.xThemeName_);
    yThemeName_=result.getString(ExhibitThemeQuery.fields.yThemeName_);
    itemName_=result.getString(ExhibitThemeQuery.fields.itemName_);
    primaryItem_=result.getBoolean(ExhibitThemeQuery.fields.primaryItem_);
    objectDate_=result.getString(ExhibitThemeQuery.fields.objectDate_);
    description_=result.getString(ExhibitThemeQuery.fields.description_);
    accessionNumber_=result.getString(ExhibitThemeQuery.fields.accession_);
    material_=result.getString(ExhibitThemeQuery.fields.material_);
    themeFilename_=result.getString(ExhibitThemeQuery.fields.themeFilename_);
    themeClient_=result.getString(ExhibitThemeQuery.fields.themeClient_);
    objectType_=result.getString(ExhibitThemeQuery.fields.objectType_);
    lastName_=result.getString(ExhibitThemeQuery.fields.lastName_);
    imgPath_=result.getString(ExhibitThemeQuery.fields.imgPath_);
    activity_=result.getBoolean(ExhibitThemeQuery.fields.activity_);
    altName_=result.getString(ExhibitThemeQuery.fields.altName_);
  }

  /**
   * takes a row of the query and puts it into an object
   * @param result the result set generated from the query
   */
  public static CollectionThemeItem new_ExhibitQuery(ResultSet result)
   throws SQLException
  {
    // created a new object and initialized the
    // variables based on the ResultSet
    CollectionThemeItem obj = new CollectionThemeItem();
    obj.init_ExhibitQuery(result);
    return obj;
  }

  /**
   * initialize an exhibit query, overridden in derived class
   * @param result the result set generated from the query
   */
  public void init_ExhibitQuery(ResultSet result)
   throws SQLException
  {
    base_init(result);
  }

  /**
   * create the parameters for the item URL
   */
  public String parameters()
  {
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink("itemid", itemID_, true, buf);
    // need to select the proper image to display from the themeera page...
//    HtmlUtils.addToLink("img", SOMETHING, false, buf);
    // This feature was once abandoned
    HtmlUtils.addToLink(SubthemeID_, subthemeID_, false, buf);
    return buf.toString();
  }

  /**
   * create the parameters for the image URL
   */
  public String imageParameters()
  {
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink("client", client_.trim(), true, buf);
    HtmlUtils.addToLink("filename", fileName_.trim(), false, buf);
    return buf.toString();
  }

  /**
   * create the parameters for the viewer.jsp link
   */
  public String viewParameters()
  {
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink("itemid", itemID_, true, buf);
    HtmlUtils.addToLink("client", client_, false, buf);
    HtmlUtils.addToLink("image", fileName_, false, buf);
    return buf.toString();
  }

  /**
   * description displayed on mouse rollover
   */
  protected String description()
  {
    return labelTitle_;
  }

  /**
   * link to interactive activity for item(if it exists)
   */
  public String activityLink()
  {
    if(!activity_)
      return "";
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink("itemid", itemID_, true, buf);
    // displayed string
    String warning="<font size=\"2\"> (Requires Flash plug-in.  If you do not have the plugin you can get it from <a href=\"http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\">Macromedia</a>)</font>";
    return HtmlUtils.anchor("activity.jsp"+buf.toString(), "Interactive Activity")+warning;
  }

  /**
   * special image tag
   */
  public String scaledSpecialImageTag(int width, int height)
  {
    return specialImage().scaledImageTag(width, height, MrSidImage.SCALE_IMAGE, subthemeName_);
  }

  /**
   * special image tag
   */
  public String boundedSpecialImageTag(int width, int height)
  {
    return specialImage().scaledImageTag(width, height, MrSidImage.INSIDE_BOUNDING_BOX, subthemeName_);
  }

  /**
   * special image tag for theme image
   */
  public String themeSpecialImageTag(int width, int height, String style)
  {
    return themeSpecialImage().scaledImageTag(width, height, MrSidImage.INSIDE_BOUNDING_BOX, (xThemeName_+yThemeName_), style);
  }

  /**
   * special theme image
   */
  protected MrSidImage themeSpecialImage()
  {
    return new MrSidImage(themeClient_, themeFilename_);
  }

  /**
   * special image
   */
  protected MrSidImage specialImage()
  {
    return new MrSidImage(client_, fileName_);
  }
}
