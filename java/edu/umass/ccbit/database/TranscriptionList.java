
/**
 * Title:        TranscriptionList<p>
 * Description:  a list of transcriptions that a document has<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ckc.util.StringUtils;
import java.util.Enumeration;
import java.util.Vector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;

public class TranscriptionList extends DbLoadableItemList
{
  protected static final String TranscriptionView_="TranscriptionPage";

  /**
   * class for a transcription of a single page
   */
  protected class TranscriptionItem implements InstanceFromResult
  {
    public String pageName_;
    public String transcription_;

    /**
     * initialie the result set
     * @param result the result set
     */
    public void init(ResultSet result) throws SQLException
    {
        pageName_=result.getString("PageName");
        transcription_ = result.getString("Transcription");
    }
  }

  /**
   * names of page images in a document
   * @return vector of page names(Strings)
   */
  public Vector getPageNames()
  {
    Vector names = new Vector();
    if(items_ != null)
    {
      Enumeration e = items_.elements();
      TranscriptionItem temp;
      while(e.hasMoreElements())
      {
        temp = (TranscriptionItem) e.nextElement();
        if(temp.pageName_ != null)
          names.add(StringUtils.removeChars(" ", temp.pageName_).toLowerCase());
      }
    }
    return names;
  }

  /**
   * the transcription text
   * @param pageName type of page
   * @return an escape character parsed transcription
   */
  public String getTranscription(String pageName)
  {
    if(items_ != null)
    {
      Enumeration e = items_.elements();
      TranscriptionItem temp;
      String db, page;
      if(pageName==null)
        return "";
      while(e.hasMoreElements())
      {
        temp = (TranscriptionItem) e.nextElement();
        db = StringUtils.removeChars(" ", temp.pageName_.toLowerCase());
        page = StringUtils.removeChars(" ", pageName.toLowerCase());

        if(db.compareTo(page)==0)
          // pb - 03/05/02 - removed call to parseTranscription
          return temp.transcription_;
      }
    }
    return "";
  }

  /**
   * new transcription item
   * @param result the result set
   * @return TranscriptionItem
   */
  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    TranscriptionItem trans = new TranscriptionItem();
    trans.init(result);
    return trans;
  }

  /**
   * construct a query
   * @param itemID the id of the document with the image
   * @return the constructed query
   */
  public String query(int itemID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(TranscriptionView_);
    buf.append(" WHERE ItemID=").append(itemID);
    buf.append(" AND Ready=1");
    return buf.toString();
  }

  /**
   * load from the database
   * @param conn the database connection manager
   * @param itemID the itemid to load the transcription for
   * @param context the servlet context to save the transcription object to
   */
  public void load(Connection conn, int itemID, ServletContext context) throws SQLException
  {
    // note: DbLoadableItemList checks if the attribute exists
    // if it doesn't, it creates it, if it does it doesn't overwrite it
    // get rid of the attribute
    context.removeAttribute(getClass().getName());
    load(conn, context, query(itemID));
  }
}
