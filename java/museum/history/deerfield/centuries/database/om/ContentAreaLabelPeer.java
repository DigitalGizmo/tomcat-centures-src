package museum.history.deerfield.centuries.database.om;

import java.lang.reflect.Array;
import java.util.Vector;
import org.apache.torque.util.Criteria;
import org.apache.torque.TorqueException;
import museum.history.deerfield.centuries.util.VectorUtil;
import museum.history.deerfield.centuries.database.om.ContentAreaLabel;

/**
 * The skeleton for this class was autogenerated by Torque on:
 *
 * [Tue Nov 04 10:27:06 EST 2003]
 *
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 */
public class ContentAreaLabelPeer extends BaseContentAreaLabelPeer {
  
  // Stores a cache of the content area label objects, so we don't have to hit the database repeatedly.
  private static Vector allContentAreaLabels_ = null;


  /**
   * Returns a Vector of ContentAreaLabels for an array of contentIDs.
   * If contentIDs array is empty, returns an empty Vector.
   */
  public static Vector getContentAreaLabels( int[] areaIDs ) {
    Vector selectedAreaLabels = new Vector();
    Vector allAreaLabels      = getContentAreaLabels();
    int    numSelectedAreas   = Array.getLength( areaIDs );
    
    // HACK ALERT:  This implementation depends upon the areaIDs in the database
    // table being sequential, non-negative numbers, starting at zero.
    for (int i=0; i<numSelectedAreas; i++) {
    	int areaID = areaIDs[i];
    	selectedAreaLabels.add( (ContentAreaLabel) allAreaLabels.get( areaID ) );
    }
    return (selectedAreaLabels);
  }


  /**
   * Returns a Vector of all ContentAreaLabels.
   */
  public static Vector getContentAreaLabels() {

    if (allContentAreaLabels_ == null) {
      allContentAreaLabels_ = new Vector();

      try {
        Criteria criteria = new Criteria();
        criteria.addAscendingOrderByColumn( CONTENTID );    
        VectorUtil.listToVector( doSelect( criteria ), allContentAreaLabels_ );
          
      } catch (TorqueException e) {
        System.err.println( "ContentAreaLabelPeer getContentAreaLabels:  Torque exception thrown." );
      }
    }
    return (allContentAreaLabels_);
  }
}
