package edu.umass.ccbit.jsp;

/**
 * Title:        GlossaryPage
 * Description:  base class for jsp page containing glossary entries
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */

import edu.umass.ccbit.database.Glossary;
import edu.umass.ccbit.util.JspSession;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

public abstract class GlossaryPage extends HttpDbJspBase
{
  private Glossary glossary_;

  protected void load(HttpSession session) throws SQLException
  {
    glossary_ = (Glossary) JspSession.load(session, Glossary.Glossary_);
    if(glossary_==null)
    {
      glossary_=new Glossary();
      glossary_.load(connection_, getServletConfig().getServletContext());
    }
  }

  protected String getTerm(int n)
  {
    return glossary_.getTerm(n);
  }

  protected String getDefinition(int n)
  {
    return glossary_.getDefinition(getTerm(n));
  }

  protected int numItems()
  {
    try
    {
      return glossary_.numItems();
    }
    catch(Exception e)
    {
      return 0;
    }
  }
}
