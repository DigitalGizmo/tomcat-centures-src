package museum.history.deerfield.centuries.activity;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.lang.reflect.Array;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import museum.history.deerfield.centuries.database.om.*;
import museum.history.deerfield.centuries.activity.ActivityDTO;
import museum.history.deerfield.centuries.activity.ActivityListDTO;
import museum.history.deerfield.centuries.activity.TeechingPlanStep;
import museum.history.deerfield.centuries.activity.WebLinc;

/**
 * ActivityService does the heavy lifting of moving data back and forth between the Torque data access objects
 * (DAOs) and the data transfer objects (DTOs).
 */
public class ActivityService {

  /**
   * Called to save an activity before preview, or after item is added to list
   *
   * @param activityDTO data transfer object containing all the properties of the activity being updated
   * @param statusLabel new status of the activity (drafted | submitted | published | deleted)
   * @param visitor author of the activity
   * @exception TorqueException 
   * 
   * Don March 2009: convert to return int for use by "keepmaking" in ActivityMakePreviewAction
   */
  public int saveActivity( ActivityDTO activityDTO, String statusLabel, Visitor visitor ) throws TorqueException {

    Criteria criteria;
    // Move the visitor from the "uzer" role to the next highest role, "author".  (If the 
    // visitor is already in a role at or greater than author, setAuthorRole() does nothing.)
    visitor.setAuthorRole();
    
    // We may be saving a new activity, or saving changes to an existing one.  If the ActivityDTO has a 
    // legitimate activityID, we're saving changes and have to retrieve the existing activity from the database.
    Activity activityDAO = null;
    int      activityID  = activityDTO.getActivityID();
    
    if (activityID > 0) {  // activity already exists
      criteria = new Criteria();
      criteria.add( ActivityPeer.ACTIVITYID, activityID );    
      List activities = ActivityPeer.doSelect( criteria );
      activityDAO = (Activity) activities.get( 0 );
      // Delete all records in linked tables; we'll replace them shortly.
      deleteLinkedRecords( activityID );
      
    } else {  // new activity
      activityDAO = new Activity();
      activityDAO.setVisitor( visitor );  // set the foreign key
    }

    try {
      // Copy the simple string properties from the DTO to the database.  Could probably be 
      // done with BeanUtils.copyProperties(), but I had problems with it, perhaps because 
      // the two beans' property names must match, rather than just the accessor methods.  Or
      // because the property's types must match, and submittedDate's does not.
      activityDAO.setTitle(            activityDTO.getTitle() );
      activityDAO.setShortDescription( activityDTO.getShortDescription() );
      activityDAO.setLongDescription(  activityDTO.getLongDescription() );  	
      
      // If the activity has just been "submitted", stamp it with today's date.
      if (statusLabel.equals( "submitted" )) {
        activityDAO.setSubmittedOn( new java.util.Date() );
      }
   
      // Update the activity's status.
      criteria = new Criteria();
      criteria.add( StatusPeer.LABEL, statusLabel );
      Status status = (Status) StatusPeer.doSelect( criteria ).get( 0 );
      activityDAO.setStatus( status );    // set the foreign key
      activityDAO.save();
      // retrieve activityID for return
      // if this has just been newly created this will be the new ID
      activityID = activityDAO.getActivityID();

      // Copy the itemIDs.
      int[] itemIDs = activityDTO.getItemIDs();
      int numItems  = Array.getLength( itemIDs );
      
      for (int i=0; i<numItems; i++) {
        ActivityItem activityItemDAO = new ActivityItem();
        int          itemID          = itemIDs[i];
        activityItemDAO.setActivity( activityDAO );  // set the foreign key
        activityItemDAO.setItemID( itemID );
        activityItemDAO.save();        
      }
      
      // Copy the teaching plan steps.
      Vector teachingPlanSteps = activityDTO.getTeachingPlanSteps();
      Iterator iter = teachingPlanSteps.iterator();
      
      while (iter.hasNext()) {
        TeachingPlanStep teachingPlanStepDAO = new TeachingPlanStep();
        TeechingPlanStep teachingPlanStepDTO = (TeechingPlanStep) iter.next();
        BeanUtils.copyProperties( teachingPlanStepDAO, teachingPlanStepDTO );
        teachingPlanStepDAO.setActivity( activityDAO );  // set the foreign key
        teachingPlanStepDAO.save();
      }

      // Copy the web links.
      Vector webLinks = activityDTO.getWebLinks();
      iter = webLinks.iterator();
      
      while (iter.hasNext()) {
        WebLink webLinkDAO = new WebLink();
        WebLinc webLinkDTO = (WebLinc) iter.next();
        BeanUtils.copyProperties( webLinkDAO, webLinkDTO );
        webLinkDAO.setActivity( activityDAO );  // set the foreign key
        webLinkDAO.save();
      }

      // Copy the grade levels.
      int[] levelIDs  = activityDTO.getSelectedGradeLevelIDs();
      int numLevelIDs = Array.getLength( levelIDs );

      for (int i=0; i<numLevelIDs; i++) {
        GradeLevel gradeLevelDAO = new GradeLevel();
        int        levelID       = levelIDs[i];
        gradeLevelDAO.setLevelID( levelID );
        gradeLevelDAO.setActivity( activityDAO );  // set the foreign key
        gradeLevelDAO.save();
      }

      // Copy the historical eras.
      int[] eraIDs  = activityDTO.getSelectedHistoricalEraIDs();
      int numEraIDs = Array.getLength( eraIDs );

      for (int i=0; i<numEraIDs; i++) {
        HistoricalEra historicalEraDAO = new HistoricalEra();
        int           eraID            = eraIDs[i];
        historicalEraDAO.setEraID( eraID );
        historicalEraDAO.setActivity( activityDAO );  // set the foreign key
        historicalEraDAO.save();
      }

      // Copy the content areas.
      int[] contentIDs  = activityDTO.getSelectedContentAreaIDs();
      int numContentIDs = Array.getLength( contentIDs );

      for (int i=0; i<numContentIDs; i++) {
        ContentArea contentAreaDAO = new ContentArea();
        int         contentID      = contentIDs[i];
        contentAreaDAO.setContentID( contentID );
        contentAreaDAO.setActivity( activityDAO );  // set the foreign key
        contentAreaDAO.save();
      }
    } catch (Exception  e) {
      System.err.println( "ActivityService saveActivity:  Exception thrown" );
    }
    // return activityID
    return activityID;

  }

