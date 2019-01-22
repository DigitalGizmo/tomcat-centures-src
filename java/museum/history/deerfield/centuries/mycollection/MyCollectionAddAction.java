package museum.history.deerfield.centuries.mycollection;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.umass.ccbit.jsp.ItemPage;
import museum.history.deerfield.centuries.database.MyCollection;

/**
 * Adds an item to the collection.  The itemID must be passed in the query string.
 */
public class MyCollectionAddAction extends Action {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    
    // Take the itemID that was sent as a query param and add it to the collection.
    int itemID_ = Integer.parseInt( request.getParameter( ItemPage.ItemID_ ) );
    
    if (itemID_ != -1) {
      HttpSession  session     = request.getSession();
    	MyCollection collection_ = (MyCollection) session.getAttribute( MyCollection.AttributeName_ );
    	
      if (collection_ == null) {
      	collection_ = new MyCollection();
        session.setAttribute( MyCollection.AttributeName_, collection_ );
      }
      
      collection_.addItem( itemID_ );
    }

    return mapping.findForward( "success" );
  }
}
