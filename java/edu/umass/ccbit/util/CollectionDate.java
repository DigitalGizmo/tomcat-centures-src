/*
 * Title:        CollectionDate<p>
 * Description:  class to encapsulate properties/operations on dates related to collection items<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: CollectionDate.java,v 1.15 2003/12/09 16:48:18 keith Exp $
 *
 * $Log: CollectionDate.java,v $
 * Revision 1.15  2003/12/09 16:48:18  keith
 * fixed single-year range display
 *
 * Revision 1.14  2003/12/09 02:44:05  keith
 * disabled smart date formatting
 *
 * Revision 1.13  2003/01/16 03:03:40  pb
 * backed out previous change, which breaks item pages
 *
 * Revision 1.12  2003/01/15 02:23:57  pb
 * removed reference to CircaYear, which is no longer used
 *
 * Revision 1.11  2002/04/12 14:08:16  pbrown
 * fixed copyright info
 *
 * Revision 1.10  2001/10/19 20:34:46  pbrown
 * fixed problem with comparison date when upper/lower yr is 0
 *
 * Revision 1.9  2000/08/03 18:51:46  tarmstro
 * added return value when low and high date range years are equal...returns lowdaterangeyear
 *
 * Revision 1.8  2000/08/01 23:01:39  tarmstro
 * fix for dependence on circayear
 *
 * Revision 1.7  2000/07/31 14:47:21  tarmstro
 * change toString method to exclude month formatting
 *
 * Revision 1.6  2000/07/11 19:40:33  tarmstro
 * commented out CircaYear
 *
 * Revision 1.5  2000/06/27 16:16:01  pbrown
 * changes to collection date (not final yet)
 * new class for reading level
 * other changes/bugifxes
 *
 * Revision 1.4  2000/05/25 22:25:10  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.3  2000/05/24 06:13:46  pbrown
 * reimplemented object dates with y/m/d fields
 *
 * Revision 1.2  2000/05/19 21:44:21  pbrown
 * added alternate constructor which gets data from result set
 *
 * Revision 1.1  2000/05/18 02:21:54  pbrown
 * moved files to util package
 *
 * Revision 1.4  2000/05/16 16:35:49  pbrown
 * added sorting by date for people/places
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

package edu.umass.ccbit.util;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Calendar;
import java.lang.Integer;
import java.lang.Math;
import java.lang.StringBuffer;
import java.text.DateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectionDate
{
  /* exact date, if known */
  protected GregorianCalendar cal_;
  /* circa date..should be non-zero positive (A.D.) or negative (B.C.E.) integer */
  protected int circaYear_;
  /* date range */
  protected int dateRangeLowYear_;
  protected int dateRangeHighYear_;
  protected static final String months_[]=
  {
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  };

  protected static final String bceQualifier_=" B.C.E.";

  /**
   * constructor for collection date object
   * which constructs collection date from ResultSet
   *
   * fields in result set must be named thusly:
   * CircaYear
   * LowDateRangeYear
   * HighDateRangeYear
   *
   * exact dates are now represented in the database as three integer fields
   * e.g., in the main table, we have
   * - CreationDateDay
   * - CreationDateMonth
   * - CreationDateYear
   *
   * the dateKey parameter should be the root parameter name for these date
   * fields, e.g., in the above case, dateKey="CreationDate"
   *
   * @param dateKey key for date field
   * @param result result set with above fields in it
   */
  public CollectionDate(String dateKey, ResultSet result) throws SQLException
  {
    int year=result.getInt(dateKey + "Year");
    
    if (year != 0) {
      // months are zero-based
      int month=result.getInt(dateKey + "Month") - 1;
      // days of month are not
      int day=result.getInt(dateKey + "Day");
      // 1=1 A.D., 0 = 1 B.C., -1=2 B.C., etc.
      if (year < 0)
        year +=1;
      cal_=new GregorianCalendar(year, month, day);
    } else {
    	cal_=null;
    }
    
    // 20051230 KRU:  disable this try/catch clause; it was filling the logs with stack traces
    // 20060117 KRU:  re-enable it; disabling it made the date disappear on itempage.jsp
    try {
      circaYear_=result.getInt("CircaYear");
    } catch (SQLException ex) {
      circaYear_=result.getInt("CircaDate");
    }
    dateRangeLowYear_=result.getInt("LowDateRangeYear");
    dateRangeHighYear_=result.getInt("HighDateRangeYear");
  }

  /**
   * formats year
   * @param year non-zero integer representing year (>0=A.D., <0=B.C.E.)
   */
  public static String formatYear(int year)
  {
    StringBuffer disp=new StringBuffer();
    if (year != 0)
    {
      disp.append(Math.abs(year));
      if (year < 0)
        disp.append(bceQualifier_);
      return disp.toString();
    }
    else
    {
      return "";
    }
  }

  /**
   * returns calendar for this object
   */
  public GregorianCalendar cal()
  {
    return cal_;
  }

  /**
   * comparison year
   * returns (approximate) year used for date comparisons
   */
  public int comparisonYear()
  {
    if (cal_ != null)
    {
      return (cal_.get(Calendar.ERA)==GregorianCalendar.BC ? -1 : 1) * cal_.get(Calendar.YEAR);
    }
    else if (circaYear_ != 0)
    {
      return circaYear_;
    }
    // check for zero values at high/low end of date range...
    // (to fix indian house door bug)
    else if (dateRangeLowYear_==0)
    {
      return dateRangeHighYear_;
    }
    else if (dateRangeHighYear_==0)
    {
      return dateRangeLowYear_;
    }
    else
    {
      /* return middle (average) of date range */
      return (dateRangeLowYear_ + dateRangeHighYear_)/2;
    }
  }


  /**
   * comparison month
   */
  public int comparisonMonth()
  {
    if (cal_ != null)
    {
      return (cal_.get(Calendar.MONTH));
    }
    else if (circaYear_ != 0)
      return 1;
    else
      return 0; //dateRangeLowMonth_;
  }

  /**
   * in date range..returns true iff value is in specified date range
   */
  public boolean inDateRange(int month, int day, int year, int rangeType, int rangeValue)
  {
    GregorianCalendar cal, start, end;
    if (year==0)
      return false;
    cal=new GregorianCalendar(year< 0 ? year + 1 : year, month, day);
    start=(GregorianCalendar) cal.clone();
    end=(GregorianCalendar) cal.clone();
    start.add(rangeType, rangeValue);
    end.add(rangeType, -rangeValue);

    if (cal_ != null)
      return cal_.after(start) && cal_.before(end);
    else if (circaYear_ != 0)
    {
      // todo
    }
    else
    {
      // todo
    }
    return false;
  }

  /**
   * formats date
   */
  public static String formatDate(GregorianCalendar cal)
  {
    DateFormat fmt=DateFormat.getDateInstance(DateFormat.MEDIUM);
    return fmt.format(cal.getTime());
  }

  /**
   * formats month, year
   */
  public static String formatMonthYear(int month, int year)
  {
    if (year != 0)
    {
      StringBuffer disp=new StringBuffer();
      if (month >= 1 && month <= 12)
        disp.append(months_[month-1]).append(", ");
      disp.append(formatYear(year));
      return disp.toString();
    }
    else
      return "";
  }

  /**
   * formats date range (Month, yyyy - Month, yyyy)
   */
  public static String formatDateRange(int mon, int yr, int Mon, int Yr)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(formatMonthYear(mon, yr));
    String hi=formatMonthYear(Mon, Yr);
    if (buf.length() > 0 || hi.length()>0)
    {
      buf.append(" - ");
    }
    buf.append(hi);
    return buf.toString();
  }

  /**
   * formats year range (yyyy-yyyy)
   */
  public static String formatYearRange(int yr, int Yr)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(formatYear(yr));
    if (yr != 0 || Yr != 0)
    {
      buf.append(" - ");
    }
    buf.append(formatYear(Yr));
    return buf.toString();
  }

  /**
   * div year, decade (div=10), century (div=100)
   */
  protected static int divYear(int yr, int div)
  {
    return (yr / div) * div;
  }

  /**
   * decade
   */
  public static int decade(int yr)
  {
    return divYear(yr, 10);
  }

  /**
   * mid-decade
   */
  public static int midDecade(int yr)
  {
    return decade(yr)+5;
  }

  /**
   * century
   */
  public static int century(int yr)
  {
    return divYear(yr, 100);
  }

  /**
   * formats decades (div=10) or centuries (div=100)
   */
  public static String formatDivYears(int yr, int div)
  {
    if (yr != 0)
    {
      int divYr=divYear(yr, div);
      StringBuffer buf=new StringBuffer();
      buf.append(Math.abs(divYr));
      buf.append("s");
      if (divYr < 0)
      {
        buf.append(bceQualifier_);
      }
      return buf.toString();
    }
    else
      return "";
  }

  /**
   * formats century
   */
  public static String formatCentury(int yr)
  {
    return formatDivYears(yr, 100);
  }

  /**
   * format decade
   */
  public static String formatDecade(int yr)
  {
    return formatDivYears(yr, 10);
  }

  /**
   * format mid decade
   */
  public static String formatMidDecade(int yr)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("mid ");
    buf.append(formatDecade(yr));
    return buf.toString();
  }

  /**
   * post year
   */
  public static String formatPostYear(int yr)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("post ").append(formatYear(yr));
    return buf.toString();
  }

  /**
   * post year
   */
  public static String formatPreYear(int yr)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("pre ").append(formatYear(yr));
    return buf.toString();
  }

  /**
   * format circa year
   */
  public static String formatCircaYear(int yr)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("c. ").append(formatYear(yr));
    return buf.toString();
  }

  /**
   * display this date object
   */
  public String toString()
  {
    if (cal_ != null)
      return formatDate(cal_);
    else if (circaYear_ != 0)
      return formatCircaYear(circaYear_);
    else
    {
      StringBuffer disp=new StringBuffer();
      // TODO: more fancy formatting of date ranges, e.g.,
      // pre-XXXX
      // post-XXXX
      // mid-xx00's
      // xxx0's etc.
      // xxxx (if January, xxxx - Dec. xxxx)
      if (dateRangeLowYear_!=0 && dateRangeHighYear_ != 0) {
      	
      	if (dateRangeLowYear_ == dateRangeHighYear_) {
      		String year  = new StringBuffer().append( dateRangeLowYear_ ).toString();
      		return (year);      		
      	} else {
      	  String range = new StringBuffer().append( dateRangeLowYear_ ).append("-").append( dateRangeHighYear_ ).toString();
      	  return (range);
      	}
      	
      	/* 20031208 KRU disabled smart formatting:
      	
        // single year (1/yyyy - 12/yyyy)
        if (dateRangeLowYear_==dateRangeHighYear_) 
        {
          // no more months...
          // this failed before because if the range was null
          // then this would return null...I think
          return formatYear(dateRangeLowYear_);
        }
        // decade
        else if (dateRangeLowYear_ % 10 == 0 && dateRangeLowYear_ % 100 !=0
                 && dateRangeHighYear_-dateRangeLowYear_==9)
        {
          return formatDecade(dateRangeLowYear_);
        }
        // century
        else if (dateRangeLowYear_ % 100 == 0 && dateRangeHighYear_-dateRangeLowYear_==99)
        {
          return formatCentury(dateRangeLowYear_);
        }
        // mid-decade
        else if (midDecade(dateRangeLowYear_)==midDecade(dateRangeHighYear_) &&
                 midDecade(dateRangeLowYear_)-dateRangeLowYear_ < 4 &&
                 dateRangeHighYear_-midDecade(dateRangeHighYear_) < 4)
        {
          return formatMidDecade(dateRangeLowYear_);
        }
        else
          return formatYearRange(dateRangeLowYear_, dateRangeHighYear_);
        */
      }
      
      else if (dateRangeLowYear_ != 0)
        return formatPostYear(dateRangeLowYear_);
      else if (dateRangeHighYear_ != 0)
        return formatPreYear(dateRangeHighYear_);
    }
    return "";
  }
}
