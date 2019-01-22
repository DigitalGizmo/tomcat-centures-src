package museum.history.deerfield.centuries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class GatekeeperAction extends Action {

  /**
   * Checks for expired sessions and unauthorized visitors.
   * 
   * Returns one of the following to the calling subclass:
   *   - "success" forward if everything's OK
   *     [Note:  The action may have no "success" forward configured, which is OK, as long as the
   *      the class that extends GatekeeperAction checks to see whether GatekeeperAction has 
   *      returned the forward mapped to "success", i.e., null.]
   *   - "expired" forward if 
   *      (1) this action depends on a session, i.e., bookmarking this page and jumping to it directly is not allowed
   *      (2) the session has expired.
   *   - "unauthorized" forward if 
   *      (1) this action requires login and 
   *      (2) the visitor is not logged in.
   * 
   * All actions that extend this class must be configured in struts-config.xml with
   * className = NavigatorActionMapping.  Various <set-property> elements can also be 
   * set in the action:
   *   protected        - (boolean) determines whether the action requires login [true]
   *   sessionDependent - (boolean) determines whether the action depends on a session [true]
   *   anchor           - (String) arbitrary forward name used for navigation
   *   loginDestiny     - (String) arbitrary forward name to which control passes after user logs in
   *   mode             - (String) used by pages whose appearance depends on visitor's intent and/or identity
   */

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp ) 
    throws Exception {

    // Is the session expired/new?
    HttpSession session   = request.getSession( false );
    boolean     isExpired = isExpired( session );
    
    NavigatorActionMapping navMapping = null;
    
    try {
      navMapping = (NavigatorActionMapping) mapping;      
    } catch (ClassCastException e) {
    	System.out.println( "GatekeeperAction execute:  action must have className attribute set to NavigatorActionMapping" );
      return (null);
    }

    // Save the anchor and loginDestiny in the session, which may need to be created.  Mode goes into the request.
    session = request.getSession();
    String anchor       = navMapping.getAnchor();      
    String loginDestiny = navMapping.getLoginDestiny();
    String mode         = navMapping.getMode();
         
  	if (anchor       != null) session.setAttribute( "anchor",       anchor );  	
  	if (loginDestiny != null) session.setAttribute( "loginDestiny", loginDestiny );
  	if (mode         != null) request.setAttribute( "mode",         mode );
    
    // Clean up if the user hit the Cancel button on login or register:  only a few logins have a specific
    // loginDestiny, and we don't want it to hang around and confuse logins that don't need it.  Also, the 
    // register.jsp page depends on loginDestiny for its appearance.
    if (isCancelled( request ))	session.removeAttribute( "loginDestiny" );

    // Assume success until we know otherwise.  Then check authorization and session dependence.
    boolean       isProtected        = navMapping.isProtected();      
    boolean       isSessionDependent = navMapping.isSessionDependent();      
    ActionForward forward            = navMapping.findForward( "success" );
    String        misconfig          = null;    
    
    if (isSessionDependent && isExpired) {
    	forward = navMapping.findForward( "expired" );
    	if (forward == null) misconfig = "expired";
    	
    } else if (isProtected && !isAuthorized( session )) {
    	forward = navMapping.findForward( "unauthorized" );
    	if (forward == null) misconfig = "unauthorized";
    }
    	
    if (misconfig != null)
    	System.out.println( "GatekeeperAction execute:  mis- or unconfigured forward [" + misconfig + "]" );
    
    return (forward);  // "success", "expired", or "unauthorized"
  }

  private boolean isExpired( HttpSession session ) {
    if (session == null) {
      return (true);
    
    } else if (session.isNew()) {
    	return (true);
    	
    } else {
  	  return (false);
    }
  }
  
  private boolean isAuthorized( HttpSession session ) {
    if (session == null) {
      return (false);
      
    } else if (session.getAttribute( Constants.VISITOR ) == null) {
      return (false);
      
    } else {
    	return (true);
    }
  }
}