  /**
   * Called whenever an activity's status changes, i.e., it gets submitted or deleted.
   *
   * @param activityDTO data transfer object containing all the properties of the activity being updated
   * @param statusLabel new status of the activity (drafted | submitted | published | deleted)
   * @param visitor author of the activity
   * @exception TorqueException
   */
  public void saveActivityStatus( ActivityDTO activityDTO, String statusLabel, Visitor visitor ) throws TorqueException {

    Criteria criteria;

    // Move the visitor from the "uzer" role to the next highest role, "author".  (If the 
    // visitor is already in a role at or greater than author, setAuthorRole() does nothing.)
    visitor.setAuthorRole();
    
    // We may be saving a new activity, or saving changes to an existing one.  If the ActivityDTO has a 
    // legitimate activityID, we're saving changes and have to retrieve the existing activity from the database.
    Activity activityDAO = null;
    int      activityID  = activityDTO.getActivityID();
    
    if (activityID > 0) {  // activity already exists
      criteria = new Criteria();
      criteria.add( ActivityPeer.ACTIVITYID, activityID );    
      List activities = ActivityPeer.doSelect( criteria );
      activityDAO = (Activity) activities.get( 0 );
      // Delete all records in linked tables; we'll replace them shortly.
      // no, don't do this for status change only
      //deleteLinkedRecords( activityID );
      
    } else {  // new activity
      System.out.println("** this should no longer get called in ActivityService, method saveActivityStatus");
      activityDAO = new Activity();
      activityDAO.setVisitor( visitor );  // set the foreign key
    }

    try {
      // If the activity has just been "submitted", stamp it with today's date.
      if (statusLabel.equals( "submitted" )) {
        activityDAO.setSubmittedOn( new java.util.Date() );
      }
   
      // Update the activity's status.
      criteria = new Criteria();
      criteria.add( StatusPeer.LABEL, statusLabel );
      Status status = (Status) StatusPeer.doSelect( criteria ).get( 0 );
      activityDAO.setStatus( status );    // set the foreign key
      activityDAO.save();
    
    } catch (Exception  e) {
      System.err.println( "ActivityService saveActivity:  Exception thrown" );
    }
  }

