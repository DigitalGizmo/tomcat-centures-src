/*
 * Title:        ScavengerHuntMainPage<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version $Id: ScavengerHuntMainPage.java,v 1.9 2002/04/12 14:08:08 pbrown Exp $
 *
 */

//  implementation of scavenger hunt

//  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//  database tables
//  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

//  ScavengerHunt
//  ======================================================================
//  ScavengerHuntID       int (autoindex)
//  Title                 text
//  ImageID               int (refers to ItemImage table)
//  ImageLevel            zoom level for image display

//  ScavengerHuntItems
//  ======================================================================
//  ScavengerHuntItemID   int (auto)
//  ScavengerHuntID       int (refers to ScavengerHunt table..the scavenger
//                             hunt this item belongs to)
//  ImageX                int |
//  ImageY                int | parameters to specify hot spots on main image, 
//  ImageHeight           int | and for extracting portions of image to be 
//  ImageWidth            int | displayed on search pages etc.

//  ScavengerHuntResults
//  ======================================================================
//  ScavengerHuntResultID int (autonumber)
//  ScavengerHuntItemID   int (refers to ScavengerHuntItems)
//  ItemID                int (refers to main table...represents a
//                             valid choice for this scavenger hunt item)

//  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++                           
//  views
//  ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
//  Web_ScavengerHuntView

//  (join ScavengerHunt & ScavengerHuntItems tables)

//////////////////////////////////////////////////////////////////////
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import edu.umass.ccbit.database.ScavengerHuntList;

public abstract class ScavengerHuntMainPage extends HttpDbJspBase
{
  protected ScavengerHuntList scavengerHuntList_;
  public static String imgTag_;

  /**
   * initialize the jsp
   * get context parameters
   * @param config the servlet config
   */
  public void jspInit(ServletConfig config)
  {
    super.jspInit();
    ServletContext context = config.getServletContext();
  }

  /**
   * load the information from the database
   */
  protected void load() throws SQLException
  {
    scavengerHuntList_=new ScavengerHuntList();
    scavengerHuntList_.load(connection_);
  }

  /**
   * the number of hunts available to the user
   */
  protected int numHunts()
  {
    return scavengerHuntList_.numHunts();
  }

  /**
   * the name of the hunt
   * @param i the index of the hunt
   */
  public String huntName(int i)
  {
    return scavengerHuntList_.title(i);
  }

  /**
   * a short description of the hunt
   * @param i the index of the hunt
   */
  public String huntDescription(int i)
  {
    return scavengerHuntList_.description(i);
  }

  /**
   * the image for use on the index page of hunts
   * @param i the index of the hunt
   */
  public String imgTag(int i)
  {
    return scavengerHuntList_.image(i);
  }

  /**
   * scavenger hunt id
   * @param i the index of the hunt
   */
  public String parameters(int i)
  {
    return scavengerHuntList_.url(i);
  }

  /**
   * parse the request parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
  }
}
