package museum.history.deerfield.centuries.activity;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.torque.TorqueException;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.activity.ActivityForm;
import museum.history.deerfield.centuries.activity.ActivityDTO;
import museum.history.deerfield.centuries.database.om.Visitor;

/**
 * activityMake.jsp displays two out of three possible buttons/links:  My Collection, Preview, 
 * Delete, and Cancel.  My Collection and Preview always appears.  Cancel appears only in "make" 
 * mode, as in "I changed my mind; throw away this new activity without saving it."  Delete appears 
 * only in "edit" mode, as in "remove this previously saved activity."
 *
 * Notes:
 * 
 * Deleted activities are not actually removed from the database; their status flag is simply reset.
 * 
 * Once someone commits to editing an activity, there's no way to cancel.  They can do make no changes
 * on activityMake.jsp, preview the activity, and then hit draft/submit/publish on the preview page.
 */
  
public class ActivityMakePreviewAction extends DispatchAction {
  
  /**
   * Called when user elects to return to the My Collection page in the middle of making or editing an activity.
   */
  public ActionForward pause( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    ActivityDTO  activityDTO      = (ActivityDTO)  request.getSession().getAttribute( Constants.ACTIVITY_MAKE_DTO );
    ActivityForm activityMakeForm = (ActivityForm) request.getAttribute( "activityMakeForm" );

    BeanUtils.copyProperties( activityDTO, activityMakeForm );

    return (mapping.findForward( "mycollection.show" ));    
  }

  public ActionForward preview( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // Note that two activityDTOs can coexist.  One lives in the session under the name ACTIVITY_MAKE_DTO;
    // it transfers all data involved in making, editing, and/or previewing an activity.  The other lives
    // in the request under the name ACTIVITY_VIEW_DTO; it transfers all data involved in viewing an activity.
    // A user can start to make or edit an activity, go do something else, and then return to finish up.
    // Since "something else" can be viewing a different activity, we have to use two different activityDTOs, 
    // lest the ACTIVITY_MAKE_DTO's data get overwritten by viewing data.
    
    HttpSession  session          = request.getSession();
    ActivityDTO  activityDTO      = (ActivityDTO)  session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    ActivityForm activityMakeForm = (ActivityForm) request.getAttribute( "activityMakeForm" );
    BeanUtils.copyProperties( activityDTO, activityMakeForm );

    // Function changed by Don October 2008 - makePreview now saves before showing preview
    ActivityService activityService = new ActivityService();
    Visitor         visitor         = (Visitor) session.getAttribute( Constants.VISITOR );
    // preserve current status, or default to "drafted" if new
    String statusLabel = activityDTO.getStatusLabel();
    if (statusLabel==null) {
      statusLabel = "drafted";
    }
    // receive ID from saveActivity. Not used in this case
    int activityID = activityService.saveActivity( activityDTO, statusLabel, visitor );  
    // can't clear activityDTO now because success leads to  ActivityPreviewAction which needs it

    return (mapping.findForward( "success" ));    
  }
    
  /** based on "preview". Used to save action when item is added. Forwards back to form.
   * could be called "save and return". preview method above does normal save.
   */
  public ActionForward save( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // following three lines the same as preview , but not copyProperties
    HttpSession  session          = request.getSession();
    ActivityDTO  activityDTO      = (ActivityDTO)  session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    // ActivityForm activityMakeForm = (ActivityForm) request.getAttribute( "activityMakeForm" );
    // don't copy form to dto here - we've been away to mycollection and the form is empty
    // activityDTO was already set up by the "pause" method.
    // BeanUtils.copyProperties( activityDTO, activityMakeForm );

    // now add save lines, as above
    ActivityService activityService = new ActivityService();
    Visitor         visitor         = (Visitor) session.getAttribute( Constants.VISITOR );
    String statusLabel = activityDTO.getStatusLabel();
    if (statusLabel==null) {
      statusLabel = "drafted";
    }

    System.out.println("*** before save (and return) what id activityID:" + activityDTO.getActivityID());

    // receive ID from saveActivity. Use for forward to keepMaking which now forwards to edit
    // solves problem of duplicate activites creted when we forwarded to make
    int activityID = activityService.saveActivity( activityDTO, statusLabel, visitor ); 

    // use form of return that can send parameter for edit
    return new ActionForward(mapping.findForward("keepMaking").getPath()+"?activityID="+activityID, false );
    // return (mapping.findForward( "keepMaking" ));    
  }
    
  public ActionForward cancel( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    	
    request.getSession().removeAttribute( Constants.ACTIVITY_MAKE_DTO );

    String anchor = (String) request.getSession().getAttribute( "anchor" );    
    return (mapping.findForward( anchor ));    
  }
  
  /** added by Don April 2, 2009
   */
  public ActionForward cancel_check( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    request.setAttribute( "status", "cancel_check" );
    // delete and delete_check call the same activityConfirm.jsp. only the statusLabel differs
    return (mapping.findForward( "deleted" ));
  }

  /** added by Don April 1, 2009
   */
  public ActionForward delete_check( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    // The confirmation page (activityConfirm.jsp) will tailor its message to the activity's
    // changed status, which is signalled with the request "status" attribute here:
    request.setAttribute( "status", "delete_check" );
    // delete and delete_check call the same activityConfirm.jsp. only the statusLabel differs
    return (mapping.findForward( "deleted" ));
  }

  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    HttpSession     session         = request.getSession();
    ActivityService activityService = new ActivityService();
    ActivityDTO     activityDTO     = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    Visitor         visitor         = (Visitor) session.getAttribute( Constants.VISITOR );
    String          statusLabel     = "deleted";
    
    // The confirmation page (activityConfirm.jsp) will tailor its message to the activity's
    // changed status, which is signalled with the request "status" attribute here:
    request.setAttribute( "status", statusLabel );

    request.getSession().removeAttribute( Constants.ACTIVITY_MAKE_DTO );

    // receive ID from saveActivity. Not used in this case
    int activityID = activityService.saveActivity( activityDTO, statusLabel, visitor );  
    return (mapping.findForward( "deleted" ));
  }
}

