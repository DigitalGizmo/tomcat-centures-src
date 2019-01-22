/**
 * Title:        Topics<p>
 * Description:  topics, read from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: Topics.java,v 1.3 2002/04/12 14:07:42 pbrown Exp $
 *
 * $Log: Topics.java,v $
 * Revision 1.3  2002/04/12 14:07:42  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2001/04/05 19:27:58  tarmstro
 * changes for search output
 *
 * Revision 1.1  2001/03/04 03:29:50  pbrown
 * changes for search pages
 *
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.StringBuffer;
import java.util.Enumeration;

public class Topics extends DbLoadableItemList
{
  protected class Topic implements InstanceFromResult
  {
    public String topic_;
    public int topicID_;

    public void init(ResultSet result) throws SQLException
    {
      topic_=result.getString("Topic");
      topicID_=result.getInt("TopicID");
    }
  }

  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    Topic topic=new Topic();
    topic.init(result);
    return topic;
  }

  public String getTopic(int index)
  {
    return ((Topic)get(index)).topic_;
  }

  public int getTopicID(int index)
  {
    return ((Topic)get(index)).topicID_;
  }

  public int numItems()
  {
    return items_.size();
  }

  /**
   * get the topic
   * @param key the id to find the topic by
   * @return the topic
   */
  public String getTopicByID(int key)
  {
    Enumeration e = items_.elements();
    Topic item;
    while(e.hasMoreElements())
    {
      item=(Topic) e.nextElement();
      if(item.topicID_ == key)
        return item.topic_;
    }
    return "";
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    /*
      CREATE VIEW dbo.Web_SearchableKeywordsView
      AS
      SELECT DISTINCT
          KeywordLink.KeywordID, Keywords.Keyword
      FROM KeywordLink INNER JOIN
          Keywords ON
          KeywordLink.KeywordID = Keywords.KeywordID INNER JOIN
          Main ON KeywordLink.ItemID = Main.ItemID
      WHERE (Keywords.FilemakerPro = 1) AND (Main.Ready = 1)
     */
    load(conn, context, "SELECT * from Topics ORDER BY Topic");
  }
}
