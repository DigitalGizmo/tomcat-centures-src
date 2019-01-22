
/**
 * Title:        BasicSearchResultsPage<p>
 * Description:  base class for search basic results jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @version 1.0
 * @author pbrown
 * @version $Id: BasicSearchResultsPage.java,v 1.4 2002/04/12 14:07:49 pbrown Exp $
 *
 * $Log: BasicSearchResultsPage.java,v $
 * Revision 1.4  2002/04/12 14:07:49  pbrown
 * fixed copyright info
 *
 * Revision 1.3  2001/07/25 20:50:22  tarmstro
 * many changes for lexicon/searching/priority
 *
 * Revision 1.2  2000/09/25 18:12:54  tarmstro
 * many changes for searching and displaying results
 *
 * Revision 1.1  2000/08/10 16:48:29  tarmstro
 * renamed class
 *
 * Revision 1.6  2000/06/27 16:14:40  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.5  2000/06/12 16:38:44  pbrown
 * some changes for better site navigation
 *
 * Revision 1.4  2000/06/12 14:21:10  pbrown
 * implemented browse
 *
 * Revision 1.3  2000/06/06 14:37:48  pbrown
 * code to link to refined search and other misc changes
 *
 * Revision 1.2  2000/06/02 21:52:56  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.1  2000/05/30 21:37:08  pbrown
 * new base class for items loadable from db, and beginnings of search/search
 * results
 *
 */

package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.BasicSearchResults;
import java.sql.SQLException;
import edu.umass.ckc.util.CkcException;
import javax.servlet.http.HttpSession;

public abstract class BasicSearchResultsPage extends SearchResultsPage
{
  /**
   * load search results
   */
  protected void load(HttpSession session) throws SQLException, CkcException
  {
    results_=new BasicSearchResults();
    results_.load(connection_, session, servletParams_);
    load(session, results_);
  }
}
