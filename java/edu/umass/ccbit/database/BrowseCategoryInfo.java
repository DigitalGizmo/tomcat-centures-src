/**
 * Title:        BrowseCategoryInfo<p>
 * Description:  get category/subcategory text from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: BrowseCategoryInfo.java,v 1.2 2002/04/12 14:07:10 pbrown Exp $
 *
 * $Log: BrowseCategoryInfo.java,v $
 * Revision 1.2  2002/04/12 14:07:10  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/06/12 14:20:00  pbrown
 * many changes for implementation of browse
 *
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.util.JspSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class BrowseCategoryInfo extends DbLoadableItem
{
  public String category_;
  public String subcategory_;
  public int categoryID_;
  public int subcategoryID_;
  protected static final String categorySessionAttribute_="browse_category_name";
  protected static final String subcategorySessionAttribute_="browse_subcategory_name";
  protected static final String categoryIDSessionAttribute_="browse_category_id";
  protected static final String subcategoryIDSessionAttribute_="browse_subcategory_id";
  
  public void init(ResultSet result) throws SQLException
  {
    category_=result.getString("CategoryName");
    subcategory_=result.getString("SubcategoryName");
    categoryID_=result.getInt("BrowseCategoryID");
  }

  protected String query(int subcategoryID)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("SELECT * FROM Web_BrowseCategoryInfoView WHERE BrowseSubcategoryID=").append(subcategoryID);
    return buf.toString();
  }

  public void load(Connection conn, int subcategoryID) throws SQLException
  {
    subcategoryID_=subcategoryID;
    load(conn, query(subcategoryID));
  }

  public void saveToSession(HttpSession session)
  {
    JspSession.save(session, categorySessionAttribute_, category_);
    JspSession.save(session, subcategorySessionAttribute_, subcategory_);
    JspSession.setInt(session, categoryIDSessionAttribute_, categoryID_);
    JspSession.setInt(session, subcategoryIDSessionAttribute_, subcategoryID_);
  }

  public void getFromSession(HttpSession session)
  {
    category_=(String) JspSession.load(session, categorySessionAttribute_);
    subcategory_=(String) JspSession.load(session, subcategorySessionAttribute_);
    categoryID_=JspSession.getInt(session, categoryIDSessionAttribute_, -1);
    subcategoryID_=JspSession.getInt(session, subcategoryIDSessionAttribute_, -1);
  }
}

    
