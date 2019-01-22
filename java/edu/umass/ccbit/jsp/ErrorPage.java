/*
 * Title:        ErrorPage<p>
 * Description:  base class for jsp error page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author 
 * @version $Id: ErrorPage.java,v 1.11 2003/12/03 21:01:09 keith Exp $
 *
 * $Log: ErrorPage.java,v $
 * Revision 1.11  2003/12/03 21:01:09  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.10  2003/07/29 23:52:02  keith
 * fixed class cast problem with Throwable/Exception
 *
 * Revision 1.9  2003/04/01 01:49:29  keith
 * added Host: to bug report
 *
 * Revision 1.8  2002/04/12 14:07:51  pbrown
 * fixed copyright info
 *
 * Revision 1.7  2001/09/28 15:05:42  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.6  2001/05/08 17:53:08  tarmstro
 * changes for different e-mail lists
 *
 * Revision 1.5  2001/02/09 22:51:49  pbrown
 * added method to dump stack...for developers only
 *
 * Revision 1.4  2001/01/11 20:10:06  pbrown
 * added useragent and remotehost
 *
 * Revision 1.3  2001/01/09 20:38:48  pbrown
 * added more form fields
 *
 * Revision 1.2  2001/01/05 21:28:30  pbrown
 * finished error handler code
 *
 * Revision 1.1  2001/01/05 14:41:46  pbrown
 * added automatic error handler
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.servlet.ServletParams;
import edu.umass.ccbit.util.ReadingLevel;
import edu.umass.ccbit.util.SessionNavigation;
import javax.servlet.http.HttpServletRequest;
import edu.umass.ccbit.util.ErrorHandler;
import java.lang.Throwable;
import java.io.IOException;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;

public abstract class ErrorPage extends HttpDbJspBase
{
  protected String smtpServer;
  protected String addr;
  protected String feedback;
  protected String username;
  protected String useremail;
  protected String category;
  protected String readinglevel;
  protected String userAgent;
  protected String remoteHost;
  protected int categoryID;

  public static final String comment_ = "General Comment";
  public static final String problem_ = "Bug Report";
  public static final int Comment_ = 0;
  public static final int Problem_ = 1;

  /**
   * initialize
   */
  public void jspInit()
  {
    super.jspInit();
    smtpServer=initParams_.getString(ErrorHandler.SmtpServer_, "");
  }

  /**
   * get servlet request parameters into ServletParams object
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    feedback     = servletParams_.getString("feedback", "");
    username     = servletParams_.getString("username", "");
    useremail    = servletParams_.getString("useremail", "");
    categoryID   = servletParams_.getInt("category", 0);
    readinglevel = ReadingLevel.getFromSession(request.getSession());
    remoteHost   = request.getRemoteHost();
    userAgent    = request.getHeader("User-Agent");
    if(categoryID==Comment_)
    {
      addr=initParams_.getString(ErrorHandler.ErrorEmailAddress_, "");
      category=comment_;
    }
    else
    {
      addr=initParams_.getString(ErrorHandler.ErrorEmailAddressBug_, "");
      category=problem_;
    }
  }

  /**
   * override to prevent navigation item from being added to session for this page
   */
  protected void addNavigationItem(HttpServletRequest request, ServletParams params)
  {
  }

  /**
   * submit feedback
   */
  protected boolean submitFeedback(HttpServletRequest request)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("PVMA web site user feedback:\n\n");
    buf.append("User name: ").append(username).append("\n");
    buf.append("User email address: ").append(useremail).append("\n");
    buf.append("Reading level: ").append(readinglevel).append("\n");
    buf.append("Remote host: ").append(remoteHost).append("\n");
    buf.append("Host: ").append(mrSidRoot).append("\n");
    buf.append("User agent: ").append(userAgent).append("\n");
    buf.append("User comments: \n\n");
    buf.append(feedback);
    buf.append("\n\n");
    buf.append("Session navigation history (last 10 items in chronological order):\n\n");
    buf.append(SessionNavigation.formatNavigation(request));

    String subject = "PVMA User Feedback: " + category;
    System.out.println( smtpServer );
    System.out.println( addr );
    System.out.println( subject );
    System.out.println( buf.toString() );
    return ErrorHandler.sendMail(smtpServer, addr, subject, buf.toString());
  }

  /**
   * format exception
   */
  public String formatException( Throwable e ) {
    return ErrorHandler.formatException( e );
  }

  /**
   * send the mail
   */
  protected boolean sendErrorMail( HttpServletRequest request, Throwable e ) {
    StringBuffer buf=new StringBuffer();

    buf.append("An error has occurred in the PVMA application\n\n");
    buf.append("Remote host: ").append(remoteHost).append("\n");
    buf.append("User agent: ").append(userAgent).append("\n\n");
    buf.append(ErrorHandler.formatException( e ));
    buf.append("\n\n");
    buf.append("Session navigation history (last 10 items in chronological order):\n\n");
    buf.append(SessionNavigation.formatNavigation(request));

    String subject = "PVMA error: ";
    subject += e.getMessage();

    return ErrorHandler.sendMail(smtpServer, addr, subject, buf.toString());
  }
}
