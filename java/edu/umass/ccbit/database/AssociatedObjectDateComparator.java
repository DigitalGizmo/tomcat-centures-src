/**
 * Title:        AssociatedObjectDateComparator<p>
 * Description:  date comparison function<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author
 * @version $Id: AssociatedObjectDateComparator.java,v 1.3 2002/04/12 14:07:08 pbrown Exp $
 *
 * $Log: AssociatedObjectDateComparator.java,v $
 * Revision 1.3  2002/04/12 14:07:08  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/05/19 21:41:58  pbrown
 * added classes for item pages
 *
 * 
 */
package edu.umass.ccbit.database;

import java.util.Comparator;
import edu.umass.ccbit.database.AssociatedObjectBase;

public class AssociatedObjectDateComparator implements Comparator
{
    public int compare(Object a, Object b)
    {
      int cmp=((AssociatedObjectBase)a).objectYear() - ((AssociatedObjectBase)b).objectYear();
      if (cmp==0)
      {
        return ((AssociatedObjectBase)a).name().compareToIgnoreCase(((AssociatedObjectBase)b).name());
      }
      else
      {
        return cmp;
      }
    }
}
