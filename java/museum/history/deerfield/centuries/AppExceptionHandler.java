package museum.history.deerfield.centuries;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.struts.config.*;
import org.apache.struts.action.*;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppExceptionHandler extends ExceptionHandler {

  private Log log = LogFactory.getLog( this.getClass() );

  public ActionForward execute ( Exception ex, ExceptionConfig config, ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse res ) throws ServletException {
                                    	
    long currTime = System.currentTimeMillis();
    Date currDate = new Date(currTime);
    String[] args = new String [1];
    args[0]       = currDate.toString();

    ActionError   error   = new ActionError( config.getKey(), args );
    ActionForward forward = new ActionForward( config.getPath() );

    storeException( request, config.getKey(), error, forward, config.getScope() );
    return forward;
  }
}