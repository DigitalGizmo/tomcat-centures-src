package museum.history.deerfield.centuries.database.om;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import org.apache.torque.util.Criteria;
import org.apache.torque.TorqueException;
import museum.history.deerfield.centuries.util.VectorUtil;
import museum.history.deerfield.centuries.database.om.Role;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Wed Mar 31 09:39:36 EST 2004]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class RolePeer extends BaseRolePeer {
  
  // Store a cache of the roles, so we don't have to hit the database repeatedly.
  private static HashMap allRoles_ = null;


  /**
   * Returns the Role object with the given roleID.
   */
  public static Role getRole( int roleID ) {
    HashMap allRoles = getRoles();
    Role    role     = (Role) allRoles.get( new Integer( roleID ) );
    return (role);
  }


  /**
   * Returns a Vector of Roles for an array of roleIDs.
   * If roleIDs array is empty, returns an empty Vector.
   */
  public static Vector getRoles( int[] roleIDs ) {
    Vector  selectedRoles    = new Vector();
    HashMap allRoles         = getRoles();
    int     numSelectedRoles = Array.getLength( roleIDs );
    
    for (int i=0; i<numSelectedRoles; i++) {
    	Integer roleID = new Integer( roleIDs[i] );
    	selectedRoles.add( (Role) allRoles.get( roleID ) );
    }
    return (selectedRoles);
  }


  /**
   * Returns a HashMap of all Roles, where the keys are the roleIDs, and the values
   * are the Role objects.
   */
  public static HashMap getRoles() {

    if (allRoles_ == null) {
      allRoles_ = new HashMap();
      
      try {
        Criteria criteria = new Criteria();
        criteria.addAscendingOrderByColumn( ROLEID );
        List     roles = doSelect( criteria );
        Iterator iter  = roles.iterator();
    
        while (iter.hasNext()) {
          Role role = (Role) iter.next();
          allRoles_.put( new Integer( role.getRoleID() ), role );
        }
          
      } catch (TorqueException e) {
        System.err.println( "RolePeer getRoles:  Torque exception thrown." );
      }
    }
    return (allRoles_);
  }
}