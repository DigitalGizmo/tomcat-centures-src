
/**
 * Title:        RelatedAssociationsList<p>
 * Description:  a list of related items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelatedAssociationsList extends DbLoadableItemList
{
  public static final String view_="Web_CollectionAssociationsRelatedItems";

  protected class RelatedAssociation implements InstanceFromResult
  {
    protected int relatedID_;
    protected String relatedName_;

    /**
     * initialize object from result
     */
    public void init(ResultSet result) throws SQLException
    {
      relatedID_=result.getInt("RelatedItemID");
      relatedName_=result.getString("ItemName");
    }
  }

  /**
   * related itemid
   */
  public int relatedItemID(int i)
  {
    return ((RelatedAssociation)get(i)).relatedID_;
  }

  /**
   * related item name
   */
  public String relatedItemName(int i)
  {
    return ((RelatedAssociation)get(i)).relatedName_;
  }

  /**
   * load data from database
   */
  public void load(Connection conn, int itemID) throws SQLException
  {
    load(conn, query(itemID));
  }

  /**
   * query
   */
  private String query(int itemID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(view_);
    buf.append(" WHERE relateditemid is not null and itemid = ").append(itemID);
    return buf.toString();
  }

  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    RelatedAssociation related = new RelatedAssociation();
    related.init(result);
    return related;
  }
}
