/**
 * Title:        MainCollectionItem<p>
 * Description:  base class to encapsulate methods/properties of items in main table<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: MainCollectionItem.java,v 1.20 2002/04/12 14:07:29 pbrown Exp $
 *
 * $Log: MainCollectionItem.java,v $
 * Revision 1.20  2002/04/12 14:07:29  pbrown
 * fixed copyright info
 *
 * Revision 1.19  2001/05/11 14:37:48  pbrown
 * made object images scaled instead of 'inside bounding box'...for
 * consistency of size wrt front/details etc...per discussion w/mm
 *
 * Revision 1.18  2001/03/08 18:33:53  pbrown
 * fixed look closer for exhibit
 *
 * Revision 1.17  2001/01/23 20:52:45  tarmstro
 * additions and changes for linking items with other items in the collection
 *
 * Revision 1.16  2001/01/18 16:14:09  tarmstro
 * added information regarding item copyright
 *
 * Revision 1.15  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.14  2000/11/01 21:45:38  pbrown
 * changes for item detail page
 *
 * Revision 1.13  2000/09/25 18:12:12  tarmstro
 * many changes for searching and storing results
 *
 * Revision 1.12  2000/08/03 20:37:23  tarmstro
 * fixed null string compares
 *
 * Revision 1.11  2000/08/02 21:20:42  tarmstro
 * added method to access page name(description field)
 *
 * Revision 1.10  2000/07/31 13:18:44  tarmstro
 * added age appropriate label changes
 *
 * Revision 1.9  2000/06/27 16:13:47  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.8  2000/06/14 18:41:12  pbrown
 * added alt text parameters
 *
 * Revision 1.7  2000/06/08 02:49:38  pbrown
 * added check for null for itemImages_
 *
 * Revision 1.6  2000/06/06 14:33:49  pbrown
 * many changes for searching
 *
 * Revision 1.5  2000/06/02 21:52:53  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.4  2000/05/26 20:52:26  pbrown
 * reimplemented attributes list as StringPairList object
 *
 * Revision 1.3  2000/05/25 22:25:07  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.2  2000/05/24 06:11:29  pbrown
 * first implementation of item pages with images and object viewer
 *
 * Revision 1.1  2000/05/19 21:41:59  pbrown
 * added classes for item pages
 *
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ccbit.jsp.ObjectViewerPage;
import edu.umass.ccbit.util.CollectionDate;
import edu.umass.ccbit.util.StringPairList;
import edu.umass.ckc.util.StringUtils;
import java.lang.StringBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Enumeration;

/*
 * this is intended to be a base class for main table items..
 * possible derived classes include documents/museum objects
 */
public class MainCollectionItem extends DbLoadableByIDItem
{
  public String accessionNumber_;
  public String itemName_;
  public String webLabel_;
  public String fifthLabel_;
  public String highLabel_;
  public String subSubject_;
  public String description_;
  public String centuryTurn_;
  public String processMaterialDesc_;
  public String nomenclatureSubcategory_;
  public String nomenclatureCategory_;
  public String placeOfOrigin_;
  public String workFormat_;
  public String copyright_;
  protected CollectionDate objectDate_;
  protected ItemImageList itemImages_;
  protected ObjectDimensions itemDimensions_;
  protected ObjectCreatorList itemCreators_;
  protected ObjectAssociationsList collectionAssociations_;
  protected RelatedAssociationsList relatedAssociations_;
  protected WebAssociationsList webAssociations_;
  protected StringPairList attributes_;

  /**
   * name of view
   */
  protected String view()
  {
    return "Web_MainView";
  }

  /**
   * init sub items
   */
  protected void initSubItems() throws SQLException
  {
    itemDimensions_=new ObjectDimensions();
    itemDimensions_.load(connection_, itemID_);
    itemCreators_=new ObjectCreatorList();
    itemCreators_.load(connection_, itemID_);
    itemImages_=new ItemImageList();
    itemImages_.load(connection_, itemID_);
    collectionAssociations_=new ObjectAssociationsList();
    collectionAssociations_.load(connection_, itemID_);
    relatedAssociations_=new RelatedAssociationsList();
    relatedAssociations_.load(connection_, itemID_);
    webAssociations_=new WebAssociationsList();
    webAssociations_.load(connection_, itemID_);
  }

  /**
   * number of images
   */
  public int imageMaxIndex()
  {
    return itemImages_ != null ? itemImages_.getCount() - 1 : 0;
  }

  /**
   * image at given index
   */
  protected CollectionImage getImage(int index)
  {
    return (CollectionImage) itemImages_.get(index);
  }

  /**
   * image type
   */
  public int imageType(int index)
  {
    return getImage(index).type();
  }

  /**
   * is document image
   */
  public boolean isDocumentImage(int index)
  {
    if(getImage(index)!=null)
     return getImage(index).isDocumentImage();
    return false;
  }

  /**
   * number of associations (collection or web)
   */
  public int associationsCount()
  {
    return collectionAssociations_.getCount() + webAssociations_.getCount();
  }

  /**
   * associated item
   */
  private AssociatedObjectBase collectionAssociation(int index)
  {
    return (AssociatedObjectBase) collectionAssociations_.get(index);
  }

  /**
   * associated web item
   */
  private WebAssociation webAssociation(int index)
  {
    return (WebAssociation) webAssociations_.get(index);
  }

  /**
   * associated item text...this conceptually joins the
   * two lists (collections assoc, web assoc) into one
   * list
   */
  public String associationText(int index)
  {
    if (index < collectionAssociations_.getCount())
      return collectionAssociation(index).name();
    else
      return webAssociation(index).link();
  }

