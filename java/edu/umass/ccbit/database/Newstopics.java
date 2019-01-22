package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.StringBuffer;
import java.util.Enumeration;

public class Newstopics extends DbLoadableArticleList {
	
  protected class Newstopic implements InstanceFromResult {
    public String topic_;
    public int topicID_;

    public void init(ResultSet result) throws SQLException {
      topic_   = result.getString( "Topic" );
      topicID_ = result.getInt( "TopicID" );
    }
  }

  protected InstanceFromResult newInstance( ResultSet result ) throws SQLException {
    Newstopic topic = new Newstopic();
    topic.init(result);
    return topic;
  }

  public String getTopic( int index ) {
    return ((Newstopic)get(index)).topic_;
  }

  public int getTopicID(int index) {
    return ((Newstopic)get(index)).topicID_;
  }

  public int numArticles() {
    return articles_.size();
  }

  /**
   * get the topic
   * @param key the id to find the topic by
   * @return the topic
   */
  public String getTopicByID(int key) {
    Enumeration e = articles_.elements();
    Newstopic article;
    while(e.hasMoreElements()) {
      article=(Newstopic) e.nextElement();
      if(article.topicID_ == key)
        return article.topic_;
    }
    return "";
  }

  public void load( Connection conn, ServletContext context ) throws SQLException {
    load( conn, context, "SELECT * from Newstopics ORDER BY Topic" );
  }
}
