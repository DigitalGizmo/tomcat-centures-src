/*
 * Title:        MrSidImage<p>
 * Description:  class to encapsulate a mrsid image<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * Revision 1.14.2.3  2003/01/16 03:04:36  pb
 * added mrSidInfoRoot member so that image/info urls need not have same hostname
 *
 * Revision 1.14.2.2  2003/01/06 18:32:33  pbrown
 * fixed bug in levelFromSize, which allowed max zoom level + 1 to be returned
 *
 * Revision 1.14.2.1  2002/08/01 16:57:32  pbrown
 * made code changes for new lizardtech image server
 *
 * Revision 1.14  2002/04/12 14:07:46  pbrown
 * fixed copyright info
 *
 * Revision 1.13  2001/10/25 18:50:20  pbrown
 * changes sid root initialization
 *
 * Revision 1.12  2001/06/13 15:17:27  pbrown
 * changed levelFromSize so that we get next zoom level larger than desired size
 *
 * Revision 1.11  2001/05/24 14:04:55  pbrown
 * added mrsid root member to httpdbjspbase...no more init servlet
 *
 * Revision 1.10  2001/05/02 17:57:48  pbrown
 * added style attribute for sid images - for exhibit theme pages
 *
 * Revision 1.9  2000/11/21 21:22:18  pbrown
 * reimplementation of file info...removes mrsidarchive_ parameter from many
 * methods
 *
 * Revision 1.8  2000/06/25 20:36:17  tarmstro
 * added bounded image method
 *
 * Revision 1.7  2000/06/14 18:42:36  pbrown
 * added optional alt text args to image tag methods
 *
 * Revision 1.6  2000/06/02 21:52:54  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.5  2000/05/24 06:12:16  pbrown
 * implemented a class hierarchy for images
 *
 * Revision 1.4  2000/05/19 21:43:44  pbrown
 * split resize method into some component methods which can be called
 * independently
 *
 * Revision 1.3  2000/05/18 18:52:20  pbrown
 * added method to produce image tag to specified height/width
 *
 * Revision 1.2  2000/05/15 04:00:09  pbrown
 * added prototype of proposed method for generating images of specified size
 *
 * Revision 1.1  2000/05/13 03:55:45  pbrown
 * class to encapsulate mrsid images
 *
 *
 */
package edu.umass.ccbit.image;

import java.io.File;
//import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.image.MrSidInfo;
import java.lang.StringBuffer;
import edu.umass.ccbit.jsp.HttpDbJspBase;

import java.util.Hashtable;

public class MrSidImage
{
  protected String client_;
  protected String image_;
  protected static String mrSidRoot=null;
  protected static String mrSidInfoRoot=null;
  // this comes from reading info out of the file
  protected MrSidInfo imageInfo_;
  public static final String imageScript_="/lizardtech/iserv";

  /*
   * constructor
   * trailing path separators should not be given on directory paths
   * this constructor should be used when creating a mrsid image object
   * to be displayed...
   * @param client subdirectory of archive directory containing image..may be compound path
   * @param image filename of the image
   */
  public MrSidImage(String client, String image)
  {
    client_=client != null ? client.trim(): null;
    image_=image != null ? image.trim() : null;
    initImageInfo();
  }

  /**
   * constructor for derived classes
   */
  public MrSidImage()
  {
  }

  /**
   * set the mrsidroot member...static method to be called by servlet/jsp init
   */
  public static void setMrSidRoot(String root, String infoRoot)
  {
    // set it once
    if (mrSidRoot==null)
      mrSidRoot=root;
    if (mrSidInfoRoot==null)
      mrSidInfoRoot=infoRoot;
  }

  /**
   * inits image info...called by default from archive/client/image constructor
   */
  public void initImageInfo()
  {
    imageInfo_=new MrSidInfo(client_, image_);
  }

  /**
   * returns true iff image is valid..i.e., has non-null client, and image names
   */
  public boolean isValid()
  {
    return client_!=null && image_!=null && imageInfo_.isValid();
  }

