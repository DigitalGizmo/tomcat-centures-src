/*
 * Title:        HttpDbJspBase<p>
 * Description:  base class for jsp pages which provides database connection as well as easy access to initialization and servlet parameters<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: HttpDbJspBase.java,v 1.23 2003/01/16 03:05:25 pb Exp $
 *
 * $Log: HttpDbJspBase.java,v $
 * Revision 1.23  2003/01/16 03:05:25  pb
 * added mrsidinfo.root init param to allow hostname to be contacted for image info to be specified
 *
 * Revision 1.22  2002/04/12 14:07:55  pbrown
 * fixed copyright info
 *
 * Revision 1.21  2001/10/25 18:50:20  pbrown
 * changes sid root initialization
 *
 * Revision 1.20  2001/09/28 15:05:44  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.19  2001/09/12 19:49:59  Administrator
 * added UserException to throws clause of parseRequestParameters
 *
 * Revision 1.18  2001/05/24 14:04:56  pbrown
 * added mrsid root member to httpdbjspbase...no more init servlet
 *
 * Revision 1.17  2001/04/10 13:08:05  pbrown
 * some bug fixs
 *
 * Revision 1.16  2001/02/26 16:47:36  pbrown
 * changes for searching, and relocation of www root
 *
 * Revision 1.15  2001/01/05 21:28:30  pbrown
 * finished error handler code
 *
 * Revision 1.14  2001/01/05 14:41:47  pbrown
 * added automatic error handler
 *
 * Revision 1.13  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.12  2000/08/02 20:45:04  pbrown
 * fixed import for connection mangler
 *
 * Revision 1.11  2000/08/02 20:43:30  pbrown
 * changes for new db connection mangler
 *
 * Revision 1.10  2000/06/27 16:14:39  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.9  2000/06/02 21:52:55  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.8  2000/05/25 22:25:09  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.7  2000/05/22 15:22:30  pbrown
 * changes reference to TcpDatabaseConnectionMgr, now located in edu.umass.ckc.database
 * package and called JdbcConnectionMgr
 *
 * Revision 1.6  2000/05/18 18:53:51  pbrown
 * added mrSidArchive_ instance variable
 *
 * Revision 1.5  2000/05/18 02:32:53  pbrown
 * changes due to moved packages
 *
 * Revision 1.4  2000/05/15 18:27:29  pbrown
 * removed old vj++ and forte4j project files, added jbuilder files
 *
 * Revision 1.3  2000/05/12 22:47:54  pbrown
 * many changes, rewritten for sprinta db driver
 *
 * Revision 1.2  2000/05/09 13:28:54  pbrown
 * fixed db connection in jsp base class
 *
 * Revision 1.1  2000/05/05 21:21:32  pbrown
 * rebuilt repository
 *
 * Revision 1.3  2000/05/04 14:13:32  pbrown
 * uses JspInitParams instead of InitParams...added cvs info
 *
 * Revision 1.2  2000/04/28 14:54:41  pbrown
 * added documentation, cvs info
 *
 */

/*
  run jdb like-a this-a:

-Dtomcat.home=H:/Apache/tomcat-debug -classpathH:/Apache/tomcat/lib/servlet.jar;H:/Apache/tomcat/lib/xml.jar;H:/Apache/tomcat/lib/webserver.jar;H:/Apache/tomcat/lib/jasper.jar;H:/Apache/tomcat/webapps/pvma/WEB-INF/classes;E:/pvma/JavaSource/sprinta org.apache.tomcat.startup.Tomcat

Java (1.1.8) classpath:

.;H:\Java\jdk1.1.8\lib\classes.zip;H:\Java\JavaSoft\JRE\1.1\lib\rt.jar;E:\JavaSource\1.1collections\lib\collections.jar;E:\JavaSource\xml\lib-tr2\xml.jar

Java2 classpath:

.;H:\Java2\jdk1.2.2\lib\tools.jar;H:\Java2\jdk1.2.2\lib\dt.jar;H:\Java2\JavaSoft\JRE\1.2\lib\rt.jar;E:\JavaSource\1.1collections\lib\collections.jar;E:\JavaSource\xml\lib-tr2\xml.jar

*/

