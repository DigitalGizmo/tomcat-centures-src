/**
 * Title:        CuratorialInfoPage<p>
 * Description:  base class for curatorial info popup<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CuratorialInfoPage.java,v 1.3 2002/04/12 14:07:50 pbrown Exp $
 * $ Log: $
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.CuratorialInfo;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.io.IOException;
import java.lang.Exception;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public abstract class CuratorialInfoPage extends HttpDbJspBase
{
  protected int itemID_;
  protected CuratorialInfo info_;
  public static final String ItemID_="itemid";

  /**
   * get servlet request parameters into ServletParams object
   * this calls parseRequestParameters in superclass, then sets data members associated with
   * servlet parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    itemID_=servletParams_.getInt(ItemID_);
  }

  /**
   * load data for item page
   */
  protected void load(HttpSession session) throws CkcException, Exception
  {
    info_=new CuratorialInfo();
    info_.load(connection_, itemID_);
  }

  /**
   * item name
   */
  protected String itemName()
  {
    return info_ != null ? info_.itemName_ : "Not found";
  }

  /**
   * number of attributes for item
   */
  protected int numAttributes()
  {
    return info_ != null ? info_.numAttributes() : 0;
  }

  /**
   * attribute name
   */
  protected String attributeName(int id)
  {
    return info_ != null ? info_.attributeName(id) : null;
  }

  /**
   * attribute value
   */
  protected String attributeValue(int id)
  {
    return info_ != null ? info_.attributeValue(id) : null;
  }
}

