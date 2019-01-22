/**
 * Title:        ObjectDimensions<p>
 * Description:  class for object dimensions<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ObjectDimensions.java,v 1.4 2002/04/12 14:07:36 pbrown Exp $
 *
 * $Log: ObjectDimensions.java,v $
 * Revision 1.4  2002/04/12 14:07:36  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2000/06/01 14:51:34  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.2  2000/05/24 06:11:30  pbrown
 * first implementation of item pages with images and object viewer
 *
 * Revision 1.1  2000/05/19 21:42:00  pbrown
 * added classes for item pages
 *
 * 
 */
package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.StringBuffer;

public class ObjectDimensions extends DbLoadableByID
{
  protected class Dimension implements InstanceFromResult
  {
    public String dim_;
    public float value_;
    public void init(ResultSet result) throws SQLException
    {
      dim_=result.getString("DimensionCode");
      value_=result.getFloat("Dimension");
    }
  }

  /**
   * database query
   */
  protected String view()
  {
    return "Web_ObjectDimensionView";
  }

  /**
   * creates new instance of dimension object
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    Dimension dim=new Dimension();
    dim.init(result);
    return dim;
  }
  
  /**
   * dimension code for index
   */
  public String code(int index)
  {
    return ((Dimension)get(index)).dim_;
  }

  /**
   * dimension value formatted for web display
   */
  public String value(int index)
  {
    StringBuffer disp=new StringBuffer();
    disp.append(((Dimension)get(index)).value_);
    disp.append("\"");
    return disp.toString();
  }
}
