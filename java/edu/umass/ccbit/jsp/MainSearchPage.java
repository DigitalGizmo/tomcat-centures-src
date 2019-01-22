/**
 * Title:        MainSearchPage<p>
 * Description:  base class for main search jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: MainSearchPage.java,v 1.13 2002/04/12 14:08:00 pbrown Exp $
 *
 * $Log: MainSearchPage.java,v $
 * Revision 1.13  2002/04/12 14:08:00  pbrown
 * fixed copyright info
 *
 * Revision 1.12  2001/07/25 20:50:22  tarmstro
 * many changes for lexicon/searching/priority
 *
 * Revision 1.11  2001/07/24 20:45:11  tarmstro
 * changes for new dropdown lists
 *
 * Revision 1.10  2001/03/08 15:42:45  pbrown
 * removed unused methods, calls
 *
 * Revision 1.9  2001/01/19 19:34:06  tarmstro
 * added method to deal with variable length textboxes
 *
 * Revision 1.8  2001/01/18 18:33:30  tarmstro
 * removed checkbox method
 *
 * Revision 1.7  2000/11/06 21:45:54  tarmstro
 * commented out obsolete, error causing methods
 *
 * Revision 1.6  2000/11/02 17:38:28  pbrown
 * added clear button for search page
 *
 * Revision 1.5  2000/10/24 19:36:21  pbrown
 * changed thumbnail option from radio buttons to checkbox
 *
 * Revision 1.4  2000/07/31 14:45:01  tarmstro
 * added glossary loading and changed date search display
 *
 * Revision 1.3  2000/06/27 16:14:40  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.2  2000/06/06 14:36:59  pbrown
 * added code allowing return to main search page with previous search parameters,
 * and added data entry methods for use in jsp
 *
 * Revision 1.1  2000/06/02 21:52:55  pbrown
 * many changes for searching..more to come
 *
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.ItemTypes;
import edu.umass.ccbit.database.Keywords;
import edu.umass.ccbit.database.Lexicon;
import edu.umass.ccbit.database.TableRanks;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.SearchParameters;
import java.io.IOException;
import java.lang.Integer;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

public abstract class MainSearchPage extends HttpDbJspBase
{
  SearchParameters searchParams_;
  // request params
  public static final String RefineSearch_="redo";
  // request values
  protected boolean refineSearch_;
  public static TableRanks tableRanks_;
  // Lexicon
  public static Lexicon lexicon_;

  protected static final String months_[][]=
  {
    {"mm", "mm"},
    {"0", "January"},
    {"1", "February"},
    {"2", "March"},
    {"3", "April"},
    {"4", "May"},
    {"5", "June"},
    {"6", "July"},
    {"7", "August"},
    {"8", "September"},
    {"9", "October"},
    {"10", "November"},
    {"11", "December"}
  };

  public void load(HttpSession session) throws SQLException
  {
    refineSearch_=servletParams_.containsKey(RefineSearch_);
    if (refineSearch_)
    {
      searchParams_=SearchParameters.loadFromSession(session);
    }
    else
    {
      searchParams_=new SearchParameters();
    }
  }

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      getLexicon(connection_);
      tableRanks_ = new TableRanks();
      tableRanks_.load(connection_, getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      // oopsy...
      System.out.println("WARNING: Unable to load data needed for search pages!");
    }
  }

  /**
   * load the lexicon
   */
  private void getLexicon(Connection conn) throws SQLException
  {
    if(lexicon_==null)
    {
      lexicon_ = new Lexicon();
      lexicon_.load(conn);
    }
  }

  /**
   * form integer value with alternate string text if zero
   */
  private String integerFormValue(int value, String text)
  {
    if (value != 0)
      return Integer.toString(value);
    else
      return text;
  }

  /**
   * search day
   */
   /*
  protected String searchDay()
  {
    return integerFormValue(searchParams_.searchDay(), "dd");
  }
  */

  /**
   * search month
   */
   /*
  protected String searchMonth()
  {
    return integerFormValue(searchParams_.searchMonth(), "mm");
  }
  */

  /**
   * lower search year
   */
  protected String searchLowerYear()
  {
    return integerFormValue(searchParams_.searchLowerYear(), "yyyy");
  }

  /**
   * upper search year
   */
  protected String searchUpperYear()
  {
    return integerFormValue(searchParams_.searchUpperYear(), "yyyy");
  }

  /**
   * search text
   */
  protected String searchText()
  {
    return searchParams_.searchText();
  }

  /**
   * write thumbnail radio buttons
   */
  protected void writeShowThumbnailCheckbox(JspWriter out) throws IOException
  {
    JspUtil.writeCheckbox(out, SearchParameters.NoThumbnails_, searchParams_.noThumbnails());
  }

  /**
   * search text input
   */
  protected void writeSearchTextInput(JspWriter out) throws IOException
  {
    writeSearchTextInput(out, AdvancedSearchPage.TextInputDefaultLength_);
  }

  /**
   * search text input with variable size
   */
  protected void writeSearchTextInput(JspWriter out, int size) throws IOException
  {
    JspUtil.writeTextInput(out, SearchParameters.SearchText_, JspUtil.formatEntryText(searchText()), size, 200);
  }

  /**
   * lower search year
   */
  protected void writeLowerSearchYearTextInput(JspWriter out) throws IOException
  {
    JspUtil.writeTextInput(out, SearchParameters.SearchLowerYear_, searchLowerYear(), 4, 5);
  }

  /**
   * search year
   */
  protected void writeUpperSearchYearTextInput(JspWriter out) throws IOException
  {
    JspUtil.writeTextInput(out, SearchParameters.SearchUpperYear_, searchUpperYear(), 4, 5);
  }

  /**
   * contains checkbox
   */
  protected void writeContainsCheckbox(JspWriter out) throws IOException
  {
    JspUtil.writeCheckbox(out, SearchParameters.ContainsTextSearch_, searchParams_.containsTextSearch());
  }

  /**
   * clear the damn form
   */
  protected void writeClearFormButton(JspWriter out) throws IOException
  {
    out.println("<script language=\"JavaScript\">");
    out.println("function clearForm(theForm)");
    out.println("{");
    out.println("  for (i=0; i<theForm.elements.length; i++) {");
    out.println("    var elt=theForm.elements[i];");
    out.println("    if (elt.selectedIndex) {");
    out.println("      elt.selectedIndex=0;");
    out.println("    }");
    out.println("    else if (elt.type==\"text\") {");
    out.println("      if (elt.name.indexOf(\"year\")!=-1) {");
    out.println("        elt.value=\"yyyy\";");
    out.println("      }");
    out.println("      else {");
    out.println("        elt.value='';");
    out.println("      }");
    out.println("    }");
    out.println("  }");
    out.println("}");
    out.println("</script>");
    out.println("<input type=button name=\"Clear\" onClick=\"clearForm(this.form)\" value=\"Clear\">");
  }
}

