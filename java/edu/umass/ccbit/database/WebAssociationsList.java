/**
 * Title:        WebAssociationsList<p>
 * Description:  class for web associations<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: WebAssociationsList.java,v 1.3 2002/04/12 14:07:44 pbrown Exp $
 *
 * $Log: WebAssociationsList.java,v $
 * Revision 1.3  2002/04/12 14:07:44  pbrown
 * fixed copyright info
 *
 * Revision 1.2  2000/06/01 14:51:35  pbrown
 * reorganization of class hierarchy
 *
 * Revision 1.1  2000/05/26 20:54:20  pbrown
 * added classes for web associations
 *
 */
package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.umass.ccbit.database.InstanceFromResult;

public class WebAssociationsList extends DbLoadableByID
{
  /**
   * database view
   */
  protected String view()
  {
    return WebAssociation.view_;
  }

  /**
   * creates new instance of dimension object
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    WebAssociation assoc=new WebAssociation();
    assoc.init(result);
    return assoc;
  }
}
