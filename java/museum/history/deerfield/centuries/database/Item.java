package museum.history.deerfield.centuries.database;

import java.util.Hashtable;

/**
 * Encapsulation of an item within a myCollection.  Includes all information 
 * needed to display the item.
 */
public class Item {
  private Hashtable imgTagParams_;  // parameters needed to construct the IMG tag for this item
  private String    url_;
  private String    name_;
  private int       itemID_;
  private String    date_;
  private String    accessionNumber_;
  private String    text_;

  public Item( int itemID, String name ) {
  	itemID_          = itemID;
  	name_            = name;
  }

  public Item( int itemID, String name, Hashtable imgTagParams ) {
    this( itemID, name );
    imgTagParams_ = imgTagParams;
  }
  	
  public Item( int itemID, String name, Hashtable imgTagParams, String url, 
               String date, String accessionNumber, String text ) {
    
    this( itemID, name, imgTagParams );
  	url_             = url;
  	date_            = date;
  	accessionNumber_ = accessionNumber;
  	text_            = text;
  }

  public Hashtable getImgTagParams()    { return (imgTagParams_);             }
  public String    getUrl()             { return (url_);                      }
  public String    getName()            { return (name_);                     }
  public String    getItemID()          { return (String.valueOf( itemID_ )); }
  public String    getDate()            { return (date_);                     }
  public String    getAccessionNumber() { return (accessionNumber_);          }
  public String    getText()            { return (text_);                     }
}
