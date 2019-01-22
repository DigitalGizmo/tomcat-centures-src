package museum.history.deerfield.centuries.activity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import museum.history.deerfield.centuries.database.om.ActivityPeer;

public class ActivityListAllAction extends Action {

    public ActionForward execute( ActionMapping mapping, ActionForm form, 
                                HttpServletRequest request, 
                                HttpServletResponse resp )  {
            
        DynaActionForm dynaForm = (DynaActionForm)form;
        
        // get params and check for nulls
        String  sortBy      = (String) dynaForm.get( "sortBy" );
        if (sortBy==null) {
            sortBy = "title";
        }
        
        List activitys  = ActivityPeer.getAllActivitys(sortBy, 2);
        dynaForm.set("entrys",  activitys);
        dynaForm.set("numEntrys",  new Integer(activitys.size()));
        
        return (mapping.findForward( "success" ));
    }
    
}
