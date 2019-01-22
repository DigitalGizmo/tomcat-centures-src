package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.runtime.*;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ccbit.database.TurnsMapItems;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

/**
 * Title:        TurnsMapPage<p>
 * Description:  base class for turns map page<p>
 * Copyright:    Copyright (c) 2000-2003<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version 1.0
 */

public abstract class TurnsMapPage extends HttpDbJspBase
{
  public TurnsMapItems turnsMap;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      turnsMap = new TurnsMapItems();
      turnsMap.load(connection_, getServletConfig().getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      System.out.println("WARNING: failed to load turns map items");
    }
  }
}
