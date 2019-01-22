/*
 * Title:        JspUtil<p>
 * Description:  class for jsp utility functions<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: JspUtil.java,v 1.8 2002/04/12 14:08:19 pbrown Exp $
 *
 * $Log: JspUtil.java,v $
 * Revision 1.8  2002/04/12 14:08:19  pbrown
 * fixed copyright info
 *
 * Revision 1.7  2001/02/09 21:08:20  tarmstro
 * added parameter to textarea writer
 *
 * Revision 1.6  2000/11/13 19:50:28  tarmstro
 * added method to create a textarea input field
 *
 * Revision 1.5  2000/10/24 20:12:51  pbrown
 * changed radio button method
 *
 * Revision 1.4  2000/06/27 16:16:02  pbrown
 * changes to collection date (not final yet)
 * new class for reading level
 * other changes/bugifxes
 *
 * Revision 1.3  2000/06/06 14:35:39  pbrown
 * added miscellaneous methods for data entry in forms
 *
 * Revision 1.2  2000/05/24 06:13:46  pbrown
 * reimplemented object dates with y/m/d fields
 *
 * Revision 1.1  2000/05/18 02:21:55  pbrown
 * moved files to util package
 *
 * Revision 1.1  2000/05/05 21:21:33  pbrown
 * rebuilt repository
 *
 * Revision 1.2  2000/05/04 14:13:32  pbrown
 * uses JspInitParams instead of InitParams...added cvs info
 *
 *
 */
package edu.umass.ccbit.util;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.servlet.ServletParams;
import edu.umass.ckc.servlet.ServletParser;
import edu.umass.ckc.util.StringUtils;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;

public class JspUtil
{
  /**
   * write dropdown list
   * @param caption caption for the list
   * @param opt array of values/options to appear on the list
   * @param name the name of the list object in the form
   * @param selected the list item which is the default selection
   * @param out output object passed from the Java server page
   */
  public static void writeDropdown(String caption, String[][] opt,
                                   String name, String selected,
                                   String onChange,
                                   int start, int end, int size,
                                   JspWriter out) throws IOException, ArrayIndexOutOfBoundsException
  {
    out.print("<NOBR>");
    if (caption != null)
      out.print(caption);
    out.print("<SELECT name=" + name + " size=");
    out.print(size);
    if (onChange != null)
    {
      out.print(" onChange='" + onChange + "'");
    }
    out.print(">\n");
    for (int i=start; i < (end > 0 && end < opt.length ? end : opt.length); i++)
    {
      out.print("  <OPTION value=\"");
      out.print(opt[i][0]);
      out.print("\"");
      if (opt[i][0].compareTo(selected)==0)
        out.print(" SELECTED");
      out.print(">");
      if (opt[i].length < 2)
        out.print(opt[i][0]);
      else
        out.print(opt[i][1]);
      out.print("</OPTION>\n");
    }
    out.print("</SELECT></NOBR>\n");
  }

  /**
   * writes dropdown list..supplies defaults for start/end
   */
  public static void writeDropdown(String caption, String[][] opt,
                                   String name, String selected, String onChange, int size,
                               JspWriter out) throws IOException, ArrayIndexOutOfBoundsException
  {
    writeDropdown(caption, opt, name, selected, onChange, 0, -1, size, out);
  }

  /**
   * writes radio button to output
   * @param name name of radio button group in form
   * @param value value of button
   * @param caption caption of button
   * @param checked value to be checked
   * @param vertical if true, arrange buttons vertically
   * @param out JspWriter output object passed from jsp page
   */
  protected static void writeRadioButton(String name, String value, String caption,
                                         String checked, boolean vertical, JspWriter out) throws IOException
  {
    out.print("<INPUT TYPE=RADIO name=" + name + " VALUE=\"" + value + "\"");
    if (value.compareTo(checked)==0)
    {
      out.print(" CHECKED");
    }
    out.print(">");
    if (caption.length() > 0)
    {
      out.print("<NOBR>" + caption + "</NOBR>");
    }
    if (vertical)
      out.print("<BR>");
    out.print("\n");
  }

  /**
   * writes a set of radio buttons to output
   * @param title title for radio button group (optional)
   * @param name name of radio button object in form
   * @param opts array of options
   * @param checked value to be checked
   * @param vertical if true buttons will be arranged vertically
   * @param out JspWriter output object passed by jsp page
   */

