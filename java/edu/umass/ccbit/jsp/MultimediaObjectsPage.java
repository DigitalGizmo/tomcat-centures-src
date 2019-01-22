
/**
 * Title:        MultimediaObjectsPage<p>
 * Description:  base class for page containing any kind of multimedia object associated with an item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.MultimediaObjects;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public abstract class MultimediaObjectsPage extends HttpDbJspBase
{
  // the multimedia object
  protected MultimediaObjects multi_;
  // the itemid
  protected int itemID_;
  protected int imageIndex_;
  protected int isExhibitItem_;
  protected boolean hiResolution_;
  
  /**
   * loads the multimedia object information for a particular item
   */
  public void load() throws SQLException
  {
    multi_ = new MultimediaObjects();
    multi_.load(connection_, getServletContext(), itemID_);
  }

  /**
   * the number of items in the multimedia object
   * @return the number of media items that are associated with a particular item
   */
  public int numItems()
  {
    return multi_.numItems();
  }

  public int mediaHeight(int i)
  {
    return multi_.mediaHeight(i);
  }
  
  public int mediaWidth(int i)
  {
    return multi_.mediaWidth(i);
  }
  
  public int mediaBorder(int i)
  {
    return multi_.mediaBorder(i);
  }
  
  public String mediaBGColor(int i)
  {
    return multi_.mediaBGColor(i);
  }
  
  public String mediaSRC(int i)
  {
    return multi_.mediaSRC(i, hiResolution_);
  }
  
  public String mediaPluginsPage(int i)
  {
    return multi_.mediaPluginsPage(i);
  }
  
  public String mediaText(int i)
  {
    return multi_.mediaText(i);
  }
  
  public boolean mediaController(int i)
  {
    return multi_.mediaController(i);
  }
  
  public boolean mediaCache(int i)
  {
    return multi_.mediaCache(i);
  }

  /**
   * returns true iff file extension matches
   */
  protected boolean hasFileExtension(int i, String ext)
  {
    return mediaSRC(i).regionMatches(true, mediaSRC(i).length()-ext.length(), ext, 0, ext.length());
  }

  /**
   * return clsid for object, based on file extension
   */
  protected String clsid(int i)
  {
    if (hasFileExtension(i, ".mov"))
      // from http://www.apple.com/quicktime/products/tutorials/activex.html
      return "clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B";
    else if (hasFileExtension(i, ".swf"))
      // from http://www.macromedia.com/support/director/how/shock/objembed.html
      return "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000";
    else
      return "";
  }

  /**
   * returns codebase based on file extension
   */
  protected String codebase(int i)
  {
    if (hasFileExtension(i, ".mov"))
      // from http://www.apple.com/quicktime/products/tutorials/activex.html
      return "http://www.apple.com/qtactivex/qtplugin.cab";
    else if (hasFileExtension(i, ".swf"))
      // from http://www.macromedia.com/support/director/how/shock/objembed.html
      return "http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0";
    else
      return "";
  }

  /**
   * returns mime type based on file extension
   */
  protected String mimeType(int index)
  {
    if (hasFileExtension(index, ".mov"))
      // from http://www.apple.com/quicktime/products/tutorials/activex.html
      return "video/quicktime";
    else if (hasFileExtension(index, ".swf"))
      // from http://www.macromedia.com/support/director/how/shock/objembed.html
      return "application/x-shockwave-flash";
    else
      return "";
  }

  /**
   * the media name to display on the media page
   * @param index the index of the item in the vector of multi objects for that item
   * @return a string containing the name of the media
   */
  protected String getMediaName(int index)
  {
    return multi_.getMediaName(index);
  }

  /**
   * the media description that is displayed in addition to the actual media
   * @param index the index of the item in the vector of multi objects for that item
   * @return a string containing the descriptions of the media
   */
  protected String getMediaDescription(int index)
  {
    return multi_.getMediaDescription(index);
  }

  /**
   * the media instructions which basically explain how the interface works and any peculiarities to the object
   * @param index the index of the item in the vector of multi objects for that item
   * @return a string containing the instructions of the media
   */
  protected String getMediaInstructions(int index)
  {
    return multi_.getMediaInstructions(index);
  }

  /**
   * the media alternative name
   * the use of this field is not certain, but would be relevant in some cases
   * @param index the index of the item in the vector of multi objects for that item
   * @return a string containing the alternative name of the media
   */
  protected String getMediaAltName(int index)
  {
    return multi_.getMediaAltText(index);
  }

  /**
   * item name
   */
  protected String getMediaItemName()
  {
    return multi_.mediaItemName();
  }

  /**
   * title link
   */
  protected String titleLink()
  {
    if (isExhibitItem_ != 0)
    {
      return HtmlUtils.anchor(ExhibitItemPage.link(itemID_, imageIndex_), getMediaItemName());
    }
    else
    {
      return HtmlUtils.anchor(ItemPage.link(itemID_, imageIndex_), getMediaItemName());
    }
  }

  /**
   * parse the parameters from the url
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    itemID_=servletParams_.getInt(ItemPage.ItemID_, 0);
    imageIndex_=servletParams_.getInt(ItemPage.ImageIndex_, 0);
    isExhibitItem_=servletParams_.getInt(ItemPage.Exhibit_, 0);
    hiResolution_ = (servletParams_.getInt("hi", 0) != 0);
  }
}
