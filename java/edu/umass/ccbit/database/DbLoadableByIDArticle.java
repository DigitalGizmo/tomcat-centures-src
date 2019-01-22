package edu.umass.ccbit.database;

//import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class DbLoadableByIDArticle extends DbLoadableArticle {
  protected int articleID_;
  /**
   * view to load from
   */
  protected abstract String view();
  
  /**
   * load
   */
  public void load(Connection conn, int articleID) throws SQLException {
    articleID_ = articleID;
    load(conn, DbQueries.selectByArticleID( view(), articleID, null ));
  }
}

