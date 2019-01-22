/**
 * Title:        DbQueries<p>
 * Description:  provides static methods for constructing queries<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbQueries.java,v 1.5 2003/08/01 11:49:44 keith Exp $
 *
 * $Log: DbQueries.java,v $
 * Revision 1.5  2003/08/01 11:49:44  keith
 * minor changes
 *
 * Revision 1.4  2003/07/28 19:00:40  keith
 * fixed compilation error
 *
 * Revision 1.3  2003/07/28 18:50:48  keith
 * added selectByArticleID method
 *
 * Revision 1.2  2002/04/12 14:07:22  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/02 21:52:52  pbrown
 * many changes for searching..more to come
 *
 */

package edu.umass.ccbit.database;

import java.lang.StringBuffer;

public class DbQueries {
  /**
   * query based on itemid
   */
  public static String selectByItemID( String view, int itemID, String orderBy ) {
    StringBuffer query = new StringBuffer();
    query.append("SELECT * FROM ").append( view ).append( " WHERE ItemID=" ).append( itemID );
    if (orderBy != null) query.append( " ORDER BY " ).append( orderBy );
    return query.toString();
  }
  
  /**
   * query based on articleid
   */
  public static String selectByArticleID( String view, int articleID, String orderBy ) {
    StringBuffer query = new StringBuffer();
    query.append( "SELECT * FROM " ).append( view ).append( " WHERE ArticleID=" ).append( articleID );
    if (orderBy != null) query.append( " ORDER BY " ).append( orderBy );
    return query.toString();
  }
}