  /**
   * associated item url
   */
  public String associationURL(int index)
  {
    if (index < collectionAssociations_.getCount())
      return collectionAssociation(index).objectUrl();
    else
      return webAssociation(index).url();
  }

  /**
   * related itemid
   */
  public int relatedItemID(int i)
  {
    return relatedAssociations_.relatedItemID(i);
  }

  /**
   * related item name
   */
  public String relatedItemName(int i)
  {
    return relatedAssociations_.relatedItemName(i);
  }

  /**
   * number of related items
   */
  public int relatedItemsCount()
  {
    return relatedAssociations_.getCount();
  }

  /**
   * number of collection associations
   */
  public int getNumCollectionAssociations()
  {
    return collectionAssociations_.getCount();
  }

  /**
   * image perspective
   */
  public String imageLinkText(int index)
  {
    if (getImage(index) != null)
      return getImage(index).linkText();
    else
      return "";
  }

  /**
   * image designation
   */
  public String imagePageName(int index)
  {
    if (getImage(index) != null)
      return getImage(index).linkText();
    else
      return null;
  }

  /**
   * object image
   */
  public String objectImage(int width, int height, int index)
  {
    if (getImage(index) != null)
    {
      getImage(index).initImageInfo();
      return getImage(index).scaledImageTag(width, height, MrSidImage.SCALE_IMAGE);
    }
    else
      return "";
  }

  /**
   * init this item from result
   */
  public void init(ResultSet result) throws SQLException
  {
    accessionNumber_=result.getString("AccessionNumber");
    itemName_=result.getString("ItemName");
    webLabel_=result.getString("WebLabel");
    fifthLabel_=result.getString("FifthGradeLabel");
    highLabel_=result.getString("HighSchoolLabel");
    subSubject_=result.getString("SubSubject");
    description_=result.getString("Description");
    centuryTurn_=result.getString("CenturyTurn");
    processMaterialDesc_=result.getString("ProcessMaterialDesc");
    nomenclatureSubcategory_=result.getString("NomenclatureSubcategory");
    nomenclatureCategory_=result.getString("NomenclatureCategory");
    placeOfOrigin_=result.getString("PlaceOfOrigin");
    workFormat_=result.getString("WorkFormat");
    copyright_=result.getString("Copyright");
    objectDate_=new CollectionDate("CreationDate", result);
    initSubItems();
    getAttributes();
  }


  /**
   * link to object viewer
   * TODO:
   */
  public String objectViewer(int imageID)
  {
    if (getImage(imageID) != null)
      return ObjectViewerPage.link(itemID_, getImage(imageID).client(), getImage(imageID).image(), imageID);
    else
      return "";
  }
  
  /**
   * link to object viewer
   */
  public String objectViewer(String title, int imageID)
  {
    if (getImage(imageID) != null)
      return ObjectViewerPage.link(itemID_, title, getImage(imageID).client(), getImage(imageID).image(), imageID);
    else
      return "";
  }
  

  /**
   * add attribute to list of attributes
   */
  protected void addAttribute(String name, String value)
  {
    attributes_.addIfNonEmpty(name.toLowerCase(), value);
  }

  /**
   * nomenclature
   * munges category/subcategory
   */
  protected String nomenclature()
  {
    StringBuffer buf=new StringBuffer();
    if (nomenclatureCategory_!=null)
      buf.append(nomenclatureCategory_);
    if (buf.length() > 0)
      buf.append("/");
    if (nomenclatureSubcategory_!=null)
      buf.append(nomenclatureSubcategory_);
    return buf.toString();
  }

  /**
   * accession number
   * prepends a '#'
   */
  protected String accessionNumber()
  {
    return "#" + accessionNumber_;
  }

  /**
   * creates vector of object attributes to be displayed on web
   * ...this is a vector of pairs of the form (name, value)
   */
  protected void getAttributes()
  {
    // creators
    // date
    // location
    // dimensions
    // nomenclature, process/materials, work format
    // accession number
    attributes_=new StringPairList();
    for (int i=0; i < itemCreators_.getCount(); i++)
    {
      addAttribute(itemCreators_.role(i), itemCreators_.name(i));
    }
    addAttribute("date", objectDate_.toString());
    addAttribute("location", placeOfOrigin_);
    for (int i=0; i < itemDimensions_.getCount(); i++)
    {
      addAttribute(itemDimensions_.code(i), itemDimensions_.value(i));
    }
    addAttribute("process/materials", processMaterialDesc_);
    addAttribute("item type", nomenclature());
    addAttribute("accession #", accessionNumber());
  }

  /**
   * number of attributes for object
   */
  public int numAttributes()
  {
    return attributes_ != null ? attributes_.size() : 0;
  }

  /**
   * name of attribute at index
   */
  public String attributeName(int index)
  {
    try
    {
      return attributes_.first(index);
    }
    catch (Exception e)
    {
      return "";
    }
  }

  /**
   * value of attribute at index
   */
  public String attributeValue(int index)
  {
    try
    {
      return attributes_.second(index);
    }
    catch (Exception e)
    {
      return "";
    }
  }

  /**
   * web label (advanced)
   */
  public String webLabel()
  {
    if (webLabel_!=null)
      return StringUtils.substitute(webLabel_, "\n", "<BR>");
    else
      return "There is currently no available label for this item.";
  }

  /**
   * fifth grade label (beginner)
   */
  public String fifthGradeLabel()
  {
    if (fifthLabel_!=null)
      return fifthLabel_;
    else
      return "There is currently no available \"Beginner\" label.  The following is the default level label: "+webLabel();
  }

  /**
   * high school label (intermediate)
   */
  public String highSchoolLabel()
  {
    if (highLabel_!=null)
      return highLabel_;
    else
      return "There is currently no available \"Intermediate\" label.  The following is the default level label: "+webLabel();
  }
}