  /**
   * img url, info or extract
   */
  public static String imageRef(String client, String image, boolean getInfo)
  {
    StringBuffer ref=new StringBuffer();
    ref.append(getInfo ? mrSidInfoRoot : mrSidRoot);
    ref.append(imageScript_);
    if (getInfo)
      ref.append("/calcrgn?style=infotext&");
    else
      ref.append("/getimage?");
    ref.append("cat=pvma&item=/").append(client);
    ref.append("/").append(image);
    return ref.toString();
  }

  /*
   * image url
   */
  public String imageRef(String client, String image, int level, int x, int y, int width, int height)
  {
		// TODO: fix the x,y thing
		// determine center point:
		StringBuffer buf=new StringBuffer(imageRef(client, image, false));
		double rx=(double) x / (double) width();
		double ry=(double) y / (double) height();
		double pwr=Math.pow(2, level);
		double sx=Math.min(1.0, (double) (x + pwr * width) / (double) (width()));
		double sy=Math.min(1.0, (double) (y + pwr * height) / (double) (height()));
		buf.append("&lev=").append(level);
		buf.append("&wid=").append(width);
		buf.append("&hei=").append(height);
		if (x != 0 && y != 0)
		{
  		buf.append("&cp=").append(rx).append(",").append(ry);
		}
		return buf.toString();
  }

  public static final int INSIDE_BOUNDING_BOX=0;
  public static final int SCALE_IMAGE=1;
  public static final int CLIP_IMAGE=2;

  /*
   * image ref
   */
  public String imageRef(int level, int x, int y, int width, int height)
  {
    return imageRef(client_, image_, level, x, y, width, height);
  }

  /**
   * returns the minimum of the ratios
   * width / actual width
   * height / actual height
   *
   * this is the ratio by which the image must be resized in order
   * to fit within the specified width x height
   */
  public double resizeRatio(int width, int height)
  {
    double w_ratio=(double) width/(double) width();
    double h_ratio=(double) height/(double) height();
    return Math.min(1.0, Math.min(w_ratio, h_ratio));
  }

  /**
   * returns level which gives 'closest' approximation to the specified size
   */
  public int levelFromSize(int width, int height)
  {
    int rsz_level=0;
    double ratio=resizeRatio(width, height);
    if (ratio < 1.0)
    {
      double log2 = Math.log(ratio) / Math.log(0.5);
      // pb - 06/13/01 - changed the following so that it truncates the log2 value
      // instead of rounding...that way, we get the next zoom level larger than the
      // desired size
      rsz_level = (int) Math.min(Math.floor(log2), levels()-1);
    }
    return rsz_level;
  }

  /**
   * creates img tag
   * @param level the zoom level
   * @param x x origin wrt upper left corner
   * @param y y origin wrt upper left corner
   * @param width width of the image
   * @param height height of the image
   * @param swidth scale width (used as WIDTH attribute in IMG tag..ignored if <= 0)
   * @param sheight scaled height (used as HEIGHT attribute in IMG tag..ignored if <=0)
   * @param style style attributes (none used if null)
   */
  public String imgTag(int level, int x, int y, int width, int height, int swidth, int sheight, String alt, String style)
  {
    StringBuffer tag=new StringBuffer();
    tag.append("<IMG SRC=\"");
    tag.append(imageRef(level, x, y, width, height));
    tag.append("\"");
    if (swidth > 0)
      tag.append(" WIDTH=").append(swidth);
    if (sheight > 0)
      tag.append(" HEIGHT=").append(sheight);
    tag.append(" BORDER=0");
    if (alt != null)
      tag.append(" ALT=\"").append(alt).append("\"");
    if (style != null)
      tag.append(" style=\"").append(style).append("\"");
    tag.append(">");
    return tag.toString();
  }

  /**
   * creates img tag
   * @param level the zoom level
   * @param x x origin wrt upper left corner
   * @param y y origin wrt upper left corner
   * @param width width of the image
   * @param height height of the image
   * @param swidth scale width (used as WIDTH attribute in IMG tag..ignored if <= 0)
   * @param sheight scaled height (used as HEIGHT attribute in IMG tag..ignored if <=0)
   */
  public String imgTag(int level, int x, int y, int width, int height, int swidth, int sheight, String alt)
  {
    return imgTag(level, x, y, width, height, swidth, sheight, alt, null);
  }

