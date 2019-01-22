/**
 * Title:        ObjectAssociationsList<p>
 * Description:  class for collection associations<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ObjectAssociationsList.java,v 1.7 2002/04/12 14:07:35 pbrown Exp $
 *
 * $Log: ObjectAssociationsList.java,v $
 * Revision 1.7  2002/04/12 14:07:35  pbrown
 * fixed copyright info
 *
 * Revision 1.6  2001/03/23 16:15:08  pbrown
 * moved some views to tables for better performance
 *
 * Revision 1.5  2000/06/27 22:01:21  pbrown
 * fixed links to p/p in associated item dropdown
 *
 * Revision 1.4  2000/06/02 21:52:53  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.3  2000/06/01 14:51:34  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.2  2000/05/26 20:53:02  pbrown
 * trivial changes
 *
 * Revision 1.1  2000/05/25 22:25:08  pbrown
 * many changes for item pages including building list of associated items
 *
 */
package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ObjectAssociationsList extends DbLoadableByID
{
  /**
   * database view
   */
  protected String view()
  {
    return "Web_AssociatedObjects";
  }

  /**
   * creates new instance of dimension object
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    int type=result.getInt(PeoplePlacesItems.fields.ItemType_);
    AssociatedObjectBase assoc=AssociatedObjectBase.newInstance(type);
    assoc.PeoplePlacesItems_init(result);
    return assoc;
  }
}
