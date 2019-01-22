package museum.history.deerfield.centuries.thingstodo;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.DynaActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import javax.mail.MessagingException;
import museum.history.deerfield.centuries.util.SendMailUtil;
import org.apache.struts.util.MessageResources;

/** this action is based on a more "modern" approach using dynoActionForms
 * as used in Shays.
 */

public final class PostcardSendAction extends Action {
	
  public ActionForward execute( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse res )
    throws IOException, ServletException {

    DynaActionForm  dynaForm        = (DynaActionForm)form;
    int    itemid     = ((Integer)dynaForm.get( "itemid" )).intValue();
    String name      = (String)dynaForm.get( "name" );
    String label      = (String)dynaForm.get( "label" );
    String imageTag   = (String)dynaForm.get( "imageTag" );
    String to_email   = (String)dynaForm.get( "to_email" );
    String from_email = (String)dynaForm.get( "from_email" );
    String from_name  = (String)dynaForm.get( "from_name" );
    String message    = (String)dynaForm.get( "message" );
    String[] sendTo   = new String[] { to_email }; 

    // limit length of title (name) for email subject
    String emailSubject = null;
      
    if (name.length() > 33) {
      emailSubject = name.substring(0,32) + "...";
    } else {
      emailSubject = name;
    }

    //build plain text and html messages
    String textMessage = "Greetings from Memorial Hall Museum \n\n" +
      message +
      "\n\nFrom: " + from_name +  
      "\n\n" + name +
      "\nView this item in the American Centuries online collection: " +
      "\nhttp://www.americancenturies.mass.edu/collection/itempage.jsp?itemid=" + itemid +
      "\n\n" + label;

// "<link href=\"../../common/am_cen.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
    String  htmlMessage = "<html><head>" +
"<title>Greetings from Memorial Hall Museum</title>\n" +
"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">\n" +
"<style type=\"text/css\" media=\"all\">" +
"h1	{font-weight: normal; font-family: Georgia, \"Times New Roman\", Times, serif; color:#333333; font-size: 24px; font-style: italic; }\n" +
"h2	{font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 18px; color:#333333}\n" +
"p	{font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 11px; color:#333333; line-height: 16px;}\n" +
".small	{font-size: 9px; color:#666666;}\n" +
"</style>" +
"</head><body>\n" +
"<div>\n" +
"<table width=\"700\" cellpadding=\"25\" bgcolor=\"#EEE5D3\">\n" +
  "<tbody><tr valign=\"top\">\n" +
    "<td width=\"450\">" + imageTag + "<br>\n" +
      "<p class=\"small\">Pocumtuck Valley Memorial Association, Deerfield MA. All rights reserved.</p>\n" +
      "<h1>" + name + "</h1>\n" + 
      "<p>" + label + "</p>\n" +
    "</td>" +
    "<td width=\"250\"> \n" +
      "<h2>Greetings from Memorial Hall Museum</h2> \n" +
      "<p>From: " + from_name + "</p> " +
      "<p><a href=\"http://www.americancenturies.mass.edu/collection/itempage.jsp?itemid=" + itemid +
      "\">See this item on our website, American Centuries</a></p>\n" +
      "<p>" + message + "</p> \n"+
    "</td> " +
  "</tr> \n" +
"</tbody></table> \n" +
"</body></html>";

    // get email addresses from resources/application.properties
    MessageResources  messageResources  = MessageResources.getMessageResources( "resources.application" );
    SendMailUtil      sendMail          = new SendMailUtil();

    int sendSuccess = 0;
    try {
      sendSuccess = sendMail.postMultiPartAuth(sendTo, emailSubject,  
          textMessage, htmlMessage, from_email);
    } catch (MessagingException me) {
      System.out.println( "PostcardSendAction execute, Exception: " + me );
    }
    // set message 
    String notificationMsg = null;
    if (sendSuccess > 0) {
      notificationMsg = "Thanks for sending an e-postcard! Please continue enjoying our site.";
    } else {
      notificationMsg = "Sorry, due to an internal error your postcard cannot be sent at this time.";
    }
    request.setAttribute( "notificationMsg", notificationMsg );

    return (mapping.findForward("success"));
  }
}
