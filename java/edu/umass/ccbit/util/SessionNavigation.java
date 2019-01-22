/**
 * Title:        SessionNavigation<p>
 * Description:  keeps track of user navigation during session for bug reporting<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: SessionNavigation.java,v 1.2 2002/04/12 14:08:22 pbrown Exp $
 *
 * $Log: SessionNavigation.java,v $
 * Revision 1.2  2002/04/12 14:08:22  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2001/01/05 14:41:47  pbrown
 * added automatic error handler
 *
 */
package edu.umass.ccbit.util;

import javax.servlet.http.HttpSession;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class SessionNavigation 
{
  /**
   * maximum length of vector
   */
  protected static final int maxlength=10;
  
  /**
   * key name for session object..returns the name of the class
   */
  protected static String sessionKey()
  {
    return "SessionNavigation";
  }
  
  /**
   * get vector of navigation items from session, if they
   * exist..otherwise create a new (empty) vector, save it
   * in the session, and return it
   */
  protected static Vector getFromSession(HttpSession session)
  {
    Vector v=(Vector) JspSession.load(session, sessionKey());
    if (v==null)
    {
      v=new Vector();
      JspSession.save(session, sessionKey(), v);
    }
    return v;
  }

  /**
   * add an item to the list
   */
  public static void addItem(HttpSession session, String item)
  {
    Vector v=getFromSession(session);
    v.addElement(item);
    if (v.size() > maxlength)
    {
      // remove top element if size exceeds max
      v.remove(0);
    }
    JspSession.save(session, sessionKey(), v);
  }

  /**
   * format session history for inclusion in error email
   */
  public static String formatNavigation(HttpServletRequest request)
  {
    StringBuffer buf=new StringBuffer();
    Vector v=getFromSession(request.getSession());
    Enumeration e=v.elements();
    while (e.hasMoreElements())
    {
      buf.append((String) e.nextElement());
      buf.append("\n");
    }
    return buf.toString();
  }
}

