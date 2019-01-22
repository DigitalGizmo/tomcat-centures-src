/**
 * Title:        WhoAreYou<p>
 * Description:  servlet which sets age level in session and sets location to home page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: WhoAreYou.java,v 1.4 2002/04/12 14:08:14 pbrown Exp $
 *
 * $Log: WhoAreYou.java,v $
 * Revision 1.4  2002/04/12 14:08:14  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2000/08/10 16:49:34  pbrown
 * uses redirect method to send correct headers (didn't work on IIS)
 *
 * Revision 1.2  2000/07/25 13:27:53  tarmstro
 * changed object written to session to reflect form selection
 *
 * Revision 1.1  2000/06/27 16:16:32  pbrown
 * servlet for reading level mechanism
 *
 */
package edu.umass.ccbit.servlet;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import edu.umass.ckc.servlet.InitParams;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.ReadingLevel;

public class WhoAreYou extends HttpServlet
{
  protected static final String HomeURL_="home.url";
  protected String homeURL_;

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    HttpSession session = request.getSession();
    InitParams initParams=new InitParams();
    initParams.storeParameters(getServletConfig());
    ReadingLevel.saveInSession(session, request.getParameter("readinglevel"));
    homeURL_=getInitParameter(HomeURL_);
    response.sendRedirect(homeURL_);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    doGet(request, response);
  }
}
