package museum.history.deerfield.centuries.activity;

import java.util.Vector;
import java.util.Date;
import java.text.SimpleDateFormat;
import museum.history.deerfield.centuries.util.VectorUtil;
import museum.history.deerfield.centuries.database.om.HistoricalEraLabelPeer;
import museum.history.deerfield.centuries.database.om.GradeLevelLabelPeer;
import museum.history.deerfield.centuries.database.om.ContentAreaLabelPeer;
import museum.history.deerfield.centuries.database.om.Activity;

/**
 * Activity data transfer object.  Used for moving activity properties back and forth between an ActivityForm
 * and an ActivityDAO (data access object).
 */
public class ActivityDTO {
  
  private int    activityID_        = -1;  // legal IDs are all > 0; -1 means this activity is not yet in the database
  private int    statusID_;
  private String title_;
  private String shortDescription_;
  private String longDescription_;
  private Date   submittedOn_;
  private String statusLabel_;
  private String authorFirstName_;
  private String authorLastName_;
  private int[]  itemIDs_           = new int[0];
  private int[]  gradeLevelIDs_     = new int[0];
  private int[]  historicalEraIDs_  = new int[0];
  private int[]  contentAreaIDs_    = new int[0];
  private Vector teachingPlanSteps_ = new Vector();
  private Vector webLinks_          = new Vector();
  
  public int    getActivityID()               {return activityID_;       }  
  public int[]  getItemIDs()                  {return itemIDs_;          }
  public int    getStatusID()                 {return statusID_;         }  
  public String getTitle()                    {return title_;            }
  public String getShortDescription()         {return shortDescription_; }
  public String getLongDescription()          {return longDescription_;  }
  public String getStatusLabel()              {return statusLabel_;      }  
  public String getAuthorFirstName()          {return authorFirstName_;  }  
  public String getAuthorLastName()           {return authorLastName_;   }  
  public int[]  getSelectedGradeLevelIDs()    {return gradeLevelIDs_;    }
  public int[]  getSelectedHistoricalEraIDs() {return historicalEraIDs_; }
  public int[]  getSelectedContentAreaIDs()   {return contentAreaIDs_;   }
  public Vector getTeachingPlanSteps()        {return teachingPlanSteps_;}
  public Vector getWebLinks()                 {return webLinks_;         }
  public Vector getGradeLevelLabels()         {return GradeLevelLabelPeer.   getGradeLevelLabels();   }
  public Vector getHistoricalEraLabels()      {return HistoricalEraLabelPeer.getHistoricalEraLabels();}
  public Vector getContentAreaLabels()        {return ContentAreaLabelPeer.  getContentAreaLabels();  }
  
  public void setItemIDs                 ( int[]  itemIDs            ) {itemIDs_           = itemIDs;          }
  public void setStatusID                ( int    statusID           ) {statusID_          = statusID;         }
  public void setTitle                   ( String title              ) {title_             = title;            }
  public void setShortDescription        ( String shortDescription   ) {shortDescription_  = shortDescription; }
  public void setLongDescription         ( String longDescription    ) {longDescription_   = longDescription;  }
  public void setSelectedGradeLevelIDs   ( int[]  gradeLevelIDs      ) {gradeLevelIDs_     = gradeLevelIDs;    }
  public void setSelectedHistoricalEraIDs( int[]  historicalEraIDs   ) {historicalEraIDs_  = historicalEraIDs; }
  public void setSelectedContentAreaIDs  ( int[]  contentAreaIDs     ) {contentAreaIDs_    = contentAreaIDs;   }
  public void setTeachingPlanSteps       ( Vector teachingPlanSteps  ) {teachingPlanSteps_ = teachingPlanSteps;}
  public void setWebLinks                ( Vector webLinks           ) {webLinks_          = webLinks;         }

  // These four properties need protection against being reset.  The activityMake.jsp page does not submit them
  // with activityMakeForm, so activityMakeForm sets them to 0/null.  We just need to make sure that they don't get
  // overwritten in the DTO when the properties are copied from the activityMakeForm to the activityDTO.
  public void setActivityID     ( int activityID         ) {if (activityID      > 0)     activityID_      = activityID;     }
  public void setStatusLabel    ( String statusLabel     ) {if (statusLabel     != null) statusLabel_     = statusLabel;    }
  public void setAuthorFirstName( String authorFirstName ) {if (authorFirstName != null) authorFirstName_ = authorFirstName;}
  public void setAuthorLastName ( String authorLastName  ) {if (authorLastName  != null) authorLastName_  = authorLastName; }
  public void setSubmittedOn    ( Date   submittedOn     ) {if (submittedOn     != null) submittedOn_     = submittedOn;    }


  public String getSubmittedOn() {
  	String submittedOn = null;

    if (submittedOn_ != null) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd MMMM yyyy" );
      submittedOn = simpleDateFormat.format( submittedOn_ );
    }
    return (submittedOn);
  }

  /**
   * Adds more itemIDs to any already stored in the itemIDs property.
   * Called when user wants to add more items to an existing activity.
   */
  public void addItemIDs( int[] newItemIDs ) {
  	Vector itemIDs = VectorUtil.arrayToVector( itemIDs_ );
  	Vector newIDs  = VectorUtil.arrayToVector( newItemIDs );
  	itemIDs.addAll( newIDs );
  	itemIDs_ = VectorUtil.vectorToArray( itemIDs );
  }
  
  public void setDeletedItemIDs( int[] deletedItemIDs ) {
  	
  	Vector deletedIDs = VectorUtil.arrayToVector( deletedItemIDs );
  	Vector itemIDs    = VectorUtil.arrayToVector( itemIDs_ );
  	itemIDs.removeAll( deletedIDs );
  	itemIDs_ = VectorUtil.vectorToArray( itemIDs );
  }
  
  /**
   * Returns Vector of ContentAreaLabel record objects associated with this activity.
   */
  public Vector getSelectedContentAreaLabels() {
  	return (ContentAreaLabelPeer.getContentAreaLabels( getSelectedContentAreaIDs() ));
  }
  
  /**
   * Returns Vector of GradeLevelLabel record objects associated with this activity.
   */
  public Vector getSelectedGradeLevelLabels() {
  	return (GradeLevelLabelPeer.getGradeLevelLabels( getSelectedGradeLevelIDs() ));
  }
  
  /**
   * Returns Vector of HistoricalEraLabel record objects associated with this activity.
   */
  public Vector getSelectedHistoricalEraLabels() {
  	return (HistoricalEraLabelPeer.getHistoricalEraLabels( getSelectedHistoricalEraIDs() ));
  }
}