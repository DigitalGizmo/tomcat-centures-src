
/*
 * Title:        PeoplePlacesItemPage<p>
 * Description:  item page for item in people/places collection<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: PeoplePlacesItemPage.java,v 1.9 2002/04/12 14:08:06 pbrown Exp $
 *
 * $Log: PeoplePlacesItemPage.java,v $
 * Revision 1.9  2002/04/12 14:08:06  pbrown
 * fixed copyright info
 *
 * Revision 1.8  2001/09/28 15:05:47  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.7  2000/11/21 21:23:03  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.6  2000/09/25 18:12:55  tarmstro
 * many changes for searching and displaying results
 *
 * Revision 1.5  2000/06/27 22:02:47  pbrown
 * changed jsp link
 *
 * Revision 1.4  2000/06/02 21:52:56  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.3  2000/05/26 20:56:25  pbrown
 * added code for producing image tag
 *
 * Revision 1.2  2000/05/25 22:25:10  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.1  2000/05/15 04:00:56  pbrown
 * base class for people/places item display
 *
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.AssociatedObjectBase;
import edu.umass.ccbit.image.MrSidImage;
import javax.servlet.http.HttpServletRequest;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import java.lang.Exception;
import java.io.IOException;

public abstract class PeoplePlacesItemPage extends HttpDbJspBase
{
  protected int itemType_;
  protected int objectID_;
  protected AssociatedObjectBase item_;

  public static final String ItemType_="itemtype";
  public static final String ObjectID_="id";
  /* the jsp page derived from this class...is this good design?? no f---n way!!!  */
  /* p.s., for added hack value, '../people_places' is added so that link works
     from item page (associated objects) as well as from people/places */
  public static final String Jsp_="../people_places/view.jsp";

  /**
   * get servlet request parameters into ServletParams object
   * this calls parseRequestParameters in superclass, then sets data members associated with
   * servlet parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    itemType_=servletParams_.getInt(ItemType_);
    objectID_=servletParams_.getInt(ObjectID_);
  }

  /**
   * load the object's info from the database
   */
  protected void load() throws CkcException, Exception
  {
    item_=AssociatedObjectBase.newInstance(itemType_);
    item_.load(connection_, objectID_);
  }

  /**
   * page title
   */
  protected String title()
  {
    return item_.name();
  }

  /**
   * object image
   * TODO:
   */
  protected String objectImage()
  {
    return item_.boundedObjectImageTag(250, 250);
  }

  /**
   * object name
   */
  protected String name()
  {
    return item_.name();
  }

  /**
   * object date
   */
  protected String objectDate()
  {
    return item_.objectDate();
  }

  /**
   * web text
   */
  protected String webText()
  {
    if (item_.webText() != null)
      return item_.webText();
    else
      return "";
  }
}
