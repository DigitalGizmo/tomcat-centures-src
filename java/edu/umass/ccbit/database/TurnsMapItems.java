/**
 * Title:        TurnsMapItems<p>
 * Description:  turns map items, read from database<p>
 * Copyright:    Copyright (c) 2000-2003<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletContext;
import java.util.Vector;

public class TurnsMapItems extends DbLoadableItemList
{
  protected class TurnsMapItem implements InstanceFromResult
  {
    // attributes
    /*
    $y=$Data->Data("YThemeID");
    $x=$Data->Data("XThemeID");
    $itemid=$Data->Data("ItemID");
    $subthemeid=$Data->Data("SubthemeID");
    $subtheme=$Data->Data("SubthemeName");
     */
      public int yThemeID_;
      public int xThemeID_;
      public int itemID_;
      public int subthemeID_;
      public String subthemeName_;
      public String xThemeName_;
      public String yThemeName_;
    /**
     * initialize from result set
     * @param result the result set
     */
    public void init(ResultSet result) throws SQLException
    {
      yThemeID_=result.getInt("YThemeID");
      xThemeID_=result.getInt("XThemeID");
      itemID_=result.getInt("ItemID");
      subthemeID_=result.getInt("SubthemeID");
      subthemeName_=result.getString("SubthemeName");
      xThemeName_=result.getString("XThemeName");
      yThemeName_=result.getString("YThemeName");
    }
  }

  Vector turnsMap_;

  /**
   * new turns map object
   * @param result the result set
   * @return AccessionNumber
   */
  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    TurnsMapItem item = new TurnsMapItem();
    item.init(result);
    return item;
  }

  protected String query()
  {
    return
      "select ExhibitXAxisTheme.XThemeID, ExhibitYAxisTheme.YThemeID, " + 
      "ExhibitXAxisTheme.ThemeName as XThemeName, " + 
      "ExhibitYAxisTheme.ThemeName as YThemeName, " + 
      "ExhibitThemes.ExhibitThemeID, ExhibitItems.ItemID, " + 
      "ExhibitSubthemes.SubthemeID, ExhibitSubthemes.SubthemeName from " + 
      "(exhibityaxistheme inner join exhibitthemes " + 
      "on exhibityaxistheme.ythemeid=exhibitthemes.ythemeid " + 
      "inner join exhibitxaxistheme on exhibitxaxistheme.xthemeid=exhibitthemes.xthemeid) " + 
      "inner join " + 
      "exhibititems on exhibititems.exhibitthemeid=exhibitthemes.exhibitthemeid " + 
      "inner join exhibitsubthemes on exhibitsubthemes.subthemeid=exhibititems.subtheme " + 
      "where exhibitthemes.exhibitid=1 " + 
      "order by exhibityaxistheme.position, exhibitxaxistheme.position";
  }

  /**
   * number of y's
   */
  public int yCount()
  {
    return turnsMap_.size();
  }

  public int xCount(int yIndex)
  {
    return ((Vector) turnsMap_.elementAt(yIndex)).size();
  }

  public int itemCount(int yIndex, int xIndex)
  {
    return ((Vector) ((Vector) turnsMap_.elementAt(yIndex)).elementAt(xIndex)).size();
  }

  public TurnsMapItem item(int yIndex, int xIndex, int i)
  {
    return ((TurnsMapItem) ((Vector) ((Vector) turnsMap_.elementAt(yIndex)).elementAt(xIndex)).elementAt(i));
  }

  public String getYThemeName(int yIndex)
  {
    return (item(yIndex, 0, 0)).yThemeName_;
  }
  
  public String getXThemeName(int yIndex, int xIndex)
  {
    return (item(yIndex, xIndex, 0)).xThemeName_;
  }
  
  public int getXThemeID(int yIndex, int xIndex)
  {
    return (item(yIndex, xIndex, 0)).xThemeID_;
  }
  
  public int getYThemeID(int yIndex)
  {
    return (item(yIndex, 0, 0)).yThemeID_;
  }
  
  public int getItemID(int yIndex, int xIndex, int i)
  {
    return (item(yIndex, xIndex, i)).itemID_;
  }
  
  public int getSubthemeID(int yIndex, int xIndex, int i)
  {
    return (item(yIndex, xIndex, i)).subthemeID_;
  }
  
  public String getSubthemeName(int yIndex, int xIndex, int i)
  {
    return (item(yIndex, xIndex, i)).subthemeName_;
  }
  
  /**
   * load information from the database with the preformed query
   * @param conn the database connection manager
   * @param context the servlet context to save the list of accession numbers in
   */
  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, query());
    int ylast=-1;
    int xlast=-1;
    turnsMap_=new Vector();
    Vector yitems=null;
    Vector xitems=null;
    for (int i=0; i < getCount(); i++)
    {
      int y=((TurnsMapItem)get(i)).yThemeID_;
      int x=((TurnsMapItem)get(i)).xThemeID_;
      if (y != ylast || yitems==null) {
        yitems=new Vector();
        turnsMap_.add(yitems);
      }
      if (x != xlast || yitems.size()==0)
      {
        xitems=new Vector();
        yitems.add(xitems);
      }
      xitems.add(get(i));
      ylast=y;
      xlast=x;
    }
  }
}
