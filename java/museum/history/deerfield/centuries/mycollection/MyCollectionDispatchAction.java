package museum.history.deerfield.centuries.mycollection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import museum.history.deerfield.centuries.mycollection.MyCollectionForm;
import museum.history.deerfield.centuries.database.MyCollection;
import museum.history.deerfield.centuries.activity.ActivityDTO;
import museum.history.deerfield.centuries.Constants;

/**
 * Handles various submissions from myCollection.jsp's form.
 */
public class MyCollectionDispatchAction extends DispatchAction {

  /**
   * Called when user clicks link to delete selected items on myCollection.jsp.
   */
  public ActionForward deleteItems( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    
    HttpSession      session          = request.getSession();
    MyCollection     collection_      = (MyCollection) session.getAttribute( MyCollection.AttributeName_ );
    MyCollectionForm myCollectionForm = (MyCollectionForm) request.getAttribute( "myCollectionForm" );
    int[]            itemIDs          = myCollectionForm.getSelectedItemIDs();
    
    if (collection_ == null) {
    	collection_ = new MyCollection();
      session.setAttribute( MyCollection.AttributeName_, collection_ );
    }

    collection_.removeItems( itemIDs ); 
    return (mapping.findForward( "items.delete" ));
  }

  /**
   * Called when user chooses to make a new activity with the selected items on the mycollection page.
   */
  public ActionForward activityMake( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    
    HttpSession      session          = request.getSession();
    MyCollectionForm myCollectionForm = (MyCollectionForm) request.getAttribute( "myCollectionForm" );
    ActivityDTO      activityDTO      = new ActivityDTO();
    
    // Tell the DTO which items have been tagged on the mycollection page.
    activityDTO.setItemIDs( myCollectionForm.getSelectedItemIDs() );
    session.setAttribute( Constants.ACTIVITY_MAKE_DTO, activityDTO );
    
    //  We're done with myCollectionForm.
    request.removeAttribute( "myCollectionForm" );

    return (mapping.findForward( "activity.make" ));
  }

  /**
   * Called when user chooses to add selected collection items to an existing activity.  (User must pick an activity
   * from the edit list.)
   */
  public ActionForward activityEditList( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    
    // We'll need to hang onto myCollectionForm, which has items selected, until the user picks an activity to edit.
    // Move the form out of the request and into the session.
    HttpSession      session          = request.getSession();
    MyCollectionForm myCollectionForm = (MyCollectionForm) request.getAttribute( "myCollectionForm" );
    session.setAttribute( "myCollectionForm", myCollectionForm );
    request.removeAttribute( "myCollectionForm" );

    return (mapping.findForward( "activity.edit.list" ));
  }

  /**
   * Called when user chooses to add selected collection items to an activity currently being edited.
   */
  public ActionForward activityEditResume( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    
    HttpSession      session          = request.getSession();
    MyCollectionForm myCollectionForm = (MyCollectionForm) request.getAttribute( "myCollectionForm" );
    ActivityDTO      activityDTO      = (ActivityDTO)      session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    
    // Tell the DTO which items have been newly tagged on the mycollection page.
    activityDTO.addItemIDs( myCollectionForm.getSelectedItemIDs() );
    
    System.out.println("** in activityDTO stil full after add items if shortDescription appears: " + activityDTO.getShortDescription());

    //  We're done with myCollectionForm.
    request.removeAttribute( "myCollectionForm" );

    /** October 2008 - Don
     * new feature: save the activity form now, Teachers assume that this addition of items 
     * will "stick."
     * just changed the forward from activity.make to the one below
     * In struts-config the forward includes method=save
     */

    return (mapping.findForward( "activity.make.preview" ));
  }

  /*
   * Handles case where method fails to get set.
   */
  public ActionForward unspecified( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    return error( mapping, form, request, resp );
  }

  public ActionForward error( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    return mapping.findForward( "error" );
  }
}
