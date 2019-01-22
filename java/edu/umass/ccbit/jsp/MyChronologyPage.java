
/**
 * Title:        MyChronologyPage<p>
 * Description:  base class for a chronology jsp page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.Chronologies;
import edu.umass.ccbit.database.ChronologyEvents;
import edu.umass.ccbit.database.MainCollectionOrderedItemsList;
import museum.history.deerfield.centuries.database.MyCollection;
import edu.umass.ccbit.util.ChronologyParameters;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.servlet.ServletParams;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

public abstract class MyChronologyPage extends ItemOrderedListList
{
  ChronologyParameters parameters_;
  ChronologyEvents chronologyEvents_;
  Chronologies chrons_=new Chronologies();
  MyCollection collection_;
  private static final String ItemPageBaseUrl_ = "chron.itempagebaseurl";
  protected String itemPageBaseUrl_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    itemPageBaseUrl_=initParams_.getString(ItemPageBaseUrl_, "");
  }

  /**
   * load the chronology info from the database, collection(if) from session
   * interlace
   */
  public void load( HttpSession session ) throws SQLException {
    parameters_ = new ChronologyParameters();
    chronologyEvents_ = new ChronologyEvents();
    chrons_.load(connection_, getServletContext());
    getChronologyParameters();
    getChronologyEvents();
    
    if (parameters_.getUseCollection()) {
    	collection_ = (MyCollection) JspSession.load( session, MyCollection.AttributeName_ );
    	
      if (collection_ == null) {
      	collection_ = new MyCollection();
        JspSession.save( session, MyCollection.AttributeName_, collection_ );
      }
      
      load ( session, collection_ );
    } else {
      collection_ = new MyCollection();
    }
  }

  /**
   * path to itempage from chronology page...
   */
  protected String itemPageBaseUrl()
  {
    return itemPageBaseUrl_;
  }

  /**
   * display chronology events for year
   */
  protected String displayChronoEvent(int year)
  {
    StringBuffer buf = new StringBuffer();
    String prefix = "<td bgcolor=\"#CCCCCC\"><font face=\"Verdana, Arial, Helvetica, sans-serif\" size=\"-1\">";
    buf.append(prefix);
    for(int i=0; i<chronologyEvents_.getCount(); i++)
    {
      int testYear = chronologyEvents_.getEventYear(i);
      if(testYear > year)
        break;
      if(testYear == year)
        buf.append(appendEvent(chronologyEvents_, i));
    }
    if(buf.length()==prefix.length())
    {
      buf.append("<B>");
      if(year<0)
        buf.append(-1*year).append(" BC");
      else
        buf.append(year);
      buf.append("</B>");
    }
    buf.append("</td>");
    return buf.toString();
  }

  /**
   * get the first collection item year
   */
  protected int getFirstYear()
  {
    int test[];
    try
    {
      for(int i=0;i<=collection_.getCount()-1;i++)
      {
        test = pager_.getDateInfo(i);
        if(test[1]!=0)
          return test[1];
      }
    }
    catch(Exception e)
    {
      return 0;
    }
    return 0;
  }

  /**
   * get the last collection item year
   */
  protected int getLastYear()
  {
    try
    {
      int test[] = pager_.getDateInfo(collection_.getCount()-1);
      return test[1];
    }
    catch(Exception e)
    {
      return 0;
    }
  }

  /**
   * display item from user collection on the chronology
   */
  protected String displayCollectionItem(int year)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<td><table width=\"95%\" border=\"0\" cellspacing=\"0\" cellpadding=\"3\">");
    for(int i=0; i<collection_.getCount(); i++)
    {
      int test[] = pager_.getDateInfo(i);
      int testYear = test[1];
      if(testYear > year)
        break;
      if(testYear == year)
        buf.append(appendItem(i, year));
    }
    buf.append("</table></td>");
    return buf.toString();
  }

  /**
   * display items without dates(this should be frowned upon)
   * by default items with no date (nd) have year 0
   * this year never occured(maybe...)
   */
  protected String displayNonDateItems(String message)
  {
    StringBuffer buf = new StringBuffer();
    String ndItems = displayCollectionItem(0);
    if(ndItems.length()!=0)
      buf.append(message).append("<table><tr>").append(ndItems).append("</tr></table>");
    return buf.toString();
 }

  /**
   * add the event to the list occuring in that year
   */
  private String appendEvent(ChronologyEvents events, int i)
  {
    StringBuffer buf = new StringBuffer();
    String tempPath = events.getEventImagePath(i);
    buf.append("<p>");
    buf.append("<b>").append(events.getEventYear(i)).append("</b>");
    buf.append(" - ");
    if(tempPath != null)
      buf.append("<img src=").append(tempPath).append(">");
    buf.append(events.getEventDesc(i));
    buf.append("</p>");
    return buf.toString();
  }

  /**
   * add the collection item to the list of occuring items in that year
   */
  private String appendItem(int i, int year)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<tr valign=\"top\" align=left>");
    buf.append("<td align=left>").append(resultImageLink(i, 150, 150)).append("</td>");
    buf.append("<td align=left><font size=\"2\">").append(year).append("</font><br>");
    buf.append("<font size=\"+1\">").append(resultLink(i)).append("</font><br>");
    buf.append(resultText(i));
    return buf.toString();
  }

  /**
   * get the parameters
   */
  public void getChronologyParameters()
  {
    parameters_.getParameters(servletParams_);
  }

  /**
   * write year
   */
  protected String writeYear(int i)
  {
    StringBuffer buf = new StringBuffer();
    if(i<0)
      buf.append(-1*i).append("s BC");
    else
      buf.append(i).append("s");
    return buf.toString();
  }

  /**
   * get the events for the selected chronology
   */
  public void getChronologyEvents() throws SQLException
  {
    chronologyEvents_.load(connection_, parameters_.getChronology());
  }

  /**
   * get the upper year
   */
  protected String getUpperYear()
  {
    StringBuffer buf = new StringBuffer();
    int upYear = parameters_.getUpperYear();
    if(upYear == 0)
      upYear = getLastYear();
    if(upYear < 0)
      buf.append(-1*upYear).append(" BC");
    else
      buf.append(upYear);
    return buf.toString();
  }

  /**
   * get the lower year
   */
  protected String getLowerYear()
  {
    StringBuffer buf = new StringBuffer();
    int lowYear = parameters_.getLowerYear();
    if(lowYear == 0)
      lowYear = getFirstYear();
    if(lowYear < 0)
      buf.append(-1*lowYear).append(" BC");
    else
      buf.append(lowYear);
    return buf.toString();
  }

  /**
   * get the polar upper year
   */
  protected int getPolarUpperYear()
  {
    int upYear = parameters_.getUpperYear();
    if(upYear == 0)
      upYear = getLastYear();
    return upYear;
  }

  /**
   * get the polar lower year
   */
  protected int getPolarLowerYear()
  {
    int lowYear = parameters_.getLowerYear();
    if(lowYear == 0)
      lowYear = getFirstYear();
    return lowYear;
  }

  /**
   * get chron name
   */
  protected String getChronName()
  {
    if(chrons_!=null)
      return isNull(chrons_.getName(parameters_.getChronology()));
    return "No Chronology Specified";
  }

  /**
   * checks to see if the string is null...returns reasonable text instead
   */
  public String isNull(String string)
  {
    if(string != null)
      return string;
    return "No Chronology Specified";
  }

  /**
   * get 'use collection?'
   */
  protected boolean getUseCollection()
  {
    return parameters_.getUseCollection();
  }
}
