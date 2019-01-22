/**
 * Title:        StringPairList<p>
 * Description:  vector of string pairs<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: StringPairList.java,v 1.3 2002/04/12 14:08:22 pbrown Exp $
 *
 * $Log: StringPairList.java,v $
 * Revision 1.3  2002/04/12 14:08:22  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/11/01 21:45:38  pbrown
 * changes for item detail page
 *
 * Revision 1.1  2000/05/26 20:55:06  pbrown
 * string pair list class
 *
 */
package edu.umass.ccbit.util;

import java.util.Vector;
import java.lang.ArrayIndexOutOfBoundsException;

public class StringPairList extends Vector
{
  /**
   * add a pair to the list
   */
  public void add(String first, String second)
  {
    String pair[]=new String[2];
    pair[0]=first;
    pair[1]=second;
    super.add(pair);
  }

  /**
   * add pair to list if both entries are non-empty
   */
  public void addIfNonEmpty(String first, String second)
  {
    if ((first!=null && first.length()>0) && (second != null && second.length() > 0))
    {
      add(first, second);
    }
  }

  /**
   * get item in position, at index
   */
  public String get(int pos, int index) throws ArrayIndexOutOfBoundsException
  {
    return ((String []) super.get(index))[pos];
  }

  /**
   * get first item at index
   */
  public String first(int index) throws ArrayIndexOutOfBoundsException
  {
    return get(0, index);
  }

  /**
   * get second item at index
   */
  public String second(int index) throws ArrayIndexOutOfBoundsException
  {
    return get(1, index);
  }
}


  

  
    
    
  
    
