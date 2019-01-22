
/**
 * Title:        ChronologyParameters<p>
 * Description:  class for chronology parameters<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.util;

import edu.umass.ckc.util.Parameters;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;

public class ChronologyParameters extends Parameters
{
  /**
   * we only need four parameters:
   *  use which chronology (chronologyid)
   *  use mycollection (yes/no)
   *  upper year (yyyy)
   *  lower year (yyyy)
   */

  public static final String WhichChronology_="selected_chronology";
  public static final String UseCollection_="use_collection";
  // for trial purposes only...might not be implemented
  public static final String UpperYear_="upper_year";
  public static final String LowerYear_="lower_year";

  // session attribute
  protected static final String SessionAttribute_="chronology_parameters";

  public ChronologyParameters()
  {
    put(WhichChronology_, "0");
    put(UseCollection_, "none");
    put(UpperYear_, "0");
    put(LowerYear_, "0");
  }

  /**
   * get parameters
   * (from SearchParameters)
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

  /**
   * get the id of the chronology that was selected
   */
  public int getChronology()
  {
    return getInt(WhichChronology_, 0);
  }

  /**
   * get the boolean to see if the collection will be included
   */
  public boolean getUseCollection()
  {
    return getString(UseCollection_, "0").compareTo("on")==0;
  }

  /**
   * Get the high range year (for use with collection items that would not be relevant).
   * This seems bad.
   */
  public int getUpperYear()
  {
    return getInt(UpperYear_, 0);
  }

  /**
   * Get the low range year (for use with collection items that would not be relevant).
   * This seems bad.
   */
  public int getLowerYear()
  {
    return getInt(LowerYear_, 0);
  }

  /**
   * load from session
   * (from SearchParameters)
   */
  public synchronized static ChronologyParameters loadFromSession(HttpSession session)
  {
    ChronologyParameters param=(ChronologyParameters) JspSession.load(session, SessionAttribute_);
    // if not found in session, return a new (empty) instance
    if (param==null)
      param=new ChronologyParameters();
    return param;
  }
}
