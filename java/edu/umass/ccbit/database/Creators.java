
/**
 * Title:        Creators<p>
 * Description:  creators, read from database<p>
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

public class Creators extends DbLoadableItemList
{
  protected class Creator implements InstanceFromResult
  {
    // attributes
    public int objectProducerID_;
    public boolean producer_;
    public int collectionPersonID_;
    public String name_;

    /**
     * initialize from result set
     * @param result the result set
     */
    public void init(ResultSet result) throws SQLException
    {
      //taken from ObjectCreator...needed a list of creators not item specific
      StringBuffer nameBuf=new StringBuffer();
      String first=result.getString("FirstName");
      String mid=result.getString("MiddleName");
      String last=result.getString("LastName");
      if(first != null)
      {
        // checks if the creator is a person or a producer
        // classifies the data accordingly
        if(first.compareTo("producer")==0)
        {
          producer_=true;
          first=null;
          objectProducerID_=result.getInt("LinkID");
        }
        else
          collectionPersonID_=result.getInt("LinkID");
      }
      if (last != null)
      {
        nameBuf.append(last);
      }
      if (first != null)
        if (nameBuf.length()>0)
          nameBuf.append(", ");
        nameBuf.append(first);
      // need to check for nulls so we don't get a name like
      // 'Ellen null Miller'
      if (mid != null)
      {
        if (nameBuf.length()>0)
          nameBuf.append(" ");
        nameBuf.append(mid);
      }
      name_=nameBuf.toString();
    }
  }

  /**
   * value tag for a dropdown list
   * @param index the index of the creator in the vector of creators
   * @return id of the creator and the type distinction(_producer or _person)
   */
  public String getValue(int index)
  {
    Creator temp = (Creator) get(index);
    if(temp.producer_)
      return temp.objectProducerID_+"_producer";
    else
      return temp.collectionPersonID_+"_person";
  }

  /**
   * name of the creator
   * @param index the index of the creator in the vector of creators
   * @return the name of the creator as in database
   */
  public String getName(int index)
  {
    return ((Creator)get(index)).name_;
  }

  /**
   * new creator item
   * @param result the result set
   * @return Creator
   */
  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    Creator creator = new Creator();
    creator.init(result);
    return creator;
  }

  /**
   * load the information from the database from the preformed query
   * @param conn the database connection manager
   * @param context the servlet context to save the creator list in
   */
  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT * FROM Web_CreatorView ORDER BY LastName, MiddleName, FirstName");
  }
}
