/**
 * Title:        BrowseCategories<p>
 * Description:  class for browse categories (and subcategories)<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: BrowseCategories.java,v 1.5 2002/04/12 14:07:09 pbrown Exp $
 *
 * $Log: BrowseCategories.java,v $
 * Revision 1.5  2002/04/12 14:07:09  pbrown
 * fixed copyright info
 *
 * Revision 1.4  2000/12/11 16:01:19  pbrown
 * uses new database view(s) to allow image to be specified
 *
 * Revision 1.3  2000/12/08 16:17:54  pbrown
 * added views to code in comment block
 *
 * Revision 1.2  2000/12/08 16:15:30  pbrown
 * added order by
 *
 * Revision 1.1  2000/06/12 14:20:00  pbrown
 * many changes for implementation of browse
 *
 */
package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.StringBuffer;
import java.sql.Connection;
import edu.umass.ccbit.image.CollectionImage;

/*
  fixed views: 12/8/00 pb

CREATE VIEW dbo.Web_BrowseCategoryItemsView
AS
SELECT CollectionBrowseSubcategory.BrowseSubcategoryID,
    CollectionBrowseSubcategory.BrowseCategoryID, 
    CollectionBrowseCategory.CategoryName, 
    CollectionBrowseCategory.Description AS CategoryDescription, 
    CollectionBrowseSubcategory.SubcategoryName, 
    CollectionBrowseSubcategory.Description AS SubcategoryDescription,
     CollectionBrowseItems.ItemID, 
    Web_UniqueItemImageView.Filename, 
    Web_UniqueItemImageView.Perspective, 
    Web_UniqueItemImageView.IsDocumentImage, 
    Web_UniqueItemImageView.Client, 
    Web_UniqueItemImageView.Description, 
    Web_UniqueItemImageView.AccessionNumberSuffix, 
    Web_UniqueItemImageView.ImageID
FROM CollectionBrowseCategory INNER JOIN
    CollectionBrowseSubcategory ON 
    CollectionBrowseCategory.BrowseCategoryID = CollectionBrowseSubcategory.BrowseCategoryID
     INNER JOIN
    CollectionBrowseItems ON 
    CollectionBrowseSubcategory.BrowseSubcategoryID = CollectionBrowseItems.BrowseSubcategoryID
     LEFT OUTER JOIN
    Web_UniqueItemImageView ON 
    CollectionBrowseItems.ItemID = Web_UniqueItemImageView.ItemID
WHERE (CollectionBrowseSubcategory.Representative = 1) AND 
    (CollectionBrowseItems.Representative = 1)

CREATE VIEW dbo.Web_BrowseSubcategoryItemsView
AS
SELECT CollectionBrowseSubcategory.BrowseSubcategoryID, 
    CollectionBrowseSubcategory.BrowseCategoryID, 
    CollectionBrowseSubcategory.SubcategoryName, 
    CollectionBrowseSubcategory.Description AS SubcategoryDescription,
    Web_UniqueItemImageView.Filename, 
    Web_UniqueItemImageView.Perspective, 
    Web_UniqueItemImageView.IsDocumentImage, 
    Web_UniqueItemImageView.Client, 
    Web_UniqueItemImageView.Description, 
    Web_UniqueItemImageView.AccessionNumberSuffix, 
    Web_UniqueItemImageView.ImageID, 
    CollectionBrowseCategory.CategoryName, 
    CollectionBrowseCategory.Description AS CategoryDescription, 
    CollectionBrowseItems.ItemID
FROM CollectionBrowseSubcategory INNER JOIN
    CollectionBrowseItems ON 
    CollectionBrowseSubcategory.BrowseSubcategoryID = CollectionBrowseItems.BrowseSubcategoryID
     INNER JOIN
    CollectionBrowseCategory ON 
    CollectionBrowseSubcategory.BrowseCategoryID = CollectionBrowseCategory.BrowseCategoryID
     LEFT OUTER JOIN
    Web_UniqueItemImageView ON 
    CollectionBrowseItems.ItemID = Web_UniqueItemImageView.ItemID
WHERE (CollectionBrowseItems.Representative = 1)

 */

public class BrowseCategories extends DbLoadableItemList
{
  public static final String Web_BrowseSubcategoryItems_All="Web_BrowseSubcategoryItems_All";
  /**
   * name of view
   * if categoryID is non-negative, returns subcategory view, otherwise, returns category view
   */
  protected String view(int categoryID)
  {
    return Web_BrowseSubcategoryItems_All;
  }

  /**
   * order by
   */
  protected String orderBy(int categoryID)
  {
    return categoryID > 0 ? "SubcategoryName" : "CategoryName";
  }
  
   
  /**
   * query to get categories (or subcategories if categoryID is non-zero)
   */
  protected String query(int categoryID)
  {
    StringBuffer query=new StringBuffer();
    query.append("SELECT * FROM ").append(view(categoryID));
    if (categoryID > 0)
    {
      query.append(" WHERE BrowseCategoryID=").append(categoryID);
    }
    else
    {
      query.append(" WHERE RepresentativeSubcategory=1");
    }
    query.append(" AND RepresentativeItem=1");
    query.append(" ORDER BY ").append(orderBy(categoryID));
    return query.toString();
  }

  /**
   * get browse (sub)category info at index
   */
  protected BrowseCategoryItem getBrowseCategoryItem(int index)
  {
    return (BrowseCategoryItem) get(index);
  }

  /**
   * browse category id at index
   */
  public int browseCategoryID(int index)
  {
    return getBrowseCategoryItem(index).browseCategoryID_;
  }

  /**
   * browse subcategory id at index
   */
  public int browseSubcategoryID(int index)
  {
    return getBrowseCategoryItem(index).browseSubcategoryID_;
  }

  /**
   * category name at index
   */
  public String categoryName(int index)
  {
    return getBrowseCategoryItem(index).categoryName_;
  }

  /**
   * category description at index
   */
  public String categoryDescription(int index)
  {
    return getBrowseCategoryItem(index).categoryDescription_;
  }

  /**
   * subcategory name at index
   */
  public String subcategoryName(int index)
  {
    return getBrowseCategoryItem(index).subcategoryName_;
  }

  /**
   * subcategory description
   */
  public String subcategoryDescription(int index)
  {
    return getBrowseCategoryItem(index).subcategoryDescription_;
  }

  /**
   * item image at index
   */
  public CollectionImage itemImage(int index)
  {
    return getBrowseCategoryItem(index).itemImage_;
  }
  
  /**
   * instantiates object from database result
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    BrowseCategoryItem browseItem=new BrowseCategoryItem();
    browseItem.init(result);
    return browseItem;
  }

  /**
   * load
   */
  public void load(Connection conn, int categoryID) throws SQLException
  {
    load(conn, query(categoryID));
  }
}
