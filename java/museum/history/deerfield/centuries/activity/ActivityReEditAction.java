package museum.history.deerfield.centuries.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.BeanUtils;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.NavigatorActionMapping;
import museum.history.deerfield.centuries.GatekeeperAction;
import museum.history.deerfield.centuries.activity.ActivityForm;
import museum.history.deerfield.centuries.activity.ActivityDTO;
import museum.history.deerfield.centuries.database.om.Visitor;

public class ActivityReEditAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    // See Keith's ActivityMakeAction.
    // This handles handles a THIRD instance of activityDTO, ACTIVITY_STATUS_DTO, which is created when
    // an activity is saved (which now happens Before preview)
    // We need to get copy the Status one back to the Make one, plus clear the Status one
    // Otherwise ActivityMakeAction will be confused if you try to make a new activity
    
    // Just put activityStatusDTO "back" into ACTIVITY_MAKE_DTO
    // don't need to address the form - ActivityMakeAction (which is called next) will do that

    HttpSession  session      = request.getSession();
    ActivityDTO  activityDTO  = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_STATUS_DTO );

    session.setAttribute( Constants.ACTIVITY_MAKE_DTO, activityDTO );
    session.removeAttribute( Constants.ACTIVITY_STATUS_DTO );
     
    return (forward);  // "success" forward returned by superclass
  }
}