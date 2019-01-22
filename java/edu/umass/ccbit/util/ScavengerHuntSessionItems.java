
/**
 * Title:        ScavengerSessionItems<p>
 * Description:  class for a search result collection<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.util;

import edu.umass.ccbit.database.DbLoadableItemList;
import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.database.MainCollectionItem;
import edu.umass.ccbit.database.InstanceFromResult;
import edu.umass.ccbit.database.ScavengerHuntInfo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpSession;


public class ScavengerHuntSessionItems extends DbLoadableItemList implements InstanceFromResult
{
  public int itemID_;
  public String client_;
  public String filename_;
  public MrSidImage itemImage_;
  public static HttpSession session_;
  public int scavItem_;
  public String scavImg_;

  /**
   * constructor
   */
  public ScavengerHuntSessionItems()
  {
  }

  /**
   * create a new instance
   */
  protected InstanceFromResult newInstance(ResultSet result)
   throws SQLException
  {
    ScavengerHuntSessionItems obj = new ScavengerHuntSessionItems();
    obj.init(result);
    return obj;
  }

  /**
   * initialize fields
   */
  public void init(ResultSet result)
   throws SQLException
  {
    itemID_ = result.getInt("ItemID");
    client_ = result.getString("Client");
    filename_ = result.getString("Filename");
    scavItem_ = JspSession.getInt(session_, "scav_item", 1);
    scavImg_ = (String) JspSession.load(session_, "scavImg");
    itemImage_ = new MrSidImage(client_, filename_);
  }

  /**
   * returns hunt session item for item at index, or null if
   * no item has been found
   * @param index the index of the item
   */
  public ScavengerHuntSessionItems itemInfo(HttpSession session, int index)
  {
    return (ScavengerHuntSessionItems) ((Vector) JspSession.load(session, "foundItems")).elementAt(index);
  }

  /**
   * item image at index, or null if no item selected
   * @param index the index of the item
   */
  public MrSidImage itemImage(HttpSession session, int index)
  {
    return itemInfo(session, index).itemImage_;
  }

  /**
   * the hotspot id that was searched for with this item
   */
  public int scavItem(HttpSession session, int index)
  {
    return itemInfo(session, index).scavItem_;
  }

  /**
   * the hotspot image associated with what was found
   */
  public String scavImg(HttpSession session, int index)
  {
    return itemInfo(session, index).scavImg_;
  }

  /**
   * construct a query
   * @param scavengerHuntID the id of the hunt
   */
  protected String query(int itemID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append("Web_ItemImageView");
    buf.append(" WHERE ItemID=").append(itemID);
    return buf.toString();
  }

  /**
   * size of vector
   */
  public int size(HttpSession session)
  {
    return ((Vector) JspSession.load(session, "foundItems")).size();
  }

  /**
   * public add item
   */
  public void addItem(Connection conn, HttpSession session, int itemID)
   throws SQLException, NullPointerException, NoSuchElementException
  {
    session_ = session;
    Vector foundItems;

    if(JspSession.load(session, "foundItems")==null)
    {
      foundItems = new Vector();
      JspSession.save(session, "foundItems", foundItems);
    }
    else
    {
      foundItems = (Vector)JspSession.load(session, "foundItems");
    }
    if(itemID!=0)
    {
      load(conn, itemID);
      try
      {
        foundItems.add(items_.firstElement());
      }
      catch(Exception e)
      {}
    }
  }

  /**
   * remove the items found when the user enters a new hunt
   */
  public void clear(HttpSession session)
  {
    Vector foundItems = (Vector)JspSession.load(session, "foundItems");
    foundItems.clear();
  }

  /**
   * remove an item from the vector
   */
  public void removeItem(Connection conn, HttpSession session, int itemID)
  {
    Vector foundItems = (Vector)JspSession.load(session, "foundItems");
    Enumeration e = foundItems.elements();

    while(e.hasMoreElements())
    {
      ScavengerHuntSessionItems item = (ScavengerHuntSessionItems) e.nextElement();
      if(item.itemID_==itemID)
      {
        System.out.println("equal itemids");
        foundItems.removeElement(item);
      }
      else
      {
        System.out.println(itemID + " " + item.itemID_);
      }
    }
  }

  public void load(Connection conn, int itemID)
   throws SQLException
  {
    load(conn, query(itemID));
  }
}
