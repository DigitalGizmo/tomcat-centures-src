package museum.history.deerfield.centuries.database.om;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.TorqueException;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Mon Oct 27 15:16:07 EST 2003]
 *
 * You should add additional methods to this class to meet the
 * application requirements.  This class will only be generated as
 * long as it does not already exist in the output directory.
 */
public class Activity extends BaseActivity implements Persistent {

  private static String DEFAULT_STATUS_LABEL = "uzer";
  private static Status DEFAULT_STATUS       = null;
  

  /**
   * Returns the default status in which all authenticated new visitors start.
   */
  public static Status getDefaultStatus() {

    if (DEFAULT_STATUS == null) {
    
      try {
        Criteria criteria = new Criteria();
        criteria.add( StatusPeer.LABEL, DEFAULT_STATUS_LABEL );
        List statuses  = StatusPeer.doSelect( criteria );
        DEFAULT_STATUS = (Status) statuses.get( 0 );
      
      } catch (TorqueException e) {
      	System.out.println( "Activity getDefaultStatus:  unable to get status for label [" + DEFAULT_STATUS_LABEL + "]" );
      }    
    }
    return (DEFAULT_STATUS);
  }


  /**
   * Note:  These next three methods are essentially database views, implemented in Torque.
   */
   
  /**
   * Returns this activity's status label.
   */
  public String getStatusLabel() {

    int    statusID    = getStatusID();
    Status status      = StatusPeer.getStatus( statusID );
    String statusLabel = status.getLabel();
    
    return (statusLabel);
  }

  /**
   * Returns this activity's author's first name.
   */
  public String getAuthorFirstName() {

    int    visitorID = getVisitorID();
  	String authorFirstName = null;
  	
    try {
      Criteria criteria = new Criteria();
      criteria.add( VisitorPeer.VISITORID, visitorID );
      List visitors   = VisitorPeer.doSelect( criteria );
      Visitor visitor = (Visitor) visitors.get( 0 );
      authorFirstName = visitor.getFirstName();
    
    } catch (TorqueException e) {
    	System.out.println( "Activity getAuthorFirstName:  unable to get author firstName for visitorID [" + visitorID + "]" );
    }
    
    return (authorFirstName);
  }

  /**
   * Returns this activity's author's last name.
   */
  public String getAuthorLastName() {

    int    visitorID = getVisitorID();
  	String authorLastName  = null;
  	
    try {
      Criteria criteria = new Criteria();
      criteria.add( VisitorPeer.VISITORID, visitorID );
      List visitors   = VisitorPeer.doSelect( criteria );
      Visitor visitor = (Visitor) visitors.get( 0 );
      authorLastName  = visitor.getLastName();
    
    } catch (TorqueException e) {
    	System.out.println( "Activity getAuthorLastName:  unable to get author lastName for visitorID [" + visitorID + "]" );
    }
    
    return (authorLastName);
  }
}