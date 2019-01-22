/**
 * Title:        DbLoadable<p>
 * Description:  base class for objects which load items from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadableItemList.java,v 1.4 2002/04/12 14:07:22 pbrown Exp $
 *
 * $Log: DbLoadableItemList.java,v $
 * Revision 1.4  2002/04/12 14:07:22  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2000/06/27 16:13:46  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.2  2000/06/06 14:33:49  pbrown
 * many changes for searching
 *
 * Revision 1.1  2000/06/02 21:52:52  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.1  2000/05/30 21:37:07  pbrown
 * new base class for items loadable from db, and beginnings of search/search
 * results
 *
 */

package edu.umass.ccbit.database;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import javax.servlet.ServletContext;

public abstract class DbLoadableItemList extends DbLoadable
{
  protected Vector items_;

  /**
   * get item at index
   */
  protected Object get(int index)
  {
    if (index >= 0 && items_ != null && index < items_.size())
      return items_.get(index);
    else
      return null;
  }

  /**
   * sort
   */
  public void sort(Comparator cmp)
  {
    int nsize=items_.size();
    Object iarray[] = items_.toArray(new Object[nsize]);
    Arrays.sort(iarray, cmp);
    items_=new Vector();
    for (int i=0; i < nsize; i++)
    {
      items_.add(iarray[i]);
    }
  }

  /**
   * number of items
   */
  public int getCount()
  {
    return items_ != null ? items_.size() : 0;
  }

  /**
   * instantiates object from database result
   */
  protected abstract InstanceFromResult newInstance(ResultSet result) throws SQLException;
  
  /**
   * initialize data in this object based on contents of result set
   */
  protected final void initFromResult(ResultSet result) throws SQLException
  {
    items_=new Vector();
    while (result.next())
    {
      items_.add(newInstance(result));
    }
  }

  /**
   * load from database, or from session
   * save to session if loaded from database
   */
  public synchronized void load(Connection conn, ServletContext context, String query) throws SQLException
  {
    items_=(Vector) context.getAttribute(getClass().getName());
    if (items_==null)
    {
      load(conn, query);
      context.setAttribute(getClass().getName(), items_);
    }
  }
}

  
