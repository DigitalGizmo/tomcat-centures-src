package museum.history.deerfield.centuries.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.actions.DispatchAction;
// import org.apache.struts.action.Action;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.torque.TorqueException;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.activity.ActivityForm;
import museum.history.deerfield.centuries.database.om.Visitor;

/**
 * Called when an author adds an item to an activity.
 */
public class ActivityNewItemSaveAction extends DispatchAction {

  public ActionForward activityEditResume( ActionMapping mapping, HttpServletRequest request, String statusLabel ) throws Exception {

    HttpSession     session         = request.getSession();
    ActivityDTO     activityDTO     = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    ActivityService activityService = new ActivityService();
    Visitor         visitor         = (Visitor) session.getAttribute( Constants.VISITOR );
    
    // The confirmation page (activityConfirm.jsp) will tailor its message to the activity's
    // changed status, which is signalled with a request attribute (drafted | submitted | published | deleted):
    request.setAttribute( "status", statusLabel );

    activityService.saveActivity( activityDTO, statusLabel, visitor );  
    session.removeAttribute( Constants.ACTIVITY_MAKE_DTO );
    return (mapping.findForward( "success" ));
  }

}
