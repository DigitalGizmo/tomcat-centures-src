
/**
 * Title:        NowReadThis<p>
 * Description:  now read this documents<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;

public class NowReadThis extends DbLoadableItemList
{
  public static final String nowReadThisView_ = "Web_NowReadThisView";

  protected class NowReadThisDoc implements InstanceFromResult
  {
    // datamembers
    public int level_;
    public String transcription_;
    public String imagePath_;
    public String docName_;
    public String description_;
    public String thumbPath_;
    public int docID_;
    public String filename_;
    public String client_;
    public int itemid_;
    public int pageNum_;

    public void init(ResultSet result) throws SQLException
    {
      level_ = result.getInt("Level");
      transcription_ = result.getString("Transcription");
      imagePath_ = result.getString("ImagePath");
      docName_ = result.getString("Name");
      description_ = result.getString("Description");
      thumbPath_ = result.getString("ThumbPath");
      docID_ = result.getInt("ReadThisID");
      filename_ = result.getString("Filename");
      client_ = result.getString("Client");
      itemid_ = result.getInt("ItemID");
      pageNum_ = result.getInt("PageNum");
    }
  }

  /**
   * get number of docs
   */
  public int numDocs()
  {
    return items_.size();
  }

  /**
   * get now read this doc
   */
  private NowReadThisDoc getNowReadThisDoc(int index)
  {
    return (NowReadThisDoc) get(index);
  }

  /**
   * get client
   */
  public String getClient(int index)
  {
    return getNowReadThisDoc(index).client_;
  }

  /**
   * get filename
   */
  public String getFilename(int index)
  {
    return getNowReadThisDoc(index).filename_;
  }

  /**
   * get itemid
   */
  public int getItemID(int index)
  {
    return getNowReadThisDoc(index).itemid_;
  }

  /**
   * get pagenum
   */
  public int getPageNum(int index)
  {
    return getNowReadThisDoc(index).pageNum_;
  }

  /**
   * get docid
   */
  public int getDocID(int index)
  {
    return getNowReadThisDoc(index).docID_;
  }

  /**
   * get image tag
   */
  public String getDocImg(int index)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<IMG SRC=\"").append(getNowReadThisDoc(index).imagePath_);
    buf.append("\">");
    return buf.toString();
  }

  /**
   * get thumb image tag
   */
  public String getThmImg(int index)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<IMG SRC=\"").append(getNowReadThisDoc(index).thumbPath_);
    buf.append("\">");
    return buf.toString();
  }

  /**
   * get the transcription of doc
   */
  public String getDocTranscription(int index)
  {
    return getNowReadThisDoc(index).transcription_;
  }

  /**
   * get level of doc
   */
  public int getDocLevel(int index)
  {
    return getNowReadThisDoc(index).level_;
  }

  /**
   * get description of doc
   */
  public String getDocDescription(int index)
  {
    return getNowReadThisDoc(index).description_;
  }

  /**
   * get name of doc
   */
  public String getDocName(int index)
  {
    return getNowReadThisDoc(index).docName_;
  }

  /**
   * new document instance
   */
  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    NowReadThisDoc doc = new NowReadThisDoc();
    doc.init(result);
    return doc;
  }

  /**
   * query for docs at a specific level
   */
  private String queryAtLevel(int level)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(nowReadThisView_);
    buf.append(" WHERE [Level]=").append(level);
    return buf.toString();
  }

  /**
   * query for one doc
   */
  private String querySingleton(int docid)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(nowReadThisView_);
    buf.append(" WHERE ReadThisID=").append(docid);
    return buf.toString();
  }

  /**
   * load a single doc from the db
   */
  public void loadSingleton(Connection conn, ServletContext context, int docid) throws SQLException
  {
    load(conn, context, querySingleton(docid));
  }

  /**
   * load the docs from the db
   */
  public void loadAtLevel(Connection conn, ServletContext context, int level) throws SQLException
  {
    // remove attribute because of DbLoadableItemList check
    context.removeAttribute(getClass().getName());
    load(conn, context, queryAtLevel(level));
  }
}