  public static void writeRadioButtons(JspWriter out, String title, String name,
                                   String[][] opts, String checked,
                                   boolean vertical, int start, int end) throws IOException, ArrayIndexOutOfBoundsException
  {
    if (title != null)
    {
      out.print(title);
      if (vertical)
        out.print("<BR>");
      out.print("\n");
    }
    for (int i=start; i < (end > 0 && end < opts.length ? end : opts.length); i++)
    {
      String caption = opts[i].length > 1 ? opts[i][1] : opts[i][0];
      writeRadioButton(name, opts[i][0], caption, checked, vertical, out);
    }
  }

  /**
   * write radio buttons...takes int argument as checked value
   */
  public static void writeRadioButtons(JspWriter out, String title, String name,
                                   String[][] opts, int checked,
                                   boolean vertical, int start, int end) throws IOException, ArrayIndexOutOfBoundsException
  {
    writeRadioButtons(out, title, name, opts, String.valueOf(checked), vertical, start, end);
  }

  /**
   * write radio buttons..takes int argument as checked value...supplies default args for start/end
   */
  public static void writeRadioButtons(JspWriter out, String title, String name,
                                   String[][] opts, int checked,
                                   boolean vertical) throws IOException, ArrayIndexOutOfBoundsException
  {
    writeRadioButtons(out, title, name, opts, checked, vertical, 0, -1);
  }

  /**
   * write radio buttons...supplies default args for start/end
   */
  public static void writeRadioButtons(JspWriter out, String title, String name,
                                   String[][] opts, String checked,
                                   boolean vertical) throws IOException, ArrayIndexOutOfBoundsException
  {
    writeRadioButtons(out, title, name, opts, checked, vertical, 0, -1);
  }

  /**
   * add string parameter to a url
   * returns false if parameter was added to url, true otherwise, so that return value can be passed to
   * subsequent calls to add(String|Int|Boolean)param
   */
  public static boolean addStringParam(String param, String value, boolean first, StringBuffer url)
  {
    if (value != null && value.length() > 0)
    {
      HtmlUtils.addToLink(param, value, first, url);
      return false;
    }
    else
      return true;
  }

  /**
   * add int parameter to url
   * returns false if parameter was added to url, true otherwise, so that return value can be passed to
   * subsequent calls to add(String|Int|Boolean)param
   */
  public static boolean addIntParam(String param, int value, int defaultValue, boolean first, StringBuffer url)
  {
    if (value != defaultValue)
    {
      HtmlUtils.addToLink(param, value, first, url);
      return false;
    }
    else
      return true;
  }

  /**
   * add boolean param to url
   * returns false if parameter was added to url, true otherwise, so that return value can be passed to
   * subsequent calls to add(String|Int|Boolean)param
   */
  public static boolean addBooleanParam(String param, boolean value, boolean defaultValue, boolean first, StringBuffer url)
  {
    if (value != defaultValue)
    {
      HtmlUtils.addToLink(param, value ? 1 : 0, first, url);
      return false;
    }
    else
      return true;
  }

  /**
   * writeTextInput
   */
  public static void writeTextInput(JspWriter out, String name, String value, int length, int maxlength) throws IOException
  {
    out.print("<input type=text");
    if (name !=null)
    {
      out.print(" name=\"");
      out.print(name);
      out.print("\"");
    }
    if (value != null)
    {
      out.print(" value=\"");
      out.print(value);
      out.print("\"");
    }
    if (length > 0)
    {
      out.print(" size=");
      out.print(length);
    }
    if (maxlength > 0)
    {
      out.print(" maxlength=");
      out.print(maxlength);
    }
    out.print(">\n");
  }

  /**
   * write checkbox
   */
  public static void writeCheckbox(JspWriter out, String name, boolean checked) throws IOException
  {
    out.print("<input type=checkbox");
    if (name !=null)
    {
      out.print(" name=\"");
      out.print(name);
      out.print("\"");
    }
    if (checked)
    {
      out.print(" checked");
    }
    out.print(">\n");
  }

  /**
   * write textbox
   * @param name name of textarea
   * @param rows number of rows
   * @param cols number of columns
   * @param text initial text
   * @param wrap wrap type (soft, hard, off)
   */
  public static void writeTextArea(JspWriter out, String name, int rows, int cols, String text, String wrap)
   throws IOException
  {
    out.print("<textarea");
    if(name != null)
    {
      out.print(" name=\"");
      out.print(name);
      out.print("\"");
    }
    out.print(" rows=");
    out.print(rows);
    out.print(" cols=");
    out.print(cols);
    out.print(" wrap=");
    out.print(wrap);
    out.print(">");
    if(text != null)
    {
      out.print(text);
    }
    out.print("</textarea>");
  }

  /**
   * format text in data entry field
   */
  public static String formatEntryText(String str)
  {
    String newstr=new String(str);
    newstr=StringUtils.substitute(newstr, "\"", "&quot;");
    return newstr;
  }
}
