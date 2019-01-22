/**
 * Title:        DbLoadableItemImage<p>
 * Description:  loads images from database using various criteria<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DbLoadableItemImage.java,v 1.6 2003/12/03 21:01:01 keith Exp $
 *
 * $Log: DbLoadableItemImage.java,v $
 * Revision 1.6  2003/12/03 21:01:01  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.5  2002/04/12 14:07:21  pbrown
 * fixed copyright info
 *
 * Revision 1.4  2001/10/25 18:51:35  pbrown
 * changes for don's thing
 *
 * Revision 1.3  2001/05/10 18:16:44  tarmstro
 * created new view for item images
 *
 * Revision 1.2  2001/05/08 19:53:15  tarmstro
 * for referencing a specific page
 *
 * Revision 1.1  2000/12/19 16:26:11  pbrown
 * class for loading an image by accession number
 *
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.image.CollectionImage;

public class DbLoadableItemImage extends DbLoadableItemList
{
  public CollectionImage img;

  public InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    int isDocumentImage=result.getInt(CollectionImage.isDocumentImageKey_);
    img=CollectionImage.newInstance(isDocumentImage);
    img.init(result);
    return img;
  }

  public void loadByItemId(Connection conn, int itemID) throws SQLException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("SELECT * from Web_ItemImagesByAccNumandImg where ItemID=");
    buf.append(itemID);
    load(conn, buf.toString());
  }

  public void loadByAccessionNumber(Connection conn, String accessionNumber) throws SQLException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("SELECT * from Web_ItemImagesByAccNumandImg where AccessionNumber like '%");
    buf.append(accessionNumber).append("%'");
    load(conn, buf.toString());
  }

  public String client(int index)
  {
    CollectionImage tempImg = (CollectionImage) items_.elementAt(index);
    return tempImg != null ? tempImg.client() : "";
  }

  public CollectionImage collectionImage()
  {
    return img;
  }

  public CollectionImage collectionImage( int index ) {
    return (CollectionImage) items_.elementAt( index );
  }

  /**
   * image file name
   */
  public String image(int index)
  {
    CollectionImage tempImg = (CollectionImage) items_.elementAt(index);
    return tempImg != null ? tempImg.image() : "";
  }
}
