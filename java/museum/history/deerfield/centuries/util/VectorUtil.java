package museum.history.deerfield.centuries.util;

import java.util.List;
import java.util.Vector;
import java.lang.reflect.Array;

public class VectorUtil {
  
  /**
   * Copies the contents of a List into a Vector.
   */
  public static Vector listToVector( List list, Vector vector ) {
  	
    int numElements = list.size();

    for (int i=0; i<numElements; i++)
      vector.add( list.get(i) );
      
    return (vector);
  }
  
  /**
   * Converts an array of ints to a Vector of Integers.
   */
  public static Vector arrayToVector( int[] array ) {

  	int    length = Array.getLength( array );
  	Vector vector = new Vector( length );
  	
  	for (int i=0; i<length; i++) {
  		Integer integer = new Integer( array[i] );
  		vector.add( integer );
  	}
  	
  	return (vector);
  }

  /**
   * Converts a Vector of Integers to an array of ints.
   */
  public static int[] vectorToArray( Vector vector ) {

  	int   length = vector.size();
    int[] array  = new int[length];
    
    for (int i=0; i<length; i++)
    	array[i] = ((Integer) vector.get( i )).intValue();
    	
  	return (array);
  }
}
