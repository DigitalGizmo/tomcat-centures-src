package museum.history.deerfield.centuries.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
 * Shows activity in its current state while user is making or editing it.
 */
public final class ActivityPreviewAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    // Note that two activityDTOs can coexist.  One lives in the session under the name ACTIVITY_MAKE_DTO;
    // it transfers all data involved in making, editing, and/or previewing an activity.  The other lives
    // in the request under the name ACTIVITY_VIEW_DTO; it transfers all data involved in viewing an activity.
    // A user can start to make or edit an activity, go do something else, and then return to finish up.
    // Since "something else" can be viewing a different activity, we have to use two different activityDTOs, 
    // lest the ACTIVITY_MAKE_DTO's data get overwritten by viewing data.
    
    ActivityDTO activityDTO = (ActivityDTO) request.getSession().getAttribute( Constants.ACTIVITY_MAKE_DTO );
    
    // It would be more consistent to call this form "activityPreviewForm", but the same JSP -- activityView.jsp
    // -- is used for both viewing and previewing, and it knows only to look for a single bean named 
    // "activityViewForm".
    ActivityForm activityViewForm = (ActivityForm) request.getAttribute( "activityViewForm" );

    BeanUtils.copyProperties( activityViewForm, activityDTO );

    return (forward);  // "success" forward returned by superclass
  }
}