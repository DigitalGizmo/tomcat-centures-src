package museum.history.deerfield.centuries;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import museum.history.deerfield.centuries.database.MyCollection;

public final class LogoutAction extends Action {
	
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res )
    throws IOException, ServletException {

    HttpSession   session       = request.getSession();
    String        anchor        = (String) session.getAttribute( "anchor" );
    ActionForward anchorForward = mapping.findForward( anchor );
    
    if (anchorForward == null)
      System.out.println( "LogoutAction execute:  no anchor has been set; logout has nowhere to go" );

    session.removeAttribute( "anchor" );
    session.removeAttribute( "loginDestiny" );
    session.removeAttribute( Constants.VISITOR );
    session.removeAttribute( Constants.ACTIVITY_MAKE_DTO );
    session.removeAttribute( MyCollection.AttributeName_ );
   
    return (anchorForward);
  }
}
