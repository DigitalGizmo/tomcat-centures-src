/**
 * Title:        ItemIDList<p>
 * Description:  list of item ids for use in search, browse, etc.<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ItemIDList.java,v 1.3 2002/04/12 14:08:17 pbrown Exp $
 *
 * $Log: ItemIDList.java,v $
 * Revision 1.3  2002/04/12 14:08:17  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/07/25 16:19:25  tarmstro
 * added method to add an item at a specific position
 *
 * Revision 1.1  2000/06/02 21:52:57  pbrown
 * many changes for searching..more to come
 *
 *
 */
package edu.umass.ccbit.util;

import java.util.Vector;
import java.lang.Integer;

public class ItemIDList extends Vector
{
  /**
   * add an item id to vector
   */
  public void add(int itemid)
  {
    super.add(new Integer(itemid));
  }

  /**
   * add an item id to a specific location in a vector
   */
  public void add(int index, int itemid)
  {
    super.add(index, new Integer(itemid));
  }

  /**
   * get value at index
   */
  public int getValue(int index)
  {
    if (index >= 0 && index < size())
    {
      return ((Integer) get(index)).intValue();
    }
    else
      return -1;
  }
}



