/*
 * Title:        ScavengerHuntInfo<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version $Id: ScavengerHuntInfo.java,v 1.7 2002/04/12 14:07:39 pbrown Exp $
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.image.MrSidInfo;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ScavengerHuntInfo implements InstanceFromResult
{
  public MrSidImage itemImage_;
  public String title_;
  public String description_;
  public String client_;
  public String filename_;
  public int imageLevel_;
  public int scavengerHuntID_;

  public ScavengerHuntInfo()
  {
  }

  public String thumbnail(int width, int height)
  {
    // TODO: return image scaled to given width, height for thumbnail on main page
    return itemImage_.scaledImageTag(width, height, MrSidImage.SCALE_IMAGE, title_);
  }

  public void init(ResultSet result)
   throws SQLException
  {
    title_=result.getString("Title");
    description_=result.getString("Description");
    imageLevel_=result.getInt("ImageLevel");
    scavengerHuntID_=result.getInt("ScavengerHuntID");
    client_=result.getString("Client");
    filename_=result.getString("Filename");
    itemImage_ = new MrSidImage(client_, filename_);
  }
}