  /**
   * Deletes all records for an activity in tables linked to Activity table by the activityID key.
   */
  private void deleteLinkedRecords( int activityID ) throws TorqueException {
    
    // 20031022 KRU I tried using doDelete, but it didn't work, for reasons described here:
    // http://www.mail-archive.com/turbine-user@jakarta.apache.org/msg05982.html
    
    StringBuffer buf;
    String tables[] = {"TeachingPlanStep","ActivityItem","WebLink","GradeLevel","HistoricalEra","ContentArea"};
    int numTables = Array.getLength( tables );
    
    for (int i=0; i<numTables; i++) {
      buf = new StringBuffer();
      buf.append( "DELETE FROM ").append( tables[i] );
      buf.append( " WHERE ACTIVITYID = " ).append( activityID );
      Torque.getDefaultDB();
      BasePeer.executeStatement( buf.toString(), ActivityPeer.DATABASE_NAME );
    }
  }

  /**
   * Gets an activity from the database and copies its properties into an ActivityDTO.
   *
   * @param activityDTO data transfer object into which activity properties will be copied
   * @param activityID ID of activity to get
   * @exception TorqueException
   */
  public void selectActivity( ActivityDTO activityDTO, int activityID ) throws TorqueException {

    Criteria criteria = new Criteria();
    criteria.add( ActivityPeer.ACTIVITYID, activityID );    
    List activities = ActivityPeer.doSelect( criteria );
    
    if (activities.size() > 0) {
      Activity activityDAO = (Activity) activities.get( 0 );
      populateActivityDTO( activityDTO, activityDAO );
    }
  }

  /**
   * Gets all activities from the database that fit specified criteria, and copies their properties into an 
   * ActivityListDTO.
   *
   * @param ActivityListDTO data transfer object into which activities' properties will be copied
   * @param criteria selection criteria in finding the activities
   * @exception TorqueException
   */
  private void selectActivities( ActivityListDTO activityListDTO, int pageNum, ActivityListForm activityListForm, Criteria criteria ) throws TorqueException {
    
    criteria.addAscendingOrderByColumn( ActivityPeer.TITLE );  // order selected activities by title

    List   activities    = ActivityPeer.doSelect( criteria );

    populateOnePage( activityListDTO, activities, pageNum, activityListForm );
    /*
    int    numActivities = activities.size();
    Vector activityDTOs  = new Vector();    
    
    for (int i=0; i<numActivities; i++) {
      Activity    activityDAO = (Activity) activities.get( i );
      ActivityDTO activityDTO = new ActivityDTO();
      populateActivityDTO( activityDTO, activityDAO );
      activityDTOs.add( activityDTO );
    }
    activityListDTO.setActivityDTOs( activityDTOs );
    */
  }

