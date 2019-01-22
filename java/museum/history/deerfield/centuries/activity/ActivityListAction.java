package museum.history.deerfield.centuries.activity;

import java.util.Vector;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.beanutils.BeanUtils;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.GatekeeperAction;
import museum.history.deerfield.centuries.activity.ActivityService;
import museum.history.deerfield.centuries.activity.ActivityListForm;
import museum.history.deerfield.centuries.activity.ActivityListDTO;
import museum.history.deerfield.centuries.database.om.Visitor;

/**
 * Displays a list of Activities.
 */
public class ActivityListAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    // Each activity displayed on the activityList.jsp page has its own ActivityForm, which in turn is mapped
    // to its own ActivityDTO, which in turn is mapped to its own ActivityDAO.  The ActivityForms are bundled
    // together into a single ActivityListForm, and the ActivityDTOs are bundled into a single ActivityListDTO.
    ActivityListForm activityListForm = (ActivityListForm) form;
    ActivityService  activityService  = new ActivityService();
    ActivityListDTO  activityListDTO  = new ActivityListDTO();

    int              pageNum          = 0;
    // handle page number
    Integer pageNumInteger = (Integer)activityListForm.getPageNum();
    if (pageNumInteger != null) {
      pageNum = pageNumInteger.intValue();
      // if pageNum isn't passed then form will send zero which isn't valid.
      if (pageNum < 1) { pageNum = 1; }
    } else {
      pageNum = 1; // default to page 1 
      // but this isn't likely to get triggered - the form passes a zero by default
    }

    // Given an empty ActivityListDTO, an ActivityService is responsible for generating a collection of ActivityDTOs
    // and populating them with values from the database.
    //
    // The specific selection of activities depends on a filter:  gradeLevel, contentArea, historicalEra, author, and 
    // all.  The first four require filterValues, such as eraID or visitorID.  The author filter is set when the
    // action-mapping has a set-property mode of "edit"; the other filters come from form submissions.
    String mode = (String) request.getAttribute( "mode" );
    
    if (("edit").equals( mode )) {
    	Visitor visitor   = (Visitor) request.getSession().getAttribute( Constants.VISITOR );
    	String  roleLabel = visitor.getRoleLabel();
    	
    	if (roleLabel.equals( "admin" )) {
          String authorLastName = activityListForm.getAuthorLastName();
          if ("".equals(authorLastName) || authorLastName==null) {   // blank
            activityService.selectAllActivities( activityListDTO, pageNum, activityListForm );  // admins can edit all activities
          } else {  // a search name has been entered
            // activityListForm contains last name
            activityService.selectAuthorNameActivities( activityListDTO, pageNum, activityListForm );  // authors can edit only their own activities

          }
    	} else {  // not admin, but still edit - get activities for one author by ID
          int authorID = visitor.getVisitorID();
    	  activityService.selectAuthorActivities( activityListDTO, pageNum, activityListForm, authorID );  // authors can edit only their own activities
    	}
    	
    } else {
      // status of 2 means only published activities will be retrieved
      activityService.selectActivities( activityListDTO, pageNum, activityListForm, 2 );
    }
    
    // Save the ActivityListDTO in the request; ActivityListPage will need it.
    request.setAttribute( Constants.ACTIVITY_LIST_DTO, activityListDTO );

    // Generate a collection of ActivityForms, i.e., an ActivityListForm.  To do this, we loop through the 
    // ActivityDTOs just populated inside the the ActivityListDTO, copying their properties to corresponding
    // ActivityForms one by one.
    Vector activityDTOs  = activityListDTO.getActivityDTOs();
    Vector activityForms = new Vector();

    int numActivities        = activityDTOs.size();

    Iterator iter = activityDTOs.iterator();
    while (iter.hasNext()) {
      ActivityDTO  activityDTO  = (ActivityDTO) iter.next();
      ActivityForm activityForm = new ActivityForm();
      BeanUtils.copyProperties( activityForm, activityDTO );
      activityForms.add( activityForm );
    }
    
    // Save all the populated ActivityForms inside the ActivityListForm, which will be used to display activityList.jsp.
    activityListForm.setActivityForms( activityForms );
    activityListForm.setPageNum( pageNum );

    return (forward);  // "success" forward returned by superclass
  }
}