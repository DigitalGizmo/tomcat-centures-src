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

public final class BugReportAction extends Action {
	
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res )
    throws IOException, ServletException {

    HttpSession   session       = request.getSession();
    // nothing yet

    return (mapping.findForward("success"));
  }
}
