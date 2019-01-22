
/**
 * Title:        NowReadThisPage<p>
 * Description:  base class for now read this jsp page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.database.NowReadThis;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;

public abstract class NowReadThisPage extends HttpDbJspBase
{
  // difficulty level info
  public static final String Level_ = "level";
  public static final String docPage_ = "docpage.jsp";
  public int level_;

  // nowreadthis document list
  NowReadThis docs_;
  public static final String NowReadThisSession_ = "nowreadthis";

  /**
   * number of documents
   */
  protected int numItems()
  {
    return docs_.numDocs();
  }

  /**
   * get name
   */
  protected String getDocName(int index)
  {
    return getLink(docs_.getDocName(index), index);
  }

  /**
   * get image
   */
  protected String getImage(int index)
  {
    return getLink(docs_.getThmImg(index), index);
  }

  /**
   * get desc
   */
  protected String getDesc(int index)
  {
    String temp = docs_.getDocDescription(index);
    if(temp != null)
      return temp;
    return "";
  }

  /**
   * get link
   */
  private String getLink(String viewable, int index)
  {
    StringBuffer buf = new StringBuffer();
    buf.append(docPage_);
    HtmlUtils.addToLink(NowReadThisDocPage.DocID_, index, true, buf);
    return HtmlUtils.anchor(buf.toString(), viewable);
  }

  /**
   * load nowreadthis documents
   */
  protected void load(HttpSession session) throws SQLException
  {
    docs_ = new NowReadThis();
    docs_.loadAtLevel(connection_, getServletContext(), level_);
    JspSession.save(session, NowReadThisSession_, docs_);
  }

  /**
   * parse url parameters
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    level_ = servletParams_.getInt(Level_, 0);
  }

}
