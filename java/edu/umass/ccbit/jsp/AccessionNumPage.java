package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.runtime.*;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ccbit.database.AccessionNumbers;
import edu.umass.ccbit.jsp.ItemPage;

/**
 * Title:        AccessionNumPage<p>
 * Description:  base class for jsp page to display all accession numbers for items that are 'ready'<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */

import edu.umass.ccbit.database.AccessionNumbers;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public abstract class AccessionNumPage extends HttpDbJspBase
{
  AccessionNumbers nums_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      nums_ = new AccessionNumbers();
      nums_.load(connection_, getServletConfig().getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      System.out.println("WARNING: failed to load list of accession numbers");
    }
  }

  protected void load(HttpSession session) throws SQLException

  {
  }

  protected String accessionNumber(int i)
  {
    return nums_.getAccessionNumber(i);
  }

  protected String itemName(int i)
  {
    return nums_.getItemName(i);
  }

  protected int itemID(int i)
  {
    return nums_.getItemID(i);
  }

  protected String getLink(int i)
  {
    return ItemPage.link(itemID(i), 0);
  }

  protected String accnumLink(int i)
  {
    return HtmlUtils.anchor(getLink(i), accessionNumber(i));
  }

  protected int numItems()
  {
    return nums_.getCount();
  }

}
