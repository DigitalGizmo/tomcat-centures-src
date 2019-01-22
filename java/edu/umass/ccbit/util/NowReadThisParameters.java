
/**
 * Title:        NowReadThisParameters<p>
 * Description:  class for textarea input<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.util;

import edu.umass.ckc.util.Parameters;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

public class NowReadThisParameters extends Parameters
{
  // defaults
  public static final String TextArea_ = "textinput";
  public static final String SessionAttribute_ = "nowreadthisparameters";

  public NowReadThisParameters()
  {
    put(TextArea_, "");
  }

  /**
   * get entered text
   */
  public String getEnteredText()
  {
    return getString(TextArea_, "");
  }

  /**
   * save to session
   */
  public synchronized void saveToSession(HttpSession session)
  {
    JspSession.save(session, SessionAttribute_, this);
  }

  /**
   * load from session
   */
  public synchronized static NowReadThisParameters loadFromSession(HttpSession session)
  {
    NowReadThisParameters param=(NowReadThisParameters) JspSession.load(session, SessionAttribute_);
    // if not found in session, return a new (empty) instance
    if (param==null)
      param=new NowReadThisParameters();
    return param;
  }

  /**
   * get parameters
   */
  public void getParameters(Parameters param)
  {
    Enumeration keys=keys();
    while (keys.hasMoreElements())
    {
      String keyName=(String) keys.nextElement();
      if (param.containsKey(keyName))
        put(keyName, param.get(keyName));
    }
  }
}
