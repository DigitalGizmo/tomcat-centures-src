
/**
 * Title:        Materials<p>
 * Description:  materials, read from database<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.ServletContext;

public class Materials extends DbLoadableItemList
{
  protected class Material implements InstanceFromResult
  {
    // attributes
    public int materialID_;
    public String materialName_;

    /**
     * initialize from result set
     */
    public void init(ResultSet result) throws SQLException
    {
      materialID_=result.getInt("ProcessMaterialID");
      materialName_=result.getString("Description");
    }
  }

  /**
   * material name
   */
  public String getMaterialName(int index)
  {
    return ((Material)get(index)).materialName_;
  }

  /**
   * get the material
   * @param key the id to find the material by
   * @return the material
   */
  public String getMaterial(int key)
  {
    Enumeration e = items_.elements();
    Material item;
    while(e.hasMoreElements())
    {
      item=(Material) e.nextElement();
      if(item.materialID_ == key)
        return item.materialName_;
    }
    return "";
  }

  /**
   * material id
   */
  public int getMaterialID(int index)
  {
    return ((Material)get(index)).materialID_;
  }

  protected InstanceFromResult newInstance(ResultSet result) throws java.sql.SQLException
  {
    Material material = new Material();
    material.init(result);
    return material;
  }

  public int numItems()
  {
    return items_.size();
  }

  public void load(Connection conn, ServletContext context) throws SQLException
  {
    load(conn, context, "SELECT * FROM Web_SearchableMaterialsView WHERE Description IS NOT NULL ORDER BY Description");
  }
}
