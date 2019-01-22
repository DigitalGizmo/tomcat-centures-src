/**
 * Title:        BrowseCategoryItem<p>
 * Description:  a browse category (or subcategory) item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: BrowseCategoryItem.java,v 1.2 2002/04/12 14:07:10 pbrown Exp $
 *
 * $Log: BrowseCategoryItem.java,v $
 * Revision 1.2  2002/04/12 14:07:10  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/12 14:20:01  pbrown
 * many changes for implementation of browse
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.image.CollectionImage;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrowseCategoryItem implements InstanceFromResult
{
  public int browseCategoryID_;
  public int browseSubcategoryID_;
  public String categoryName_;
  public String categoryDescription_;
  public String subcategoryName_;
  public String subcategoryDescription_;
  public CollectionImage itemImage_;

  /**
   * init item from database record
   */
  public void init(ResultSet result) throws SQLException
  {
    browseCategoryID_=result.getInt("BrowseCategoryID");
    browseSubcategoryID_=result.getInt("BrowseSubcategoryID");
    categoryName_=result.getString("CategoryName");
    categoryDescription_=result.getString("CategoryDescription");
    subcategoryName_=result.getString("SubcategoryName");
    subcategoryDescription_=result.getString("SubcategoryDescription");
    itemImage_=CollectionImage.newInstance(result);
  }
}

  
    
    

  
  

  
