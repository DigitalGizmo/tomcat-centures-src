/*
 * Title:        ErrorHandler<p>
 * Description:  provides auto-error handling capabilities including sending email etc.<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ErrorHandler.java,v 1.7 2003/07/29 23:51:29 keith Exp $
 *
 * $Log: ErrorHandler.java,v $
 * Revision 1.7  2003/07/29 23:51:29  keith
 * fixed class cast problem with Throwable/Exception
 *
 * Revision 1.6  2002/04/12 14:08:16  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/05/08 17:53:25  tarmstro
 * changes for different e-mail lists
 *
 * Revision 1.4  2001/03/04 03:31:03  pbrown
 * changes for search pages
 *
 * Revision 1.3  2001/01/11 20:10:45  pbrown
 * allows multiple mail addresses separated by semicolons
 *
 * Revision 1.2  2001/01/05 21:28:31  pbrown
 * finished error handler code
 *
 * Revision 1.1  2001/01/05 14:41:47  pbrown
 * added automatic error handler
 *
 */
package edu.umass.ccbit.util;

import java.io.ByteArrayOutputStream;
import edu.umass.ckc.util.CkcSmtpClient;
import java.util.StringTokenizer;
import java.io.PrintWriter;

public class ErrorHandler
{
  public static final String SmtpServer_="smtp.server";
  public static final String ErrorEmailAddress_="error.email";
  public static final String ErrorEmailAddressBug_="error.emailbug";

  /**
   * send mail to error email address..
   */
  public static boolean sendMail(String smtpServer, String addr, String subject, String msg)
  {
    try
    {
      CkcSmtpClient mailer=new CkcSmtpClient();
      mailer.setSmtpHost(smtpServer);
      StringTokenizer st=new StringTokenizer(addr, ";");
      while (st.hasMoreTokens())
      {
        String tok=st.nextToken();
        mailer.sendMail(tok, tok, subject, msg);
      }
    }
    catch (Exception e)
    {
      return false;
    }
    return true;
  }

  /**
   * formats exception info for mail message
   */
  public static String formatException( Throwable e )
  {
    StringBuffer buf=new StringBuffer();
    if (e != null)
    {
      buf.append(e.getMessage());
      buf.append("\n\n");
      ByteArrayOutputStream ostr=new ByteArrayOutputStream();
      PrintWriter pw=new PrintWriter(ostr);
      e.printStackTrace(pw);
      pw.flush();
      pw.close();
      buf.append(ostr.toString());
    }
    return buf.toString();
  }
}
