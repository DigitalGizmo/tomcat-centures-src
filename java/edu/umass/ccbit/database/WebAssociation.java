/**
 * Title:        WebAssociation<p>
 * Description:  class for web association (link/url)<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: WebAssociation.java,v 1.2 2002/04/12 14:07:43 pbrown Exp $
 *
 * $Log: WebAssociation.java,v $
 * Revision 1.2  2002/04/12 14:07:43  pbrown
 * fixed copyright info
 *
 * Revision 1.1  2000/05/26 20:54:20  pbrown
 * added classes for web associations
 *
 */
package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WebAssociation implements InstanceFromResult
{
  public static final String view_="CollectionWebAssociations";
  protected String link_;
  protected String url_;
  /**
   * initialize object from result
   */
  public void init(ResultSet result) throws SQLException
  {
    link_=result.getString("LinkName");
    url_=result.getString("URL");
  }

  /**
   * link
   */
  public String link()
  {
    return link_;
  }

  /**
   * url
   */
  public String url()
  {
    return url_;
  }
}
  
