package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.jasper.runtime.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @version 1.0
 */

public abstract class SearchErrorPage extends ErrorPage
{
  /**
   * initialize
   */
  public void jspInit()
  {
    super.jspInit();
  }

  /**
   * parse the request parameters
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException
  {
    remoteHost=request.getRemoteHost();
  }

  /**
   * the message to display to the user
   */
  protected String exceptionMessage(Throwable e)
  {
    return e.getMessage();
  }
}
