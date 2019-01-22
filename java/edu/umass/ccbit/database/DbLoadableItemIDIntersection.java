/**
 * Title:        DbLoadableItemIDIntersection<p>
 * Description:  loads item id list and intersects it with current contents, if any<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pb
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.util.ItemIDList;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbLoadableItemIDIntersection extends DbLoadableItemIDList
{
  /**
   * init
   */
  public void initFromResult(ResultSet result) throws SQLException
  {
    if (itemIDs_==null)
      super.initFromResult(result);
    else
    {
      ItemIDList idList=new ItemIDList();
      DbLoadableItemIDList.initFromResult(idList, result);
      retainAll(idList);
    }
  }
}
