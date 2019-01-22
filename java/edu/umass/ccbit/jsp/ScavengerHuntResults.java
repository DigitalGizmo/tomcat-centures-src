
/**
 * Title:        ScavengerHuntResults<p>
 * Description:  base class for answer page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.CollectionThemeItem;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.ScavengerHuntSessionItems;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Vector;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public abstract class ScavengerHuntResults extends HttpDbJspBase
{
  public static final String view_ = "Web_ScavengerHuntResultsView";
  public Vector items_ = new Vector();
  public MrSidImage itemImage_;
  public int scavID_;
  public int scavengerHuntID_;
  public String scavImg_;
  public int loc_;

  /**
   * construct a query
   * @param scavID the id which is associated with this subtheme in the database
   */
  protected String query(int scavID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(view_);
    buf.append(" WHERE ScavengerHuntItemID=").append(scavID);
    return buf.toString();
  }

  /**
   * parse the parameters
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    scavID_ = servletParams_.getInt("scav_item");
    loc_ = servletParams_.getInt("loc");
    scavengerHuntID_ = servletParams_.getInt("scav_id");
  }

  /**
   * load the information from the database
   * @param config the servlet config
   */
  public void load(HttpSession session, ServletConfig config) throws SQLException
  {
    ServletContext context = config.getServletContext();
    scavImg_ = ((ScavengerHuntSessionItems) ((Vector) JspSession.load(session, "foundItems")).elementAt(loc_)).scavImg(session, loc_);

    CollectionThemeItem temp;
    Statement st = connection_.createStatement();
    ResultSet result = st.executeQuery(query(scavID_));
    items_.clear();

    while(result.next())
    {
      temp = new CollectionThemeItem();
      temp.itemName_=result.getString("ItemName");
      temp.accessionNumber_=result.getString("AccessionNumber");
      temp.fileName_=result.getString("Filename");
      temp.client_=result.getString("Client");
      items_.addElement(temp);
      System.out.println(items_.size());
    }
  }

  /**
   * get an item from the vector at index
   * @param i the index of the item in a vector
   */
  public CollectionThemeItem item(int i)
  {
    return (CollectionThemeItem) items_.get(i);
  }

  /**
   * the image of the hotspot from the hunt
   */
  public String scavImg()
  {
    return "<img src=\""+scavImg_+"\">";
  }

  /**
   * link parameter to return to current hunt with current items
   */
  public String parameters()
  {
    return "?scav_id="+scavengerHuntID_;
  }

  /**
   * item name link to itempage
   * change the static link to a context parameter
   * @param i the index of the item in a vector
   */
  public String itemNameLink(int i)
  {
    return item(i).itemName_;
  }

  /**
   * item image link to itempage
   * change the static link to a context parameter
   * @param i the index of the item in a vector
   */
  public String imageLink(int i)
  {
    return imgTag(i);
  }

  /**
   * the image tag for an item
   * @param i the index of the item in a vector
   */
  public String imgTag(int i)
  {
    return item(i).scaledSpecialImageTag(150, 150);
  }

  /**
   * the accession number of an item
   * @param i the index of the item in a vector
   */
  public String accessionNumber(int i)
  {
    return item(i).accessionNumber_;
  }

  /**
   * the number of items matching this hunt item
   */
  public int numItems()
  {
    return items_.size();
  }

}
