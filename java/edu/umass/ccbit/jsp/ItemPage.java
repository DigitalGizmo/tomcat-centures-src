/**
 * Title:        ItemPage<p>
 * Description:  base class for item jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: ItemPage.java,v 1.53 2003/12/03 21:01:10 keith Exp $
 *
 * $Log: ItemPage.java,v $
 * Revision 1.53  2003/12/03 21:01:10  keith
 * revisions made during implementation of persistent collections and activities
 *
 * Revision 1.52  2003/09/12 16:26:51  keith
 * disabled nonPluginActivityLink method, which is apparently deprecated
 *
 * Revision 1.51  2003/04/30 17:40:14  keith
 * fixed bug in pagesDropdown()
 *
 * Revision 1.50  2003/02/05 20:26:52  keith
 * many changes related to implementing subthemeid URL param
 *
 * Revision 1.49  2003/02/04 22:12:06  keith
 * merged three dropdown methods into one: pagesDropdown()
 *
 * Revision 1.48  2002/04/12 14:07:57  pbrown
 * fixed copyright info
 *
 * Revision 1.47  2001/11/14 18:41:40  pbrown
 * fixed links to multimedia page
 *
 * Revision 1.46  2001/09/28 15:05:45  pbrown
 * added throws UserException wherever parse is called, added activity forum code
 *
 * Revision 1.45  2001/09/25 16:49:32  pbrown
 * added phantom image to fix overlapping transcription text
 *
 * Revision 1.44  2001/07/25 20:50:22  tarmstro
 * many changes for lexicon/searching/priority
 *
 * Revision 1.43  2001/06/11 15:01:11  pbrown
 * fixed navigation probs to/from itempage/viewer
 *
 * Revision 1.42  2001/06/07 03:00:46  pbrown
 * fixed incorrect urls on exhibit item pages
 *
 * Revision 1.41  2001/05/24 18:57:56  pbrown
 * loads static db data in jspInit instead of load..saves in instance vars, not in session
 *
 * Revision 1.40  2001/05/24 16:57:13  pbrown
 * fixed prev/next
 *
 * Revision 1.39  2001/05/02 17:57:48  pbrown
 * added style attribute for sid images - for exhibit theme pages
 *
 * Revision 1.38  2001/04/24 19:44:47  tarmstro
 * fixed transcription problem
 *
 * Revision 1.37  2001/02/27 19:49:55  tarmstro
 * made activity links popups
 *
 * Revision 1.36  2001/02/26 16:47:36  pbrown
 * changes for searching, and relocation of www root
 *
 * Revision 1.35  2001/02/09 20:44:39  tarmstro
 * added a relative path link to item detail
 *
 * Revision 1.34  2001/01/26 17:03:36  pbrown
 * fixed (temporarily?) crash when no transcription data
 *
 * Revision 1.33  2001/01/25 20:47:14  tarmstro
 * changes for transcription linking/display
 *
 * Revision 1.32  2001/01/25 18:42:40  tarmstro
 * added information for searched transcriptions
 *
 * Revision 1.31  2001/01/23 20:53:06  tarmstro
 * changes for linking items to other items
 *
 * Revision 1.30  2001/01/19 19:34:50  tarmstro
 * changed multimedia information and transcription conditionals
 *
 * Revision 1.29  2001/01/18 18:24:40  tarmstro
 * changes in transcription data
 *
 * Revision 1.28  2001/01/18 16:07:05  tarmstro
 * changes for links from itempage and changes for see also
 *
 * Revision 1.27  2001/01/11 19:05:01  tarmstro
 * changed parameters for activity methods
 *
 * Revision 1.26  2001/01/11 16:57:50  pbrown
 * added thumbnails to item pages
 *
 * Revision 1.25  2001/01/05 21:28:30  pbrown
 * finished error handler code
 *
 * Revision 1.24  2001/01/04 19:21:15  tarmstro
 * changed javascript to open window rather than change url
 *
 * Revision 1.23  2000/12/18 17:51:23  pbrown
 * changed links to my collection..base url must be specified
 *
 * Revision 1.22  2000/12/04 21:41:51  tarmstro
 * fixed comparison for transcriptions
 *
 * Revision 1.21  2000/11/21 21:23:02  pbrown
 * changes due to reimplementation of mrsid info...mrsidarchive_ parameter is
 * no longer needed and has been removed from many method calls
 *
 * Revision 1.20  2000/11/01 21:45:38  pbrown
 * changes for item detail page
 *
 * Revision 1.19  2000/10/11 20:01:53  tarmstro
 * added method to link to add item to collection
 *
 * Revision 1.18  2000/09/25 18:12:55  tarmstro
 * many changes for searching and displaying results
 *
 * Revision 1.17  2000/08/03 20:37:07  tarmstro
 * fixed null string compare
 *
 * Revision 1.16  2000/08/03 18:54:27  tarmstro
 * added methods for transcriptions
 *
 * Revision 1.15  2000/08/02 21:21:21  tarmstro
 * added methods for transcriptions
 *
 * Revision 1.14  2000/08/02 15:42:24  tarmstro
 * added methods to parse label and add pp links
 *
 * Revision 1.13  2000/07/31 14:45:32  tarmstro
 * many changes for age appropriate labels, glossary
 *
 * Revision 1.12  2000/06/27 16:14:39  pbrown
 * many changes for searching, bugfixes etc.
 *
 * Revision 1.11  2000/06/14 18:42:08  pbrown
 * adds import
 *
 * Revision 1.10  2000/06/13 21:57:59  pbrown
 * changed image lists
 *
 * Revision 1.9  2000/06/12 16:38:44  pbrown
 * some changes for better site navigation
 *
 * Revision 1.8  2000/06/12 14:21:09  pbrown
 * implemented browse
 *
 * Revision 1.7  2000/06/08 02:47:55  pbrown
 * added code for paging through search results and returning to
 * search results page
 *
 * Revision 1.6  2000/06/02 21:52:55  pbrown
 * many changes for searching..more to come
 *
 * Revision 1.5  2000/05/30 18:52:42  pbrown
 * added code to produce 'see also..' dropdown list
 *
 * Revision 1.4  2000/05/26 20:55:47  pbrown
 * added methods for handling associated items (web/collection items)
 *
 * Revision 1.3  2000/05/25 22:25:09  pbrown
 * many changes for item pages including building list of associated items
 *
 * Revision 1.2  2000/05/24 06:12:55  pbrown
 * first implementation of item pages, object viewer
 *
 * Revision 1.1  2000/05/19 21:42:30  pbrown
 * base class for item jsp's
 *
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.BrowseSubcategoryItems;
import edu.umass.ccbit.database.DbLoadableItemIDList;
import edu.umass.ccbit.database.MainCollectionItem;
import edu.umass.ccbit.database.MainCollectionItemsPager;
import edu.umass.ccbit.database.BasicSearchResults;
import edu.umass.ccbit.database.AdvancedSearchResults;
import edu.umass.ccbit.database.Glossary;
import edu.umass.ccbit.database.PeoplePlacesItems;
import edu.umass.ccbit.database.InteractiveActivity;
import edu.umass.ccbit.database.MultimediaObjects;
import edu.umass.ccbit.database.TranscriptionList;
import edu.umass.ccbit.database.TranscriptionSearchResults;
import edu.umass.ccbit.database.SearchResults;
import edu.umass.ccbit.image.CollectionImage;
import edu.umass.ccbit.util.ItemIDList;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.ReadingLevel;
import edu.umass.ccbit.util.SearchParameters;
import edu.umass.ccbit.util.AdvancedSearchParameters;
import edu.umass.ckc.html.HtmlUtils;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.util.StringUtils;
import java.io.IOException;
import java.lang.Exception;
import java.lang.Math;
import java.lang.StringBuffer;
import java.util.Vector;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;

public abstract class ItemPage extends HttpDbJspBase
{
  protected int itemID_;
  protected int imageIndex_;
  protected MainCollectionItem item_;
  protected int tScript_;
  public static final String ItemID_="itemid";
  public static final String ImageIndex_="img";
  public static final String Exhibit_="exhibit";
  public static final String ReadingLevel_="level";
  public static final String Transcription_="transcription";
  public static final String Jsp_="/collection/itempage.jsp";
  // urls called by this page
  protected String searchResultsBaseUrl_;
  protected String searchAdvResultsBaseUrl_;
  protected String searchMainBaseUrl_;
  protected String searchAdvMainBaseUrl_;
  protected String browseMainBaseUrl_;
  protected String browseSubcategoryBaseUrl_;
  protected String activityBaseUrl_;
  protected String multimediaBaseUrl_;
  protected String pagerMainUrl_;
  protected String pagerResultsUrl_;
  protected String copyNotice_;
  // image link lists
  protected Vector objectImageLinks_;
  protected ItemIDList documentPages_;
  protected ItemIDList transcriptionPages_=null;
  // init param to get search results...should be a jsp parameter, but
  // needs to be a context parameter until tomcat is fixed
  protected static final String SearchMainBaseUrl_="item.searchmainurl";
  protected static final String SearchAdvMainBaseUrl_="item.searchadvmainurl";
  protected static final String SearchResultsBaseUrl_="item.searchbaseurl";
  protected static final String SearchAdvResultsBaseUrl_="item.searchadvbaseurl";
  protected static final String BrowseMainBaseUrl_="item.browsebaseurl";
  protected static final String BrowseSubcategoryBaseUrl_="item.browsesubcategoryurl";
  protected static final String CopyNotice_="item.copyright";
  // activity page
  protected static final String ActivityBaseUrl_="turns.activityurl";
  // multimedia page
  protected static final String MultimediaBaseUrl_="item.multimediaurl";
  // image-less item's page name
  protected static final String TranscriptOnly_ = "transcriptOnly";
  // search results
  protected DbLoadableItemIDList itemPagerList_;
  // index of item in search results
  private int itemPagerIndex_;
  // page number of in item pager
  private int pageNumber_;
  // view mode for this page
  private int viewMode_;
  // reading level for this page
  protected String readingLevel_;
  // glossary
  protected Glossary glossary_=null;
  // people and places
  protected PeoplePlacesItems ppitems_=null;
  // interactive activity
  protected InteractiveActivity activity_;
  // multimedia objects
  protected MultimediaObjects multi_;
  // list of transcriptions of document images, item specific
  protected TranscriptionList tranList_;
  protected TranscriptionSearchResults scriptSearch_;
  protected String searchText_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    searchAdvMainBaseUrl_     = initParams_.getString(SearchAdvMainBaseUrl_, "");
    searchMainBaseUrl_        = initParams_.getString(SearchMainBaseUrl_, "");
    searchAdvResultsBaseUrl_  = initParams_.getString(SearchAdvResultsBaseUrl_, "");
    searchResultsBaseUrl_     = initParams_.getString(SearchResultsBaseUrl_, "");
    browseMainBaseUrl_        = initParams_.getString(BrowseMainBaseUrl_, "");
    browseSubcategoryBaseUrl_ = initParams_.getString(BrowseSubcategoryBaseUrl_, "");
    activityBaseUrl_          = initParams_.getString(ActivityBaseUrl_, "");
    multimediaBaseUrl_        = initParams_.getString(MultimediaBaseUrl_, "");
    copyNotice_               = initParams_.getString(CopyNotice_, "");
    try
    {
      // load this stuff once and save in instance vars..no need for sessions
      getDbConnection();
      getGlossaryData();
      getPeoplePlacesData();
      releaseDbConnection();
    }
    catch (Exception e)
    {
      // sorry kids..no glossary or p&p for you!
      System.out.println("WARNING: failed to load glossary and/or people & places data!");
    }
  }

  /**
   * construct a link w/item id attached
   */
  public String itemDetailLink()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("/collection/item_detail.jsp");
    HtmlUtils.addToLink(ItemID_, itemID_, true, buf);
    return buf.toString();
  }

  /**
   * item detail link using relative path
   */
  public String itemDetailLink(String relPath)
  {
    if(!relPath.endsWith("/"))
      relPath += "/";
    StringBuffer buf = new StringBuffer();
    buf.append(relPath).append("item_detail.jsp");
    HtmlUtils.addToLink(ItemID_, itemID_, true, buf);
    return buf.toString();
  }

  /**
   * the jsp page base url as a method to be called from methods which construct
   * urls so it will return right value for derived classes
   */
  protected String jsp()
  {
    return Jsp_;
  }

  /**
   * construct a link to this page
   */
  public static String link(int itemID, int imageIndex_)
  {
    return link(Jsp_, itemID, imageIndex_);
  }

  /**
   * construct a link to this page
   */
  public static String link(String jsp, int itemID, int imageIndex_)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(jsp);
    HtmlUtils.addToLink(ItemID_, itemID, true, buf);
    HtmlUtils.addToLink(ImageIndex_, imageIndex_, false, buf);
    return buf.toString();
  }

  /**
   * construct a link to this page with a different label
   * @param itemID the unique itemid for this item
   * @param imageIndex_ the current image for this item
   * @param readingLevel_ the reading level to switch to
   * @param transcription 1 if switching to page with transcription
   */
  public static String link(int itemID, int imageIndex_, String readingLevel_, int transcription)
  {
    return link(Jsp_, itemID, imageIndex_, readingLevel_, transcription);
  }

  /**
   * construct a link to this page with a different label
   * @param itemID the unique itemid for this item
   * @param imageIndex_ the current image for this item
   * @param readingLevel_ the reading level to switch to
   * @param transcription 1 if switching to page with transcription
   */
  public static String link(String jsp, int itemID, int imageIndex_, String readingLevel_, int transcription)
  {
    StringBuffer buf = new StringBuffer(link(jsp, itemID, imageIndex_));
    HtmlUtils.addToLink(ReadingLevel_, readingLevel_, false, buf);
    HtmlUtils.addToLink(Transcription_, transcription, false, buf);
    return buf.toString();
  }

  /**
   * get servlet request parameters into ServletParams object
   * this calls parseRequestParameters in superclass, then sets data members associated with
   * servlet parameters
   * @param request the servlet request
   */
  protected void parseRequestParameters(HttpServletRequest request) throws IOException, CkcException, UserException
  {
    super.parseRequestParameters(request);
    itemID_=servletParams_.getInt(ItemID_);
    imageIndex_=servletParams_.getInt(ImageIndex_, 0);
    readingLevel_=servletParams_.getString(ReadingLevel_, "session");
    tScript_=servletParams_.getInt(Transcription_, 0);
  }

  /**
   * number of images
   */
  protected int imageCount()
  {
    return item_.imageMaxIndex() + 1;
  }

  /**
   * object image count
   */
  protected int objectImageCount()
  {
    return objectImageLinks_.size();
  }

  /**
   * document page count
   */
  protected int documentPageCount()
  {
    return documentPages_.size();
  }

  /**
   * transcription page count
   */
  protected int transcriptionPageCount()
  {
    return transcriptionPages_ != null ? transcriptionPages_.size() : 0;
  }

  /**
   * object image link
   */
  protected String objectImageLink(int index)
  {
    return (String) objectImageLinks_.get(index);
  }

  /**
   * age appropriate image link
   * @param index the index of the reading level (beginnger, intermediate, advanced)
   */
  protected String ageImageLinks(int index)
  {
    return HtmlUtils.anchor(link(jsp(), itemID_,imageIndex_, ReadingLevel.ages_[index], tScript_), ReadingLevel.image(ReadingLevel.ages_[index], readingLevel_));
  }

  /**
   * search result link
   */
  protected String itemPagerLink()
  {
    if (itemPagerIndex_ != -1 && itemPagerList_ != null)
    {
      return itemPagerList_.pagerLink(pagerMainUrl_, pagerResultsUrl_, pageNumber_);
    }
    else
      return "";
  }

  /**
   * search results description (this also applies to browse subcategory)
   */
  protected String searchResultsDescription()
  {
    if (itemPagerIndex_ != -1 && itemPagerList_ != null)
    {
      StringBuffer buf=new StringBuffer();
      buf.append(itemPagerIndex_ + 1);
      buf.append(" of ");
      buf.append(itemPagerList_.getCount());
      buf.append(" items found");
      return buf.toString();
    }
    else
      return "";
  }

  /**
   * link to previous item in search results
   */
  protected String previousItemLink()
  {
    final String linkText="&lt;&nbsp;previous";
    if (itemPagerIndex_ < 0 || itemPagerList_==null)
      return "";
    else if (itemPagerIndex_==0)
      return linkText;
    else
    {
      int previousItem=itemPagerList_.itemID(itemPagerIndex_-1);
      return HtmlUtils.anchor(link(jsp(), previousItem, 0, readingLevel_, tScript_), linkText);
    }
  }

  /**
   * link to next item in search results
   */
  protected String nextItemLink()
  {
    final String linkText="next&nbsp;&gt;";
    if (itemPagerIndex_ < 0 || itemPagerList_ == null)
      return "";
    else if (itemPagerIndex_ < itemPagerList_.getCount()-1)
    {
      int nextItem=itemPagerList_.itemID(itemPagerIndex_+1);
      return HtmlUtils.anchor(link(jsp(), nextItem, 0, readingLevel_, tScript_), linkText);
    }
    else
      return linkText;
  }

  /**
   * get view mode data
   */
  protected void getViewModeData(HttpSession session)
  {
    viewMode_=JspSession.getItemPageViewMode(session);
    if (viewMode_==JspSession.itemPageViewModes.Search_)
    {
      itemPagerList_=new BasicSearchResults();
      pagerMainUrl_=SearchResultsPage.refineSearchURL(searchMainBaseUrl_);
      pagerResultsUrl_=searchResultsBaseUrl_;
    }
    else if (viewMode_==JspSession.itemPageViewModes.Browse_)
    {
      itemPagerList_=new BrowseSubcategoryItems();
      pagerMainUrl_=browseMainBaseUrl_;
      pagerResultsUrl_=browseSubcategoryBaseUrl_;
    }
    else if (viewMode_==JspSession.itemPageViewModes.AdvSearch_)
    {
      itemPagerList_=new AdvancedSearchResults();
      pagerMainUrl_=AdvancedSearchResultsPage.refineSearchURL(searchAdvMainBaseUrl_);
      pagerResultsUrl_=searchAdvResultsBaseUrl_;
    }
    if (itemPagerList_ != null)
    {
      itemPagerList_.getFromSession(session);
      itemPagerIndex_=itemPagerList_.indexForItemID(itemID_);
      pageNumber_=MainCollectionItemsPager.resultsPageNumber(session, itemPagerIndex_);
    }
  }

  /**
   * get reading level data
   * @param session the current session
   */
  protected void getReadingLevelData(HttpSession session)
  {
    // if the reading level isn't a parameter, then get it from the session
    if(readingLevel_.compareToIgnoreCase("session")==0)
      readingLevel_ = ReadingLevel.getFromSession(session);
  }

  /**
   * get interactive activity data
   */
  protected void getActivityData() throws SQLException
  {
    activity_ = new InteractiveActivity();
    activity_.load(connection_, getServletContext(), itemID_);
  }

  /**
   * get multimedia object data
   */
  protected void getMultimediaData() throws SQLException
  {
    multi_ = new MultimediaObjects();
    multi_.load(connection_, getServletContext(), itemID_);
  }

  /**
   * get glossary data
   */
  protected void getGlossaryData() throws SQLException
  {
    if(glossary_==null)
    {
      glossary_=new Glossary();
      glossary_.load(connection_, getServletConfig().getServletContext());
    }
  }

  /**
   * get p&p data...this should work the same way as the glossary
   */
  protected void getPeoplePlacesData() throws SQLException
  {
    if(ppitems_==null)
    {
      ppitems_=new PeoplePlacesItems();
      ppitems_.load(connection_, 0, 0);  // 0,0 denotes "all people and places"
    }
  }

  /**
   * get image data...this appears to split the image list in two, but
   * in reality, only one of the lists should be non-empty!
   */
  protected void getImageData()
  {
    objectImageLinks_=new Vector();
    documentPages_=new ItemIDList();
    for (int index=0; index < imageCount(); index++)
    {
      if (item_.imageType(index)==CollectionImage.Types.Object_)
        objectImageLinks_.add(index == imageIndex_ ? HtmlUtils.bold(item_.imageLinkText(index)) : imageLink(index));
      else if (item_.imageType(index)==CollectionImage.Types.Document_)
        documentPages_.add(index);
    }
  }

  /**
   * get the transcription data for an image if it is a document
   */
  protected void getTranscriptionData(HttpSession session) throws SQLException, CkcException
  {
    tranList_ = new TranscriptionList();
    tranList_.load (connection_, itemID_, getServletContext());
    transcriptionPages_ = new ItemIDList();

    for (int index=0; index < imageCount(); index++) {
      if (item_.imageType(index)==CollectionImage.Types.Document_ &&
          (
            tranList_.getPageNames().contains(StringUtils.removeChars(" ", item_.imagePageName(index).toLowerCase()))
            ||
            tranList_.getPageNames().contains(item_.imagePageName(index))
          )
        )
        transcriptionPages_.add(index);
    }
    
    if (scriptSearch_!=null) scriptSearch_.clear();
    
    if (tranList_.getCount()>0) {
      SearchParameters searchParams = (SearchParameters) JspSession.load(session, SearchParameters.SessionAttribute_);
      if(searchParams==null)
        searchParams = (AdvancedSearchParameters) JspSession.load(session, AdvancedSearchParameters.SessionAttribute_);
      if(searchParams!=null)
      {
        String searchMode = searchParams.getString(SearchParameters.ContainsTextSearch_);
        scriptSearch_ = new TranscriptionSearchResults();
        scriptSearch_.load(connection_, itemID_, searchText_, item_, searchMode);
      }
    }
  }

  /**
   * description of matching pages
   */
  protected String scriptPagesFound(String noMatch, String oneMatch, String manyMatch)
  {
    if(scriptSearch_!=null)
    {
      StringBuffer buf = new StringBuffer();
      if(scriptSearch_.size()<1)
        return noMatch;
      if(scriptSearch_.size()==1)
        buf.append(oneMatch);
      else
        buf.append(manyMatch);
      for(int i=0;i<scriptSearch_.size();i++)
      {
        buf.append("<a href=\"");
        buf.append(link(jsp(), itemID_, scriptSearch_.imgIndex(scriptSearch_.elementAt(i)), readingLevel_, 1));
        buf.append("\">").append(scriptSearch_.elementAt(i)).append("</a>");
        if((i+1)<scriptSearch_.size())
          buf.append(", ");
      }
      return buf.toString();
    }
    return "";
  }

  /**
   * load data for item page
   */
  protected void load(HttpSession session) throws CkcException, Exception
  {
    item_=new MainCollectionItem();
    item_.load(connection_, itemID_);
    imageIndex_=Math.min(imageIndex_, item_.imageMaxIndex());
    searchText_ = (String) JspSession.load(session, SearchResults.SearchText_);
    getImageData();
    getReadingLevelData(session);
    getViewModeData(session);
    getActivityData();
    getMultimediaData();
    getTranscriptionData(session);
  }

  /**
   * page title
   */
  protected String title()
  {
    return itemName();
  }

  /**
   * item name
   */
  protected String itemName()
  {
    return item_ != null ? item_.itemName_ : "Not found";
  }

  /**
   * copyright notice
   * @return either what is in the database or the default context parameter by deafault
   */
  protected String copyrightNotice()
  {
    return item_.copyright_ != null ? item_.copyright_ : copyNotice_;
  }

  /**
   * image tag
   *
   * Returns the HTML tag needed to display the item's current image.  If the 
   * item has no image, returns its transcription instead.
   */
  protected String imageTag(int width, int height) {
  	
  	if (item_.imageMaxIndex() >= 0) {
      return item_.objectImage(width, height, imageIndex_);
      
    } else {
     	return documentTranscription();
    }
  }

  /**
   * thumbnail image
   */
  protected String thumbnailImage(int width, int height, int index)
  {
    return HtmlUtils.anchor(link(jsp(), itemID_, index, readingLevel_, tScript_), item_.objectImage(width, height, index));
  }

  /**
   * image link
   */
  protected String imageLink(int index)
  {
    return HtmlUtils.anchor(link(jsp(), itemID_, index, readingLevel_, tScript_), item_.imageLinkText(index));
  }

  /**
   * transcription
   *
   * Returns the HTML-formatted text of the transcript for the current image of
   * the item.  If the item has no image, return the item's transcription.
   */
  protected String documentTranscription() {
    String pgNm;
    boolean isDocument;
    
    if (imageIndex_ >= 0) {
    	pgNm       = item_.imagePageName(imageIndex_);
    	isDocument = item_.isDocumentImage(imageIndex_);
    } else {
    	pgNm       = TranscriptOnly_;
    	isDocument = true;
    }
    String transcription = tranList_.getTranscription(pgNm);

    if (isDocument && transcription!="" && transcription!=null) {
      StringBuffer buf = new StringBuffer();
      // generic formatting of font and color
      buf.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"20\" bordercolor=\"#996633\">");
      buf.append("<tr bgcolor=\"#E3D9B8\"><td>").append(transcription).append("</td></tr></table>");
      // alternative of formatting included in the database
      return buf.toString();
    
    } else {
      return "";
    }
  }

  protected String newspaperTranscription() {
  	
    String pgNm = "document";
    String transcription = tranList_.getTranscription(pgNm);

    if (transcription!="" && transcription!=null) {      
      StringBuffer buf = new StringBuffer();
      // generic formatting of font and color
      buf.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"20\" bordercolor=\"#996633\">");
      buf.append("<tr bgcolor=\"#E3D9B8\"><td>").append(transcription).append("</td></tr></table>");
      // alternative of formatting included in the database
      return buf.toString();
    
    } else {
      return "";
    }
  }

  /**
   * transcription link
   */
  protected String transcriptionLink(String message)
  {
    String transcription = tranList_.getTranscription(item_.imagePageName(imageIndex_));
    if(item_.isDocumentImage(imageIndex_) && transcription!="" && tScript_!=1)
    {
      StringBuffer buf = new StringBuffer();
      buf.append("<a href=\"").append(link(jsp(), itemID_, imageIndex_, readingLevel_, 1)).append("\">");
      buf.append(message);
      buf.append("</a>");
      return buf.toString();
    }
    return "";
  }

  /**
   * document image link
   */
  protected String documentImageLink(String message)
  {
    String transcription = tranList_.getTranscription(item_.imagePageName(imageIndex_));
    if(item_.isDocumentImage(imageIndex_) && tScript_==1)
    {
      StringBuffer buf = new StringBuffer();
      buf.append("<a href=\"").append(link(jsp(), itemID_, imageIndex_, readingLevel_, 0)).append("\">");
      buf.append(message);
      buf.append("</a>");
      return buf.toString();
    }
    return "";
  }

  /**
   * multimedia link
   */
  protected String multimediaLink(String message)
  {
    StringBuffer buf = new StringBuffer();
    if(multi_.numItems()!=0)
    {
      HtmlUtils.addToLink(ItemID_, itemID_, true, buf);
      HtmlUtils.addToLink(ImageIndex_, imageIndex_, false, buf);
      return HtmlUtils.anchor(multimediaBaseUrl_+buf.toString(), message);
    }
    return "";
  }

  /**
   * activity link
   */
  protected String activityLink(String message)
  {
    int temp = activity_.itemID(0);
    if(temp==itemID_)
    {
      StringBuffer buf = new StringBuffer();
      HtmlUtils.addToLink(ItemID_, itemID_, true, buf);
      String url = "javascript:popupWindow('"+activityBaseUrl_+buf.toString()+"','popup','menubar=yes,scrollbars=yes,resizable=yes,width=500,height=250')";
      return HtmlUtils.anchor(url, null, message);
    }
    else
      return "";
  }

  /**
   * nonflash activity link
   */
  protected String nonflashActivityLink(String message)
  {
    int temp = activity_.itemID(0);
    if(temp==itemID_ && activity_.nonflashPath(0)!=null)
    {
      String url = "javascript:popupWindow('"+activity_.nonflashPath(0)+"','popup','menubar=yes,scrollbars=yes,resizable=yes,width=500,height=250')";
      return HtmlUtils.anchor(url, null, message);
    }
    else
      return "";
  }
  
  /**
   * collection link(adding item)
   */
  protected String collectionLink(String baseUrl, String linkText)
  {
    StringBuffer buf = new StringBuffer();
    buf.append(baseUrl);
    HtmlUtils.addToLink(ItemID_,itemID_,true,buf);
    //HtmlUtils.addToLink("method","add",false,buf);
    return HtmlUtils.anchor(buf.toString(), linkText);
  }

  /**
   * number of attributes for item
   */
  protected int numAttributes()
  {
    return item_ != null ? item_.numAttributes() : 0;
  }

  /**
   * attribute name
   */
  protected String attributeName(int id)
  {
    return item_ != null ? item_.attributeName(id) : null;
  }

  /**
   * attribute value
   */
  protected String attributeValue(int id)
  {
    return item_ != null ? item_.attributeValue(id) : null;
  }

  /**
   * web label
   */
  protected String label()
  {
    if(readingLevel_.compareToIgnoreCase("beginner")==0)
      return parseLabel(fifthGradeLabel());
    if(readingLevel_.compareToIgnoreCase("intermediate")==0)
      return parseLabel(highSchoolLabel());
    if(readingLevel_.compareToIgnoreCase("advanced")==0)
      return parseLabel(webLabel());
    return "";
  }

  /**
   * parse label
   */
  private String parseLabel(String label)
  {
    String temp1 = glossary_.glossaryParse(label);
    String temp2 = ppitems_.ppParse(temp1);
    return temp2;
  }

  /**
   * web label (advanced)
   */
  protected String webLabel()
  {
    return item_ != null ? item_.webLabel() : null;
  }

  /**
   * fifth grade label (beginner)
   */
  protected String fifthGradeLabel()
  {
    return item_ != null ? item_.fifthGradeLabel() : null;
  }

  /**
   * high school label (intermediate)
   */
  protected String highSchoolLabel()
  {
    return item_ != null ? item_.highSchoolLabel() : null;
  }

  /**
   * object viewer
   */
  protected String objectViewer()
  {
    return item_ != null ? item_.objectViewer(imageIndex_) : null;
  }

  /**
   * number of web associations
   */
  protected int associationsCount()
  {
    return item_.associationsCount();
  }

  /**
   * association text
   */
  protected String associationText(int index)
  {
    return item_.associationText(index);
  }

  /**
   * association url
   */
  protected String associationURL(int index)
  {
    return item_.associationURL(index);
  }

  /**
   * related item link
   */
  protected String relatedItemLink(int index)
  {
    return HtmlUtils.anchor(URI_+"?itemid="+item_.relatedItemID(index), item_.relatedItemName(index));
  }


  /**
   * page dropdown
   */
  protected String pagesDropdown() {
  	String prompt;
  	String tScriptParam;
  	int pageCount;
  	ItemIDList pages;
  	
    if (tScript_ == 1) {
    	pageCount = transcriptionPageCount();
    	
		  if (pageCount > 1) {
			  prompt = "Select a transcription:";
			  pages  = transcriptionPages_;			
		  } else {
			  return "";
		  }
		  
	  } else {	
    	pageCount = documentPageCount();
    	
    	if (pageCount > 1) {
			  prompt   = "Select a page:";
			  pages    = documentPages_;
			  tScript_ = 0;
    	} else {
			  return "";
		  }
    }
    
    StringBuffer buf=new StringBuffer();
    buf.append("<script language=\"JavaScript\">\n");
    buf.append("<!--\n");
    buf.append("function goToPage(olist)\n");
    buf.append("{\n");
    buf.append("  var val=olist.options[olist.selectedIndex].value;\n");
    buf.append("  if (val.length > 0) {\n");
    buf.append("    window.location.href=\"").append(URI_).append( "?" );
    buf.append(     pagesRequestParams() ).append( "\n" );
    buf.append("  }\n");
    buf.append("}\n");
    buf.append("//-->\n");
    buf.append("</SCRIPT>\n");
    buf.append("<form>\n");
    buf.append("  " + prompt + "<select name=pages size=1>\n");

    for (int i=0; i < pageCount; i++) {
      int imgIndex=pages.getValue(i);
      buf.append("  <option value=").append(imgIndex);
      if (imageIndex_==imgIndex) buf.append(" selected");
      buf.append(">");
      buf.append(item_.imageLinkText(imgIndex)).append("</option>\n");
    }
    buf.append("  </select>\n");
    buf.append("<input type=button onClick=\"goToPage(this.form.pages)\" value=\"Go...\">\n");
    buf.append("</form>");
    return buf.toString();
  }

  /**
   * param string that gets appended to each URL in the page dropdown; broken out as a separate
   * method so descendant classes can override it to add additional params
   */  
  protected String pagesRequestParams() {
    StringBuffer buf = new StringBuffer();
    buf.            append(ItemID_).       append("=").append(itemID_);
    buf.append("&").append(ReadingLevel_). append("=").append(readingLevel_);
    buf.append("&").append(Transcription_).append("=").append(tScript_);
    buf.append("&").append(ImageIndex_).   append("=").append("\" + val;");
  	return buf.toString();
  }

  /**
   * related items list...
   */
  protected String relatedItemsList()
  {
    int size=item_.relatedItemsCount();
    StringBuffer buf = new StringBuffer();
    for(int i=0;i<size;i++)
    {
      buf.append("<p>").append(relatedItemLink(i)).append("</p>");
    }
    return buf.toString();
  }

  /**
   * associated items list...
   */
  protected String associatedItemsList()
  {
    int size = item_.getNumCollectionAssociations();
    StringBuffer buf = new StringBuffer();
    for (int i=0; i < associationsCount(); i++)
    {
      if(i >= size)
      {
        buf.append("<p><a href=\"").append(associationURL(i)).append("\">");
        buf.append(associationText(i)).append("</a></p>");
      }
      else
      {
        buf.append("<p><a href=\"javascript:onClick=\"popupWindow('").append(associationURL(i));
        buf.append("','popup','menubar=yes,scrollbars=yes,resizable=yes,width=500,height=250')\">");
        buf.append(associationText(i)).append("</a></p>");
      }
    }
    return buf.toString();
  }
}

