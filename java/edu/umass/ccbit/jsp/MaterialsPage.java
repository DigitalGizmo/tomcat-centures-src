package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.umass.ckc.html.HtmlUtils;
import org.apache.jasper.runtime.*;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ccbit.util.AdvancedSearchParameters;

/**
 * Title:        MaterialsPage<p>
 * Description:  base class for jsp containing active materials<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */

import edu.umass.ccbit.database.Materials;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public abstract class MaterialsPage extends HttpDbJspBase
{
  Materials mat_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      mat_ = new Materials();
      mat_.load(connection_, getServletConfig().getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      System.out.println("WARNING: unable to load list of materials from database");
    }
  }

  protected void load(HttpSession session) throws SQLException
  {
  }

  protected String getMaterial(int i)
  {
    String link=HtmlUtils.addToLink("results.jsp?",
                HtmlUtils.linkParameter(AdvancedSearchParameters.Material_, mat_.getMaterialID(i)));
    // a blank search text parameter triggers a new search
    link=HtmlUtils.addToLink(link, SearchParameters.SearchText_, "");    
    return HtmlUtils.anchor(link, mat_.getMaterialName(i));
  }

  protected int numItems()
  {
    return mat_.numItems();
  }
}
