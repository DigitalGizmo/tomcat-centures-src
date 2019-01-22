
/**
 * Title:        glossary<p>
 * Description:  base class for glossary of terms<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.StringUtils;
import edu.umass.ckc.html.HtmlUtils;
import java.util.Enumeration;
import java.util.Vector;
import java.lang.StringBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

public class Glossary extends DbLoadableItemList
{
  public static final String Glossary_="glossary";

  protected class GlossaryItems implements InstanceFromResult
  {
    public String term_;
    public String definition_;
    public int glossaryID_;

    public void init(ResultSet result) throws SQLException
    {
      term_=result.getString("Term");
      definition_=result.getString("Definition");
      glossaryID_=result.getInt("GlossaryID");
    }
  }

  /**
   * get a specific term
   */
  public String getTerm(int n)
  {
    return ((GlossaryItems) items_.elementAt(n)).term_;
  }

  /**
   * instance from resultset
   * @param result the result set generated from the query
   * @return GlossaryItems
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    GlossaryItems glossary = new GlossaryItems();
    glossary.init(result);
    return glossary;
  }

  /**
   * get the definition of a term
   * @param key the term to find the definition for
   * @return the definition
   */
  public String getDefinition(String key)
  {
    Enumeration e = items_.elements();
    GlossaryItems item;
    while(e.hasMoreElements())
    {
      item=(GlossaryItems) e.nextElement();
      if(item.term_.compareToIgnoreCase(key)==0)
        return item.definition_;
    }
    return "";
  }

  /**
   * search through the string and replace the text...
   * verifies that the found word is not a substring
   * @param source the string you are parsing through looking for glossary terms
   * @param term the glossary item you are looking for in the source string
   * @return parsed and replaced string
   */
  public static String searchString(String source, GlossaryItems term)
   throws IndexOutOfBoundsException
  {
    int size = term.term_.length();
    int index = 0;
    // unallowed proximity characters
    String errors = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890'";
    String previous="";
    String next="";
    String add;
    boolean found=false;
    StringBuffer buf = new StringBuffer();
    if(source == null || term.term_ == null)
    {
      return null;
    }
    int start = source.indexOf(term.term_, index);
    while(start >= 0)
    {
      if (index != start)
      {
        buf.append(source.substring(index, start));
      }
      try
      {
        previous = new String((new Character(source.charAt(start-1))).toString());
      }
      catch(Exception e)
      {
        previous = " ";
      }
      try
      {
        next = new String((new Character(source.charAt(start+size))).toString());
      }
      catch(Exception e)
      {
        next = " ";
      }
      if(errors.indexOf(previous)==(-1) && errors.indexOf(next)==(-1))
      {
        add = glossaryLink(term);
        buf.append(add);
        found=true;
      }
      else
        buf.append(term.term_);
      index = start + size;
      start = source.indexOf(term.term_, index);
      if(found)
        break;
    }
    buf.append(source.substring(index));
    return buf.toString();
  }

  /**
   * check string for glossary terms and replace the term with glossary link
   * @param text the term to find in the glossary
   * @return parsed and replaced string
   */
  public String glossaryParse(String text)
  {
    GlossaryItems term;
    Enumeration e;
    if(items_!=null)
      e = items_.elements();
    else
      e = (new Vector()).elements();
    while(e.hasMoreElements())
    {
      term = (GlossaryItems) e.nextElement();
      text = searchString(text, term);
    }
    return text;
  }

  /**
   * link from a glossary term in a string
   * when selected pops up an alert box with the definition of the term
   * todo: hover definition, formatting, etc...
   * note: if definition contains glossary term when parsing for that term
   * it is found and nests the anchor tags and quotes....very messy
   * @param term the glossary term to construct the link for
   * @return url with javascript to open glossary window
   */
  public static String glossaryLink(GlossaryItems term)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<A HREF=\"javascript:popupWindow('define.jsp?glossaryID=").append(term.glossaryID_);
    buf.append("','popup','menubar=yes,scrollbars=yes,resizable=yes,height=300,width=300')\">").append(term.term_).append("</A>");
    return buf.toString();
  }

  public int numItems()
  {
    return items_.size();
  }

  /**
   * load the glossary from the db
   * @param conn the database connection
   * @param context the servlet context
   */
  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT * from Glossary where definition is not null ORDER BY Term");
  }
}
