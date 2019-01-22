
/**
 * Title:        AdvancedSearchParameters<p>
 * Description:  additional parameters to search for including inherited parameters<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.util;

import edu.umass.ccbit.jsp.AdvancedSearchPage;
import edu.umass.ccbit.database.Topics;
import edu.umass.ccbit.database.Keywords;
import edu.umass.ccbit.database.Materials;
import edu.umass.ccbit.database.Kinds;
import edu.umass.ccbit.database.Substances;
import edu.umass.ccbit.database.ItemTypes;
import java.util.StringTokenizer;
import javax.servlet.http.HttpSession;

public class AdvancedSearchParameters extends SearchParameters {
  public static final String SessionAttribute_="adv_search_parameters";
  //advanced search parameter names
  public static final String Material_="material";
  public static final String Accession_="accession";
  public static final String AccessionText_="accessiontext";
  public static final String Creator_="creator";
  public static final String Title_="title";
  public static final String PlaceName_="placename";
  public static final String Topic_="topic";
  public static final String Owner_="owner";
  public static final String Kind_="kind";
  public static final String Substance_="substance";
  private Topics topics_;
  private Keywords keywords_;
  private Materials materials_;
  private ItemTypes itemtypes_;
  private Kinds kinds_;
  private Substances substances_;

  public AdvancedSearchParameters()
  {
    // call search parameter constructor
    super();
    // put advanced fields
    put(Material_, "");
    put(Accession_, "");
    put(AccessionText_, "");
    put(Creator_, "");
    put(Title_, "");
    put(PlaceName_, "");
    put(Owner_, "");
    put(Topic_, "");
    put(Kind_, "");
    put(Substance_, "");
    put(ItemType_, "");
  }

  /**
   * topic id...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public int topic()
  {
    return getInt(Topic_, 0);
  }

  /**
   * kind id...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public String kind()
  {
    return getString(Kind_, "");
  }

  /**
   * substance id...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public String substance()
  {
    return getString(Substance_, "");
  }

  /**
   * formulates contains clause
   */
  private String containsClause(String fields, String searchText)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("(contains(");
    buf.append(fields).append(", '\"");
    buf.append(searchText);
    buf.append("\"'))");
    return buf.toString();
  }

  /**
   * formulates search query from parameters in this object and accession number
   * match using contains or freetext within the ItemName field
   */
  public String searchAccessionQuery()
  {
    if (accessionText().length()>0)
    {
      StringBuffer buf = new StringBuffer();
      //insert the default query
      buf.append(query());
      buf.append(searchableConstraint());
      buf.append(" and ").append("Main.AccessionNumber like '%").append(accessionText()).append("%'");
      return buf.toString();
    }
    else
      return null;
  }

  /**
   * formulates search query from parameters in this object and item text
   * match using contains within the ItemName field
   */
  public String searchItemNameQuery()
  {
    if (title().length()>0)
    {
      StringBuffer buf = new StringBuffer();
      buf.append(query());
      buf.append(searchableConstraint());
      buf.append(" and ");
      buf.append(containsClause("Main.ItemName", title()));
      return buf.toString();
    }
    else
      return null;
  }

  /**
   * search collection places
   */
  public String searchCollectionPlaces()
  {
    if (placeName().length()>0)
    {
      StringBuffer buf = new StringBuffer();
      buf.append(query());
      buf.append(" left outer join CollectionPlaces on CollectionPlaces.CollectionPlaceID=Main.PlaceOfOriginID ");
      buf.append(searchableConstraint());
      buf.append(" and ").append(containsClause("CollectionPlaces.Name", placeName()));
      return buf.toString();
    }
    else
      return null;
  }

  /**
   * search collection places
   */
  public String searchMainLinkTablesQuery()
  {
    if (itemType().length() > 0 || material() != 0 || topic()!=0 || keyWord()!=0 || kind().length() > 0 || substance().length() > 0 || itemType().length()!=0)
    {
      StringBuffer buf = new StringBuffer();
      buf.append(query());
      if(keyWord() != 0)  // KEYWORDS
      {
        buf.append(" inner join KeywordLink on KeywordLink.ItemID=Main.ItemID ");
      }
      if(itemType().length() > 0)
      {
        buf.append(" inner join Nomenclature on Nomenclature.NomenclatureID=Main.NomenclatureID ");
      }
      if(topic() != 0)
      {
        buf.append(" inner join TopicLink on TopicLink.ItemID=Main.ItemID ");
      }
      if(kind().length() > 0)
      {
        buf.append(" inner join Nomenclature on Nomenclature.NomenclatureID=Main.NomenclatureID ");
      }
      if(substance().length() > 0)
      {
        buf.append(" inner join ProcessMaterials on ProcessMaterials.ProcessMaterialID=Main.ProcessMaterialID ");
      }
      buf.append(searchableConstraint());
      if (material() != 0)
      {
        buf.append(" and Main.ProcessMaterialID=").append(material()).append(" ");
      }
      if(keyWord() != 0)
      {
        buf.append(" and KeywordLink.KeywordID=").append(keyWord()).append(" ");
      }
      if(itemType().length() > 0)
      {
        buf.append(" and Nomenclature.Category='").append(itemType()).append("' ");
      }
      if(topic() != 0)
      {
        buf.append(" and TopicLink.TopicID=").append(topic()).append(" ");
      }
      if(kind().length() > 0)
      {
        buf.append(" and Nomenclature.SubCategory='").append(kind()).append("' ");
      }
      if(substance().length() > 0)
      {
        buf.append(" and ProcessMaterials.WebMat='").append(substance()).append("' ");
      }
      return buf.toString();
    }
    else
      return null;
  }

  /**
   * search collection places
   */
  public String searchMainByTextDateQuery()
  {
    if (searchText().length() > 0 || (searchUpperYear() != 0 && searchLowerYear() != 0))
      return textSearchQuery();
    else
      return null;
  }

  /**
   * search collection people
   */
  public String searchCollectionPeople()
  {
    if (creator().length() > 0)
    {
      StringBuffer buf = new StringBuffer();
      buf.append(query());
      buf.append(" left outer join ObjectCreatorLink on ObjectCreatorLink.itemid=Main.itemid ");
      buf.append(" left outer join CollectionPeople on CollectionPeople.CollectionPersonID=ObjectCreatorLink.CollectionPersonID ");
      buf.append(searchableConstraint());
      buf.append(" and ").append(containsClause("CollectionPeople.*", creator()));
      buf.append(unionQuery());
      buf.append(" left outer join ObjectCreatorLink on ObjectCreatorLink.itemid=Main.itemid ");
      buf.append(" left outer join ObjectProducer on ObjectProducer.ObjectProducerID=ObjectCreatorLink.ObjectProducerID ");
      buf.append(searchableConstraint());
      buf.append(" and ").append(containsClause("ObjectProducer.*", creator()));
      return buf.toString();
    }
    else
      return null;
  }

  /**
   * search summary
   *     Material_
   *     Keyword_
   *
   *     Accession_
   *     AccessionText_
   *     Creator_
   *     Title_
   *     PlaceName_
   *     Owner_
   *     ItemType_
   */
  public String searchSummary(HttpSession session)
  {
    topics_ = AdvancedSearchPage.topics_;
    materials_ = AdvancedSearchPage.materials_;
    kinds_ = AdvancedSearchPage.kinds_;
    substances_ = AdvancedSearchPage.substances_;
    keywords_ = AdvancedSearchPage.keywords_;
    StringBuffer buf = new StringBuffer();
    buf.append(super.searchSummary());
    if(accession().length()!=0)
      buf.append(", ").append(accession());
    if(accessionText().length()!=0)
      buf.append(", ").append(accessionText());
    if(creator().length()!=0)
      buf.append(", ").append(creator());
    if(title().length()!=0)
      buf.append(", ").append(title());
    if(placeName().length()!=0)
      buf.append(", ").append(placeName());
    if(owner().length()!=0)
      buf.append(", ").append(owner());
    if(itemType().length()!=0)
      buf.append(", ").append(itemType());
    if(material()!=0 && materials_ != null)
    {
      buf.append(", ").append(materials_.getMaterial(material()));
    }
    if(topic()!=0 && topics_ != null)
    {
      buf.append(", ").append(topics_.getTopicByID(topic()));
    }
    if(kind().length() > 0)
      buf.append(", ").append(kind());
    if(substance().length() > 0)
      buf.append(", ").append(substance());
    if(keyWord()!=0 && keywords_ != null)
    {
      buf.append(", ").append(keywords_.getKeywordByID(keyWord()));
    }
    if(buf.length()>0 && buf.charAt(0)==',')
      buf.replace(0,1,"");
    return buf.toString();
  }

  /**
   * material
   */
  public int material()
  {
    return getInt(Material_, 0);
  }

  /**
   * accession number
   */
  public String accession()
  {
    return getString(Accession_, "");
  }

  /**
   * accession search string
   */
  public String accessionText()
  {
    return getString(AccessionText_, "");
  }

  /**
   * creator
   */
  public String creator()
  {
    return getString(Creator_, "");
  }

  /**
   * title
   */
  public String title()
  {
    return getString(Title_, "");
  }

  /**
   * placename
   */
  public String placeName()
  {
    return getString(PlaceName_, "");
  }

  /**
   * owner
   */
  public String owner()
  {
    return getString(Owner_, "");
  }

  /**
   * exact spelling
   * I'm not going to implement this yet....I don't think we need it at all
   */
  public boolean exactSpellingSearch()
  {
    return true;
  }

  /**
   * save to session
   */
  public synchronized void saveToSession(HttpSession session)
  {
    JspSession.save(session, SessionAttribute_, this);
  }

  /**
   * load from session
   */
  public synchronized static AdvancedSearchParameters loadAdvFromSession(HttpSession session)
  {
    AdvancedSearchParameters param=(AdvancedSearchParameters) JspSession.load(session, SessionAttribute_);
    // if not found in session, return a new (empty) instance
    if (param==null)
      param=new AdvancedSearchParameters();
    return param;
  }
}
