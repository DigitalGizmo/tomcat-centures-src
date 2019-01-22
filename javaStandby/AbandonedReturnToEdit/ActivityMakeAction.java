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

public class ActivityMakeAction extends GatekeeperAction {

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
    
    HttpSession  session          = request.getSession();
    ActivityDTO  activityDTO      = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    ActivityForm activityMakeForm = (ActivityForm) form;
     
    // If we got here from the mycollection page, the activityDTO will have already been created.
    // Otherwise, it may not yet exist.
    // added by Don 2008/10/27: We may come here from Preview - return to edit. 
    // In this case we need to retreive ACTIVITY_MAKE_DTO from ACTIVITY_STATUS_DTO
    if (activityDTO == null) {
      ActivityDTO  activityStatusDTO  = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_STATUS_DTO );
      if (activityStatusDTO != null) { // we are coming from the preview page- reset  activityDTO
        // set form for return to edit from "status" copy
        BeanUtils.copyProperties( activityMakeForm, activityStatusDTO );
        /// ack!! do I need put ACTIVITY_MAKE_DTO back into the session?
        /// if so will it mess up clean create?

        // remove ACTIVITY_STATUS_DTO, otherwise it will linger and interfere if you
        // try to create a new activity
        session.removeAttribute( Constants.ACTIVITY_STATUS_DTO );
      } else { // create a new one
     	activityDTO = new ActivityDTO();
        session.setAttribute( Constants.ACTIVITY_MAKE_DTO, activityDTO );
        BeanUtils.copyProperties( activityMakeForm, activityDTO );
      }
    } else { // valid activityDTO in session
      // Give the activityMakeForm the info it needs to display the page.
      BeanUtils.copyProperties( activityMakeForm, activityDTO );
    }
    // Tell the DTO the author's name.  The preview page will need to display it.
    Visitor visitor = (Visitor) session.getAttribute( Constants.VISITOR );
    activityDTO.setAuthorFirstName( visitor.getFirstName() );
    activityDTO.setAuthorLastName(  visitor.getLastName()  );
    

    return (forward);  // "success" forward returned by superclass
  }
}