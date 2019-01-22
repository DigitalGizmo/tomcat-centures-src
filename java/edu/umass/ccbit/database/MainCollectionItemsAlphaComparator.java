
/**
 * Title:        MainCollectionItemsAlphaComparator<p>
 * Description:  compares two main collection items based on itemname<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.util.Comparator;
import edu.umass.ccbit.database.CollectionItemInfo;

public class MainCollectionItemsAlphaComparator implements Comparator
{
  public int compare(Object a, Object b)
  {
    CollectionItemInfo a_ = (CollectionItemInfo) a;
    CollectionItemInfo b_ = (CollectionItemInfo) b;
    String a_name = a_.itemName();
    if(a_name.charAt(0)=='"')
      a_name = a_name.substring(1);
    String b_name = b_.itemName();
    if(b_name.charAt(0)=='"')
      b_name = b_name.substring(1);
    return a_name.compareToIgnoreCase(b_name);
//    return ((CollectionItemInfo) a).itemName().compareToIgnoreCase(((CollectionItemInfo) b).itemName());
  }
}