package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.NavigationBar;
import edu.umass.ccbit.util.JspInitParams;
import edu.umass.ccbit.util.SessionNavigation;
import edu.umass.ccbit.image.MrSidImage;
import edu.umass.ckc.database.JdbcConnectionMgr;
import edu.umass.ckc.database.BaseConnectionMgr;
import edu.umass.ckc.servlet.ServletParams;
import edu.umass.ckc.servlet.ServletParser;
import edu.umass.ckc.util.CkcException;
import java.io.IOException;
import java.lang.Exception;
import java.sql.Connection;
import javax.servlet.http.HttpServletRequest;
import org.apache.jasper.runtime.HttpJspBase;
import java.sql.SQLException;
import edu.umass.ckc.util.UserException;

public abstract class HttpDbJspBase extends HttpJspBase
{
  // special parameter values common to each jsp
  protected String tempDirectory=null;
  protected int maxContentLength=1048576;
  protected String URI_;
  // parameter objects - init parameters (from xml), and servlet parameters
  protected JspInitParams initParams_;
  protected ServletParams servletParams_;
  // database access
  protected JdbcConnectionMgr connectionMgr_;
  protected Connection connection_;
  public static String mrSidRoot=null;
  public static String mrSidInfoRoot=null;

  /**
   * initializes jsp page
   */
  public void jspInit()
  {
    try
    {
      initParams_=new JspInitParams();
      initParams_.storeParameters(getServletConfig());
      if (mrSidRoot==null)
      {
        mrSidRoot=initParams_.getString("mrsid.root", "");
        mrSidInfoRoot=initParams_.getString("mrsidinfo.root", "");
        MrSidImage.setMrSidRoot(mrSidRoot, mrSidInfoRoot);
      }
      tempDirectory=initParams_.getString("temp.directory", "");
      maxContentLength=initParams_.getInt("max.contentlength", 0);
      connectionMgr_=(JdbcConnectionMgr) BaseConnectionMgr.getConnectionMgr(initParams_, null);
    }
    catch (Exception e)
    {
      // i will croak later (can't throw exception from here)
      System.out.println("In HttpDbJspBase.java " + e.getMessage());
      e.printStackTrace(System.out);
      
    }
  }

  /**
   * format request for inclusion in session history
   */
  protected String formatRequest(HttpServletRequest request, ServletParams params) throws CkcException
  {
    StringBuffer buf=new StringBuffer();
    String server=request.getServerName();

    buf.append("http://");
    buf.append(server);
    buf.append(request.getRequestURI());
    String query=params.getQueryString();
    if (query.length() > 0)
    {
      buf.append("?").append(query);
    }
    return buf.toString();
  }

  /**
   * add navigation item to session...trivially override to
   * prevent item from being added (e.g., for error page)
   */
  protected void addNavigationItem(HttpServletRequest request, ServletParams params) throws CkcException
  {
    SessionNavigation.addItem(request.getSession(), formatRequest(request, params));
  }

  /**
   * get servlet request parameters into ServletParams object
   * @param request the servlet request
   */
  protected synchronized void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    // save this uri
    URI_=request.getRequestURI();
    servletParams_ = new ServletParams();
      ServletParser parser=new ServletParser(maxContentLength, tempDirectory);
    parser.parse(request, servletParams_);
    // this is for debug only
    addNavigationItem(request, servletParams_);
  }

  /**
   * get database connection
   */
  synchronized protected void getDbConnection() throws Exception 
  {
    connection_=connectionMgr_.getConnection();
  }
  
  /**
   * release database connection
   */
  synchronized protected void releaseDbConnection() throws CkcException
  {
    connectionMgr_.releaseConnection(connection_);
  }
}

