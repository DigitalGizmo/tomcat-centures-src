
/**
 * Title:        ItemOrderedListList<p>
 * Description:  allows for a list rather than paginated form of ItemOrderedListPager<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.DbLoadableItemIDList;
import edu.umass.ccbit.database.MainCollectionOrderedItemsList;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class ItemOrderedListList extends ItemOrderedListPager
{
  /**
   * load
   */
  protected void load(HttpSession session, DbLoadableItemIDList itemList) throws SQLException
  {
    pager_=new MainCollectionOrderedItemsList();
    pager_.load(connection_, session, servletParams_, itemList);
  }
}
