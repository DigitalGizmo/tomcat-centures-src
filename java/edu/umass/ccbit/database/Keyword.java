/**
 * Title:        Keyword<p>
 * Description:  keyword in database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: Keyword.java,v 1.2 2002/04/12 14:07:27 pbrown Exp $
 *
 * $Log: Keyword.java,v $
 * Revision 1.2  2002/04/12 14:07:27  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2001/03/12 17:20:52  pbrown
 * added keywords to curatorial info
 *
 */
package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Keyword implements InstanceFromResult
{
  public String keyword_;
  public int keywordID_;

  public void init(ResultSet result) throws SQLException
  {
    keyword_=result.getString("Keyword");
    keywordID_=result.getInt("KeywordID");
  }
}
