package museum.history.deerfield.centuries;

import org.apache.struts.action.ActionMapping;

/**
 * NavigatorActionMapping is used in conjunction with GatekeeperAction and its subclasses.
 * It's used to pass miscellaneous parameters to GatekeeperAction via struts-config.xml.
 */
 
public final class NavigatorActionMapping extends ActionMapping {

  private String  anchor_           = null; // arbitrary forward name used for navigation
  private String  loginDestiny_     = null; // forward name to which control passes after visitor logs in
  private String  mode_             = null; // used by pages whose appearance depends on visitor's intent and/or identity
  private boolean protected_        = true; // determines whether the action requires login [true]
  private boolean sessionDependent_ = true; // determines whether the action depends on a session [true]

  public String  getAnchor()          {return anchor_;          }
  public String  getLoginDestiny()    {return loginDestiny_;    }
  public String  getMode()            {return mode_;            }
  public boolean isProtected()        {return protected_;       }
  public boolean isSessionDependent() {return sessionDependent_;}

  public void setAnchor          ( String  anchor           ) {anchor_           = anchor;          }
  public void setLoginDestiny    ( String  loginDestiny     ) {loginDestiny_     = loginDestiny;    }
  public void setMode            ( String  mode             ) {mode_             = mode;            }
  public void setProtected       ( boolean protekted        ) {protected_        = protekted;       }
  public void setSessionDependent( boolean sessionDependent ) {sessionDependent_ = sessionDependent;}
}