  /**
   * calculates scaled image tag/ref
   */
  protected String calcScaledImage(int width, int height, int sizeOpt, String alt, String style, boolean isTag)
  {
    if (isValid())
    {
      int rsz_level=levelFromSize(width, height);
      int pwr=(int) Math.pow(2, rsz_level);
      int rsz_width=width() / pwr;
      int rsz_height=height() / pwr;
      int scale_width=0;
      int scale_height=0;
      if (sizeOpt == SCALE_IMAGE)
      {
        double ratio=resizeRatio(width, height);
        scale_width=(int) (width() * ratio);
        scale_height=(int) (height() * ratio);
      }
      if (isTag)
        return imgTag(rsz_level, 0, 0, rsz_width, rsz_height, scale_width, scale_height, alt, style);
      else
        return imageRef(rsz_level, 0, 0, rsz_width, rsz_height);
    }
    else
      return "";
  }

  /**
   * returns a map of dynamically specified URL params and IMG tag attributes
   * needed by Struts' <html:img> custom tag
   */
  public Hashtable getImgTagParams( int width, int height ) {
    Hashtable imgTagParams = new Hashtable();
      
    int rsz_level    = levelFromSize( width, height );
    int pwr          = (int) Math.pow( 2, rsz_level );
    int rsz_width    = width()  / pwr;
    int rsz_height   = height() / pwr;
    double ratio     = resizeRatio( width, height );
    int scale_width  = (int) (width()  * ratio);
    int scale_height = (int) (height() * ratio);

    imgTagParams.put( "cat", "pvma" );
    imgTagParams.put( "item", '/' + client() + '/' + image() );
    imgTagParams.put( "wid",    String.valueOf( rsz_width ));
    imgTagParams.put( "hei",    String.valueOf( rsz_height ));
    imgTagParams.put( "lev",    String.valueOf( rsz_level  ));
    imgTagParams.put( "WIDTH",  String.valueOf( scale_width ));
    imgTagParams.put( "HEIGHT", String.valueOf( scale_height ));
    
    return (imgTagParams);
  }

  /**
   * returns image tag for mrsid image with zoom level selected to best
   * approximate desired height, width
   */
  public String scaledImageTag(int width, int height, int sizeOpt, String alt, String style)
  {
    return calcScaledImage(width, height, sizeOpt, alt, style, true);
  }

  /**
   * returns image url for mrsid image with zoom level selected to best
   * approximate desired height, width
   */
  public String scaledImageURL(int width, int height)
  {
    return calcScaledImage(width, height, SCALE_IMAGE, null, null, false);
  }

  /**
   * returns image tag for mrsid image with zoom level selected to best
   * approximate desired height, width
   */
  public String scaledImageTag(int width, int height, int sizeOpt, String alt)
  {
    return scaledImageTag(width, height, sizeOpt, alt, null);
  }
  
   /**
   * returns image tag for mrsid image with zoom level selected to best
   * approximate desired height, width
   */
  public String boundedImageTag(int width, int height, int swidth, int sheight, int sizeOpt, String alt)
  {
    if (isValid())
    {
      int rsz_level=levelFromSize(width, height);
      int pwr=(int) Math.pow(2, rsz_level);
      int rsz_width=width() / pwr;
      int rsz_height=height() / pwr;
      int scale_width=0;
      int scale_height=0;
      if (sizeOpt == INSIDE_BOUNDING_BOX)
      {
        scale_width=swidth;
        scale_height=sheight;
      }
      return imgTag(rsz_level, 0, 0, rsz_width, rsz_height, scale_width, scale_height, alt);
    }
    else
      return "";
  }

  /*
   * width
   */
  public int width()
  {
    return imageInfo_.width();
  }

  /*
   * height
   */
  public int height()
  {
    return imageInfo_.height();
  }

  /*
   * levels
   */
  public int levels()
  {
    return imageInfo_.levels();
  }

  /*
   * client
   */
  public String client()
  {
    return client_;
  }

  /*
   * image
   */
  public String image()
  {
    return image_;
  }
}
