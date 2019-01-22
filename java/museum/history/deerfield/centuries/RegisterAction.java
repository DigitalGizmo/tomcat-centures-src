package museum.history.deerfield.centuries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import museum.history.deerfield.centuries.database.om.VisitorPeer;
import museum.history.deerfield.centuries.database.om.Visitor;

public final class RegisterAction extends GatekeeperAction {

  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse resp )
    throws Exception {

    // If the GatekeeperAction detected problems, abort now.
    ActionForward forward = super.execute( mapping, form, request, resp );
    if (forward != mapping.findForward( "success" )) return (forward);

    HttpSession   session       = request.getSession();
    String        anchor        = (String) session.getAttribute( "anchor" );
    ActionForward anchorForward = mapping.findForward( anchor );

    // Cancel:  return to anchor page, the same location that the Cancel button on the login page takes us.
    if (isCancelled( request )) return (anchorForward);

    DynaActionForm dynaForm    = (DynaActionForm) form;
    String         visitorName = (String) dynaForm.get( "visitorName" );
    String         password    = (String) dynaForm.get( "password" );
    String         lastName    = (String) dynaForm.get( "lastName" );
    String         firstName   = (String) dynaForm.get( "firstName" );

    if (null == VisitorPeer.findVisitor( visitorName )) {
      Visitor visitor = VisitorPeer.createVisitor( visitorName, password, lastName, firstName );
      session.setAttribute( Constants.VISITOR, visitor );
      
      // Success:  return to anchor page, the same location that the Cancel button on the login page takes us.
      return (anchorForward);
      
    } else {
      ActionErrors errors = new ActionErrors();
      errors.add( ActionErrors.GLOBAL_ERROR, new ActionError( "error.visitorname.nonunique" ) );
      saveErrors( request, errors );
      return (mapping.findForward( "failure" ));
    }
  }
}