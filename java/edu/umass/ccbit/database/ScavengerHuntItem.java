
/**
 * Title:        ScavengerHuntItem<p>
 * Description:  a hotspot of a scavenger hunt<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.jsp.HttpDbJspBase;
import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.image.MrSidImage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ScavengerHuntItem extends DbLoadableByIDItem
{
  public MrSidImage itemImage_;
  public String title_;
  public String description_;
  public String client_;
  public String filename_;
  public int scavengerHuntItemID_;
  public int scavengerHuntID_;
  public int imageX_;
  public int imageY_;
  public int imageHeight_;
  public int imageWidth_;
  public int imageLevel_;
  public int width_;
  public int height_;

  // NEW IMAGE MAP FIX
  public int imgX1_, imgX2_, imgY1_, imgY2_;

  /**
   * constructor
   */
  public ScavengerHuntItem()
  {
  }

  public String view()
  {
    return "Web_ScavengerHuntView";
  }

  /**
   * query to load a single item...this will be used by search pages to display the item
   * that the user is searching for...
   */
  protected String query(int scavengerHuntItemID)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("select * from ").append(view());
    buf.append(" where ScavengerHuntItemID=").append(scavengerHuntItemID);
    return buf.toString();
  }

  /**
   * return image to be used on search pages etc.
   */
  public String image()
  {
    return itemImage_.imageRef(imageLevel_, imageX_, imageY_, imageWidth_, imageHeight_);
  }

  public void init(ResultSet result) throws SQLException
  {
    title_ = result.getString("Title");
    description_ = result.getString("Description");
    scavengerHuntID_ = result.getInt("ScavengerHuntID");
    scavengerHuntItemID_ = result.getInt("ScavengerHuntItemID");
    imageX_ = result.getInt("ImageX");
    imageY_ = result.getInt("ImageY");
    imageHeight_ = result.getInt("ImageHeight");
    imageWidth_ = result.getInt("ImageWidth");
    imageLevel_ = result.getInt("ImageLevel");
    client_ = result.getString("Client");
    filename_ = result.getString("Filename");
    width_ = result.getInt("Width");
    height_ = result.getInt("Height");
    imgX1_ = result.getInt("ImgX1");
    imgY1_ = result.getInt("ImgY1");
    imgX2_ = result.getInt("ImgX2");
    imgY2_ = result.getInt("ImgY2");
  }

  /**
   * load method so that it can also load itself
   */
  public void load(Connection conn, int scavengerHuntItemID)
   throws SQLException
  {
    scavengerHuntItemID_=scavengerHuntItemID;
    load(conn, query(scavengerHuntItemID));
  }
}
