/**
 * Title:        IntegerMap<p>
 * Description:  hash where keys and values are integers<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: IntegerMap.java,v 1.2 2002/04/12 14:08:17 pbrown Exp $
 *
 * $Log: IntegerMap.java,v $
 * Revision 1.2  2002/04/12 14:08:17  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/08 02:47:01  pbrown
 * utility class for mapping integers to integers
 *
 */
package edu.umass.ccbit.util;

import java.util.Hashtable;
import java.lang.Integer;

public class IntegerMap extends Hashtable
{
  /**
   * put entry in hash
   */
  public Integer put(int key, int value)
  {
    return (Integer) put(new Integer(key), new Integer(value));
  }

  /**
   * get entry from hash
   */
  public Integer get(int key)
  {
    return (Integer) get(new Integer(key));
  }
}


  
