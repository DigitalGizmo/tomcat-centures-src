package museum.history.deerfield.centuries.activity;

import java.util.Vector;
import org.apache.struts.action.ActionForm;
import museum.history.deerfield.centuries.database.om.ContentAreaLabelPeer;
import museum.history.deerfield.centuries.database.om.HistoricalEraLabelPeer;
import museum.history.deerfield.centuries.database.om.GradeLevelLabelPeer;

// This is actually a list of ActivityForms and might therefore be more logically
// call ActivityFormList, but this name is more in keeping with Struts conventions --
// this is the form behind the activityList.jsp page.

public class ActivityListForm extends ActionForm {

  // The "filter" is the criterion through which the visitor chooses to filter their menu of activities.
  // For example, they might want to see only those activities that fall into the Civil War historical era.
  // The filter type would be historical era, and the filter value would be Civil War.
  private Vector activityForms_;
  private int    gradeLevelID_    = -1;
  private int    historicalEraID_ = -1;
  private int    contentAreaID_   = -1;
  // added by Don Sept 2008 for pagination
  private int   pageNum_          = 0;
  private int   nextPage_         = 0;
  private int   prevPage_         = 0;
  private int   numActivities_    = 0;
  private String itemRange_       = "x - x";
  private String authorLastName_  = null;

  public Vector getActivityForms()   {return activityForms_;  }
  public int    getGradeLevelID()    {return gradeLevelID_;   }
  public int    getHistoricalEraID() {return historicalEraID_;}
  public int    getContentAreaID()   {return contentAreaID_;  }
  public int    getPageNum()         {return pageNum_;  }
  public int    getNextPage()        {return nextPage_;  }
  public int    getPrevPage()        {return prevPage_;  }
  public int    getNumActivities()   {return numActivities_;  }
  public String getItemRange()       {return itemRange_;  }
  public String getAuthorLastName()  {return authorLastName_; }  

  // Strictly speaking, this form should have no direct access to the database -- it should have to go through a
  // data transfer object.  But since no DTO exists, it seems pointless to make one specifically to get this info
  // out of the database, since it's read-only and static.  (Also, if we created a DTO, we'd have to create an Action
  // class simply to copy data from it to this form.  The base action class is handling everything else.)
  public Vector getGradeLevelLabels()    {return GradeLevelLabelPeer.   getGradeLevelLabels();   }
  public Vector getHistoricalEraLabels() {return HistoricalEraLabelPeer.getHistoricalEraLabels();}
  public Vector getContentAreaLabels()   {return ContentAreaLabelPeer.  getContentAreaLabels();  }

  public void setActivityForms  ( Vector activityForms   ) {activityForms_   = activityForms;}
  public void setGradeLevelID   ( int    gradeLevelID    ) {gradeLevelID_    = gradeLevelID;   } 
  public void setHistoricalEraID( int    historicalEraID ) {historicalEraID_ = historicalEraID;} 
  public void setContentAreaID  ( int    contentAreaID   ) {contentAreaID_   = contentAreaID;  } 
  public void setPageNum        ( int    pageNum         ) {pageNum_         = pageNum;  } 
  public void setNextPage       ( int    nextPage        ) {nextPage_        = nextPage;  } 
  public void setPrevPage       ( int    prevPage        ) {prevPage_        = prevPage;  } 
  public void setNumActivities  ( int    numActivities   ) {numActivities_   = numActivities;  } 
  public void setItemRange      ( String itemRange       ) {itemRange_       = itemRange;  } 
  public void setAuthorLastName ( String authorLastName  ) {authorLastName_  = authorLastName;          } 
}