/**
 * Title:        AgeLevel<p>
 * Description:  age levels for site<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ReadingLevel.java,v 1.4 2002/04/12 14:08:20 pbrown Exp $
 *
 * $Log: ReadingLevel.java,v $
 * Revision 1.4  2002/04/12 14:08:20  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2001/01/18 16:04:43  tarmstro
 * fixed image issue
 *
 * Revision 1.2  2000/07/25 13:31:13  tarmstro
 * changed mappings and default level
 *
 * Revision 1.1  2000/06/27 16:16:02  pbrown
 * changes to collection date (not final yet)
 * new class for reading level
 * other changes/bugifxes
 *
 */
package edu.umass.ccbit.util;

import edu.umass.ckc.servlet.InitParams;
import javax.servlet.http.HttpSession;
import java.lang.StringBuffer;

public class ReadingLevel
{
  public static final String Level_="readinglevel";
  public static final String defaultReadingLevel_="advanced";
  public static final String ages_[]={"beginner","intermediate","advanced"};
  protected static final String sessionAttributeName_="reading_level";

  static final String[][] levels_ =
  {
    {"elementary", "beginner"},
    {"middle", "intermediate"},
    {"highschool", "advanced"},
    {"public", "advanced"},
    {"teacher", "advanced"},
    {"researcher", "advanced"}
  };

  /**
   * 0..2
   *    'elementary'        0 (beginner)
   *    'middle'            1 (intermediate)
   *    'highschool'        2 (advanced) this is the default
   *    'public'            2 (advanced)
   *    'teacher'           2 (advanced)
   *    'researcher'        2 (advanced)
   */
  protected static String getLevel(String levelDescription)
  {
    for (int i=0; i < levels_.length; i++)
    {
      if (levelDescription.compareToIgnoreCase(levels_[i][0])==0)
        return levels_[i][1];
    }
    return defaultReadingLevel_;
  }

  /**
   * save reading level in parameters in session
   */
  public synchronized static void saveInSession(HttpSession session, String levelDescription)
  {
    JspSession.save(session, sessionAttributeName_, getLevel(levelDescription));
  }

  /**
   * get reading level from session
   */
  public synchronized static String getFromSession(HttpSession session)
  {
    String level=(String) JspSession.load(session, sessionAttributeName_);
    if (level != null)
      return level;
    else
      return defaultReadingLevel_;
  }

  /**
   * image for reading level
   */
  public static String image(String level, String selectedLevel)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<IMG SRC=\"");
    buf.append("images/").append(level);
    buf.append(level.compareToIgnoreCase(selectedLevel)==0 ? "_on" : "");
    buf.append(".gif");
    buf.append("\" border=0>");
    return buf.toString();
  }
}
