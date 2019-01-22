/**
 * Title:        InstanceFromResult<p>
 * Description:  interface for objects which can be instantiated from row in result set<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: InstanceFromResult.java,v 1.2 2002/04/12 14:07:25 pbrown Exp $
 *
 * $Log: InstanceFromResult.java,v $
 * Revision 1.2  2002/04/12 14:07:25  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/05/24 06:11:28  pbrown
 * first implementation of item pages with images and object viewer
 *
 * 
 */
package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface InstanceFromResult
{
  public void init(ResultSet result) throws SQLException;
}

  

