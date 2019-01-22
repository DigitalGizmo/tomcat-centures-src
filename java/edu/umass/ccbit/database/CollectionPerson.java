/*
 * Title:        CollectionPerson<p>
 * Description:  collection person<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CollectionPerson.java,v 1.11 2003/04/03 15:20:41 keith Exp $
 *
 * $Log: CollectionPerson.java,v $
 * Revision 1.11  2003/04/03 15:20:41  keith
 * changed name() to screen out null first names
 *
 * Revision 1.10  2002/04/12 14:07:15  pbrown
 * fixed copyright info
 *
 * Revision 1.9  2000/06/27 16:13:45  pbrown
 * update for searching, navigation bar
 *
 * Revision 1.8  2000/06/02 21:52:50  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.7  2000/05/25 22:25:07  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.6  2000/05/16 16:35:49  pbrown
 * added sorting by date for people/places
 *
 * Revision 1.5  2000/05/15 04:01:36  pbrown
 * added methods for displaying people/places object info on item page
 *
 * Revision 1.4  2000/05/14 03:31:49  pbrown
 * some changes to initialization due to new PeoplePlacesItems class
 *
 * Revision 1.3  2000/05/13 03:55:03  pbrown
 * changed class factory and initialization methods
 *
 * Revision 1.2  2000/05/12 22:47:53  pbrown
 * many changes, rewritten for sprinta db driver
 *
 * Revision 1.1  2000/05/09 13:22:42  pbrown
 * added files for people/places
 *
 *
 */

package edu.umass.ccbit.database;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.lang.StringBuffer;
import edu.umass.ckc.util.CkcException;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.util.CollectionDate;

public class CollectionPerson extends AssociatedObjectBase
{
  public String firstName_;
  public String middleName_;
  public int dob_;
  public int dod_;
  public String biography_;
  public String significance_;

  /**
   * item type
   */
  protected int itemType()
  {
    return AssociatedObjectBase.AssociatedItemType.People_;
  }

  /**
   * name
   * formats name from first, middle, and last names (last name is name_)
   */
  public String name()
  {
    StringBuffer name=new StringBuffer();
    
    if (firstName_ != null && firstName_.length()>0) 
      name.append(firstName_);
      
    if (middleName_ != null && middleName_.length()>0) 
      name.append(" ").append(middleName_);
      
    name.append(" ").append(name_);
    return name.toString();
  }

  /**
   * object date
   * person's life dates
   */
  public String objectDate()
  {
    return formatLifeDates(dob_, dod_);
  }

  /**
   * format life dates
   */
  public static String formatLifeDates(int dob, int dod)
  {
    if (dob!=0 && dod!=0)
    {
      StringBuffer dates=new StringBuffer();
      dates.append("(");
      if (dob!=0)
      {
        dates.append(CollectionDate.formatYear(dob));
      }
      dates.append("-");
      if (dod!=0)
      {
        dates.append(CollectionDate.formatYear(dod));
      }
      dates.append(")");
      return dates.toString();
    }
    else
    {
      return "";
    }
  }

  /**
   * object year
   * return date of birth (if known), otherwise
   * return date of death
   */
  public int objectYear()
  {
    if (dob_ != 0)
    {
      return dob_;
    }
    else
    {
      return dod_;
    }
  }

  /**
   * init name fields, life dates
   */
  protected void initNameAndDates(ResultSet result) throws SQLException
  {
    firstName_=result.getString(PeoplePlacesItems.fields.FirstName_);
    middleName_=result.getString(PeoplePlacesItems.fields.MiddleName_);
    dob_=result.getInt(PeoplePlacesItems.fields.DOB_);
    dod_=result.getInt(PeoplePlacesItems.fields.DOD_);
  }

  /**
   * init values for people/places view
   */
  public void PeoplePlacesItems_init(ResultSet result) throws SQLException
  {
    super.PeoplePlacesItems_init(result);
    initNameAndDates(result);
  }

  protected static final String view_="Web_CollectionPeopleView";

  /**
   * table/view name
   */
  protected String table()
  {
    return view_;
  }

  /* fields in view which are not included in people/places view */
  public interface fields
  {
    static final String Biography_="Biography";
    static final String Significance_="Significance";
  }

  /**
   * init values for single object
   */
  public void init(ResultSet result) throws SQLException
  {
    super.init(result);
    initNameAndDates(result);
    biography_=result.getString(fields.Biography_);
    significance_=result.getString(fields.Significance_);
    AssociatedObjectBase b;
  }

  /**
   * web text
   */
  public String webText()
  {
    return biography_;
  }
}

