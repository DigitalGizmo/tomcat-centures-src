/*
 * Title:        JspInitParams<p>
 * Description:  extends init params to Java2 (gets servlet context params)<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author 
 * @version $Id: JspInitParams.java,v 1.2 2002/04/12 14:08:18 pbrown Exp $
 *
 * $Log: JspInitParams.java,v $
 * Revision 1.2  2002/04/12 14:08:18  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/05/18 02:21:55  pbrown
 * moved files to util package
 *
 * Revision 1.1  2000/05/05 21:21:33  pbrown
 * rebuilt repository
 *
 * Revision 1.1  2000/05/04 14:16:29  pbrown
 * derived class of InitParams...reads init params from servlet context
 *
 *
 */
package edu.umass.ccbit.util;

import edu.umass.ckc.servlet.InitParams;
import java.util.Enumeration;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

public class JspInitParams extends edu.umass.ckc.servlet.InitParams
{
  public void storeParameters(ServletConfig config)
  {
    super.storeParameters(config);
    /* get servlet context initialization parameters */
    ServletContext context=config.getServletContext();
    Enumeration e = context.getInitParameterNames();
    while (e.hasMoreElements())
    {
      String key = (String)e.nextElement();
      put(key, context.getInitParameter(key));
    }
  }
}
