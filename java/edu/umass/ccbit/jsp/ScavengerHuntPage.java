
/**
 * Title:        ScavengerHuntPage<p>
 * Description:  base class for a scavenger hunt page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ccbit.database.ScavengerHunt;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.util.JspSession;
import java.io.IOException;
import java.sql.SQLException;
import java.lang.ArrayIndexOutOfBoundsException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletContext;
import javax.servlet.ServletConfig;

public abstract class ScavengerHuntPage extends HttpDbJspBase
{
  protected int scavengerHuntID_;
  protected ScavengerHunt scavengerHunt_;
  public static String searchPage_;
  public static String imgTag_;
  public static int itemID_;
  public boolean rem_;

  /**
   * checks to see if the hunt is valid
   */
  public boolean isValid()
  {
    if(scavengerHunt_.isValid())
      return true;
    return false;
  }

  /**
   * change the status of the items at the bottom of the page
   * @param session the current session
   */
  protected void changeItems(HttpSession session) throws SQLException
  {
    if(rem_)
    {
      System.out.println("removing item...");
      scavengerHunt_.sessionItems_.removeItem(connection_, session, itemID_);
    }
    else
    {
      scavengerHunt_.sessionItems_.addItem(connection_, session, itemID_);
    }
  }

  /**
   * create an image map of scavenger hunt items
   */
  protected String imageMap()
  {
    return scavengerHunt_.imageMap(searchPage_);
  }

  /**
   * initialize the jsp
   * get context parameters
   * @param config the servlet config
   */
  public void jspInit(ServletConfig config)
  {
    super.jspInit();
    ServletContext context = config.getServletContext();
    searchPage_ = context.getInitParameter("scav.search");
  }

  /**
   * load the information from the database
   * @param session the current session
   */
  protected void load(HttpSession session) throws SQLException
  {
    scavengerHunt_=new ScavengerHunt();
    int tempID = JspSession.getInt(session, "scav_id", -1);
    if (tempID!=-1 && tempID != scavengerHuntID_)
    {
      scavengerHunt_.sessionItems_.clear(session);
    }
    JspSession.setInt(session, "scav_id", scavengerHuntID_);
    scavengerHunt_.load(connection_, session, scavengerHuntID_);
    changeItems(session);
  }

  /**
   * parse the request parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request, HttpSession session)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    int tempID = -1;
    try{ tempID=servletParams_.getInt("scav_id"); }
    catch(Exception e){}
    int tempSessionID = JspSession.getInt(session, "scav_id", -1);
    if(tempID != tempSessionID)
    {
      if(tempID < 0)
      {
        if(tempSessionID < 0)
        {
          scavengerHuntID_ = -1;
        }
        else
        {
          scavengerHuntID_ = tempSessionID;
        }
      }
      else
      {
        if(tempSessionID < 0)
        {
          scavengerHuntID_ = tempID;
        }
        else
        {
          scavengerHunt_.sessionItems_.clear(session);
          scavengerHuntID_ = tempID;
        }
      }
    }
    else
    {
      scavengerHuntID_ = tempID;
    }
    try{ itemID_=servletParams_.getInt("itemid"); }
    catch(Exception e){ itemID_=0; }
    try{ rem_=servletParams_.getBoolean("rem"); }
    catch(Exception e){ rem_=false; }
  }

  /**
   * image tag for item found
   * @param i the index of the item in the session
   */
  public String imgFoundTag(HttpSession session, int i)
   throws NullPointerException, ArrayIndexOutOfBoundsException
  {
    try
    {
      if(numItems(session)==0)
      {
        return "Click on an item's hot spot above";
      }
      return scavengerHunt_.sessionItems_.itemImage(session, i).scaledImageTag(75,75,MrSidImage.SCALE_IMAGE,scavengerHunt_.sessionItems_.itemInfo(session, i).filename_);
    }
    catch(Exception e)
    {
      return "Click on an item's hot spot above";
    }
  }

  /**
   * image tag to use for what you think you found
   * for use above imgTag(HttpSession, int)
   * @param session the current session
   */
  public String imgSearchTag(HttpSession session, int i)
   throws ArrayIndexOutOfBoundsException, NullPointerException
  {
    try
    {
      if(numItems(session)==0)
      {
        return "";
      }
      return "<img src=\""+scavengerHunt_.sessionItems_.scavImg(session, i)+"\" width=75 height=30>";
    }
    catch(Exception e)
    {
      return "";
    }
  }

  /**
   * link to check result
   * @param session the current session
   */
  public String foundCheckLink(HttpSession session, int i)
   throws ArrayIndexOutOfBoundsException, NullPointerException
  {
    try
    {
      if(numItems(session)==0)
      {
        return "";
      }
      StringBuffer buf = new StringBuffer();
      buf.append("<a href=\"verify.jsp?scav_item=");
      buf.append(scavengerHunt_.sessionItems_.scavItem(session, i));
      buf.append("&loc=").append(i);
      buf.append("&scav_id=").append(scavengerHuntID_);
      buf.append("\">see our answers</a>");
      return buf.toString();
    }
    catch(Exception e)
    {
      return "";
    }
  }

  /**
   * remove this item link
   */
  public String removeItemLink(HttpSession session, int i)
   throws ArrayIndexOutOfBoundsException, NullPointerException
  {
    try
    {
      if(numItems(session)==0)
      {
        return "";
      }
      StringBuffer buf = new StringBuffer();
      buf.append("<a href=\"scavenger.jsp?itemid=");
      buf.append(scavengerHunt_.sessionItems_.itemInfo(session, i).itemID_);
      buf.append("&rem=true");
      buf.append("\">remove this item</a>");
      return buf.toString();
    }
    catch(Exception e)
    {
      return "";
    }
  }

  /**
   * image tag to use for the hunt image
   * get the image tag for the hunt and append which image map to use
   * @param session the session containing information about the image
   */
  public String imgHuntTag(HttpSession session)
  {
    //make the image tag from the MrSid image tag methods
    String temp = scavengerHunt_.huntImgTag(session);
    //remove the '>' at the end of the image tag
    //append the image map information at the end of the tag
    //reattach the '>' at the end of the new string
    return temp.substring(0, temp.length()-1).concat(" ISMAP usemap='#hunt_map'>");
  }

  /**
   * title of the hunt
   */
  public String title()
  {
    return scavengerHunt_.title();
  }

  /**
   * a description of the hunt
   */
  public String description()
  {
    return scavengerHunt_.description();
  }

  /**
   * the number of things to look for
   */
  public int numItems()
  {
    return scavengerHunt_.numItems_;
  }

  /**
   * the number of things that you have found
   */
  public int numItems(HttpSession session) throws NullPointerException
  {
    return scavengerHunt_.sessionItems_.size(session);
  }
}
