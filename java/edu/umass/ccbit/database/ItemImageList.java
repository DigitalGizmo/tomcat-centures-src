/**
 * Title:        ItemImageList<p>
 * Description:  list of item images for an item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ItemImageList.java,v 1.7 2003/12/03 21:01:02 keith Exp $
 *
 * $Log: ItemImageList.java,v $
 * Revision 1.7  2003/12/03 21:01:02  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.6  2002/04/12 14:07:26  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/12/03 17:39:07  pbrown
 * adds orderby clause
 *
 * Revision 1.4  2001/03/23 16:15:08  pbrown
 * moved some views to tables for better performance
 *
 * Revision 1.3  2000/06/14 18:41:12  pbrown
 * added alt text parameters
 *
 * Revision 1.2  2000/06/01 14:51:33  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.1  2000/05/24 06:11:29  pbrown
 * first implementation of item pages with images and object viewer
 *
 * 
 */
package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.image.CollectionImage;
//import java.sql.ResultSet;

public class ItemImageList extends DbLoadableByID
{
  /**
   * name of view
   */
  protected String view()
  {
    return "Web_OrderedItemImage";
  }

  /**
   * order by
   */
  protected String orderBy()
  {
    return "PageStart";
  }
  
  /**
   * instantiate object for list
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    int type=result.getInt(CollectionImage.isDocumentImageKey_);
    CollectionImage img=CollectionImage.newInstance(type);
    img.init(result);
    return img;
  }
}
