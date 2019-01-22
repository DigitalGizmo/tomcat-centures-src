package museum.history.deerfield.centuries.activity;

import java.lang.reflect.Array;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import org.apache.struts.action.ActionForm;
import museum.history.deerfield.centuries.Constants;
import museum.history.deerfield.centuries.database.om.GradeLevelLabelPeer;
import museum.history.deerfield.centuries.database.om.GradeLevelLabel;
import museum.history.deerfield.centuries.database.om.HistoricalEraLabelPeer;
import museum.history.deerfield.centuries.database.om.HistoricalEraLabel;
import museum.history.deerfield.centuries.database.om.ContentAreaLabelPeer;
import museum.history.deerfield.centuries.database.om.ContentAreaLabel;

/**
 * Buffer object for activity properties submitted to and displayed in a form.
 */
public class ActivityForm extends ActionForm {

  // These properties hold the options selected in the "Find an Activity:" section of activityHome.jsp:
  private int listContentAreaID_   = -1;
  private int listGradeLevelID_    = -1;
  private int listHistoricalEraID_ = -1;
  
  private int       activityID_        = -1;  // legal IDs are all > 0; -1 means this activity is not yet in the database
  private Vector    items_             = null;
  private String    title_             = null;
  private String    shortDescription_  = null;
  private String    longDescription_   = null;
  private String    submittedOn_       = null;
  private Vector    teachingPlanSteps_ = null;
  private Vector    webLinks_          = null;
  private String    statusLabel_       = null;
  private String    authorFirstName_   = null;
  private String    authorLastName_    = null;
  private Hashtable firstImgTagParams_ = new Hashtable();
  
  // Initialize these properties (which represent checkboxes on activityMake.jsp), or they'll throw an 
  // exception when we call Array.getLength() later...
  private int[] deletedItemIDs_           = new int[0];
  private int[] selectedGradeLevelIDs_    = new int[0];
  private int[] selectedHistoricalEraIDs_ = new int[0];
  private int[] selectedContentAreaIDs_   = new int[0];

  // Read-only display information drawn from the database.
  // Use Vectors instead of Hashtables because display order is important.
  private Vector gradeLevelLabels_    = null;
  private Vector historicalEraLabels_ = null;
  private Vector contentAreaLabels_   = null;

  public int    getListContentAreaID()        {return listContentAreaID_;       }
  public int    getListGradeLevelID()         {return listGradeLevelID_;        }
  public int    getListHistoricalEraID()      {return listHistoricalEraID_;     }
  public int[]  getDeletedItemIDs()           {return deletedItemIDs_;          }
  public int[]  getSelectedGradeLevelIDs()    {return selectedGradeLevelIDs_;   }
  public int[]  getSelectedHistoricalEraIDs() {return selectedHistoricalEraIDs_;}
  public int[]  getSelectedContentAreaIDs()   {return selectedContentAreaIDs_;  }
  public int    getActivityID()               {return activityID_;              }
  public Vector getItems()                    {return items_;                   }
  public String getTitle()                    {return title_;                   }
  public String getShortDescription()         {return shortDescription_;        }
  public String getLongDescription()          {return longDescription_;         }
  public String getSubmittedOn()              {return submittedOn_;             }
  public Vector getGradeLevelLabels()         {return gradeLevelLabels_;        }  // read-only display info
  public Vector getHistoricalEraLabels()      {return historicalEraLabels_;     }  // read-only display info
  public Vector getContentAreaLabels()        {return contentAreaLabels_;       }  // read-only display info
  public String getStatusLabel()              {return statusLabel_;             }        
  public String getAuthorLastName()           {return authorLastName_;          }  
  public String getAuthorFirstName()          {return authorFirstName_;         }  
  public Hashtable getFirstImgTagParams()     {return firstImgTagParams_;       }

