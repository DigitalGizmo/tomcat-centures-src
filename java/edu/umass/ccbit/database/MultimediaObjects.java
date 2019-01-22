
/**
 * Title:        MultimediaObject<p>
 * Description:  a proprietary formatted multimedia object that is associated with an item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.ServletContext;

public class MultimediaObjects extends DbLoadableItemList
{
  public static final String MultimediaView_ = "MultimediaObjects";

  protected class MultimediaObject implements InstanceFromResult
  {
    // fields
    public String mediaName_;
    public String mediaInstructions_;
    public String mediaDescription_;
    public String mediaAltText_;
    public String mediaItemName_;

    // embed parameters
    public int mediaHeight_;
    public int mediaWidth_;
    public int mediaBorder_;
    public String mediaBGColor_;
    public String mediaSRC_;
    public String mediaPluginsPage_;
    public String mediaText_;
    public String mediaSRCHi_;
    public boolean mediaController_;
    public boolean mediaCache_;

    /**
     * media src, hi/low resolution
     */
    public String mediaSRC(boolean hiResolution)
    {
      if (hiResolution && mediaSRCHi_ != null)
      {
        return mediaSRCHi_;
      }
      else
      {
        return mediaSRC_;
      }
    }

    /*
     * media text
     */
    public String mediaText()
    {
      return mediaText_ != null ? mediaText_ : "";
    }

    /**
     * embed tag for particular multimedia object
     * handles nullity of strings in the tag and will omit undefined portions
     * @return the embed tag
     */
    public String mediaEmbedTag(boolean hiResolution)
    {
    /*
     typical embed tag:
      <EMBED
        BGCOLOR="#ffffff"
        SRC="http://a192.g.akamai.net/7/192/51/522e216f43f2c1/www.apple.com/quicktime/preview/gallery/media/paris_vegas.mov"
        HEIGHT="336"
        WIDTH="480"
        CONTROLLER="true"
        BORDER="0"
        PLUGINSPAGE="http://www.apple.com/quicktime/download/"
        cache="true">
    */
      StringBuffer buf = new StringBuffer();
      buf.append("<EMBED ");
      if(mediaBGColor_!=null)
        buf.append(" BGCOLOR=\"").append(mediaBGColor_).append("\" ");
      if(mediaSRC(hiResolution) !=null)
        buf.append(" SRC=\"").append(mediaSRC(hiResolution)).append("\" ");
      buf.append(" HEIGHT=\"").append(mediaHeight_).append("\" ");
      buf.append(" WIDTH=\"").append(mediaWidth_).append("\" ");
      buf.append(" CONTROLLER=\"").append(mediaController_).append("\" ");
      buf.append(" BORDER=\"").append(mediaBorder_).append("\" ");
      if(mediaPluginsPage_!=null)
        buf.append(" PLUGINSPAGE=\"").append(mediaPluginsPage_).append("\" ");
      buf.append(" CACHE=\"").append(mediaCache_).append("\" ");
      buf.append(">");
      return buf.toString();
    }

    /**
     * initialize fields for each multimedia object returned
     * @param result the resultset of tuples that are relevant for this item
     */
    public void init(ResultSet result) throws SQLException
    {
      mediaName_ = result.getString("Name");
      mediaInstructions_ = result.getString("Instructions");
      mediaDescription_ = result.getString("Description");
      mediaAltText_ = result.getString("AltText");
      mediaHeight_ = result.getInt("Height");
      mediaWidth_ = result.getInt("Width");
      mediaBorder_ = result.getInt("Border");
      mediaBGColor_ = result.getString("BGColor");
      mediaSRC_ = result.getString("MediaPath");
      mediaPluginsPage_ = result.getString("PluginPath");
      mediaText_ = result.getString("MediaText");
      mediaSRCHi_ = result.getString("MediaPathHi");
      mediaController_ = result.getBoolean("Controller");
      mediaCache_ = result.getBoolean("Cache");
      mediaItemName_=result.getString("ItemName");
    }
  }

  /**
   * get media object
   * @param i the index of the multimedia object in the vector or objects
   */
  private MultimediaObject getMedia(int i)
  {
    return (MultimediaObject) get(i);
  }

  public int mediaHeight(int i)
  {
    return getMedia(i).mediaHeight_;
  }
  
  public int mediaWidth(int i)
  {
    return getMedia(i).mediaWidth_;
  }
  
  public int mediaBorder(int i)
  {
    return getMedia(i).mediaBorder_;
  }
  
  public String mediaBGColor(int i)
  {
    return getMedia(i).mediaBGColor_;
  }
  
  public String mediaSRC(int i)
  {
    return getMedia(i).mediaSRC_;
  }
  
  public String mediaSRC(int i, boolean hiResolution)
  {
    return getMedia(i).mediaSRC(hiResolution);
  }
  
  public String mediaPluginsPage(int i)
  {
    return getMedia(i).mediaPluginsPage_;
  }
  
  public String mediaText(int i)
  {
    return getMedia(i).mediaText();
  }
  
  public boolean mediaController(int i)
  {
    return getMedia(i).mediaController_;
  }
  
  public boolean mediaCache(int i)
  {
    return getMedia(i).mediaCache_;
  }
        
  /**
   * the name of the media object
   * @param i the index of the multimedia object in the vector or objects
   */
  public String getMediaName(int i)
  {
    return getMedia(i).mediaName_;
  }

  /**
   * the instructions on how to use this media object
   * @param i the index of the multimedia object in the vector or objects
   */
  public String getMediaInstructions(int i)
  {
    return getMedia(i).mediaInstructions_;
  }

  /**
   * a description of the media in the event that the plugin is not available
   * @param i the index of the multimedia object in the vector or objects
   */
  public String getMediaDescription(int i)
  {
    return getMedia(i).mediaDescription_;
  }

  /**
   * an alternative text for the media
   * @param i the index of the multimedia object in the vector or objects
   */
  public String getMediaAltText(int i)
  {
    return getMedia(i).mediaAltText_;
  }

  /**
   * embed tag for media
   * @param i the index of the multimedia object in the vector or objects
   */
  public String mediaEmbedTag(int i, boolean hiResolution)
  {
    return getMedia(i).mediaEmbedTag(hiResolution);
  }

  /**
   * item name
   */
  public String mediaItemName()
  {
    return getMedia(0).mediaItemName_;
  }

  /**
   * load the data from the database
   * @param conn the database connection manager
   * @param context the servlet context to save the list of multimedia items to
   * @param itemID the id of the item to search for multimedia items with
   */
  public void load(Connection conn, ServletContext context, int itemID) throws SQLException
  {
    // remove attribute because of DbLoadableItemList check
    context.removeAttribute(getClass().getName());
    load(conn, context, query(itemID));
  }

  /**
   * construct a query
   * @param itemID id to search by
   * @return query string
   */
  protected String query(int itemID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT MultimediaObjects.*, Main.ItemName FROM MultimediaObjects ");
    buf.append("left outer join Main on Main.ItemID=MultimediaObjects.ItemID ");
    buf.append("where MultimediaObjects.ItemID=").append(itemID);
    return buf.toString();
  }

  /**
   * number of multimedia objects associated with an item
   * return the number of items
   */
  public int numItems()
  {
    return items_.size();
  }

  /**
   * new instance of a media object from resultset
   * @param result the result set generated from the query
   * @return a single media object that is associated with a particular item
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    MultimediaObject item = new MultimediaObject();
    item.init(result);
    return item;
  }
}
