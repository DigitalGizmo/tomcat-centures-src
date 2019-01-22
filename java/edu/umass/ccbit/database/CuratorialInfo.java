/**
 * Title:        CuratorialInfo.java<p>
 * Description:  curatorial info from main table<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CuratorialInfo.java,v 1.0 2008-10-28 13:08:46-05 don Exp don $
 *
 * $Log: CuratorialInfo.java,v $
 * Revision 1.0  2008-10-28 13:08:46-05  don
 * Initial revision
 *
 * Revision 1.5  2008/10/27 14:07:17  don
 * removed notes from the attribute list
 *
 * Revision 1.4  2002/04/12 14:07:17  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2001/03/12 17:20:52  pbrown
 * added keywords to curatorial info
 *
 * Revision 1.2  2000/11/02 14:08:16  pbrown
 * changed fields
 *
 * Revision 1.1  2000/11/01 21:46:16  pbrown
 * curatorial info page
 *
 **/

package edu.umass.ccbit.database;

import edu.umass.ccbit.util.StringPairList;
import edu.umass.ccbit.database.DbLoadableByIDItem;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CuratorialInfo extends DbLoadableByIDItem
{
  protected StringPairList attributes_;
  public String itemName_;
  public String accessionNumber_;

  /**
   * add attribute
   */
  protected void addAttribute(String name, String value)
  {
    attributes_.addIfNonEmpty(name.toLowerCase(), value);
  }

  /**
   * name of view
   */
  protected String view()
  {
    return "Main";
  }

  /**
   * init from result set
   */
  public void init(ResultSet result) throws SQLException
  {
    itemName_=result.getString("ItemName");
    accessionNumber_=result.getString("AccessionNumber");
    attributes_=new StringPairList();
    addAttribute("Inscription", result.getString("Inscriptions"));
    addAttribute("Description", result.getString("Description"));
    // addAttribute("Notes", result.getString("Notes"));
    addAttribute("Significance Note", result.getString("SignificanceNote"));
    addAttribute("References", result.getString("Reference"));
    addAttribute("Provenance", result.getString("Provenance"));
    addAttribute("Signature", result.getString("Signature"));
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
   * number of attributes for object
   */
  public int numAttributes()
  {
    return attributes_ != null ? attributes_.size() : 0;
  }

  /**
   * load
   */
  public void load(Connection conn, int itemID) throws SQLException
  {
    super.load(conn, itemID);
    ItemKeywords keywords=new ItemKeywords();
    keywords.load(conn, itemID);
    addAttribute("Keywords", keywords.formatKeywords());
  }
}
