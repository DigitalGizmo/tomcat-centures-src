
/**
 * Title:        ScavengerHunt<p>
 * Description:  class for a scavenger hunt<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ScavengerHuntSessionItems;
import edu.umass.ccbit.database.ScavengerHuntItem;
import edu.umass.ccbit.image.MrSidImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public class ScavengerHunt extends DbLoadableItemList
{
  public static String ScavengerHuntItemID_="scav_item";
  public static String ScavengerHuntID_="scav_id";
  public static String scavengerImage_;
  public ScavengerHuntSessionItems sessionItems_;
  public ScavengerHuntItem scavengerHuntItem_;
  public int numItems_;

  /**
   * constructor
   */
  public ScavengerHunt()
  {
    sessionItems_ = new ScavengerHuntSessionItems();
  }

  /**
   * checks to see if this is a valid hunt
   */
  public boolean isValid()
  {
    try{ String temp=scavengerHuntItem_.title_; }
    catch(Exception e){ return false; }
    return true;
  }

  /**
   * the view from the database to retrive a result set from
   */
  protected static String view()
  {
    return "Web_ScavengerHuntView";
  }

  /**
   * construct a query
   */
  protected String query(int scavengerHuntID)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("select * from ").append(view());
    buf.append(" where ScavengerHuntID=").append(scavengerHuntID);
    return buf.toString();
  }

  protected ScavengerHuntItem InstanceFromResult(ResultSet result) throws SQLException
  {
    scavengerHuntItem_=new ScavengerHuntItem();
    scavengerHuntItem_.init(result);
    return scavengerHuntItem_;
  }

  /**
   * creates a new instance of a scavengerhunt item
   * @param result the result set generated from a query
   */
  public InstanceFromResult newInstance(ResultSet result)
   throws SQLException
  {
    return InstanceFromResult(result);
  }

  /**
   * get a list of items on image and form an image map out of it
   * we consider the origin to be the upper left hand corner of
   * the image.
   *
   * currently this only uses rectangles...we could add a field to the database
   * for each hunt item denoting a shape to use in the image map
   * seeing the mockup for the tea party, it might be useful for non-text
   *
   * @param searchPage the name of the search jsp page
   */
  public String imageMap(String searchPage)
  {
    Enumeration e = items_.elements();
    ScavengerHuntItem huntItem;
    StringBuffer buf = new StringBuffer();
    buf.append("<map name=\"hunt_map\">\n");
    numItems_=0;
    while(e.hasMoreElements())
    {
      huntItem = (ScavengerHuntItem) e.nextElement();
      buf.append("<area shape=\"rect\" coords=");
      buf.append("\"").append(coords(huntItem)).append("\"");
      buf.append(" href=\"").append(searchLink(numItems_, searchPage)).append("\">\n");
      numItems_++;
    }
    buf.append("</map>");
    return buf.toString();
  }

  /**
   * create a string to denote the upper left and lower right coords of a subimage
   * comma delimited
   * @param huntItem the particular section of an image to be used as a hotspot
   */
  private String coords(ScavengerHuntItem huntItem)
  {
    StringBuffer buf = new StringBuffer();
    buf.append(huntItem.imgX1_).append(",");
    buf.append(huntItem.imgY1_).append(",");
    buf.append(huntItem.imgX2_).append(",");
    buf.append(huntItem.imgY2_);
    return buf.toString();
  }

  /**
   * the link text to go to a search page for a particular listing on a page
   */
  protected String searchLink(int index, String searchPage)
  {
    StringBuffer buf = new StringBuffer();
    buf.append(searchPage).append("?scav_item=");
    buf.append(getScavengerHuntItem(index).scavengerHuntItemID_);
    return buf.toString();
  }

  /**
   * the image tag for use on its hunt home
   */
  public String huntImgTag(HttpSession session)
  {
    MrSidImage img=new MrSidImage(getDefaultHuntItem().client_, getDefaultHuntItem().filename_);
    return img.imgTag(getDefaultHuntItem().imageLevel_, 0, 0, getDefaultHuntItem().width_, getDefaultHuntItem().height_,
                      -1, -1, title());
  }

  /**
   * the title of the hunt
   */
  public String title()
  {
    return scavengerHuntItem_.title_;
  }

  /**
   * the description of the hunt
   */
  public String description()
  {
    return scavengerHuntItem_.description_;
  }

  /**
   * get a particular hunt item
   * @param index the index of the hunt item in the vector
   */
  protected ScavengerHuntItem getScavengerHuntItem(int index)
  {
    return (ScavengerHuntItem) get(index);
  }

  /**
   * hunt information from any row of the result set
   */
  protected ScavengerHuntItem getDefaultHuntItem()
  {
    return getScavengerHuntItem(0);
  }

  /**
   * return image tag for image at index, or blank box if no image exists
   * @param index the index of the image for use at the bottom of the page
   */
  protected String itemImage(HttpSession session, int index)
  {
    return sessionItems_.itemImage(session, index).toString();
  }

  public void load(Connection conn, HttpSession session, int scavengerHuntID)
   throws SQLException, NullPointerException
  {
    load(conn, query(scavengerHuntID));
    session.setAttribute("scav_id", new Integer(scavengerHuntID));
    try
    {
      session.setAttribute("Client", getScavengerHuntItem(0).client_.trim());
      session.setAttribute("Filename", getScavengerHuntItem(0).filename_.trim());
    }
    catch(Exception e){}
  }
}
