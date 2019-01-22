package museum.history.deerfield.centuries;

public final class Constants {

    /**
     * The package name for this application.
     */
    public static final String Package = "museum.history.deerfield.centuries";

    /**
     * The session scope attribute under which the Visitor object
     * for the currently logged in visitor is stored.
     */
    public static final String VISITOR           = "visitor";

    /**
     * The session scope attribute under which the data transfer object
     * for the activity currently being made or edited is stored.
     */
    public static final String ACTIVITY_MAKE_DTO = "activityMakeDTO";

    /**
     * The session scope attribute under which the data transfer object
     * for the activity currently being made or edited is stored.
     * This is short-lived--created just to carry the status info from
     * preview to ActivitySaveStatusAction. Added by Don Oct 23, 2008
     */
    public static final String ACTIVITY_STATUS_DTO = "activityStatusDTO";

    /**
     * The request scope attribute under which the data transfer object
     * for the activity currently being viewed or previewed is stored.
     */
    public static final String ACTIVITY_VIEW_DTO = "activityViewDTO";

    /**
     * The request scope attribute under which the data transfer object
     * for the list of activities currently being viewed is stored.
     */
    public static final String ACTIVITY_LIST_DTO = "activitListDTO";
    
    /**
     * The maximum number of steps in an activity's teaching plan.
     */
    public static final int    TEACHING_PLAN_STEPS = 8;
    
    /**
     * The maximum number of web links in an activity.
     */
    public static final int    ACTIVITY_LINKS      = 4;
}
