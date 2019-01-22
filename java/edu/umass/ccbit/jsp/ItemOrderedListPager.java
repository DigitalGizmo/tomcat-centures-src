
/**
 * Title:        ItemOrderedListPager<p>
 * Description:  allows for ordering of the results by result attributes<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.MainCollectionOrderedItemsPager;
import edu.umass.ccbit.database.DbLoadableItemIDList;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public abstract class ItemOrderedListPager extends ItemListPager
{
  /**
   * load
   */
  protected void load(HttpSession session, DbLoadableItemIDList itemList) throws SQLException
  {
    pager_=new MainCollectionOrderedItemsPager();
    pager_.load(connection_, session, servletParams_, itemList);
  }

  public int[] orderByYear(int index)
  {
    return pager_.getDateInfo(index);
  }
}
