/*
 * Title:        PeoplePlacesPage<p>
 * Description:  base class for people/places/events jsp pages<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author 
 * @version $Id: PeoplePlacesPage.java,v 1.14 2002/04/12 14:08:07 pbrown Exp $
 *
 * $Log: PeoplePlacesPage.java,v $
 * Revision 1.14  2002/04/12 14:08:07  pbrown
 * fixed copyright info
 *
 * Revision 1.13  2001/09/28 15:05:47  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.12  2000/11/21 21:23:03  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.11  2000/06/02 21:52:56  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.10  2000/05/30 21:35:04  pbrown
 * trivial change
 *
 * Revision 1.9  2000/05/26 20:56:49  pbrown
 * changed image tag code
 *
 * Revision 1.8  2000/05/19 21:42:30  pbrown
 * base class for item jsp's
 *
 * Revision 1.7  2000/05/18 19:05:44  pbrown
 * added objectimage method
 *
 * Revision 1.6  2000/05/15 03:59:16  pbrown
 * removed redundant import
 *
 * Revision 1.5  2000/05/14 03:32:22  pbrown
 * changes due to new PeoplePlacesViewQuery class
 *
 * Revision 1.4  2000/05/13 03:56:57  pbrown
 * changed query formulation method
 *
 * Revision 1.3  2000/05/12 22:47:54  pbrown
 * many changes, rewritten for sprinta db driver
 *
 * Revision 1.2  2000/05/09 17:02:28  pbrown
 * added query and some other code
 *
 * Revision 1.1  2000/05/09 13:28:54  pbrown
 * fixed db connection in jsp base class
 *
 */

package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.AssociatedObjectBase;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.image.MrSidImage;
import javax.servlet.http.HttpServletRequest;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.lang.Exception;
import java.io.IOException;

public abstract class PeoplePlacesPage extends HttpDbJspBase
{
  protected int viewMode_;
  protected int sortBy_;
  protected static final String ViewMode_="view";
  protected static final String SortBy_="sort";
  protected PeoplePlacesItems ppitems_;

  /**
   * get servlet request parameters into ServletParams object
   * this calls parseRequestParameters in superclass, then sets data members associated with
   * servlet parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    viewMode_=servletParams_.getInt(ViewMode_, 0);
    sortBy_=servletParams_.getInt(SortBy_, 0);
  }

  /**
   * title text for page, based on view mode
   */
  public String title()
  {
  	StringBuffer buf = new StringBuffer();
  	buf.append( numItems() );
  	
    switch (viewMode_) {
      case PeoplePlacesItems.ViewModes.ViewPeople:
        buf.append( " People" );
        return (buf.toString());
        
      case PeoplePlacesItems.ViewModes.ViewPlaces:
        buf.append( " Places" );
        return (buf.toString());
        
      case PeoplePlacesItems.ViewModes.ViewEvents:
        buf.append( " Important Events" );
        return (buf.toString());
        
      case PeoplePlacesItems.ViewModes.ViewAll:
      default:
        buf.append( " People and Places and Important Events" );
        return (buf.toString());
    }
  }

  /**
   * image tag for object
   */
  public String objectImage(int nitem)
  {
    return ppitems_.item(nitem).scaledObjectImageTag(150, 180);
  }

  /**
   * number of items returned by query
   */
  public int numItems()
  {
    return ppitems_.getCount();
  }

  /**
   * returns associated object at given index
   */
  public AssociatedObjectBase item(int nitem)
  {
    return ppitems_.item(nitem);
  }

  /**
   * gets list of people/places/events from database
   */
  protected void load() throws CkcException, Exception
  {
    ppitems_=new PeoplePlacesItems();
    ppitems_.load(connection_, viewMode_, sortBy_);
  }
}
