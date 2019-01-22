
/**
 * Title:        AdvancedSearchPage<p>
 * Description:  base class for advanced search form jsp<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.jsp;

import edu.umass.ccbit.database.Creators;
import edu.umass.ccbit.database.Materials;
import edu.umass.ccbit.database.Kinds;
import edu.umass.ccbit.database.Substances;
import edu.umass.ccbit.database.AccessionNumbers;
import edu.umass.ccbit.database.ItemTypes;
import edu.umass.ccbit.database.Keywords;
import edu.umass.ccbit.database.Topics;
import edu.umass.ccbit.database.Glossary;
import edu.umass.ccbit.database.TableRanks;
import edu.umass.ccbit.util.JspUtil;
import edu.umass.ccbit.util.JspSession;
import edu.umass.ccbit.util.AdvancedSearchParameters;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;

public abstract class AdvancedSearchPage extends MainSearchPage //HttpDbJspBase
{
  // search param saved names
  public static final String Topics_ = "topics";
  public static final String Keywords_ = "keywords";
  public static final String Materials_ = "materials";
  public static final String Kinds_ = "kinds";
  public static final String Substances_ = "substances";
  // request params
  public static final String RefineSearch_="redo";
  //default text input length
  public static final int TextInputDefaultLength_=50;
  // request values
  protected boolean refineSearch_;
  // Creator object
  Creators creators_;
  // Material object
  public static Materials materials_;
  // Kind object
  public static Kinds kinds_;
  // Substance object
  public static Substances substances_;
  // Accession object
  AccessionNumbers accessionNumbers_;
  ItemTypes itemTypes_;
  public static Topics topics_;
  public static Keywords keywords_;
  public static Glossary glossary_;

  // adv. parameters
  AdvancedSearchParameters searchAdvParams_;

  /**
   * init...calls base class then sets its own init parameters
   */
  public void jspInit()
  {
    super.jspInit();
    try
    {
      getDbConnection();
      tableRanks_ = new TableRanks();
      tableRanks_.load(connection_, getServletContext());
      itemTypes_=new ItemTypes();
      itemTypes_.load(connection_, getServletContext());
      topics_=new Topics();
      topics_.load(connection_, getServletContext());
      keywords_=new Keywords();
      keywords_.load(connection_, getServletContext());
      // load new data - accession #'s, creators, materials
      //creators_ = new Creators();
      //creators_.load(connection_, getServletContext());
      materials_ = new Materials();
      materials_.load(connection_, getServletContext());
      kinds_ = new Kinds();
      kinds_.load(connection_, getServletContext());
      substances_ = new Substances();
      substances_.load(connection_, getServletContext());
      accessionNumbers_ = new AccessionNumbers();
      accessionNumbers_.load(connection_, getServletContext());
      glossary_=new Glossary();
      glossary_.load(connection_, getServletContext());
      releaseDbConnection();
    }
    catch (Exception e)
    {
      // oopsy...
      System.out.println("WARNING: Unable to load data needed for advanced search page!");
      System.out.println(e.toString());
    }
  }

  /**
   * load from session
   */
  public void load(HttpSession session) throws SQLException
  {
    refineSearch_=servletParams_.containsKey(RefineSearch_);
    if (refineSearch_)
    {
      searchAdvParams_=AdvancedSearchParameters.loadAdvFromSession(session);
    }
    else
    {
      searchAdvParams_=new AdvancedSearchParameters();
    }
  }

  /**
   * title text
   */
  protected String titleText()
  {
    return searchAdvParams_.title();
  }

  /**
   * place name text
   */
  protected String placeNameText()
  {
    return searchAdvParams_.placeName();
  }

  /**
   * owner text
   */
  protected String ownerText()
  {
    return searchAdvParams_.owner();
  }

  /**
   * accession text
   */
  protected String accessionText()
  {
    return searchAdvParams_.accessionText();
  }

  /**
   * creator text
   */
  protected String creatorText()
  {
    return searchAdvParams_.creator();
  }

  /**
   * form integer value with alternate string text if zero
   */
  private String integerFormValue(int value, String text)
  {
    if (value != 0)
      return Integer.toString(value);
    else
      return text;
  }

  /**
   * lower search year
   */
  protected String searchLowerYear()
  {
    return integerFormValue(searchAdvParams_.searchLowerYear(), "yyyy");
  }

  /**
   * upper search year
   */
  protected String searchUpperYear()
  {
    return integerFormValue(searchAdvParams_.searchUpperYear(), "yyyy");
  }

  /**
   * search text
   */
  protected String searchText()
  {
    return searchAdvParams_.searchText();
  }

  /**
   * creator dropdown
   */
  protected void writeCreatorDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.Creator_).append(" size=1>\n");
    buf.append("<OPTION value=\"\">creator is...\n");
    for (int i=0; i<creators_.getCount(); i++)
    {
      buf.append("<OPTION value='").append(creators_.getValue(i)).append("'");
      if (creators_.getValue(i).compareTo(searchAdvParams_.creator())==0 && creators_.getName(i).length()>0)
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(creators_.getName(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * creator text input
   */
  protected void writeCreatorTextInput(JspWriter out) throws IOException
  {
    writeCreatorTextInput(out, TextInputDefaultLength_);
  }

  /**
   * creator text input variable size
   */
  protected void writeCreatorTextInput(JspWriter out, int size) throws IOException
  {
    JspUtil.writeTextInput(out, AdvancedSearchParameters.Creator_, creatorText(), size, 200);
  }

  /**
   * accession dropdown
   */
  protected void writeAccessionDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.Accession_).append(" size=1>\n");
    buf.append("<OPTION value=\"\">accession number is...\n");
    for (int i=0; i<accessionNumbers_.getCount(); i++)
    {
      buf.append("<OPTION value=").append(accessionNumbers_.getAccessionNumber(i));
      if (accessionNumbers_.getAccessionNumber(i).compareTo(searchAdvParams_.accession())==0)
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(accessionNumbers_.getAccessionNumber(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * item type dropdown
   */
  protected void writeItemTypeDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.ItemType_).append(" size=1>\n");
    buf.append("<OPTION value=\"\">item type is...\n");
    for (int i=0; i<itemTypes_.getCount(); i++)
    {
      buf.append("<OPTION value=\"").append(itemTypes_.getItemType(i)).append("\"");
      if (itemTypes_.getItemType(i).compareTo(searchAdvParams_.itemType())==0)
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(itemTypes_.getItemType(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * topic dropdown
   */
  protected void writeTopicDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.Topic_).append(" size=1>\n");
    buf.append("<OPTION value=0>topic is...\n");
    for (int i=0; i<topics_.getCount(); i++)
    {
      buf.append("<OPTION value=").append(topics_.getTopicID(i));
      if (topics_.getTopicID(i)==searchAdvParams_.topic())
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(topics_.getTopic(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * substance dropdown
   */
  protected void writeSubstanceDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.Substance_).append(" size=1>\n");
    buf.append("<OPTION value=''>material is...\n");
    for (int i=0; i<substances_.getCount(); i++)
    {
      buf.append("<OPTION value='").append(substances_.getSubstance(i)).append("'");
      if (substances_.getSubstance(i)==searchAdvParams_.substance())
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(substances_.getSubstance(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * category dropdown (itemtype)
   */
  protected void writeCategoryDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.ItemType_).append(" size=1>\n");
    buf.append("<OPTION value=''>item type is...\n");
    for (int i=0; i<itemTypes_.getCount(); i++)
    {
      buf.append("<OPTION value='").append(itemTypes_.getItemType(i)).append("'");
      if (itemTypes_.getItemType(i)==searchAdvParams_.itemType())
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(itemTypes_.getItemType(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * material dropdown
   */
  protected void writeMaterialDropdown(JspWriter out) throws IOException
  {
    StringBuffer buf=new StringBuffer();
    buf.append("<SELECT name=").append(AdvancedSearchParameters.Material_).append(" size=1>\n");
    buf.append("<OPTION value=0>material is...\n");
    for (int i=0; i<materials_.getCount(); i++)
    {
      buf.append("<OPTION value=").append(materials_.getMaterialID(i));
      if (materials_.getMaterialID(i)==searchAdvParams_.material())
        buf.append(" SELECTED");
      buf.append(">");
      buf.append(materials_.getMaterialName(i)).append("\n");
    }
    buf.append("</SELECT>\n");
    out.print(buf.toString());
  }

  /**
   * accession input
   */
  protected void writeAccessionTextInput(JspWriter out) throws IOException
  {
    writeAccessionTextInput(out, TextInputDefaultLength_);
  }

  /**
   * accession input with variable size
   */
  protected void writeAccessionTextInput(JspWriter out, int size) throws IOException
  {
    JspUtil.writeTextInput(out, AdvancedSearchParameters.AccessionText_, accessionText(), size, 200);
  }

  /**
   * title
   */
  protected void writeTitleTextInput(JspWriter out) throws IOException
  {
    writeTitleTextInput(out, TextInputDefaultLength_);
  }

  /**
   * title
   */
  protected void writeTitleTextInput(JspWriter out, int size) throws IOException
  {
    JspUtil.writeTextInput(out, AdvancedSearchParameters.Title_, titleText(), size, 200);
  }

  /**
   * place name
   */
  protected void writePlaceNameTextInput(JspWriter out) throws IOException
  {
    writePlaceNameTextInput(out, TextInputDefaultLength_);
  }

  /**
   * place name
   */
  protected void writePlaceNameTextInput(JspWriter out, int size) throws IOException
  {
    JspUtil.writeTextInput(out, AdvancedSearchParameters.PlaceName_, placeNameText(), size, 200);
  }

  /**
   * owner
   */
  protected void writeOwnerTextInput(JspWriter out) throws IOException
  {
    writeOwnerTextInput(out, TextInputDefaultLength_);
  }

  /**
   * owner
   */
  protected void writeOwnerTextInput(JspWriter out, int size) throws IOException
  {
    JspUtil.writeTextInput(out, AdvancedSearchParameters.Owner_, ownerText(), size, 200);
  }

  /**
   * contains checkbox
   */
  protected void writeContainsCheckbox(JspWriter out) throws IOException
  {
    JspUtil.writeCheckbox(out, AdvancedSearchParameters.ContainsTextSearch_, searchAdvParams_.containsTextSearch());
  }

  /**
   * write thumbnail radio buttons
   */
  protected void writeShowThumbnailCheckbox(JspWriter out) throws IOException
  {
    JspUtil.writeCheckbox(out, AdvancedSearchParameters.NoThumbnails_, searchAdvParams_.noThumbnails());
  }
}
