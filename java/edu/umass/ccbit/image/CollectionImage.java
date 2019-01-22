/**
 * Title:        CollectionImage<p>
 * Description:  base class for database images<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CollectionImage.java,v 1.7 2003/12/03 21:01:07 keith Exp $
 *
 * $Log: CollectionImage.java,v $
 * Revision 1.7  2003/12/03 21:01:07  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.6  2002/04/12 14:07:45  pbrown
 * fixed copyright info
 *
 * Revision 1.5  2001/01/11 16:57:50  pbrown
 * added thumbnails to item pages
 *
 * Revision 1.4  2000/12/19 16:27:00  pbrown
 * made changes so that object viewer page can be loaded by giving accession
 * number and optional title
 *
 * Revision 1.3  2000/06/14 18:42:36  pbrown
 * added optional alt text args to image tag methods
 *
 * Revision 1.2  2000/06/12 14:22:21  pbrown
 * added new class factory method
 *
 * Revision 1.1  2000/05/24 06:12:15  pbrown
 * implemented a class hierarchy for images
 *
 * 
*/
package edu.umass.ccbit.image;

import edu.umass.ccbit.database.InstanceFromResult;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

public abstract class CollectionImage extends MrSidImage implements InstanceFromResult
{
  // these instance variables come from the database
  protected String description_;
  protected String accessionNumberSuffix_;
  // database key for image type
  public static final String isDocumentImageKey_="IsDocumentImage";
  // image type
  protected int imageType_;

  public Hashtable getImgTagParams( int height, int width ) {
    Hashtable imgTagParams = super.getImgTagParams( height, width );
    imgTagParams.put( "ALT", linkText() );
    return (imgTagParams);
  }

  public interface Types
  {
    public static final int Object_=0;
    public static final int Document_=1;
  }

  /**
   * class factory
   */
  public static CollectionImage newInstance(boolean isDocumentImage)
  {
    return newInstance(isDocumentImage ? Types.Document_ : Types.Object_);
  }

  /**
   * image type
   */
  public int type()
  {
    return imageType_;
  }

  /**
   * is document image
   */
  public boolean isDocumentImage()
  {
    return imageType_== Types.Document_;
  }
  
  /**
   * class factory
   */
  public static CollectionImage newInstance(int type)
  {
    switch (type)
    {
      case (Types.Object_):
        return new ObjectImage();
      case (Types.Document_):
        return new DocumentImage();
      default:
        return null;
    }
  }

  /**
   * new instance, and init from result set
   */
  public static CollectionImage newInstance(ResultSet result) throws SQLException
  {
    boolean isDocumentImage=result.getBoolean("IsDocumentImage");
    CollectionImage img=newInstance(isDocumentImage);
    img.init(result);
    return img;
  }

  /**
   * link text (perspective, or page range)
   */
  public abstract String linkText();

  /**
   * scaled image tag...incorporates linkText as alt attribute
   */
  public String scaledImageTag(int width, int height, int sizeOpt)
  {
    return scaledImageTag(width, height, sizeOpt, linkText());
  }
  
  /**
   * initializer from result set
   */
  public void init(ResultSet result) throws SQLException
  {
    client_=result.getString("Client");
    if (client_ != null)
    {
      client_=client_.trim();
    }
    image_=result.getString("Filename");
    if (image_!=null)
    {
      image_=image_.trim();
    }
    description_=result.getString("Description");
    if (description_ != null)
    {
      description_=description_.trim();
    }
    accessionNumberSuffix_=result.getString("AccessionNumberSuffix");
  }
}
