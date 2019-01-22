
/**
 * Title:        MainCollectionItemsDateComparator<p>
 * Description:  compares two main collection items based on date<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.util.Comparator;
import edu.umass.ccbit.util.CollectionDate;
import edu.umass.ccbit.database.CollectionItemInfo;

public class MainCollectionItemsDateComparator implements Comparator
{
  public int compare(Object a, Object b)
  {
    CollectionDate tempA = ((CollectionItemInfo) a).date_;
    CollectionDate tempB = ((CollectionItemInfo) b).date_;
    int cmp = tempA.comparisonYear() - tempB.comparisonYear();
    if(cmp == 0)
      cmp = tempA.comparisonMonth() - tempB.comparisonMonth();
    return cmp;
  }
}
