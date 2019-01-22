
/**
 * Title:        NowReadThisDocPage<p>
 * Description:  base class for new read this document page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.database.NowReadThis;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.jsp.JspWriter;
import java.net.URLEncoder;

public abstract class NowReadThisDocPage extends HttpDbJspBase
{
  // nowreadthis document list(should be a single item list)
  NowReadThis docs_;

  // defaults
  public static final String TextArea_ = "textinput";
  public static final String DefaultText_ = "enter your transcription here...";

  // docid
  public int docID_;
  public static final String DocID_ = "docid";

  /**
   * search text input
   */
  protected void writeTranscriptionTextAreaInput(JspWriter out) throws IOException
  {
    JspUtil.writeTextArea(out, TextArea_, 10, 80, "", "hard");
  }

  /**
   * look closer link
   */
  protected String objectViewer()
  {
    try
    {
      int itemid = docs_.getItemID(docID_);
      String client = docs_.getClient(docID_).trim();
      String filename = docs_.getFilename(docID_).trim();
      int pageNum = docs_.getPageNum(docID_);
      return ObjectViewerPage.link(itemid, URLEncoder.encode(getDocName()), client, filename, pageNum);
    }
    catch(Exception e){System.out.println(e.toString());}
    return "";
  }

  /**
   * get name of doc
   */
  protected String getDocName()
  {
    if(docID_ < 0)
      return "";
    try
    {
      return docs_.getDocName(docID_);
    }
    catch(Exception e){System.out.println(e.toString());}
    return "";
  }

  /**
   * get imagepath of doc
   */
  protected String getDocImagePath()
  {
    if(docID_ < 0)
      return "";
    try
    {
      return docs_.getDocImg(docID_);
    }
    catch(Exception e){System.out.println(e.toString());}
    return "";
  }

  /**
   * load nowreadthis documents
   */
  protected void load(HttpSession session) throws SQLException
  {
    docs_ = null;
    //while(docs_ == null)
    //{
      docs_ = (NowReadThis) JspSession.load(session, NowReadThisPage.NowReadThisSession_);
    //}
    JspSession.setInt(session, DocID_, docID_);
  }

  /**
   * parse url parameters
   * @param request the http request
   */
  public void parseRequestParameters(HttpServletRequest request)
  throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    docID_ = servletParams_.getInt(DocID_, -1);
  }
}
