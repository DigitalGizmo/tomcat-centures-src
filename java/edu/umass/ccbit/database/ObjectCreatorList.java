/**
 * Title:        ObjectCreatorList<p>
 * Description:  list of object creators for an item<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ObjectCreatorList.java,v 1.5 2002/04/12 14:07:36 pbrown Exp $
 *
 * $Log: ObjectCreatorList.java,v $
 * Revision 1.5  2002/04/12 14:07:36  pbrown
 * fixed copyright info
 *
 * Revision 1.4  2000/06/01 14:51:34  pbrown
 * reorganization of class hierarchy
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

public class ObjectCreatorList extends DbLoadableByID
{
  /**
   * name of view
   */
  protected String view()
  {
    return ObjectCreator.view_;
  }

  /**
   * order by
   */
  protected String orderBy()
  {
    return ObjectCreator.orderBy_;
  }

  /**
   * instantiate object for list
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    ObjectCreator creator=new ObjectCreator();
    creator.init(result);
    return creator;
  }
  
  /**
   * creator name
   */
  public String name(int index)
  {
    return ((ObjectCreator)get(index)).name_;
  }

  /**
   * creator role
   */
  public String role(int index)
  {
    String role=((ObjectCreator)get(index)).creatorRole_;
    return role != null ? role : "creator";
  }

  /**
   * primary creator
   */
  public boolean primaryCreator(int index)
  {
    return ((ObjectCreator)get(index)).primaryCreator_;
  }

  /**
   * attributed
   */
  public boolean attributed(int index)
  {
    return ((ObjectCreator)get(index)).attributed_;
  }
}
