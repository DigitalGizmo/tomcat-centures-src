package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ArticleIDList;
import edu.umass.ckc.database.RecordSet;
import edu.umass.ccbit.database.RecordSetUnion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Arrays;

public abstract class DbLoadableArticleIDRankedIntersection extends DbLoadableArticleIDList {
  // new treemap
  private TreeMap newArticles;

  private static final String Value_ = "value";

  /**
   * load the data
   */
  public void load (Connection conn, Vector queries) throws SQLException {
    connection_=conn;
    Iterator e = queries.iterator();
    RecordSetUnion result = new RecordSetUnion();
    
    while(e.hasNext()) {
      String query = (String) e.next();
      Statement stmt=conn.createStatement();
      stmt.setQueryTimeout(60);
      ResultSet set = stmt.executeQuery(query);
      result.add(set);
      set.close();
      stmt.close();
    }
    
    if (result.numRows()>0) initFromResult(result);
  }

  /**
   * init
   */
  public void initFromResult( ResultSet result ) throws SQLException
  {
    newArticles = new TreeMap();
    initFromResult(newArticles, result);

    Set keysView = newArticles.keySet();
    Object [] array = keysView.toArray();
    sortStringArray(array);

    if(articleIDs_==null)
    {
      articleIDs_ = new ArticleIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newArticles.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          articleIDs_.add(new Integer((String)ids.elementAt(j)));
        }
      }
    }
    else if(articleIDs_.size()>0)
    {
      ArticleIDList idList=new ArticleIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newArticles.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          idList.add(new Integer((String)ids.elementAt(j)));
        }
      }
      retainAll(idList);
    }
    else
    {
      articleIDs_ = new ArticleIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newArticles.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          articleIDs_.add(new Integer((String)ids.elementAt(j)));
        }
      }
    }
  }

  /**
   * init article list from result set
   */
  public static void initFromResult(TreeMap articles, ResultSet result) throws SQLException
  {
    TreeMap tempArticles = new TreeMap();
    
    boolean valueColumnExists = false;
    try {
      ResultSetMetaData rsmd = result.getMetaData();
      int nLastColumn = rsmd.getColumnCount();
      
      for (int i=1; i<=nLastColumn; i++) {
        if (rsmd.getColumnName( i ).equals( Value_ )) {
           valueColumnExists = true;
           break;
        }
      }          
    } catch (SQLException e) {
    	System.out.println( "WARNING:  database access error occurred in getting column name from resultset metadata." );
    }
        
    while(result.next()) {
      String articleid = result.getString(ArticleID_);
      String value = "0";
      
      if (valueColumnExists) value = result.getString( Value_ );
     	
      String oldValue = (String) tempArticles.put(articleid, value);
      if(oldValue!=null) {
        Integer newValue = new Integer((new Integer(value)).intValue() + (new Integer(oldValue)).intValue());
        tempArticles.put(articleid, newValue.toString());
      }
    }

    Set keys = tempArticles.keySet();
    Iterator inputIter = keys.iterator();
    while(inputIter.hasNext())
    {
      String key = (String)inputIter.next();
      String value = tempArticles.get(key).toString();
      Vector atValue = (Vector) articles.get(value);
      if(atValue==null)
        atValue = new Vector();
      atValue.add(key);
      articles.put(value, atValue);
    }
  }

  /**
   * init
   */
  public void initFromResult(RecordSet result) throws SQLException
  {
    newArticles = new TreeMap();
    initFromResult(newArticles, result);

    Set keysView = newArticles.keySet();
    Object [] array = keysView.toArray();
    sortStringArray(array);

    if(articleIDs_==null)
    {
      articleIDs_ = new ArticleIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newArticles.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          articleIDs_.add(new Integer((String)ids.elementAt(j)));
        }
      }
    }
    else
    {
      ArticleIDList idList=new ArticleIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newArticles.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          idList.add(new Integer((String)ids.elementAt(j)));
        }
      }
      retainAll(idList);
    }
  }

  /**
   * init article list from result set
   */
  public static void initFromResult(TreeMap articles, RecordSet result) throws SQLException
  {
    TreeMap tempArticles = new TreeMap();
    for(int i=0; i<result.numRows(); i++)
    {
      String value = null;
      String articleid = null;
      try
      {
        articleid = result.getString(i, ArticleID_);
        value = result.getString(i, Value_);
      }
      catch(Exception e)
      {
        value = "0";
      }
      String oldValue = (String) tempArticles.put(articleid, value);
      if(oldValue!=null)
      {
        Integer newValue = new Integer((new Integer(value)).intValue() + (new Integer(oldValue)).intValue());
        tempArticles.put(articleid, newValue.toString());
      }
    }

    Set keys = tempArticles.keySet();
    Iterator inputIter = keys.iterator();
    while(inputIter.hasNext())
    {
      String key = (String)inputIter.next();
      String value = tempArticles.get(key).toString();
      Vector atValue = (Vector) articles.get(value);
      if(atValue==null)
        atValue = new Vector();
      atValue.add(key);
      articles.put(value, atValue);
    }
  }

  private static void sortStringArray(Object [] array) {
    int [] list = new int[array.length];
    for(int i=0; i<array.length; i++)
    {
      list[i] = (new Integer((String)array[i])).intValue();
    }
    Arrays.sort(list);
    for(int i=0; i<array.length; i++)
    {
      array[i] = new Integer(list[i]).toString();
    }
  }
}
