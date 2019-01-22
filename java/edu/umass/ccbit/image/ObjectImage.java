/**
 * Title:        ObjectImage<p>
 * Description:  2d/3d object images<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ObjectImage.java,v 1.4 2002/04/12 14:07:47 pbrown Exp $
 *
 * $Log: ObjectImage.java,v $
 * Revision 1.4  2002/04/12 14:07:47  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2001/01/11 16:57:50  pbrown
 * added thumbnails to item pages
 *
 * Revision 1.2  2000/12/19 16:27:00  pbrown
 * made changes so that object viewer page can be loaded by giving accession
 * number and optional title
 *
 * Revision 1.1  2000/05/24 06:12:16  pbrown
 * implemented a class hierarchy for images
 *
 * 
 */
package edu.umass.ccbit.image;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
public class ObjectImage extends CollectionImage
{
  protected String perspective_;

  /**
   * constructor
   */
  public ObjectImage()
  {
    imageType_=CollectionImage.Types.Object_;
  }

  /**
   * link text
   */
  public String linkText()
  {
    return perspective_;
  }
  
  /**
   * initializer from result set
   */
  public void init(ResultSet result) throws SQLException
  {
    super.init(result);
    perspective_=result.getString("Perspective");
    if (perspective_ != null)
    {
      perspective_=perspective_.trim();
    }
  }
}
