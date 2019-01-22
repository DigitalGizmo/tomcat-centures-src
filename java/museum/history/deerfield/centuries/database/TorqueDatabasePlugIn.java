package museum.history.deerfield.centuries.database;

import javax.servlet.ServletException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.torque.TorqueException;
import org.apache.torque.Torque;

public final class TorqueDatabasePlugIn implements PlugIn {

  private Log log = LogFactory.getLog( this.getClass() );

  /**
   * The web application resource path of our persistent database
   * storage file.  (Set in struts-config.xml.)
   */
  private String torqueProperties = null;

  public String getTorqueProperties() {
    return (this.torqueProperties);
  }

  public void setTorqueProperties( String torqueProperties ) {
    this.torqueProperties = torqueProperties;
  }

  /**
   * Initialize our database.
   *
   * @param servlet The ActionServlet for this web application
   * @param config The ApplicationConfig for our owning module
   *
   * @exception ServletException if we cannot configure ourselves correctly
   */
  public void init( ActionServlet servlet, ModuleConfig config ) throws ServletException {
  	
    log.info( "Initializing Torque database from '" + torqueProperties + "'" );

    try {
      Torque.init( torqueProperties );      
    } catch (TorqueException e) {
      log.error( "Initializing database", e );
      throw new ServletException( "Cannot initialize database from '" + torqueProperties + "'", e );
    }
  }
  
  /**
   * Shut down this database.
   */
  public void destroy() {
    log.info( "Finalizing database plugin." );
  }
}
