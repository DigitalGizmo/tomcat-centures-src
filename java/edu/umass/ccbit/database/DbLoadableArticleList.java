package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.servlet.ServletContext;

public abstract class DbLoadableArticleList extends DbLoadable {
  protected Vector articles_;

  /**
   * get article at index
   */
  protected Object get(int index) {
    if (index >= 0 && articles_ != null && index < articles_.size())
      return articles_.get(index);
    else
      return null;
  }

  /**
   * sort
   */
  public void sort(Comparator cmp) {
    int nsize=articles_.size();
    Object iarray[] = articles_.toArray(new Object[nsize]);
    Arrays.sort(iarray, cmp);
    articles_=new Vector();
    for (int i=0; i < nsize; i++)
    {
      articles_.add(iarray[i]);
    }
  }

  /**
   * number of articles
   */
  public int getCount() {
    return articles_ != null ? articles_.size() : 0;
  }

  /**
   * instantiates object from database result
   */
  protected abstract InstanceFromResult newInstance(ResultSet result) throws SQLException;
  
  /**
   * initialize data in this object based on contents of result set
   */
  protected final void initFromResult(ResultSet result) throws SQLException {
    articles_=new Vector();
    while (result.next()) {
      articles_.add(newInstance(result));
    }
  }

  /**
   * load from database, or from session
   * save to session if loaded from database
   */
  public synchronized void load(Connection conn, ServletContext context, String query) throws SQLException {
    articles_=(Vector) context.getAttribute(getClass().getName());
    if (articles_==null) {
      load(conn, query);
      context.setAttribute(getClass().getName(), articles_);
    }
  }
}

  
