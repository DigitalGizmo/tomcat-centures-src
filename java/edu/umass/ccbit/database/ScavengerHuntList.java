/*
 * Title:        ScavengerHuntList<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version $Id: ScavengerHuntList.java,v 1.7 2002/04/12 14:07:40 pbrown Exp $
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.jsp.HttpDbJspBase;
//import javax.servlet.ServletConfig;
//import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ScavengerHuntList extends DbLoadableItemList
{
  /**
   * constructor
   */
  public ScavengerHuntList()
  {
  }

  protected static String view()
  {
    // this just reads from the scavenger hunt table
    return "Web_ScavengerHuntIndexView";
  }

  /**
   * title of scavenger hunt at index
   * @param index the index of the hunt
   */
  public String title(int index)
  {
    return getScavengerHuntInfo(index).title_;
  }

  /**
   * short description of the hunt at index
   * @param index the index of the hunt
   */
  public String description(int index)
  {
    return getScavengerHuntInfo(index).description_;
  }

  /**
   * thumbnail image for scavenger hunt at index
   */
  public String image(int index)
  {
    //ScavengerHuntInfo tom = getScavengerHuntInfo(index);
    //return tom.description_;
    return getScavengerHuntInfo(index).thumbnail(100, 100);
  }

  /**
   * url for scavenger hunt page associated with index
   */
  public String url(int index)
  {
    // need to know what the url should be formed as...
    StringBuffer buf = new StringBuffer();
    buf.append("?scav_id=").append(getScavengerHuntInfo(index).scavengerHuntID_);
    return buf.toString();
  }

  protected String query()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("select * from ").append(view());
    return buf.toString();
  }

  protected ScavengerHuntInfo getScavengerHuntInfo(int index)
  {
    return (ScavengerHuntInfo) get(index);
  }

  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    ScavengerHuntInfo scavengerHuntInfo=new ScavengerHuntInfo();
    scavengerHuntInfo.init(result);
    return scavengerHuntInfo;
  }

  /**
   * the number of hunts available to the user
   */
  public int numHunts()
  {
    return items_.size();
  }

  public void load(Connection conn) throws SQLException
  {
    load(conn, query());
  }
}
