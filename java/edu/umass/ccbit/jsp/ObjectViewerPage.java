/*
 * Title:        ObjectViewerPage<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author 
 * @version $Id: ObjectViewerPage.java,v 1.27 2003/12/03 21:01:14 keith Exp $
 *
 * $Log: ObjectViewerPage.java,v $
 * Revision 1.27  2003/12/03 21:01:14  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.26  2003/02/05 20:27:17  keith
 * many changes related to implementing subthemeid URL param
 *
 * Revision 1.25  2003/01/16 14:46:33  pbrown
 * merged in lizardtech changes
 *
 * Revision 1.24.2.4  2002/09/12 15:59:29  pbrown
 * fixed bug which caused image not to appear when called with accession number parameter
 *
 * Revision 1.24.2.3  2002/08/05 19:25:01  pbrown
 * fixed initial value of center point
 *
 * Revision 1.24.2.2  2002/08/05 18:28:03  pbrown
 * fixed problem with panning beyond edges of image
 *
 * Revision 1.24.2.1  2002/08/01 16:58:02  pbrown
 * made code changes for new lizardtech image server
 *
 * Revision 1.24  2002/04/12 14:08:05  pbrown
 * fixed copyright info
 *
 * Revision 1.23  2001/09/28 15:05:47  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.22  2001/06/11 15:01:11  pbrown
 * fixed navigation probs to/from itempage/viewer
 *
 * Revision 1.21  2001/05/31 15:43:14  pbrown
 * url encode/decode title so that quotes don't get munged
 *
 * Revision 1.20  2001/05/08 19:53:40  tarmstro
 * changes for referencing a specific page
 *
 * Revision 1.19  2001/04/24 19:48:28  tarmstro
 * reinstated return link
 *
 * Revision 1.18  2001/04/05 19:36:45  tarmstro
 * at request: removed linking to itempage/exhibit itempage
 *
 * Revision 1.17  2001/03/08 19:00:13  pbrown
 * add hidden data item for exhibit flag
 *
 * Revision 1.16  2001/03/08 18:33:53  pbrown
 * fixed look closer for exhibit
 *
 * Revision 1.15  2001/02/28 16:09:58  pbrown
 * fixes link for navigator view
 *
 * Revision 1.14  2000/12/19 16:37:05  pbrown
 * added title to form values
 *
 * Revision 1.13  2000/12/19 16:27:40  pbrown
 * made changes so that object viewer can be called w/accession num and optional
 * title
 *
 * Revision 1.12  2000/12/18 17:51:24  pbrown
 * changed links to my collection..base url must be specified
 *
 * Revision 1.11  2000/11/21 21:23:03  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.10  2000/10/25 20:04:10  pbrown
 * made default window (or display) size 400x400, removed text for window (display)
 * size dropdown
 *
 * Revision 1.9  2000/10/25 16:53:53  tarmstro
 * added functionality to add to mycollection from viewer pages
 *
 * Revision 1.8  2000/10/24 19:36:58  pbrown
 * some interface changes including radio buttons instead of dropdown for
 * zoom size
 *
 * Revision 1.7  2000/09/25 18:18:16  pbrown
 * changed call to image servlet
 *
 * Revision 1.6  2000/09/24 04:24:04  pbrown
 * calls servlet to put show rectangle in image navigation view...coordinates
 * of rectangle still not quite right
 *
 * Revision 1.5  2000/07/06 20:20:21  tarmstro
 * many changes for layout, display and functionality
 *
 * Revision 1.4  2000/06/27 16:14:40  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.3  2000/06/02 21:52:56  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.2  2000/05/25 22:25:09  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.1  2000/05/24 06:12:55  pbrown
 * first implementation of item pages, object viewer
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.database.DbLoadableItemImage;
import javax.servlet.jsp.JspWriter;
import java.util.Vector;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletRequest;
import edu.umass.ccbit.util.JspUtil;
import java.lang.Integer;
import java.io.File;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;
import edu.umass.ckc.util.Parameters;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.lang.StringBuffer;
import edu.umass.ccbit.database.CollectionItemInfo;
import edu.umass.ccbit.database.CollectionThemeItem;
import java.sql.SQLException;
import java.net.URLEncoder;
import java.net.URLDecoder;

public abstract class ObjectViewerPage extends HttpDbJspBase
{
  String client_;
  String image_;
  String imgsize_;
  String title_;
  MrSidImage mrSidImage_;
  String accessionNumber_;
  CollectionItemInfo itemInfo_;
  int itemID_, imageIndex_;
  int isExhibitItem_;
  int subthemeID_;
  int initLevel_, level_, width_, height_, maxwidth_, maxheight_, currWidth_, currHeight_;
  int fullHeight_, fullWidth_, levels_, x_, y_, zoom_, thLevel_;
  boolean scaleToFit_;
  boolean debug_;
  // size of thumbnail
  static final int thHeight_=150;
  static final int thWidth_=150;
  // parameters
  public static final String Client_="client";
  public static final String Image_="image";
  public static final String ItemID_="itemid";
  public static final String Title_="title";
  public static final String AccessionNumber_="accnum";
  public static final String IsExhibitItem_="exhibit";
  // for development only
  public static final String Debug_="debug";
  public static final String Jsp_="viewer.jsp";  

  // again, we need to know the name of the page that will derive from this class...
  // lousy design, but no way around it

  // initialize vector of sizes
  protected static final String[][] sizes_ =
  {
    {"", "full view"},
    {"256x256"},
    {"400x400"},
    {"600x400"},
  };

  protected static final String[][] zoomlevels_ =
  {
    {"0", ""}, // 100%
    {"1", ""}, // 50%
    {"2", ""}, // 25%
    {"3", ""}, // 12.5%
    {"4", ""}, // 6.25%
    {"5", ""}, // 3.125%
    {"6", ""}  // 1.5625%
  };

  protected static final String[][] zoomopts_ =
  {
    {"1", "Zoom back"},
    {"0", "No zoom"},
    {"-1", "Zoom in"}
  };

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
  }

  /**
   * construct a link to this page
   */
  public static String link(int itemID, String title, String client, String filename, int imageIndex)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(Jsp_);
    HtmlUtils.addToLink(ItemID_, itemID, true, buf);
    if (title != null)
    {
      HtmlUtils.addToLink(Title_, URLEncoder.encode(title), false, buf);
    }
    HtmlUtils.addToLink(Client_, client, false, buf);
    HtmlUtils.addToLink(Image_, filename, false, buf);
    HtmlUtils.addToLink(ItemPage.ImageIndex_, imageIndex, false, buf);
    return buf.toString();
  }

  /**
   * construct a link to this page
   */
  public static String link(int itemID, String client, String filename, int imageIndex)
  {
    return link(itemID, null, client, filename, imageIndex);
  }

  /**
   * collection link(adding item)
   */
  protected String collectionLink(String baseUrl, String linkText)
  {
    if(itemID_>0)
    {
      StringBuffer buf = new StringBuffer();
      buf.append(baseUrl);
      HtmlUtils.addToLink(ItemID_,itemID_,true,buf);
      //HtmlUtils.addToLink("method","add",false,buf);
      return HtmlUtils.anchor(buf.toString(), linkText);
    }
    return "";
  }

  /**
   * initialize height/width and get requested size or set a default
   */
  protected void initSizes(Parameters param)
  {
    // default size is 400x400!!!
    imgsize_=param.getString("imgsize", sizes_[2][0]);

    int ind=imgsize_.indexOf('x');
    if (ind > -1)
    {
      try
      {
        maxwidth_=Integer.parseInt(imgsize_.substring(0, ind));
        maxheight_=Integer.parseInt(imgsize_.substring(ind+1));
      }
      catch (Exception e)
      {
        currHeight_=currWidth_=maxwidth_=maxheight_=256;
      }
      height_=maxheight_;
      width_=maxwidth_;
    }
    else
    {
      scaleToFit_=true;
    }
  }

  /**
   * get servlet request parameters into ServletParams object
   * this calls parseRequestParameters in superclass, then sets data members associated with
   * servlet parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    x_=y_=0;
    scaleToFit_=false;
    super.parseRequestParameters(request);
    image_=servletParams_.getString(Image_, "");
    debug_=servletParams_.getBoolean(Debug_, false);
    client_=servletParams_.getString(Client_, "");
	mrSidImage_=new MrSidImage(client_, image_);
    try
    {
      title_=URLDecoder.decode(servletParams_.getString(Title_, ""));
    }
    catch (Exception e)
    {
      throw new CkcException(e.getMessage());
    }
    itemID_=servletParams_.getInt(ItemID_, 0);
    imageIndex_=servletParams_.getInt(ItemPage.ImageIndex_, 0);
    accessionNumber_=servletParams_.getString(AccessionNumber_, "");
    isExhibitItem_=servletParams_.getInt(IsExhibitItem_, 0);
    
    if (isExhibitItem_ != 0) subthemeID_ = servletParams_.getInt( CollectionThemeItem.SubthemeID_, 0 );
  }

  /**
   * handles form input events
   * determines which button was clicked and takes appropriate action
   */
  protected void handleFormInput(Parameters param)
  {
    if (param.containsKey("image.x") && param.containsKey("image.y"))
      // user clicked on the image
      onImageClick(param);
    else if (param.containsKey("navig.x") && param.containsKey("navig.y"))
      // image navigator
      onNavigatorClick(param);
    else if (param.containsKey("pan"))
      onPanButtonClick(param);
    else
      adjustHeightWidth();
  }

  /**
   * write dropdown control for selecting image size to output
   * @param out output object passed from the Java server page
   */
  protected void writeSizeDropdown(JspWriter out) throws IOException
  {
    JspUtil.writeDropdown("", sizes_, "imgsize", imgsize_, "", 1, out);
  }

  /**
   * write dropdown control for zoom level to output
   * @param out output object passed from the Java server page
   */
  protected void writeZoomDropdown(JspWriter out) throws IOException
  {
    JspUtil.writeDropdown("Percentage: <BR>", zoomlevels_,
                          "level", String.valueOf(level_),
                           "", 0, levels_ + 1, 1, out);
  }

  /**
   * write dropdown control for zoom level to output
   * @param out output object passed from the Java server page
   */
  protected void writeZoomLevelButtons(JspWriter out) throws IOException
  {
    JspUtil.writeRadioButtons(out, "", "level", zoomlevels_, level_, false);
  }

  /**
   * writes zoom control buttons to output
   */
  protected void writeZoomButtons(JspWriter out) throws IOException
  {
    out.print("Select a zoom option, then click on a point in the image:");
    out.print("<BR>\n");
    JspUtil.writeRadioButtons(out, null, "zoom", zoomopts_, zoom_, true);
  }

  /**
   * set default values
   */
  protected void getImageInfo()
  {
    mrSidImage_=new MrSidImage(client_, image_);
    if (mrSidImage_.isValid())
    {
      levels_=mrSidImage_.levels();
      // set initial size of 400 x 400 (image will be scaled to fit
      // within 400 x 400 bounding box
      initLevel_=mrSidImage_.levelFromSize(400, 400);
      fullHeight_=mrSidImage_.height();
      fullWidth_=mrSidImage_.width();
      // get info for thumbnail view
      thLevel_=mrSidImage_.levelFromSize(thWidth_, thHeight_);
    }
  }

  /**
   * initializes image info
   */
  protected void initImageInfo(Parameters param) {
  	
    if (param.containsKey("levels") &&
        param.containsKey("fullheight") && param.containsKey("fullwidth")) {
      levels_=param.getInt("levels", 0);
      fullHeight_=param.getInt("fullheight", 0);
      fullWidth_=param.getInt("fullwidth", 0);
      currHeight_=param.getInt("currheight", 0);
      currWidth_=param.getInt("currwidth", 0);
      thLevel_=param.getInt("thlevel", 0);
      
    } else {
      getImageInfo();
    }
    
    x_=param.getInt("x", fullWidth_/2);
    y_=param.getInt("y", fullHeight_/2);
    level_= param.getInt("level", initLevel_);
  }

  /**
   * print debug info
   */
  protected String debugInfo()
  {
    if (debug_)
    {
      StringBuffer buf=new StringBuffer();
      buf.append("<BR><B>Accession number: </B> #").append(accessionNumber_);
      buf.append("<BR><B>Filename: </B>").append(client_).append("/").append(image_);
      return buf.toString();
    }
    else
      return "";
  }

  /**
   * adjusts height width based on zoom level
   */
  protected void adjustHeightWidth()
  {
    int pwr = (int) Math.pow(2, level_);
    int scaledheight = fullHeight_ / pwr;
    int scaledwidth = fullWidth_ / pwr;
    if (scaleToFit_)
    {
      height_=scaledheight;
      width_=scaledwidth;
			x_=y_=0;
    }
    else
    {
			width_=Math.min(maxwidth_, scaledwidth);
			height_=Math.min(maxheight_, scaledheight);
			// compute bounds for x,y so that we won't pan past
			// the boundaries of the image
			int xmin=(width_/2)*pwr;
			int xmax=fullWidth_-xmin;
			int ymin=(height_/2)*pwr;
			int ymax=fullHeight_-ymin;
			x_=Math.min(Math.max(x_, xmin), xmax);
			y_=Math.min(Math.max(y_, ymin), ymax);
    }
  }

  /**
   * apply zoom selection to level
   */
  protected void applyZoom(Parameters param)
  {
    zoom_=param.getInt("zoom", 0);
    if (level_ + zoom_ >= 0 && level_ + zoom_ <= levels_)
      level_ += zoom_;
    if (level_==0 || level_==levels_)
      zoom_=0;
  }

  /**
   * handles click on image
   */
  protected void onImageClick(Parameters param)
  {
    int imagex=param.getInt("image.x", 0);
    int imagey=param.getInt("image.y", 0);
    int pwr=(int) Math.pow(2, level_);
    x_ += ((imagex - currWidth_ / 2) * pwr);
    y_ += ((imagey - currHeight_ / 2) * pwr);
    applyZoom(param);
    adjustHeightWidth();
  }

  /**
   * handles click on navigator
   */
  protected void onNavigatorClick(Parameters param)
  {
    int imagex=param.getInt("navig.x", 0);
    int imagey=param.getInt("navig.y", 0);
    int pwr=(int) Math.pow(2, thLevel_);
    x_=imagex * pwr;
    y_=imagey * pwr;
    adjustHeightWidth();
  }

  /**
   * handles pan buttons
   */
  protected void onPanButtonClick(Parameters param)
  {
    int pwr= (int) Math.pow(2, level_);
    String pan=param.getString("pan", "");
    if (pan.indexOf("N") > -1)
      y_ -= currHeight_*pwr;
    if (pan.indexOf("E") > -1)
      x_ += currWidth_*pwr;
    if (pan.indexOf("S") > -1)
      y_ += currHeight_*pwr;
    if (pan.indexOf("W") > -1)
      x_ -= currWidth_*pwr;
    adjustHeightWidth();
  }

  /**
   * url for image
   */
  protected String imageRef(int level, int x, int y, int width, int height)
  {
    return mrSidImage_.imageRef(level, x, y, width, height);
  }

  /**
   * url for image
   */
  protected String imageRef()
  {
    return imageRef(level_, x_, y_, width_, height_);
  }

  /**
   * image tag
   */
  protected void writeImageTag(JspWriter out) throws IOException
  {
    out.print("<INPUT type=image height=" + String.valueOf(height_)
              + " width=" + String.valueOf(width_)
              + " name=image src=\"" + imageRef() + "\">\n");
    out.print(debugInfo());
  }

  /**
   * writes a hidden form value
   */
  protected void writeFormValue(String name, String value, JspWriter out) throws IOException
  {
    out.print("  <INPUT type=hidden name=" + name + " value=\"" + value + "\">\n");
  }

  /**
   * writes a hidden form value
   */
  protected void writeFormValue(String name, int value, JspWriter out) throws IOException
  {
    writeFormValue(name, String.valueOf(value), out);
  }

  /**
   * writes a hidden form value
   */
  protected void writeFormValue(String name, double value, JspWriter out) throws IOException
  {
    writeFormValue(name, String.valueOf(value), out);
  }

  /**
   * writes form values to output
   * @param out output object passed from the Java server page
   */
  protected void writeFormValues(JspWriter out) throws IOException
  {
    writeFormValue(Client_, client_, out);
    writeFormValue(Image_,  image_, out);
    writeFormValue(Title_,  URLEncoder.encode(title_), out);
    writeFormValue(ItemID_, itemID_, out);
    if (isExhibitItem_ != 0)
    {
      writeFormValue(IsExhibitItem_, isExhibitItem_, out);
    }
    writeFormValue("levels", levels_, out);
    writeFormValue("x", x_, out);
    writeFormValue("y", y_, out);
    writeFormValue("fullheight", fullHeight_, out);
    writeFormValue("fullwidth", fullWidth_, out);
    writeFormValue("currheight", height_, out);
    writeFormValue("currwidth", width_, out);
    writeFormValue("level", level_, out);
    writeFormValue("thlevel", thLevel_, out);
  }

  /**
   * creates thumbnail image navigator
   */
  protected void writeImageNavigator(JspWriter out) throws IOException
  {
    int pwr=(int) Math.pow(2, thLevel_);
    double rszpwr=Math.pow(2, level_-thLevel_);
    int scaledheight = fullHeight_ / pwr;
    int scaledwidth = fullWidth_ /pwr;
    int x=scaledwidth / 2;
    int y=scaledheight / 2;

    int rx=(int) Math.max(0, x_ / pwr - width_ * rszpwr / 2);
    int ry=(int) Math.max(0, y_ / pwr - height_ * rszpwr / 2);
    // horizontal, vertical dimensions of rectangle (subtract 2 so
    // it won't disappear when full image
    int rh= (int) (width_ * rszpwr) - 2;
    int rv=(int) (height_ * rszpwr) -2;

    String mrSidImageSrc=mrSidImage_.imageRef(thLevel_, 0, 0, scaledwidth, scaledheight);
    String imgsrc=URLEncoder.encode(mrSidImageSrc);

    out.print("<INPUT type=image "
              + " name=navig src=\"/servlet/ImageServlet?"
              + "imgsrc=" + imgsrc
              + "&rx=" + rx
              + "&ry=" + ry
              + "&rh=" + rh
              + "&rv=" + rv
              + "\">\n");
  }

  /**
   * writes image info to output
   */
  protected void writeImageInfo(JspWriter out) throws IOException
  {
    out.print("current size: ");
    out.print(String.valueOf(width_) + " x " + String.valueOf(height_));
    out.print(" &#149; ");
    out.print("largest size: ");
    out.print(String.valueOf(fullWidth_) + " x " + String.valueOf(fullHeight_));
  }

  /**
   * start form
   */
  protected void startForm(HttpServletRequest request, JspWriter out) throws IOException
  {
    out.print("<FORM METHOD=POST ACTION=\"" + request.getRequestURI() + "\">\n");
  }

  /**
   * end form
   */
  protected void endForm(JspWriter out) throws IOException
  {
    writeFormValues(out);
    out.print("</FORM>\n");
  }

  /**
   * load
   */
  protected void load() throws SQLException
  {
    if (itemID_ != 0)
    {
      itemInfo_=new CollectionItemInfo();
      itemInfo_.load(connection_, itemID_);
    }
    if (image_.length()==0 && accessionNumber_.length()!=0)
    {
      // load image info from database by accession number
      DbLoadableItemImage img=new DbLoadableItemImage();
      img.loadByAccessionNumber(connection_, accessionNumber_);
      client_=img.client(imageIndex_);
      image_=img.image(imageIndex_);
    }
    initSizes(servletParams_);
    initImageInfo(servletParams_);
    adjustHeightWidth();
    handleFormInput(servletParams_);
  }

  /**
   * title link
   */
  protected String titleLink(String baseURL)
  {
    if (title_.length()!=0)
      return title_;
    else if (itemID_!=0 && itemInfo_!=null)
      return HtmlUtils.anchor(ItemPage.link(baseURL, itemID_, imageIndex_), itemInfo_.itemName());
    else
      return "";
  }

  /**
   * title link
   */
  protected String titleLink()
  {
    if (isExhibitItem_ != 0)
    {
      return HtmlUtils.anchor(ExhibitItemPage.link(itemID_, imageIndex_, subthemeID_), title_);
    }
    else if (title_.length()!=0)
      return title_;
    else if (itemID_!=0 && itemInfo_!=null)
      return HtmlUtils.anchor(ItemPage.link(itemID_, imageIndex_), itemInfo_.itemName());
    else
      return "";
  }
}
