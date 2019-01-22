package edu.umass.ccbit.util;

import java.util.Vector;
import java.lang.Integer;

public class ArticleIDList extends Vector {
  /**
   * add an article id to vector
   */
  public void add( int articleid ) {
    super.add( new Integer( articleid ) );
  }

  /**
   * add an article id to a specific location in a vector
   */
  public void add(int index, int articleid) {
    super.add( index, new Integer(articleid) );
  }

  /**
   * get value at index
   */
  public int getValue(int index) {
    if (index >= 0 && index < size()) {
      return ((Integer) get( index )).intValue();
    }
    else
      return -1;
  }
}