  /**
   * added by Don Sept 2008
   * limit to results to those above specified status
   * Previously all activities were retrieved (and SIDs generated) even though only the published ones were displayes
   */
  public void selectActivities( ActivityListDTO activityListDTO, int pageNum, ActivityListForm activityListForm, int statusID ) throws TorqueException { 
    
    Criteria criteria = new Criteria();
    
    int eraID     = activityListForm.getHistoricalEraID();
    int levelID   = activityListForm.getGradeLevelID();
    int contentID = activityListForm.getContentAreaID();
    
    if (eraID     != -1) {
      criteria.addJoin( HistoricalEraPeer.ACTIVITYID, ActivityPeer.ACTIVITYID );    
      criteria.add(     HistoricalEraPeer.ERAID,      eraID );
    }
    if (levelID   != -1) {
      criteria.addJoin( GradeLevelPeer.ACTIVITYID,    ActivityPeer.ACTIVITYID );    
      criteria.add(     GradeLevelPeer.LEVELID,       levelID );
    }
    if (contentID != -1) {
      criteria.addJoin( ContentAreaPeer.ACTIVITYID,   ActivityPeer.ACTIVITYID );    
      criteria.add(     ContentAreaPeer.CONTENTID,    contentID );
    }

    // new criteria added by Don Sept 2008
    // if anything other than "published" is needed in the future, 
    // use greaterThan
    criteria.add(ActivityPeer.STATUSID, statusID );

    criteria.addAscendingOrderByColumn( ActivityPeer.TITLE );  // order selected activities by title

    List   activities    = ActivityPeer.doSelect( criteria );

    populateOnePage( activityListDTO, activities, pageNum, activityListForm );
  }

  /**
   * Sends one page worth of activites to populateActivityDTO
   *
   * @param  ActivityListDTO to be populated
   * @param  List the whole set activity records
   * @exception TorqueException
   *  added by Don Oct 2008
   */
  private void populateOnePage( ActivityListDTO activityListDTO, List activities, int pageNum, ActivityListForm activityListForm ) throws TorqueException {

    Vector activityDTOs  = new Vector();    
    // for pagination in either version of selectActivities
    int    numActivities = activities.size();
    // System.out.println("num Activities in ActivityService: " + numActivities + " pageNum: " + pageNum);
    int    limitThisPage = 0;
    int    startThisPage = 0;
    int    numPerPage    = 10;
    int    nextPage       = 0;
    int    prevPage       = pageNum - 1; // 0 will trigger no prevPage link

    startThisPage = numPerPage * (pageNum - 1);
    if (numActivities < numPerPage * pageNum) {  // last page doesnt fill up 10
      limitThisPage = numActivities;  
      nextPage = 0;
    } else {
      limitThisPage = numPerPage * pageNum;
      nextPage = pageNum + 1;
    }
    for (int i=startThisPage; i < limitThisPage; i++) {   // numActivities  int i=0
      Activity    activityDAO = (Activity) activities.get( i );
      ActivityDTO activityDTO = new ActivityDTO();
      populateActivityDTO( activityDTO, activityDAO );
      activityDTOs.add( activityDTO );
    }

    activityListDTO.setActivityDTOs( activityDTOs );
    // populate form for page display
    activityListForm.setNumActivities(numActivities);
    activityListForm.setNextPage(nextPage);
    activityListForm.setPrevPage(prevPage);
    activityListForm.setItemRange((startThisPage+1) + " - " + limitThisPage);
  }

