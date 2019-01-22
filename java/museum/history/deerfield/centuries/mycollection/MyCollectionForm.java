package museum.history.deerfield.centuries.mycollection;

import java.util.Vector;
import org.apache.struts.action.ActionForm;

/**
 * Bean to encapsulate a myCollection.  A myCollection comprises a Hashtable of Item beans, keyed
 * by itemID, plus an array of itemIDs that have been selected on the myCollection.jsp page.  
 */
public class MyCollectionForm extends ActionForm {

  private Vector items_           = new Vector();
  private int[]  selectedItemIDs_ = new int[0];

  public Vector getItems()           {return items_;          }
  public int[]  getSelectedItemIDs() {return selectedItemIDs_;}

  public void setItems          ( Vector items           ) {items_           = items;          }
  public void setSelectedItemIDs( int[]     selectedItemIDs ) {selectedItemIDs_ = selectedItemIDs;}
}