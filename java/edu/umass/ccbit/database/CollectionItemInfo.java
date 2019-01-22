/*
 * Title:        CollectionItemInfo<p>
 * Description:  <p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CollectionItemInfo.java,v 1.7 2003/12/05 00:35:21 keith Exp $
 *
 * $Log: CollectionItemInfo.java,v $
 * Revision 1.7  2003/12/05 00:35:21  keith
 * added itemID() method
 *
 * Revision 1.6  2002/04/12 14:07:13  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2000/06/27 16:13:44  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.4  2000/06/06 14:33:48  pbrown
 * many changes for searching
 *
 * Revision 1.3  2000/06/02 21:52:50  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.2  2000/06/01 14:51:33  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.1  2000/05/25 22:25:07  pbrown
 * many changes for item pages including building list of associated items
 *
 *
 * basic info about a collection item, e.g., item name
 *
 */
package edu.umass.ccbit.database;
import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.util.CollectionDate;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectionItemInfo extends DbLoadableByIDItem
{
  public String itemName_;
  public int itemID_;
  public String idSentence_;
  public String accessionNumber_;
  public CollectionImage itemImage_;
  public CollectionDate date_;
  protected int imageType_;
  protected String client_;
  protected String filename_;
  public static final String view_="Web_CollectionItemInfoView";
  // field names
  public interface fields
  {
    public static final String AccessionNumber_="AccessionNumber";
    public static final String Client_="Client";
    public static final String Filename_="Filename";
    public static final String ItemID_="ItemID";
    public static final String ItemName_="ItemName";
    public static final String IsDocumentImage_="IsDocumentImage";
    public static final String IDSentence_="IDSentence";
  }
        
  /**
   * view
   */
  protected String view()
  {
    return view_;
  }

  /**
   * item date
   */
  public String itemDate()
  {
    return date_.toString();
  }

  /**
   * init
   */
  public void init(ResultSet result) throws SQLException
  {
    itemName_=result.getString(fields.ItemName_);
    itemID_=result.getInt(fields.ItemID_);
    accessionNumber_=result.getString(fields.AccessionNumber_);
    idSentence_=result.getString(fields.IDSentence_);
    imageType_=result.getInt(fields.IsDocumentImage_);
    itemImage_=CollectionImage.newInstance(imageType_);
    date_=new CollectionDate("CreationDate", result);
    itemImage_.init(result);
  }

  /**
   * id sentence
   */
  public String idSentence()
  {
    return idSentence_ != null ? idSentence_ : "";
  }

  /**
   * item name
   */
  public String itemName()
  {
    return itemName_;
  }

  /**
   * item ID
   */
  public int itemID() {
    return itemID_;
  }
}
