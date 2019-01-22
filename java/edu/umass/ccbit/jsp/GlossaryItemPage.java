
/**
 * Title:        GlossaryItemPage<p>
 * Description:  base class for glossary item jsp page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public abstract class GlossaryItemPage extends HttpDbJspBase
{
  public static final String GlossaryTable_="Glossary";
  protected String term_;
  protected String definition_;
  protected int glossaryID_;

  /**
   * load the information from the database
   */
  public void load() throws SQLException
  {
    Statement st = connection_.createStatement();
    ResultSet result = st.executeQuery(query());
    result.next();
    term_=result.getString("Term");
    definition_=result.getString("Definition");
    result.close();
    st.close();
  }

  /**
   * checks for a null string...returns '' if null
   * @param string the string to check the nullity of
   * @param alt the alternatice string to display
   */
  public String isNull(String string, String alt)
  {
    if(string == null)
      return alt;
    return string;
  }

  /**
   * get the term
   */
  public String getTerm()
  {
    return isNull(term_,"Term unavailable");
  }

  /**
   * get the definition
   */
  public String getDefinition()
  {
    return isNull(definition_,"Definition unavailable");
  }

  /**
   * construct a query for a single item
   */
  protected String query()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(GlossaryTable_);
    buf.append(" where GlossaryID=").append(glossaryID_);
    return buf.toString();
  }

  /**
   * parse the parameters from the url
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    glossaryID_=servletParams_.getInt("glossaryID");
  }
}
