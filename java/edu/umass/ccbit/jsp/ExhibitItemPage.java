
/**
 * Title:        ExhibitItemPage<p>
 * Description:  base class for view jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import edu.umass.ccbit.database.CollectionThemeItem;
import edu.umass.ccbit.database.MainCollectionItem;
import edu.umass.ccbit.util.ReadingLevel;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ckc.util.CkcException;
import edu.umass.ckc.util.UserException;
import edu.umass.ckc.util.StringUtils;
import edu.umass.ckc.html.HtmlUtils;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Vector;
import java.net.URLEncoder;

public abstract class ExhibitItemPage extends ItemPage
{
  public String exhibitView_ = "Web_ExhibitThemeView";
  public CollectionThemeItem obj_;
  public int subthemeID_;
  public int xMax_;
  public int exhibitID_;
  public Vector subthemeItems_;
  public static final String Jsp_ ="/turns/view.jsp";

  /**
   * load the information from the database
   */
  public void load(HttpSession session) throws SQLException, Exception
  {
    super.load(session);
    if(exhibitID_ == -1)
      exhibitID_ = JspSession.getInt(session, ExhibitIndexPage.ExhibitID_, 1);
    xMax_ = JspSession.getInt(session, ExhibitIndexPage.XMax_, 0);

    Statement st = connection_.createStatement();
    ResultSet result = st.executeQuery(query());
    if(result.next()==true)
      obj_ = CollectionThemeItem.new_ExhibitQuery(result);
    else
      obj_ = new CollectionThemeItem();
    result.close();
    st.close();

    st = connection_.createStatement();
    result = st.executeQuery( subthemeQuery() );
    subthemeItems_ = new Vector();
    CollectionThemeItem obj;
    while (result.next()==true) {
	obj = CollectionThemeItem.new_ExhibitQuery(result);
	subthemeItems_.add( obj );
    }
    result.close();
    st.close();

    getTranscriptionData(session);
  }

  /**
   * link from exhibit itempage to itempage
   */
  protected String itemPage(String message)
  {
    StringBuffer buf = new StringBuffer();
    HtmlUtils.addToLink(ItemID_,itemID_,true,buf);
    return HtmlUtils.anchor(super.Jsp_+buf.toString(), message);
  }

  /**
   * param string that gets appended to each URL in the page dropdown
   */  
  protected String pagesRequestParams() {
    StringBuffer buf = new StringBuffer();
    buf.append( CollectionThemeItem.SubthemeID_ ).append( "=" ).append( subthemeID_ );
    buf.append( "&" ).append( super.pagesRequestParams() );
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
   * transcription link:  overrides super method to include subthemeID
   */
  protected String transcriptionLink(String message)
  {
    String transcription = tranList_.getTranscription(item_.imagePageName(imageIndex_));
    if(item_.isDocumentImage(imageIndex_) && transcription!="" && tScript_!=1)
    {
      StringBuffer buf = new StringBuffer();
      buf.append("<a href=\"").append(link(jsp(), itemID_, imageIndex_, readingLevel_, 1, subthemeID_)).append("\">");
      buf.append(message);
      buf.append("</a>");
      return buf.toString();
    }
    return "";
  }

  /**
   * document image link:  overrides super method to include subthemeID
   */
  protected String documentImageLink(String message) {
    String transcription = tranList_.getTranscription(item_.imagePageName(imageIndex_));
    
    if(item_.isDocumentImage(imageIndex_) && tScript_==1) {
      StringBuffer buf = new StringBuffer();
      buf.append("<a href=\"").append(link(jsp(), itemID_, imageIndex_, readingLevel_, 0, subthemeID_)).append("\">");
      buf.append(message);
      buf.append("</a>");
      return buf.toString();
    }
    return "";
  }

  /**
   * @param itemID the unique itemid for this item
   * @param imageIndex_ the current image for this item
   * @param readingLevel_ the reading level to switch to
   * @param transcription 1 if switching to page with transcription
   * @param subthemeID_ the current subtheme
   */
  public static String link(String jsp, int itemID, int imageIndex_, String readingLevel_, int transcription, int subthemeID_)
  {
    StringBuffer buf = new StringBuffer(link(jsp, itemID, imageIndex_, readingLevel_, transcription));
    HtmlUtils.addToLink( CollectionThemeItem.SubthemeID_, subthemeID_, false, buf );
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
   * construct a link to this page
   */
  public static String link(int itemID, int imageIndex_, int subthemeID_)
  {
  	StringBuffer buf = new StringBuffer();
    buf.append( link( Jsp_, itemID, imageIndex_ ) );
	HtmlUtils.addToLink( CollectionThemeItem.SubthemeID_, subthemeID_, false, buf );
	return buf.toString();
  }

  /**
   * construct a link to this page
   */
  public static String link(int itemID, int imageIndex_)
  {
    return link(Jsp_, itemID, imageIndex_);
  }

  /**
   * returns parameter string "?itemID=___&subthemeid=___" for an item in an era with the same subtheme
   * @param xThemeID index of the era
   */
  public String jumpEraParams( int xThemeID ) {
      CollectionThemeItem item = (CollectionThemeItem)subthemeItems_.get( xThemeID-1 );
      return item.parameters();
  }

  /**
   * construct a query for a single item
   */
  public String query()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(exhibitView_);
    buf.append(" WHERE ItemID=").append(itemID_);
    buf.append(" AND exhibitid=").append(exhibitID_);
    return buf.toString();
  }

  /**
   * query to get all items in a subtheme for all eras
   */
  public String subthemeQuery()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(exhibitView_);
    buf.append(" WHERE subthemeid=").append(subthemeID_);
    buf.append(" AND yThemeID=").append(yThemeID());
    buf.append(" AND exhibitid=").append(exhibitID_);
    buf.append(" ORDER BY xThemeID");
    return buf.toString();
  }

  /**
   * object viewer link
   * @param relPath the relative path to the viewer jsp page
   */
  public String objectViewer(String relPath)
  {
    StringBuffer buf=new StringBuffer();
    buf.append(relPath);
    buf.append("/");
    String name = obj_.itemName_ != null ? URLEncoder.encode(obj_.itemName_) : "";
    buf.append(item_.objectViewer(name, imageIndex_));
    HtmlUtils.addToLink( ObjectViewerPage.IsExhibitItem_, 1, false, buf );
    HtmlUtils.addToLink( CollectionThemeItem.SubthemeID_, subthemeID_, false, buf );
    return buf.toString();
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
      HtmlUtils.addToLink(Exhibit_, 1, false, buf);
      return HtmlUtils.anchor(multimediaBaseUrl_+buf.toString(), message);
    }
    return "";
  }

  /**
   * replace spaces in title with '%20'
   */
  private String parseTitle(String title)
  {
    return StringUtils.substitute(title, " ", "%20");
  }

  /**
   * related item link
   */
  protected String relatedItemLink(int index)
  {
    return HtmlUtils.anchor("../collection/itempage.jsp?itemid="+item_.relatedItemID(index), item_.relatedItemName(index));
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
   * image tag
   */
  protected String imageTag(int width, int height)
  {
    return item_.objectImage(width, height, imageIndex_);
  }

  /**
   * get alt name
   */
  protected String getAltName()
  {
    if(obj_.altName_ != null)
      return obj_.altName_;
    return "";
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
    subthemeID_ = servletParams_.getInt(CollectionThemeItem.SubthemeID_, 0);
    exhibitID_  = servletParams_.getInt("id", -1);
  }

  /**
   * get flash tag
   */
  protected String getFlashTag(int index)
  {
    StringBuffer buf = new StringBuffer();
    buf.append("<OBJECT classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"");
    buf.append(" codebase=\"http://active.macromedia.com/flash2/cabs/swflash.cab#version=4,0,0,0\"");
    buf.append(" ID=").append(activity_.activityName(index));
    buf.append(" WIDTH=").append(activity_.width(index));
    buf.append(" HEIGHT=").append(activity_.height(index)).append(">");
    buf.append("<PARAM NAME=movie VALUE=\"").append(activity_.activityPath(index)).append("\">");
    buf.append("<PARAM NAME=quality VALUE=high> <PARAM NAME=bgcolor VALUE=#FFFFFF>");
    buf.append("<EMBED src=\"").append(activity_.activityPath(index)).append("\"");
    buf.append(" quality=high bgcolor=#FFFFFF");
    buf.append(" WIDTH=").append(activity_.width(index));
    buf.append(" HEIGHT=").append(activity_.height(index));
    buf.append(" TYPE=\"application/x-shockwave-flash\"");
    buf.append(" PLUGINSPAGE=\"http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\"></EMBED></OBJECT>");
    return buf.toString();
  }

  /**
   * an age appropriate label for this image
   * in the context of a particular theme and era
   */
  public String label()
  {
    String label="";
    if(readingLevel_.compareToIgnoreCase("beginner")==0)
      label = StringUtils.substitute(StringUtils.substitute(obj_.begLabel_, "\n", "<BR>"),"\t","&nbsp;");
    if(readingLevel_.compareToIgnoreCase("intermediate")==0)
      label = StringUtils.substitute(StringUtils.substitute(obj_.intLabel_, "\n", "<BR>"),"\t","&nbsp;");
    if(readingLevel_.compareToIgnoreCase("advanced")==0 || label==null)
      label = StringUtils.substitute(StringUtils.substitute(obj_.label_, "\n", "<BR>"),"\t","&nbsp;");
    if(label==null)
      return "";
    return label;
  }

  /**
   * returns the name of one of the three eras
   * @param xThemeID index of the era
   * */
  public String era( int xThemeID ) {
      CollectionThemeItem item = (CollectionThemeItem)subthemeItems_.get( xThemeID-1 );
      return item.xThemeName_;
  }

  /**
   * the era name
   */
  public String era()
  {
    return obj_.xThemeName_;
  }

  /**
   * the theme name
   */
  public String theme()
  {
    return obj_.yThemeName_;
  }

  /** 
   * the subtheme name
   */
  public String subthemeName() {
      // all items in subthemeItems_ are in the same subtheme, so any one of them will do
      CollectionThemeItem item = (CollectionThemeItem)subthemeItems_.get( 0 );
      return (item.subthemeName_);
  }

  /**
   * the era id
   */
  public int xThemeID()
  {
    return obj_.xThemeID_;
  }

  /**
   * the theme id
   */
  public int yThemeID()
  {
    return obj_.yThemeID_;
  }
}