  public void setListContentAreaID       ( int listContentAreaID          ) {listContentAreaID_        = listContentAreaID;       } 
  public void setListGradeLevelID        ( int listGradeLevelID           ) {listGradeLevelID_         = listGradeLevelID;        }
  public void setListHistoricalEraID     ( int listHistoricalEraID        ) {listHistoricalEraID_      = listHistoricalEraID;     }
  public void setDeletedItemIDs          ( int[] deletedItemIDs           ) {deletedItemIDs_           = deletedItemIDs;          }
  public void setSelectedGradeLevelIDs   ( int[] selectedGradeLevelIDs    ) {selectedGradeLevelIDs_    = selectedGradeLevelIDs;   }
  public void setSelectedHistoricalEraIDs( int[] selectedHistoricalEraIDs ) {selectedHistoricalEraIDs_ = selectedHistoricalEraIDs;}
  public void setSelectedContentAreaIDs  ( int[] selectedContentAreaIDs   ) {selectedContentAreaIDs_   = selectedContentAreaIDs;  }
  public void setActivityID              ( int activityID                 ) {activityID_               = activityID;              }  
  public void setItems                   ( Vector items                   ) {items_                    = items;                   }  
  public void setTitle                   ( String title                   ) {title_                    = title;                   }  
  public void setShortDescription        ( String shortDescription        ) {shortDescription_         = shortDescription;        }  
  public void setLongDescription         ( String longDescription         ) {longDescription_          = longDescription;         }  
  public void setSubmittedOn             ( String submittedOn             ) {submittedOn_              = submittedOn;             }  
  public void setGradeLevelLabels        ( Vector gradeLevelLabels        ) {gradeLevelLabels_         = gradeLevelLabels;        }  // read-only display info
  public void setHistoricalEraLabels     ( Vector historicalEraLabels     ) {historicalEraLabels_      = historicalEraLabels;     }  // read-only display info
  public void setContentAreaLabels       ( Vector contentAreaLabels       ) {contentAreaLabels_        = contentAreaLabels;       }  // read-only display info
  public void setStatusLabel             ( String statusLabel             ) {statusLabel_              = statusLabel;             }        
  public void setAuthorLastName          ( String authorLastName          ) {authorLastName_           = authorLastName;          } 
  public void setAuthorFirstName         ( String authorFirstName         ) {authorFirstName_          = authorFirstName;         } 
  public void setFirstImgTagParams       ( Hashtable firstImgTagParams    ) {firstImgTagParams_        = firstImgTagParams;       } 

  // these two accessors are needed by Struts html custom tags that have indexed="true"
  public TeechingPlanStep getTeachingPlanStep( int i ) {
    return ((TeechingPlanStep) getTeachingPlanStepsPadded().get( i ));
  }

  public WebLinc getWebLink( int i ) {
    return ((WebLinc) getWebLinksPadded().get( i ));
  }

  /**
   * Returns vector of teaching plan steps, with empty steps removed.
   */
  public Vector getTeachingPlanSteps() {
    if (teachingPlanSteps_ == null) teachingPlanSteps_ = new Vector();
    Iterator iter = teachingPlanSteps_.iterator();
    
    while (iter.hasNext()) {
      TeechingPlanStep teachingPlanStep = (TeechingPlanStep) iter.next();
      if (teachingPlanStep.getDescription().length() == 0) iter.remove();
    }
    return (teachingPlanSteps_);
  }

  /**
   * Returns vector of activity links, with empty links removed.
   */
  public Vector getWebLinks() {
    if (webLinks_ == null) webLinks_ = new Vector();
    Iterator iter = webLinks_.iterator();
    
    while (iter.hasNext()) {
      WebLinc webLink = (WebLinc) iter.next();
      if ((webLink.getTitle().length() == 0 ) || (webLink.getUrl().length() == 0)) {
        iter.remove();
      }
    }
    return (webLinks_);
  }

  /**
   * Returns vector of teaching plan steps, with empty steps inserted if they number
   * fewer than Constants.TEACHING_PLAN_STEPS.
   */
  public Vector getTeachingPlanStepsPadded() {
    if (teachingPlanSteps_ == null) teachingPlanSteps_ = new Vector();
    
    if (teachingPlanSteps_.size() < Constants.TEACHING_PLAN_STEPS) {
      for (int stepNum=1; stepNum <= Constants.TEACHING_PLAN_STEPS; stepNum++) {
        
        TeechingPlanStep newTeachingPlanStep = new TeechingPlanStep();
        newTeachingPlanStep.setStepNum( stepNum );
        
        if (stepNum > teachingPlanSteps_.size()) {
          teachingPlanSteps_.add( newTeachingPlanStep );
          
        } else {
          TeechingPlanStep existingTeachingPlanStep = (TeechingPlanStep) teachingPlanSteps_.get( stepNum-1 );
          
          if (existingTeachingPlanStep.getStepNum() != stepNum) {
            teachingPlanSteps_.add( stepNum-1, newTeachingPlanStep );
          }          
        }
      }
    }
    return (teachingPlanSteps_);
  }

