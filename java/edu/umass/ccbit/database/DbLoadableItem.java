/**
 * Title:        DbLoadableItem<p>
 * Description:  single object loadable from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadableItem.java,v 1.2 2002/04/12 14:07:19 pbrown Exp $
 *
 * $Log: DbLoadableItem.java,v $
 * Revision 1.2  2002/04/12 14:07:19  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/06 14:33:14  pbrown
 * new class for items which either load themselves, or are loaded into a list
 *
 */

package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbLoadableItem extends DbLoadable implements InstanceFromResult
{
  /**
   * initialize data in this object based on contents of result set
   */
  protected final void initFromResult(ResultSet result) throws SQLException
  {
    if (result.next())
      init(result);
  }
}

  
