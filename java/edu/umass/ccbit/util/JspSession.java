/**
 * Title:        JspSession<p>
 * Description:  static methods for accessing values in session<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: JspSession.java,v 1.7 2003/12/03 21:01:16 keith Exp $
 *
 * $Log: JspSession.java,v $
 * Revision 1.7  2003/12/03 21:01:16  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.6  2002/04/12 14:08:18  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/01/11 19:15:03  tarmstro
 * added mode for advanced search
 *
 * Revision 1.4  2000/06/27 16:16:01  pbrown
 * changes to collection date (not final yet)
 * new class for reading level
 * other changes/bugifxes
 *
 * Revision 1.3  2000/06/12 14:22:58  pbrown
 * added item page view mode methods
 *
 * Revision 1.2  2000/06/06 14:35:10  pbrown
 * added methods to save objects to session
 *
 * Revision 1.1  2000/06/02 21:52:57  pbrown
 * many changes for searching..more to come
 *
 *
 */
package edu.umass.ccbit.util;

import javax.servlet.http.HttpSession;
import java.lang.Integer;
import edu.umass.ckc.util.Parameters;

public class JspSession
{
  private static final String ItemPageViewMode_="item_page_view_mode";

  public interface itemPageViewModes
  {
    public static final int Item_=0;
    public static final int Search_=1;
    public static final int Browse_=2;
    public static final int ScavengerHunt_=3;
    public static final int AdvSearch_=4;
  }

  /**
   * set view mode for item pages
   */
  public synchronized static void setItemPageViewMode(HttpSession session, int mode)
  {
    setInt(session, ItemPageViewMode_, mode);
  }

  /**
   * get view mode for item pages
   */
  public synchronized static int getItemPageViewMode(HttpSession session)
  {
    return getInt(session, ItemPageViewMode_, itemPageViewModes.Item_);
  }

  /**
   * set integer value
   */
  public synchronized static void setInt(HttpSession session, String name, int value)
  {
    save(session, name, new Integer(value));
  }

  /**
   * get integer value or default
   */
  public synchronized static int getInt(HttpSession session, String name, int defaultValue)
  {
    Integer ival=(Integer) session.getAttribute(name);
    return ival !=null ? ival.intValue() : defaultValue;
  }

  /**
   * save object
   * @param session http session object
   * @param name name of attribute under which parameters will be saved
   * @param obj object to be saved
   */
  public synchronized static void save(HttpSession session, String name, Object obj)
  {
    if (name == null || session == null) {
      return;
    } else if (obj != null) {
      session.setAttribute(name, obj);
    } else {
      session.removeAttribute(name);
    }
  }

  /**
   * load object from session
   * @param session http session object
   * @param name name of attribute under which parameters were saved
   * @param obj object to be loaded
   */
  public synchronized static Object load(HttpSession session, String name)
  {
    try
    {
      return session.getAttribute(name);
    }
    catch (Exception e)
    {
      return null;
    }
  }
}



