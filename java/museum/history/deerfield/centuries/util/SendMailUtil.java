package museum.history.deerfield.centuries.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.*;
import org.apache.struts.util.MessageResources;

public class SendMailUtil {
  
  /*
  * adapted from: http://www.javacommerce.com/displaypage.jsp?name=javamail.sql&id=18274
  * see also link to version that performs authentication with username and password, if needed.
  */
  public int postMail( String recipients[ ], String subject, String message , String from)  {
      boolean          debug            = false;
      MessageResources messageResources = MessageResources.getMessageResources( "resources.application" );
  
      try {
        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", messageResources.getMessage("notification.host"));

        // create some properties and get the default Session
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
        for (int i = 0; i < recipients.length; i++)
        {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Optional : You can also set your custom headers in the Email if you Want
        msg.addHeader("MyHeaderName", "myHeaderValue");

        // Setting the Subject and Content Type
        msg.setSubject(subject);
        msg.setContent(message, "text/plain");
        Transport.send(msg);
        return 1;
      } catch (MessagingException e) {
        System.out.println("Messaging exception in SendMailUtil: " + e.getMessage());
        return -1;
      }
  } 

  /*
  * This version handles an SMTP mail server that requires authentication
  */
  public int postMailAuth( String recipients[ ], String subject,
                            String message , String from) throws MessagingException {
    boolean debug = false;
    MessageResources messageResources = MessageResources.getMessageResources( "resources.application" );

    try {
        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", messageResources.getMessage("notification.auth.host"));
        props.put("mail.smtp.auth", "true");
        
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug);
        
        // create a message
        Message msg = new MimeMessage(session);
        
        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
           addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        
        // Setting the Subject and Content Type
        subject += " (" +  messageResources.getMessage("notification.server") + ")";
        msg.setSubject(subject);

        // simple send
        msg.setContent(message, "text/html");



        Transport.send(msg);
        // System.out.println("** sending via authorized mail");
        return 1;
     } catch (MessagingException e) {
       System.out.println("Messaging exception in SendMailUtil-Auth: " + e.getMessage());
       return -1;
     }
 }

  /*
  * This version handles HTML formatted postcard. Also uses authentication
  */
  public int postMultiPartAuth( String recipients[ ], String subject, String textMessage,
                            String htmlMessage , String from) throws MessagingException {
    boolean debug = false;
    MessageResources messageResources = MessageResources.getMessageResources( "resources.application" );

    try {
        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", messageResources.getMessage("notification.auth.host"));
        props.put("mail.smtp.auth", "true");
        
        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);
        session.setDebug(debug);
        
        // create a message
        Message msg = new MimeMessage(session);
        
        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);
        
        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
           addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);
        
        // Set the Subject 
        msg.setSubject(subject);

        // simple send
        // msg.setContent(message, "text/html");
        
        // start adaptation of multipart example from
        // http://www.jguru.com/faq/view.jsp?EID=132654
        // Create a multi-part to combine the parts
        Multipart multipart = new MimeMultipart("alternative");
        
        // Create your text message part
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(textMessage);
        
        // Add the text part to the multipart
        multipart.addBodyPart(messageBodyPart);
        
        // Create the html part
        messageBodyPart = new MimeBodyPart();
        // String htmlText = "<H1>I am the html part</H1>";
        messageBodyPart.setContent(htmlMessage, "text/html");
        
        // Add html part to multi part
        multipart.addBodyPart(messageBodyPart);
        
        // Associate multi-part with message
        msg.setContent(multipart);
        

        Transport.send(msg);
        // System.out.println("** sending via authorized mail");
        return 1;
     } catch (MessagingException e) {
       System.out.println("Messaging exception in SendMailUtil-Auth: " + e.getMessage());
       return -1;
     }
 }

  /**
  * SimpleAuthenticator is used to do simple authentication
  * when the SMTP server requires it.
  */
  private class SMTPAuthenticator extends javax.mail.Authenticator{
    MessageResources messageResources = MessageResources.getMessageResources( "resources.application" );

      public PasswordAuthentication getPasswordAuthentication()
      {
          String username = messageResources.getMessage("notification.auth.user");
          String password = messageResources.getMessage("notification.auth.pass");
          return new PasswordAuthentication(username, password);
      }
  }
}