  /**
   * Copies an activity's properties from an ActivityDAO into an ActivityDTO.
   *
   * @param ActivityDTO data transfer object into which the activity's properties will be copied
   * @param Activity data access object from which the activity's properties will be copied
   * @exception TorqueException
   */
  private void populateActivityDTO( ActivityDTO activityDTO, Activity activityDAO ) throws TorqueException {
    
    int activityID = activityDAO.getActivityID();
      
    Criteria criteria = new Criteria();
    criteria.add( ActivityItemPeer.ACTIVITYID, activityID );    
    List activityItemDAOs = ActivityItemPeer.doSelect( criteria );

    criteria = new Criteria();
    criteria.add( GradeLevelPeer.ACTIVITYID, activityID );    
    List gradeLevelDAOs = GradeLevelPeer.doSelect( criteria );
    
    criteria = new Criteria();
    criteria.add( ContentAreaPeer.ACTIVITYID, activityID );    
    List contentAreaDAOs = ContentAreaPeer.doSelect( criteria );
    
    criteria = new Criteria();
    criteria.add( HistoricalEraPeer.ACTIVITYID, activityID );    
    List historicalEraDAOs = HistoricalEraPeer.doSelect( criteria );
     
    criteria = new Criteria();
    criteria.add( TeachingPlanStepPeer.ACTIVITYID, activityID );    
    criteria.addAscendingOrderByColumn( TeachingPlanStepPeer.STEPNUM );  // make sure steps are ordered
    List teachingPlanStepDAOs = TeachingPlanStepPeer.doSelect( criteria );

    // Make sure the web links are ordered.  If they're not, ActivityForm.getWebLinksPadded() will
    // have problems appending empty links to the end of the set -- it'll insert them in between.
    criteria = new Criteria();
    criteria.add( WebLinkPeer.ACTIVITYID, activityID );    
    criteria.addAscendingOrderByColumn( WebLinkPeer.LINKNUM );
    List webLinkDAOs = WebLinkPeer.doSelect( criteria );

    int numItems    = activityItemDAOs.    size();
    int numLevels   = gradeLevelDAOs.      size();
    int numContents = contentAreaDAOs.     size();
    int numEras     = historicalEraDAOs.   size();
    int numSteps    = teachingPlanStepDAOs.size();
    int numLinks    = webLinkDAOs.         size();
      
    int[]  itemIDs           = new int[numItems   ];
    int[]  gradeLevelIDs     = new int[numLevels  ];
    int[]  contentAreaIDs    = new int[numContents];
    int[]  historicalEraIDs  = new int[numEras    ];
    Vector teachingPlanSteps = new Vector( numSteps );
    Vector webLinks          = new Vector( numLinks );
    
    for (int i=0; i<numItems; i++) {
      int itemID = ((ActivityItem) activityItemDAOs.get( i )).getItemID();
      itemIDs[i] = itemID;
    }
    for (int i=0; i<numLevels; i++) {
      int levelID = ((GradeLevel) gradeLevelDAOs.get( i )).getLevelID();
      gradeLevelIDs[i] = levelID;
    }
    for (int i=0; i<numContents; i++) {
      int contentID = ((ContentArea) contentAreaDAOs.get( i )).getContentID();
      contentAreaIDs[i] = contentID;
    }
    for (int i=0; i<numEras; i++) {
      int eraID = ((HistoricalEra) historicalEraDAOs.get( i )).getEraID();
      historicalEraIDs[i] = eraID;
    }

    try {
      for (int i=0; i<numSteps; i++) {
        TeechingPlanStep teachingPlanStepDTO = new TeechingPlanStep();
        TeachingPlanStep teachingPlanStepDAO = (TeachingPlanStep) teachingPlanStepDAOs.get( i );
        BeanUtils.copyProperties( teachingPlanStepDTO, teachingPlanStepDAO );
        teachingPlanSteps.add( teachingPlanStepDTO );
      }

      for (int i=0; i<numLinks; i++) {
        WebLinc webLinkDTO = new WebLinc();
        WebLink webLinkDAO = (WebLink) webLinkDAOs.get( i );
        BeanUtils.copyProperties( webLinkDTO, webLinkDAO );
        webLinks.add( webLinkDTO );
      }
    } catch (Exception e) {
      System.out.println( "ActivityService populateActivityDTO, copy steps and links:  Exception thrown by BeanUtils.copyProperties." );
    }
    
    activityDTO.setItemIDs                 ( itemIDs           );
    activityDTO.setSelectedGradeLevelIDs   ( gradeLevelIDs     );
    activityDTO.setSelectedContentAreaIDs  ( contentAreaIDs    );
    activityDTO.setSelectedHistoricalEraIDs( historicalEraIDs  );
    activityDTO.setTeachingPlanSteps       ( teachingPlanSteps );
    activityDTO.setWebLinks                ( webLinks          );
    
    try {
      BeanUtils.copyProperties( activityDTO, activityDAO );
    } catch (Exception e) {
      // For some reason, copyProperties throws an Exception, which means we have to copy some properties the hard way.
      // [The reason is probably that submittedOn's type does not match in the two beans.]
      activityDTO.setTitle      ( activityDAO.getTitle()       );
      activityDTO.setStatusLabel( activityDAO.getStatusLabel() );
      activityDTO.setSubmittedOn( activityDAO.getSubmittedOn() );
      // disable sys out -- always called and it just fills up the error log
      // System.out.println( "ActivityService populateActivityDTO, whole DTO:  Exception thrown by BeanUtils.copyProperties:" + e.getMessage() );
    }
  }
  
