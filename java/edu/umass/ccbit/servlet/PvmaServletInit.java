/**
 * Title:        PvmaServletInit<p>
 * Description:  this servlet is loaded when the servlet engine starts up
 *               and its init() method is executed at that time..therefore,
 *               any database initialization etc. can be performed here
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: PvmaServletInit.java,v 1.7 2002/04/12 14:08:13 pbrown Exp $
 * @deprecated this servlet is no longer used...db initialization is done via an sql script
 */
package edu.umass.ccbit.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import edu.umass.ccbit.util.JspInitParams;
import edu.umass.ckc.database.BaseConnectionMgr;
import edu.umass.ckc.database.JdbcConnectionMgr;
import edu.umass.ckc.util.CkcException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class PvmaServletInit extends HttpServlet
{
  protected Connection connection;
  public static String siteRoot;

  /**
   * execute query
   */
  protected void databaseInitUpdateQuery(Statement stmt, String query) throws SQLException
  {
    int rows=stmt.executeUpdate(query);
    System.out.print("databaseInit: ");
    System.out.print(rows);
    System.out.println(" row(s) affected");
  }

  /**
   * execute query
   */
  protected void databaseInitExecuteQuery(Statement stmt, String query) throws SQLException
  {
    boolean result=stmt.execute(query);
  }

/* queries to initialize the database...should be run each time database is updated */
/*
update Main set Searchable=0
go

update Main set Searchable=1 where ItemID in (select ItemID from Web_SearchableItemsView)
go


delete from Web_AssociatedObjects
go

insert into Web_AssociatedObjects select * from Web_AssociatedObjectsView
go

delete from Web_OrderedItemImage
go

insert into Web_OrderedItemImage select * from Web_OrderedItemImageView
go

delete from Web_PeoplePlaces
go

insert into Web_PeoplePlaces select * from Web_PeoplePlacesView
go

SET CONCAT_NULL_YIELDS_NULL OFF update collectionpeople set firstlastname=rtrim(ltrim(rtrim(firstname) + ' ' + ltrim(lastname)))
go

SET CONCAT_NULL_YIELDS_NULL OFF update collectionpeople set fullname=rtrim(rtrim(ltrim(firstname)) + ' ' + rtrim(ltrim(middlename))) + ' ' + rtrim(ltrim(lastname))
go

sp_fulltext_catalog @ftcat='PVMACatalog', @action='start_incremental'
go

*/

  /**
   * initialize database
   * this initializes the 'Searchable' field in the Main table to indicate which items in the
   * main table can be returned as search results...the Web_SearchableItemsView view returns
   * item IDs of these items
   */
  protected void databaseInit() throws SQLException
  {
    Statement stmt=connection.createStatement();
    stmt.setQueryTimeout(60);
    // update searchable flags in main table
    databaseInitUpdateQuery(stmt, "update Main set Searchable=0");
    databaseInitUpdateQuery(stmt, "update Main set Searchable=1 where ItemID in (select ItemID from Web_SearchableItemsView)");
    // read associated objects view into table
    databaseInitExecuteQuery(stmt, "delete from Web_AssociatedObjects");
    databaseInitExecuteQuery(stmt, "insert into Web_AssociatedObjects select * from Web_AssociatedObjectsView");
    // read ordered item image view into table
    databaseInitExecuteQuery(stmt, "delete from Web_OrderedItemImage");
    databaseInitExecuteQuery(stmt, "insert into Web_OrderedItemImage select * from Web_OrderedItemImageView");
    // read people/places items into table
    databaseInitExecuteQuery(stmt, "delete from Web_PeoplePlaces");
    databaseInitExecuteQuery(stmt, "insert into Web_PeoplePlaces select * from Web_PeoplePlacesView");
    // setup search for collection people
    databaseInitUpdateQuery(stmt, "SET CONCAT_NULL_YIELDS_NULL OFF update collectionpeople set firstlastname=rtrim(ltrim(rtrim(firstname) + ' ' + ltrim(lastname)))");
    databaseInitUpdateQuery(stmt, "SET CONCAT_NULL_YIELDS_NULL OFF update collectionpeople set fullname=rtrim(rtrim(ltrim(firstname)) + ' ' + rtrim(ltrim(middlename))) + ' ' + rtrim(ltrim(lastname))");
    databaseInitExecuteQuery(stmt, "sp_fulltext_catalog @ftcat='PVMACatalog', @action='start_incremental'");
    stmt.close();
  }

  /**
   * override init to initialize web application
   */
  public void init(ServletConfig config) throws ServletException
  {
    System.out.println("PvmaServletInit: initializing pvma web application!");
    super.init(config);
    JspInitParams initParams=new JspInitParams();
    initParams.storeParameters(getServletConfig());
    siteRoot=initParams.getString("site.root", "");
    try
    {
      JdbcConnectionMgr connectionMgr=(JdbcConnectionMgr) BaseConnectionMgr.getConnectionMgr(initParams, null);
      connection=connectionMgr.getConnection();
      databaseInit();
      connectionMgr.releaseConnection(connection);
    }
    catch (Exception e)
    {
      System.out.println("### WARNING: pvma web application initialization failed!");
      System.out.print("Reason: ");
      System.out.println(e.getMessage());
      throw new ServletException(e.getMessage());
    }
  }
}