  /**
   * Returns vector of activity links, with empty links appended if they number
   * fewer than Constants.ACTIVITY_LINKS.
   */
  public Vector getWebLinksPadded() {
    if (webLinks_ == null) webLinks_ = new Vector();
    
    if (webLinks_.size() < Constants.ACTIVITY_LINKS) {
      for (int linkNum=1; linkNum <= Constants.ACTIVITY_LINKS; linkNum++) {
        
        WebLinc newWebLink = new WebLinc();
        newWebLink.setLinkNum( linkNum );
        
        if (linkNum > webLinks_.size()) {
          webLinks_.add( newWebLink );
          
        } else {
          WebLinc existingWebLink = (WebLinc) webLinks_.get( linkNum-1 );
          if (existingWebLink.getLinkNum() != linkNum) {
            webLinks_.add( linkNum-1, newWebLink );
          }          
        }
      }
    }
    return (webLinks_);
  }

  public void setTeachingPlanSteps( Vector teachingPlanSteps ) {
    teachingPlanSteps_ = teachingPlanSteps;
  }

  public void setWebLinks( Vector webLinks ) {    
    webLinks_ = webLinks;
  }
  
  public Vector getSelectedGradeLevelLabels() {
  	int[] levelIDs = getSelectedGradeLevelIDs(); 
  	return (GradeLevelLabelPeer.getGradeLevelLabels( levelIDs ));
  }

  public Vector getSelectedHistoricalEraLabels() {
  	int[] eraIDs = getSelectedHistoricalEraIDs(); 
  	return (HistoricalEraLabelPeer.getHistoricalEraLabels( eraIDs ));
  }

  public Vector getSelectedContentAreaLabels() {
  	int[] contentIDs = getSelectedContentAreaIDs(); 
  	return (ContentAreaLabelPeer.getContentAreaLabels( contentIDs ));
  }

  public String getGradeLevelsCommaList() {
  	Vector labelObjs = getSelectedGradeLevelLabels();
    String commaList = "";

    if ((labelObjs != null) && (labelObjs.size() > 0)) {
      Iterator iter = labelObjs.iterator();
      while (iter.hasNext()) commaList += ((GradeLevelLabel) iter.next()).getLabel() + ", ";
      commaList = commaList.substring( 0, commaList.length()-2 );
    }
    return (commaList);
  }
  
  public String getHistoricalErasCommaList() {
  	Vector labelObjs = getSelectedHistoricalEraLabels();
    String commaList = "";

    if ((labelObjs != null) && (labelObjs.size() > 0)) {
      Iterator iter = labelObjs.iterator();
      while (iter.hasNext()) commaList += ((HistoricalEraLabel) iter.next()).getLabel() + ", ";
      commaList = commaList.substring( 0, commaList.length()-2 );
    }
    return (commaList);
  }
  
  public String getContentAreasCommaList() {
  	Vector labelObjs = getSelectedContentAreaLabels();
    String commaList = "";

    if ((labelObjs != null) && (labelObjs.size() > 0)) {
      Iterator iter = labelObjs.iterator();
      while (iter.hasNext()) commaList += ((ContentAreaLabel) iter.next()).getLabel() + ", ";
      commaList = commaList.substring( 0, commaList.length()-2 );
    }
    return (commaList);
  }
  
  private String getCommaList( Vector vector ) {
    String commaList = "";

    if ((vector != null) && (vector.size() > 0)) {
      Iterator iter = vector.iterator();

      while (iter.hasNext()) {
      	String label = ((GradeLevelLabel) iter.next()).getLabel();      	
      	commaList = commaList + label + ", ";
      }
      commaList = commaList.substring( 0, commaList.length()-2 );
    }
    return (commaList);
  }
}