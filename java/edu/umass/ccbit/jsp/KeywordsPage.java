package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.runtime.*;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ccbit.util.SearchParameters;

/**
 * Title:        KeywordsPage<p>
 * Description:  base class for jsp page to display all 'active' keywords<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */

import edu.umass.ccbit.database.Keywords;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public abstract class KeywordsPage extends HttpDbJspBase
{
  Keywords words_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      words_ = new Keywords();
      words_.load(connection_, getServletConfig().getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      System.out.println("WARNING: Unable to load list of keywords from database");
    }
  }

  protected void load(HttpSession session) throws SQLException
  {
  }

  protected String getKeyword(int i)
  {
   String link=HtmlUtils.addToLink("results.jsp?",
                HtmlUtils.linkParameter(SearchParameters.KeyWord_, words_.getKeywordID(i)));
    // a blank search text parameter triggers a new search
    link=HtmlUtils.addToLink(link, SearchParameters.SearchText_, "");    
    return HtmlUtils.anchor(link, words_.getKeyword(i));
  }

  protected int numItems()
  {
    return words_.numItems();
  }

}
