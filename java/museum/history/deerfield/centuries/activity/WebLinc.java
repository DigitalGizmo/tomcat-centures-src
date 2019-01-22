package museum.history.deerfield.centuries.activity;

/**
 * Bean used by ActivityForm and ActivityDTO to hold web link data passed to and from the 
 * Activity data access object.  Funky spelling avoids name confusion with the Torque class
 * (the DAO) of almost the same name.
 */
public class WebLinc {
  
  private int    linkNum_;
  private String title_;
  private String url_;

  public int    getLinkNum() {return linkNum_;}
  public String getTitle()   {return title_;  }
  public String getUrl()     {return url_;    }

  public void setLinkNum( int linkNum  ) {linkNum_ = linkNum;}
  public void setTitle  ( String title ) {title_   = title;  }

  // Make sure all URLs start with http://
  public void setUrl( String url ) {
    if (url.length() > 0) {
    	if (!url.startsWith( "http://" )) {
    		url = "http://" + url;
    	}
    }
    url_ = url;
  }
  
}
