package museum.history.deerfield.centuries.activity;

/**
 * Bean used by ActivityForm and ActivityDTO to hold teaching plan step data passed to and from
 * the Activity data access object.  Funky spelling avoids name confusion with the Torque class
 * (the DAO) of almost the same name.
 */
public class TeechingPlanStep {
	
  private int    stepNum_;
  private String description_;
  
  public int    getStepNum()     {return (stepNum_);    }
  public String getDescription() {return (description_);}

  public void setStepNum    ( int    stepNum     ) {stepNum_     = stepNum;    }
  public void setDescription( String description ) {description_ = description;}
}

