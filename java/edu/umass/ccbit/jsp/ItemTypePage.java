package edu.umass.ccbit.jsp;

/**
 * Title:        ItemTypePage<p>
 * Description:  base class for a listing of item type page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */

import edu.umass.ccbit.database.ItemTypes;
import edu.umass.ccbit.database.Kinds;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ccbit.util.AdvancedSearchParameters;
import edu.umass.ckc.html.HtmlUtils;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public abstract class ItemTypePage extends HttpDbJspBase
{
  Kinds kinds_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      kinds_ = new Kinds();
      kinds_.load(connection_, getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      System.out.println("WARNING: unable to load list of subcategories from database");
    }
  }

  protected void load(HttpSession session) throws SQLException
  {
  }

  protected String getItemType(int i)
  {
    String link=HtmlUtils.addToLink("results.jsp?",
                HtmlUtils.linkParameter(AdvancedSearchParameters.Kind_, kinds_.getKind(i)));
    // a blank search text parameter triggers a new search
    link=HtmlUtils.addToLink(link, SearchParameters.SearchText_, "");
    return HtmlUtils.anchor(link, kinds_.getKind(i));
  }

  protected int numItems()
  {
    return kinds_.numItems();
  }
}
