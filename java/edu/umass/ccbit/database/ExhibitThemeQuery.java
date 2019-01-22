
/**
 * Title:        ExhibitQuery<p>
 * Description:  base class for a query db for the theme/era page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import edu.umass.ccbit.database.CollectionThemeItem;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.Enumeration;
import java.lang.StringBuffer;

public class ExhibitThemeQuery
{
  public Vector items_;
  public String xThemeName_;
  public String yThemeName_;
  public String themeItemName_;
  public String themeCircaYear_;
  public String themeCreator_;
  public String themeInterpretation_;
  public static final String exhibitView_ = "Web_ExhibitThemeView";

  /**
   * strings used in the query
   */
  public interface fields
  {
    static final String itemID_="ItemID";
    static final String itemName_="ItemName";
    static final String exhibitID_="ExhibitID";
    static final String xThemeID_="XThemeID";
    static final String yThemeID_="YThemeID";
    static final String xThemeName_="XThemeName";
    static final String yThemeName_="YThemeName";
    static final String primaryItem_="PrimaryItem";
    static final String fileName_="Filename";
    static final String client_="Client";
    static final String labelTitle_="InterpretationTitle";
    static final String label_="Interpretation";
    static final String intLabel_="IntLabel";
    static final String begLabel_="BegLabel";
    static final String subthemeName_="SubthemeName";
    static final String subthemeID_="SubthemeID";
    static final String accession_="AccessionNumber";
    static final String creator_="Creator";
    static final String description_="Description";
    static final String objectDate_="ObjectDate";
    static final String themeInterpretation_="ThemeInterpretation";
    static final String themeClient_="ThemeClient";
    static final String themeFilename_="ThemeFilename";
    static final String material_="Material";
    static final String objectType_="Nomenclature";
    static final String lastName_="LastName";
    static final String themeItemName_="ThemeItemName";
    static final String themeCircaYear_="ThemeCircaYear";
    static final String themeCreator_="ThemeCreator";
    static final String imgPath_="ImagePath";
    static final String activity_="HasActivity";
    static final String altName_="AltName";
  }

  /**
   * access a vector item
   * @param index the index of the item in the vector
   */
  public CollectionThemeItem item(int index)
  {
    return (CollectionThemeItem) items_.get(index);
  }

  /**
   * construct a query
   * select all columns from the specified view
   * @param xThemeID the x axis theme id
   * @param yThemeID the y axis theme id
   */
  public String query(int xThemeID, int yThemeID)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(exhibitView_);
    buf.append(" WHERE (").append(fields.xThemeID_);
    buf.append("=").append(xThemeID).append(")");
    buf.append(" AND (").append(fields.yThemeID_);
    buf.append("=").append(yThemeID).append(")");
    return buf.toString();
  }

  /**
   * initialize the result set
   * @param result the result set generated from the query
   */
  public void init(ResultSet result)
   throws SQLException
  {
    base_init(result);
  }

  /**
   * base initialization
   * @param result the result set generated from the query
   */
  protected void base_init(ResultSet result)
   throws SQLException
  {
    xThemeName_=result.getString(fields.xThemeName_);
    yThemeName_=result.getString(fields.yThemeName_);
    themeCircaYear_=result.getString(fields.themeCircaYear_);
    themeCreator_=result.getString(fields.themeCreator_);
    themeItemName_=result.getString(fields.themeItemName_);
    themeInterpretation_=result.getString(fields.themeInterpretation_);
  }

  /**
   * run the query, store the result set in a vector
   * the results are linearly defined and displayed, so we only need
   * a vector.
   * @param conn the database connection
   * @param xThemeID the x axis theme id
   * @param yThemeID the y axis theme id
   */
  public void execute(Connection conn, int xThemeID, int yThemeID)
   throws SQLException
  {
    items_ = new Vector();
    Statement st = conn.createStatement();
    ResultSet result = st.executeQuery(query(xThemeID, yThemeID));

    while(result.next())
    {
      init(result);
      items_.addElement(CollectionThemeItem.new_ExhibitQuery(result));
    }
    result.close();
    st.close();
  }

  /**
   * number of items in the vector
   */
  public int numItems()
  {
    return items_.size();
  }
}
