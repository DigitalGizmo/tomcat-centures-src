package museum.history.deerfield.centuries.activity;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.BeanUtils;
import museum.history.deerfield.centuries.mycollection.MyCollectionForm;
import museum.history.deerfield.centuries.GatekeeperAction;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.activity.ActivityForm;

public class ActivityEditAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    HttpSession     session          = request.getSession();
    ActivityDTO     activityDTO      = new ActivityDTO();
    ActivityService activityService  = new ActivityService();
    ActivityForm    activityMakeForm = (ActivityForm) request.getAttribute( "activityMakeForm" );
    
    // Call on the activityService to copy the selected activity's properties from the database 
    // to the activityDTO.  The selected activity is identified by the activityID request parameter.
    int activityID = Integer.parseInt( (String) request.getParameter( "activityID" ) );    
    activityService.selectActivity( activityDTO, activityID );
    
    // Read the array of selected items from the myCollectionForm (if the user has a collection)
    // and copy them into the activityDTO.
    MyCollectionForm myCollectionForm = (MyCollectionForm) session.getAttribute( "myCollectionForm" );
    if (myCollectionForm != null) activityDTO.addItemIDs( myCollectionForm.getSelectedItemIDs() );
    session.setAttribute( Constants.ACTIVITY_MAKE_DTO, activityDTO );
    session.removeAttribute( "myCollectionForm" );  // no longer needed
    
    // Give the activityMakeForm the info it needs to display the page.
    BeanUtils.copyProperties( activityMakeForm, activityDTO );

    return (forward);  // "success" forward returned by superclass
  }
}