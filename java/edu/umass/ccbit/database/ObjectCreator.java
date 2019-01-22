/**
 * Title:        ObjectCreator<p>
 * Description:  base class for object creators<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ObjectCreator.java,v 1.4 2002/04/12 14:07:35 pbrown Exp $
 *
 * $Log: ObjectCreator.java,v $
 * Revision 1.4  2002/04/12 14:07:35  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2000/05/25 22:25:08  pbrown
 * many changes for item pages including building list of associated items
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

public class ObjectCreator implements InstanceFromResult
{
  public int itemID_;
  public String creatorRole_;
  public boolean primaryCreator_;
  public boolean attributed_;
  public String name_;
  public int dob_;
  public int dod_;

  public static final String view_="Web_ObjectCreatorView";
  public static final String orderBy_="PrimaryCreator";

  /*
   * instantiate new object creator from result
   */
  public void init(ResultSet result) throws SQLException
  {
    itemID_=result.getInt("ItemID");
    creatorRole_=result.getString("CreatorRole");
    primaryCreator_=result.getBoolean("PrimaryCreator");
    attributed_=result.getBoolean("Attributed");
    dob_=result.getInt("DOB");
    dod_=result.getInt("DOD");
    int collectionPersonID=result.getInt("CollectionPersonID");
    int objectProducerID=result.getInt("ObjectProducerID");
    StringBuffer nameBuf=new StringBuffer();
    if (collectionPersonID > 0)
    {
      String first=result.getString("FirstName");
      String mid=result.getString("MiddleName");
      String last=result.getString("LastName");
      if (first != null)
        nameBuf.append(first);
      // need to check for nulls so we don't get a name like
      // 'Ellen null Miller'
      if (mid != null)
      {
        if (nameBuf.length()>0)
          nameBuf.append(" ");
        nameBuf.append(mid);
      }
      if (last != null)
      {
        if (nameBuf.length()>0)
          nameBuf.append(" ");
        nameBuf.append(last);
      }
      if (dob_ != 0 && dod_ != 0)
      {
        nameBuf.append(" ").append(CollectionPerson.formatLifeDates(dob_, dod_));
      }
    }
    else
    {
      nameBuf.append(result.getString("ObjectProducerName"));
    }
    if (attributed_)
      nameBuf.append(" (attributed)");
    name_=nameBuf.toString();
  }
}
