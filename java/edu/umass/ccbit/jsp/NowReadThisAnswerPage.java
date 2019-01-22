
/**
 * Title:        NowReadThisAnswerPage<p>
 * Description:  base class for new read this answer page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.NowReadThis;
import edu.umass.ccbit.util.NowReadThisParameters;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.Parameters;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;import java.net.URLEncoder;

public abstract class NowReadThisAnswerPage extends HttpDbJspBase
{
  NowReadThisParameters params_;
  // nowreadthis document list(should be a single item list)
  NowReadThis docs_;

  int docID_;

  String [] hardness = {"easy","medium","hard"};

  /**
   * load
   */
  protected void load(HttpSession session)
  {
    getSearchParameters(servletParams_);
    docs_ = (NowReadThis) JspSession.load(session, NowReadThisPage.NowReadThisSession_);
    docID_ = JspSession.getInt(session, NowReadThisDocPage.DocID_, 0);
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
   * get entered transcription
   */
  protected String getEnteredText()
  {
    return params_.getEnteredText();
  }

  /**
   * get the document name
   */
  protected String getDocName()
  {
    return docs_.getDocName(docID_);
  }

  /**
   * get the next level
   */
  protected int getLevel()
  {
    return docs_.getDocLevel(docID_);
  }

  /**
   * get the image path
   */
  protected String getDocImagePath()
  {
    return docs_.getDocImg(docID_);
  }

  /**
   * next level
   */
  protected int getNextLevel()
  {
    return getLevel() + 1;
  }

  /**
   * get word level
   */
  protected String getDifficulty()
  {
    return hardness[getLevel()];
  }

  /**
   * get next work level
   */
  protected String getNextDifficulty()
  {
    if(getNextLevel() < 3)
      return hardness[getNextLevel()];
    return null;
  }

  /**
   * get the document transcription
   */
  protected String getDocTrans()
  {
    return docs_.getDocTranscription(docID_);
  }

  /**
   * get search parameters
   */
  protected void getSearchParameters(Parameters param)
  {
    params_=new NowReadThisParameters();
    params_.getParameters(param);
  }

}