  public void selectAllActivities( ActivityListDTO activityListDTO, int pageNum, ActivityListForm activityListForm ) throws TorqueException {
    Criteria criteria = new Criteria();
    criteria.add( ActivityPeer.STATUSID, -1, Criteria.GREATER_THAN );  // Omit deleted  
    selectActivities( activityListDTO, pageNum, activityListForm, criteria );
  }
  
  public void selectAuthorActivities( ActivityListDTO activityListDTO, int pageNum, ActivityListForm activityListForm, int authorID ) throws TorqueException {
    Criteria criteria = new Criteria();
    criteria.add( ActivityPeer.STATUSID, -1, Criteria.GREATER_THAN );    
    criteria.add( ActivityPeer.VISITORID, authorID );    
    selectActivities( activityListDTO, pageNum, activityListForm, criteria );
  }
  public void selectAuthorNameActivities( ActivityListDTO activityListDTO, int pageNum, ActivityListForm activityListForm ) throws TorqueException {
    Criteria criteria = new Criteria();
    criteria.add( ActivityPeer.STATUSID, -1, Criteria.GREATER_THAN ); 
    criteria.addJoin(ActivityPeer.VISITORID, VisitorPeer.VISITORID);
    criteria.add( VisitorPeer.LASTNAME,  activityListForm.getAuthorLastName() );
    
    selectActivities( activityListDTO, pageNum, activityListForm, criteria );
  }
  /* not currently used
  public void selectHistoricalEraActivities( ActivityListDTO activityListDTO, int pageNum, int eraID ) throws TorqueException {
    Criteria criteria = new Criteria();
    criteria.addJoin( HistoricalEraPeer.ACTIVITYID, ActivityPeer.ACTIVITYID );    
    criteria.add(     HistoricalEraPeer.ERAID,      eraID );
    selectActivities( activityListDTO, pageNum, criteria );
  }
  
  public void selectContentAreaActivities( ActivityListDTO activityListDTO, int pageNum, int contentID ) throws TorqueException {
    Criteria criteria = new Criteria();
    criteria.addJoin( ContentAreaPeer.ACTIVITYID, ActivityPeer.ACTIVITYID );    
    criteria.add(     ContentAreaPeer.CONTENTID,  contentID );    
    selectActivities( activityListDTO, pageNum, criteria );
  }
  
  public void selectGradeLevelActivities( ActivityListDTO activityListDTO, int pageNum, int levelID ) throws TorqueException {
    Criteria criteria = new Criteria();
    criteria.addJoin( GradeLevelPeer.ACTIVITYID, ActivityPeer.ACTIVITYID );    
    criteria.add(     GradeLevelPeer.LEVELID,    levelID );    
    selectActivities( activityListDTO, pageNum, criteria );
  }
  */
}