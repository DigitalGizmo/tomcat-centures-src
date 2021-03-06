package museum.history.deerfield.centuries.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.BeanUtils;
import museum.history.deerfield.centuries.GatekeeperAction;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.activity.ActivityForm;
import museum.history.deerfield.centuries.activity.ActivityDTO;

/**
 * Shows activity in its current state.
 */
public final class ActivityViewAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    ActivityDTO     activityDTO      = new ActivityDTO();
    ActivityService activityService  = new ActivityService();
    ActivityForm    activityViewForm = (ActivityForm) request.getAttribute( "activityViewForm" );
    
    int activityID = Integer.parseInt( (String) request.getParameter( "activityID" ) );    
    activityService.selectActivity( activityDTO, activityID );
    
    BeanUtils.copyProperties( activityViewForm, activityDTO );
    
    // Note that two activityDTOs can coexist.  One lives in the session under the name ACTIVITY_MAKE_DTO;
    // it transfers all data involved in making, editing, and/or previewing an activity.  The other lives
    // in the request under the name ACTIVITY_VIEW_DTO; it transfers all data involved in viewing an activity.
    // A user can start to make or edit an activity, go do something else, and then return to finish up.
    // Since "something else" can be viewing a different activity, we have to use two different activityDTOs, 
    // lest the one used for making activities get overwritten by the one used for viewing them.
    
    request.setAttribute( Constants.ACTIVITY_VIEW_DTO, activityDTO );
    
    return (forward);  // "success" forward returned by superclass
  }
}