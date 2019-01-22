/*
 * Title:        PeoplePlacesItems<p>
 * Description:  class to encapsulate people/places view query<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: PeoplePlacesItems.java,v 1.11 2003/04/18 14:18:56 keith Exp $
 *
 */

package edu.umass.ccbit.database;
import edu.umass.ccbit.database.AssociatedObjectBase;
import edu.umass.ccbit.database.AssociatedObjectDateComparator;
import edu.umass.ckc.util.CkcException;
import java.lang.Exception;
import java.lang.StringBuffer;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;
import java.util.Enumeration;

public class PeoplePlacesItems extends DbLoadableItemList
{
  public static final String view_="Web_PeoplePlaces";
  public static final String PeoplePlaces_ = "ppitems";

  interface SortBy
  {
    int Alpha_=0;
    int Date_=1;
  }

  public interface fields
  {
    static final String AccessionNumber_="AccessionNumber";
    static final String ObjectID_="ObjectID";
    static final String FirstName_="FirstName";
    static final String MiddleName_="MiddleName";
    static final String Name_="Name";
    static final String DOB_="DOB";
    static final String DOD_="DOD";
    static final String ItemID_="ItemID";
    static final String Filename_="Filename";
    static final String Client_="Client";
    static final String ItemType_="ItemType";
    static final String ItemDate_="ItemDate";
    static final String CircaDate_="CircaDate";
    static final String LowDateRangeYear_="LowDateRangeYear";
    static final String HighDateRangeYear_="HighDateRangeYear";
    static final String IdSentence_="IdSentence";
  }

  public interface ViewModes
  {
    int ViewAll=0;
    int ViewPeople=1;
    int ViewPlaces=2;
    int ViewEvents=3;
  }

  /*
   * query to read people places view from database
   */
  protected String query(int viewMode)
  {
    StringBuffer query=new StringBuffer();
    query.append("SELECT * FROM ");
    query.append(view_);

    switch (viewMode)
    {
      case ViewModes.ViewPeople:
        query.append(" WHERE ").append(fields.ItemType_);
        query.append(" IN (");
        query.append(AssociatedObjectBase.AssociatedItemType.People_).append(", ");
        query.append(AssociatedObjectBase.AssociatedItemType.Producer_).append(")");
        break;
      case ViewModes.ViewPlaces:
        query.append(" WHERE ").append(fields.ItemType_);
        query.append("=").append(AssociatedObjectBase.AssociatedItemType.Place_);
        break;
      case ViewModes.ViewEvents:
        query.append(" WHERE ").append(fields.ItemType_);
        query.append("=").append(AssociatedObjectBase.AssociatedItemType.Event_);
        break;
      default:
        break;
    }
    query.append(" ORDER BY ").append(fields.Name_);
    return query.toString();
  }

  /*
   * sort by date
   */
  protected void sortItemsByDate()
  {
    sort(new AssociatedObjectDateComparator());
  }

  /**
   * new instance
   */
  protected InstanceFromResult newInstance(ResultSet result) throws SQLException
  {
    return AssociatedObjectBase.new_PeoplePlacesItemsObject(result);
  }

  /*
   * execute query, put results in vector
   */
  public void load(Connection conn, int viewMode, int sortBy) throws SQLException
  {
    load(conn, query(viewMode));
    if (sortBy==SortBy.Date_)
    {
      sortItemsByDate();
    }
  }

  /**
   * item at given position
   */
  public AssociatedObjectBase item(int nitem)
  {
    return (AssociatedObjectBase) get(nitem);
  }

  /**
   * search through the string and replace the text...
   * verifies that the found word is not a substring
   * able to distinguish between identically named people or places
   * @param source the string you are parsing through looking for glossary terms
   * @param term the glossary item you are looking for in the source string
   */
  public static String searchString( String source, AssociatedObjectBase pp )
    throws IndexOutOfBoundsException {
   	
    String name = pp.name();
    int size = name.length();
    int index = 0;
    String errors = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890'";
    String previous="";
    String next="";
    String add;
    /**
     * When two people might be confused because their names are identical, or because
     * one is a substring of the other (e.g., John Williams and Reverend John Williams),
     * we look for an HTML comment immediately following the name.  The comment includes
     * the ObjectID of the person to whom the name refers.
     */
    String  tagOpen    = "<!--ObjectID=";
    String  tagClose   = "-->";
    int     tagOpenLen = tagOpen.length();
    boolean found      = false;
    StringBuffer buf = new StringBuffer();

    if(source == null || name == null) return null;

    int start = source.indexOf(name, index);

    while(start >= 0) {
      if (index != start) {
        buf.append(source.substring(index, start));
      }
      
      try {
        previous = new String((new Character(source.charAt(start-1))).toString());
      } catch (Exception e) {
        previous = " ";
      }
      
      try {
        next = new String((new Character(source.charAt(start+size))).toString());
      } catch (Exception e) {
        next = " ";
      }
      
      if (errors.indexOf(previous)==(-1) && errors.indexOf(next)==(-1)) {
        /* We've found a pp name in the source.  Check for a comment immediately following it,
         * and if one exists, substitute the ObjectID inside the comment for the one associated
         * with the pp.
         */        
      	int idPos = start+size+tagOpenLen; // idPos is where the ObjectID value would start
      	
      	if ((source.length() >= idPos) && (source.substring( start+size, idPos ).equals( tagOpen ))) {
          String objectID = source.substring( idPos, source.indexOf( tagClose, idPos ) );
          add = ppLink( pp, objectID );
          found = false; // found an ambiguous name, like Reverend John Williams; keep looking for another, like John Williams
        } else {
          add = ppLink( pp );
          found = true;
        }
            
        buf.append(add);
      } else {
        buf.append(name);
      }
      
      index = start + size;
      start = source.indexOf(name, index);
      if (found) break;
    }
    
    buf.append(source.substring(index));
    return buf.toString();
  }

  /**
   * link to pp item page
   */
  protected static String ppLink( AssociatedObjectBase pp ) {
    StringBuffer buf = new StringBuffer();
    buf.append("<a href=\"javascript:popupWindow('").append(pp.objectUrl());
    buf.append("','popup','menubar=yes,scrollbars=yes,resizable=yes,width=500,height=250')\">");
    buf.append(pp.name()).append("</a>");
    return buf.toString();
  }

  /**
   * link to a pp item page explicitly specified by objectID
   */
  protected static String ppLink( AssociatedObjectBase pp, String objectID ) {
    StringBuffer buf = new StringBuffer();
    buf.append("<a href=\"javascript:popupWindow('").append( pp.objectUrl( objectID ) );
    buf.append("','popup','menubar=yes,scrollbars=yes,resizable=yes,width=500,height=250')\">");
    buf.append(pp.name()).append("</a>");
    return buf.toString();
  }

  /**
   * check string for pp items and replace them with a link to pp
   * @param text the pp to find
   */
  public String ppParse(String text)
  {
    AssociatedObjectBase pp;
    Enumeration e;
    
    if (items_ != null)
      e = items_.elements();
    else
      e = (new Vector()).elements();
      
    while (e.hasMoreElements()) {
      pp   = (AssociatedObjectBase) e.nextElement();
      text = searchString(text, pp);
    }
    return text;
  }
}






