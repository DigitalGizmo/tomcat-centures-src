package museum.history.deerfield.centuries.activity;

import java.util.Vector;

/**
 * Each activity displayed on the activityList.jsp page has its own ActivityForm, which in turn is mapped
 * to its own ActivityDTO, which in turn is mapped to its own ActivityDAO.  The ActivityForms are bundled
 * together into a single ActivityListForm, and the ActivityDTOs are bundled into a single ActivityListDTO.
 */
public class ActivityListDTO {

  private Vector activityDTOs_;

  public Vector getActivityDTOs()  {return activityDTOs_;}

  public void setActivityDTOs ( Vector activityDTOs  ) {activityDTOs_  = activityDTOs; }
}