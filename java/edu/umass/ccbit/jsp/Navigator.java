/**
 * Title:        Navigator<p>
 * Description:  provides methods for displaying user's current location in site w/links<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: Navigator.java,v 1.2 2002/04/12 14:08:03 pbrown Exp $
 *
 * $Log: Navigator.java,v $
 * Revision 1.2  2002/04/12 14:08:03  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/12 14:21:55  pbrown
 * class for creating site navigation links at top of page
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.html.HtmlUtils;
import java.util.Vector;
import java.util.Enumeration;

public class Navigator
{
  protected Vector items_;
  public static final String linkSeparator_=" &gt; ";

  /**
   * constructor
   */
  public Navigator()
  {
    items_=new Vector();
  }

  /**
   * add link
   */
  public void add(String link)
  {
    items_.add(link);
  }

  /**
   * add link (url/link text)
   */
  public void add(String url, String linkText)
  {
    add(HtmlUtils.anchor(url, linkText));
  }

  /**
   * display navigation items
   */
  public String toString()
  {
    StringBuffer buf=new StringBuffer();
    Enumeration e=items_.elements();
    while (e.hasMoreElements())
    {
      buf.append(linkSeparator_);
      buf.append(e.nextElement());
    }
    return buf.toString();
  }
}
