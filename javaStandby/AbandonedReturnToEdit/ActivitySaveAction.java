package museum.history.deerfield.centuries.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.torque.TorqueException;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.activity.ActivityForm;
import museum.history.deerfield.centuries.database.om.Visitor;
// for sending mail on submit
import museum.history.deerfield.centuries.util.SendMailUtil;
import org.apache.struts.util.MessageResources;

/**
 * Called when an author or admin sets or changes the status of an activity.
 */
public class ActivitySaveAction extends DispatchAction {


  public ActionForward getForward( ActionMapping mapping, HttpServletRequest request, String statusLabel, boolean sendNotice ) throws Exception {

    HttpSession     session         = request.getSession();
    ActivityDTO     activityDTO     = (ActivityDTO) session.getAttribute( Constants.ACTIVITY_MAKE_DTO );
    ActivityService activityService = new ActivityService();
    Visitor         visitor         = (Visitor) session.getAttribute( Constants.VISITOR );
    
    // The confirmation page (activityConfirm.jsp) will tailor its message to the activity's
    // changed status, which is signalled with a request attribute (drafted | submitted | published | deleted):
    request.setAttribute( "status", statusLabel );

    // receive ID from saveActivity. Not used in this case
    int activityID = activityService.saveActivity( activityDTO, statusLabel, visitor );  
    // ** session.removeAttribute( Constants.ACTIVITY_MAKE_DTO );

    // send notice if activity is being "submitted"
    if (sendNotice) {
      // get email addresses from resources/application.properties
      MessageResources messageResources = MessageResources.getMessageResources( "resources.application" );
      SendMailUtil sendMail = new SendMailUtil();
      String[] sendTo = new String[] {messageResources.getMessage("activity.notification.address1"), 
                                      messageResources.getMessage("activity.notification.address2")};  
      int sendSuccess = sendMail.postMailAuth(sendTo, "Activity posted",  
          "An activity titled \""+activityDTO.getTitle()+ "\" was submitted by "+activityDTO.getAuthorFirstName()+" "+activityDTO.getAuthorLastName(), 
          messageResources.getMessage("activity.notification.from"));
      // set message 
      String notificationMsg = null;
      if (sendSuccess > 0) {
        notificationMsg = "An email has been sent to the adminstrator.";
      } else {
        notificationMsg = "Your activity has been submitted, but due to an error an email message was not sent to the administrator";
      }
      request.setAttribute( "notificationMsg", notificationMsg );
    }
    return (mapping.findForward( "success" ));
  }

  public ActionForward draft( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {    	
    return (getForward( mapping, request, "drafted", false ));
  }

  public ActionForward submit( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    // System.out.println("*** ActivitySaveAction : got to submit");
   // sendNotice = true;

    return (getForward( mapping, request, "submitted", true ));
  }

  public ActionForward publish( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    return (getForward( mapping, request, "published", false ));
  }

  public ActionForward delete( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {
    return (getForward( mapping, request, "deleted", false ));
  }
}