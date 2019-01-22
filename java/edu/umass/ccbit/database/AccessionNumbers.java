
/**
 * Title:        AccessionNumbers<p>
 * Description:  accession numbers, read from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;

public class AccessionNumbers extends DbLoadableItemList
{
  protected class AccessionNumber implements InstanceFromResult
  {
    // attributes
    public int itemID_;
    public String accessionNumber_;
    public String itemName_;

    /**
     * initialize from result set
     * @param result the result set
     */
    public void init(ResultSet result) throws SQLException
    {
      itemID_=result.getInt("ItemID");
      accessionNumber_=result.getString("AccessionNumber");
      itemName_=result.getString("ItemName");
    }
  }

  /**
   * accession number
   * @param index the index of the accession number in the vector of accession numbers
   * @return the accession number
   */
  public String getAccessionNumber(int index)
  {
    return ((AccessionNumber)get(index)).accessionNumber_;
  }

  /**
   * accession number
   * @param index the index of the accession number in the vector of accession numbers
   * @return the accession number
   */
  public int getItemID(int index)
  {
    return ((AccessionNumber)get(index)).itemID_;
  }

  /**
   * accession number
   * @param index the index of the accession number in the vector of accession numbers
   * @return the accession number
   */
  public String getItemName(int index)
  {
    return ((AccessionNumber)get(index)).itemName_;
  }

  /**
   * new accession number object
   * @param result the result set
   * @return AccessionNumber
   */
  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    AccessionNumber accession = new AccessionNumber();
    accession.init(result);
    return accession;
  }

  /**
   * load information from the database with the preformed query
   * @param conn the database connection manager
   * @param context the servlet context to save the list of accession numbers in
   */
  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT ItemID, AccessionNumber, ItemName from Main where Ready=1 order by AccessionNumber");
  }
}
