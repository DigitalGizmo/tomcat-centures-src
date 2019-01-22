/**
 * Title:        DocumentImage<p>
 * Description:  document images<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: DocumentImage.java,v 1.3 2002/04/12 14:07:45 pbrown Exp $
 *
 * $Log: DocumentImage.java,v $
 * Revision 1.3  2002/04/12 14:07:45  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/12/08 16:15:05  pbrown
 *
 * removed pagestart/pageend
 *
 * Revision 1.1  2000/05/24 06:12:16  pbrown
 * implemented a class hierarchy for images
 *
 * 
 */
package edu.umass.ccbit.image;

import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.database.InstanceFromResult;

public class DocumentImage extends CollectionImage implements InstanceFromResult
{
  // from database
  int pageStart_;
  int pageEnd_;

  /**
   * constructor
   */
  public DocumentImage()
  {
    imageType_=CollectionImage.Types.Document_;
  }

  /**
   * link text
   */
  public String linkText()
  {
    // TODO: is description_ what we want??
    // maybe DocumentImage table needs a separate field for this
    return description_;
  }

  /**
   * initializer from result set
   */
  public void init(ResultSet result) throws SQLException
  {
    super.init(result);
    //pageStart_=result.getInt("PageStart");
    //pageEnd_=result.getInt("PageEnd");
  }
}
