
/**
 * Title:        MakeChronologyPage<p>
 * Description:  base class for chronology specification jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

// use the static final data members to name the parameters in the constructed selectable items
import edu.umass.ccbit.util.ChronologyParameters;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.database.Chronologies;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.lang.StringBuffer;
import java.sql.SQLException;

public abstract class MakeChronologyPage extends HttpDbJspBase
{
  protected Chronologies chrono_=new Chronologies();
  protected ChronologyParameters chronoParams_;
  public static final String Chrono_="chrono";

  /**
   * load
   */
  public void load(HttpSession session)
   throws SQLException
  {
    chrono_.load(connection_, getServletContext());
    chronoParams_ = new ChronologyParameters();
  }

  /**
   * write drop down list of chronologies
   * element in drop down list of the form "CHRONOLOGY NAME | CHRONOLOGYID"
   * ordered alphabetically
   * @param out the jspwriter
   */
  protected void writeUseChronologyDropDown(JspWriter out)
   throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(ChronologyParameters.WhichChronology_).append(" size=1>\n");
    buf.append("<OPTION value=0>Select a chronology\n");
    for (int i=0; i<chrono_.getCount(); i++)
    {
      buf.append("<OPTION value=").append(chrono_.getChronID(i));
      if (chrono_.getChronID(i)==chronoParams_.getChronology())
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(chrono_.getChronName(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * lower bound year
   */
  protected void writeLowerYearTextInput(JspWriter out) throws IOException
  {
    JspUtil.writeTextInput(out, ChronologyParameters.LowerYear_, "yyyy", 4, 5);
  }

  /**
   * upper bound year
   */
  protected void writeUpperYearTextInput(JspWriter out) throws IOException
  {
    JspUtil.writeTextInput(out, ChronologyParameters.UpperYear_, "yyyy", 4, 5);
  }

  /**
   * write a checkbox for user collection inclusion
   * default: checked
   * @param out the jspwriter
   */
  protected void writeUseCollectionCheckBox(JspWriter out)
   throws IOException
  {
    JspUtil.writeCheckbox(out, ChronologyParameters.UseCollection_, true);
  }
}
