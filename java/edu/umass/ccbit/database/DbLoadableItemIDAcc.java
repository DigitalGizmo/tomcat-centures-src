
/**
 * Title:        DbLoadableItemIDAcc<p>
 * Description:  accumulator list of itemids<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ItemIDList;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbLoadableItemIDAcc extends DbLoadableItemIDList
{
  /**
   * init
   */
  public void initFromResult(ResultSet result) throws SQLException
  {
    int index=getCount();
    System.out.println();
    if(itemIDs_==null)
    {
      itemIDs_=new ItemIDList();
      while (result.next())
      {
        itemIDs_.add(index, result.getInt(ItemID_));
        System.out.println("itemIDs_[" + String.valueOf(index) + "]=" + String.valueOf(itemIDs_.elementAt(index)));
        index++;
      }
    }
    else
    {
      Integer itemid;
      while(result.next())
      {
        itemid = new Integer(result.getInt(ItemID_));
        if(!itemIDs_.contains(itemid))
        {
          itemIDs_.add(index, itemid.intValue());
          System.out.println("itemIDs_[" + String.valueOf(index) + "]=" + String.valueOf(itemIDs_.elementAt(index)));
          index++;
        }
      }
    }
  }
}
