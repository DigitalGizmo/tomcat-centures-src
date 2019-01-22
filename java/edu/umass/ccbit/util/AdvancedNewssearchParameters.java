package edu.umass.ccbit.util;

import edu.umass.ccbit.jsp.AdvancedNewssearchPage;
import edu.umass.ccbit.database.Newstopics;
import java.util.StringTokenizer;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

public class AdvancedNewssearchParameters extends NewssearchParameters {
  public static final String SessionAttribute_="adv_newssearch_parameters";
  //advanced search parameter names
  public static final String Topic_="topic";
  private Newstopics topics_;

  public AdvancedNewssearchParameters() {
    // call search parameter constructor
    super();
    // put advanced fields; thee are three Topic dropdown menus
    put( Topic_+"1", "" );
    put( Topic_+"2", "" );
    put( Topic_+"3", "" );
  }

  /**
   * topic id...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public int topic( int nTopic ) {
    return getInt( Topic_ + nTopic, 0 );
  }

  /**
   * search summary
   */
  public String searchSummary(HttpSession session) {
    topics_ = AdvancedNewssearchPage.topics_;
    StringBuffer buf = new StringBuffer();
    buf.append(super.searchSummary());
    int topicID;
    ArrayList uniqueTopicIDs = new ArrayList( 3 );
    
    if (topics_ != null) {
    	for (int i=1; i<=3; i++) {
    		topicID = topic(i);
        if (topicID !=0) {
        	// Ignore topics that have been chosen more than once.
        	if (!uniqueTopicIDs.contains( new Integer( topicID ))) {
        		uniqueTopicIDs.add( new Integer( topicID ) );
        	  buf.append( ", " ).append( topics_.getTopicByID( topicID ) );
        	}
        }
      }
    }
      
    if(buf.length()>0 && buf.charAt(0)==',')
      buf.replace(0,1,"");
    return buf.toString();
  }

  public String searchMainLinkTablesQuery() {
    if (topic(1) + topic(2) + topic(3) > 0) {
      StringBuffer buf = new StringBuffer();
      
      // Construct a query that will effectively AND together the chosen topics.
      
      buf.append( query() );
      buf.append( ", NewstopicLink WHERE NewstopicLink.ArticleID = NewsIndex.ArticleID AND (" );

      int topicID;
			int numOfTopics = 0;

			for (int i=1; i<=3; i++) {
				topicID = topic(i);
				if (topicID != 0) {
					buf.append( "(NewstopicLink.TopicID = " ).append( topicID ).append( ") OR " );
					numOfTopics++;
				}
			}
			
			buf.delete( buf.length()-4, buf.length() );
			buf.append( ") GROUP BY NewsIndex.ArticleID HAVING COUNT(*) >= " ).append( numOfTopics );
      return buf.toString();
    }
    else
      return null;
  }

  /**
   * save to session
   */
  public synchronized void saveToSession(HttpSession session) {
    JspSession.save(session, SessionAttribute_, this);
  }

  /**
   * load from session
   */
  public synchronized static AdvancedNewssearchParameters loadAdvFromSession(HttpSession session) {
    AdvancedNewssearchParameters param=(AdvancedNewssearchParameters) JspSession.load(session, SessionAttribute_);
    // if not found in session, return a new (empty) instance
    if (param==null) param=new AdvancedNewssearchParameters();
    return param;
  }
}
