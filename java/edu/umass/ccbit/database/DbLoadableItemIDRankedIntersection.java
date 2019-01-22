package edu.umass.ccbit.database;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author
 * @version 1.0
 */

import edu.umass.ccbit.util.ItemIDList;
import edu.umass.ckc.database.RecordSet;
import edu.umass.ccbit.database.RecordSetUnion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Arrays;

public abstract class DbLoadableItemIDRankedIntersection extends DbLoadableItemIDList
{
  // new treemap
  private TreeMap newItems;

  private static final String Value_ = "value";

  /**
   * load the data
   */
  public void load(Connection conn, Vector queries) throws SQLException
  {
    connection_=conn;
    Iterator e = queries.iterator();
    RecordSetUnion result = new RecordSetUnion();
    while(e.hasNext())
    {
      String query = (String) e.next();
      Statement stmt=conn.createStatement();
      stmt.setQueryTimeout(60);
      ResultSet set = stmt.executeQuery(query);
      result.add(set);
      set.close();
      stmt.close();
    }
    if(result.numRows()>0)
      initFromResult(result);
  }

  /**
   * init
   */
  public void initFromResult(ResultSet result) throws SQLException
  {
    newItems = new TreeMap();
    initFromResult(newItems, result);

    Set keysView = newItems.keySet();
    Object [] array = keysView.toArray();
    sortStringArray(array);

    if(itemIDs_==null)
    {
      itemIDs_ = new ItemIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newItems.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          itemIDs_.add(new Integer((String)ids.elementAt(j)));
        }
      }
    }
    else if(itemIDs_.size()>0)
    {
      ItemIDList idList=new ItemIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newItems.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          idList.add(new Integer((String)ids.elementAt(j)));
        }
      }
      retainAll(idList);
    }
    else
    {
      itemIDs_ = new ItemIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newItems.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          itemIDs_.add(new Integer((String)ids.elementAt(j)));
        }
      }
    }
  }

  /**
   * init item list from result set
   */
  public static void initFromResult(TreeMap items, ResultSet result) throws SQLException
  {
    TreeMap tempItems = new TreeMap();
    while(result.next())
    {
      String value = null;
      String itemid = result.getString(ItemID_);
      // 20051230 KRU disabled first part, which was filling log with stack traces
      //try {
      //  value = result.getString(Value_);
      //} catch(SQLException e) {
        value = "0";
      //}
      String oldValue = (String) tempItems.put(itemid, value);
      if(oldValue!=null)
      {
        Integer newValue = new Integer((new Integer(value)).intValue() + (new Integer(oldValue)).intValue());
        tempItems.put(itemid, newValue.toString());
      }
    }

    Set keys = tempItems.keySet();
    Iterator inputIter = keys.iterator();
    while(inputIter.hasNext())
    {
      String key = (String)inputIter.next();
      String value = tempItems.get(key).toString();
      Vector atValue = (Vector) items.get(value);
      if(atValue==null)
        atValue = new Vector();
      atValue.add(key);
      items.put(value, atValue);
    }
  }

  /**
   * init
   */
  public void initFromResult(RecordSet result) throws SQLException
  {
    newItems = new TreeMap();
    initFromResult(newItems, result);

    Set keysView = newItems.keySet();
    Object [] array = keysView.toArray();
    sortStringArray(array);

    if(itemIDs_==null)
    {
      itemIDs_ = new ItemIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newItems.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          itemIDs_.add(new Integer((String)ids.elementAt(j)));
        }
      }
    }
    else
    {
      ItemIDList idList=new ItemIDList();
      for(int i=array.length-1; i>=0; i--)
      {
        Vector ids = (Vector) newItems.get(array[i]);
        for(int j=0; j<ids.size(); j++)
        {
          idList.add(new Integer((String)ids.elementAt(j)));
        }
      }
      retainAll(idList);
    }
  }

  /**
   * init item list from result set
   */
  public static void initFromResult(TreeMap items, RecordSet result) throws SQLException
  {
    TreeMap tempItems = new TreeMap();
    for(int i=0; i<result.numRows(); i++)
    {
      String value = null;
      String itemid = null;
      try
      {
        itemid = result.getString(i, ItemID_);
        value = result.getString(i, Value_);
      }
      catch(Exception e)
      {
        value = "0";
      }
      String oldValue = (String) tempItems.put(itemid, value);
      if(oldValue!=null)
      {
        Integer newValue = new Integer((new Integer(value)).intValue() + (new Integer(oldValue)).intValue());
        tempItems.put(itemid, newValue.toString());
      }
    }

    Set keys = tempItems.keySet();
    Iterator inputIter = keys.iterator();
    while(inputIter.hasNext())
    {
      String key = (String)inputIter.next();
      String value = tempItems.get(key).toString();
      Vector atValue = (Vector) items.get(value);
      if(atValue==null)
        atValue = new Vector();
      atValue.add(key);
      items.put(value, atValue);
    }
  }

  private static void sortStringArray(Object [] array)
  {
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
