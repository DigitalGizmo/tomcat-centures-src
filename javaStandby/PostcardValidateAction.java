package museum.history.deerfield.centuries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.DynaValidatorForm;
import museum.history.deerfield.centuries.database.MyCollection;
import museum.history.deerfield.centuries.database.om.VisitorPeer;
import museum.history.deerfield.centuries.database.om.Visitor;

public final class LoginAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    HttpSession   session             = request.getSession();
    String        anchor              = (String) session.getAttribute( "anchor"       );
    String        loginDestiny        = (String) session.getAttribute( "loginDestiny" );
    ActionForward anchorForward       = mapping.findForward( anchor       );
    ActionForward loginDestinyForward = mapping.findForward( loginDestiny );
    
    if (anchorForward == null)
      System.out.println( "LoginAction execute:  no anchor has been set; cancel has nowhere to go" );

    if (loginDestinyForward == null)
      loginDestinyForward = anchorForward;    
    
    if (isCancelled( request )) return (anchorForward);
 
    DynaValidatorForm dynaForm    = (DynaValidatorForm) form;
    String            visitorName = (String) dynaForm.get( "visitorName" );
    String            password    = (String) dynaForm.get( "password" );
    Visitor           visitor     = VisitorPeer.findVisitor( visitorName );
    
    if ((visitor != null) && (visitor.getPassword().equals( password ))) {
    	// Successful login should flush the visitor's collection from the session to the database,
    	// regardless of where (i.e., what page) the visitor logs in.
      session.setAttribute( Constants.VISITOR, visitor );
      MyCollection collection_ = (MyCollection) session.getAttribute( MyCollection.AttributeName_ );

      if (collection_ == null) {
      	collection_ = new MyCollection();
        session.setAttribute( MyCollection.AttributeName_, collection_ );
      }
      
      collection_.persist( visitor );
      return (loginDestinyForward);

    } else {	  
	    ActionErrors errors = new ActionErrors();
      errors.add( ActionErrors.GLOBAL_ERROR, new ActionError( "error.password.wrong" ) );
      saveErrors( request, errors );
      return (mapping.findForward( "failure" ));
    }
  }   
}