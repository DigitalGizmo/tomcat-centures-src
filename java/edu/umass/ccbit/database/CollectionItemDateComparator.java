/**
 * Title:        CollectionItemDateComparator<p>
 * Description:  date comparison function for collection items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author
 * @version $Id: CollectionItemDateComparator.java,v 1.2 2002/04/12 14:07:13 pbrown Exp $
 *
 * $Log: CollectionItemDateComparator.java,v $
 * Revision 1.2  2002/04/12 14:07:13  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/27 16:13:44  pbrown
 * update for searching, navigation bar
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.database.CollectionItemInfo;
import java.util.Comparator;
import edu.umass.ccbit.util.CollectionDate;

public class CollectionItemDateComparator implements Comparator
{
  public int compare(Object a, Object b)
  {
    CollectionDate date_a=((CollectionItemInfo)a).date_;
    CollectionDate date_b=((CollectionItemInfo)b).date_;
  
    if (date_a.cal() != null && date_b.cal() != null)
      return date_a.cal().getTime().compareTo(date_b.cal().getTime());
    else
    {
      int cmp=date_a.comparisonYear()-date_b.comparisonYear();
      if (cmp != 0)
        return cmp;
      else
        return date_a.comparisonMonth() - date_b.comparisonMonth();
    }
  }
}
