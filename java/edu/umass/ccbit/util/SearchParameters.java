/**
 * Title:        SearchParameters<p>
 * Description:  class for search parameters<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author pbrown
 * @version $Id: SearchParameters.java,v 1.33 2003/12/09 00:02:00 keith Exp $
 *
 * $Log: SearchParameters.java,v $
 * Revision 1.33  2003/12/09 00:02:00  keith
 * disabled searching of CollectionPeople and CollectionPlaces
 *
 * Revision 1.32  2003/07/26 19:38:00  keith
 * reorganized for better readability (moved unused methods to end)
 *
 * Revision 1.31  2002/04/12 14:08:21  pbrown
 * fixed copyright info
 *
 * Revision 1.30  2001/08/07 19:59:57  tarmstro
 * changes for searching/quoting/sorting
 *
 * Revision 1.29  2001/08/01 20:44:12  tarmstro
 * changes for search syntax and booleans
 *
 * Revision 1.28  2001/07/25 20:50:23  tarmstro
 * many changes for lexicon/searching/priority
 *
 * Revision 1.27  2001/07/24 20:44:00  tarmstro
 * changes for ranked searching
 *
 * Revision 1.26  2001/05/30 20:37:22  pbrown
 * change less than to less than or equal in date comparisons
 *
 * Revision 1.25  2001/05/30 19:45:13  pbrown
 * changes for advanced search fixes etc.
 *
 * Revision 1.24  2001/04/10 13:08:05  pbrown
 * some bug fixs
 *
 * Revision 1.23  2001/03/14 18:55:07  pbrown
 * added quotes around search text to fix errors in exact phrase search w/more than one word
 *
 * Revision 1.22  2001/03/08 16:18:08  pbrown
 * fixed missing date search constraints
 *
 * Revision 1.21  2001/03/08 15:43:47  pbrown
 * fixed dump when search text is empty
 *
 * Revision 1.20  2001/03/04 03:31:03  pbrown
 * changes for search pages
 *
 * Revision 1.19  2001/02/26 17:38:12  pbrown
 * fixed missing textSearchClause
 *
 * Revision 1.15  2001/02/12 19:20:08  pbrown
 * changed queries to use new Searchable field in Main table
 *
 * Revision 1.14  2001/02/09 22:51:20  pbrown
 * rewrote multiple queries as union for better performance
 *
 * Revision 1.13  2001/01/25 18:44:07  tarmstro
 * made searching for subcategory AND category active
 *
 * Revision 1.12  2001/01/23 20:53:32  tarmstro
 * changes searching of category to subcategory
 *
 * Revision 1.11  2001/01/23 18:11:50  tarmstro
 * changes for displaying search criterion
 *
 * Revision 1.10  2001/01/19 19:35:47  tarmstro
 * allowed quoting of strings with freetext queries
 *
 * Revision 1.9  2001/01/18 18:26:01  tarmstro
 * changes for default freetext searching
 *
 * Revision 1.8  2000/11/02 17:38:28  pbrown
 * added clear button for search page
 *
 * Revision 1.7  2000/10/24 20:12:33  pbrown
 * changed thumbnail option
 *
 * Revision 1.6  2000/09/25 18:13:31  tarmstro
 * abstracted out SearchParameters for use in different searches
 *
 * Revision 1.5  2000/08/10 16:40:57  tarmstro
 * changed comment
 *
 * Revision 1.4  2000/07/31 14:48:30  tarmstro
 * many changes for date searching, ordered results based on underlying criteria
 *
 * Revision 1.3  2000/06/27 16:16:02  pbrown
 * changes to collection date (not final yet)
 * new class for reading level
 * other changes/bugifxes
 *
 * Revision 1.2  2000/06/12 16:38:45  pbrown
 * some changes for better site navigation
 *
 * Revision 1.1  2000/06/06 14:34:37  pbrown
 * search parameters, used by main search page and results page
 *
 */
package edu.umass.ccbit.util;

import edu.umass.ccbit.jsp.MainSearchPage;
import edu.umass.ckc.util.Parameters;
import edu.umass.ckc.util.StringUtils;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.Vector;

public class SearchParameters extends Parameters
{
  // session attribute
  public static final String SessionAttribute_="search_parameters";
  // request parameter names
  public static final String SearchText_="searchtext";
  public static final String Lexicon_="lexicon";
  public static final String ContainsTextSearch_="contains";
  public static final String SearchLowerYear_="lower_year";
  public static final String SearchUpperYear_="upper_year";
  public static final String ItemType_="type";
  public static final String NoThumbnails_="nopic";
  public static final String KeyWord_="keyword";

  //odd quoting
  public static final String quote_= (new String("\"")).substring(1,1);

  public static final int circa_=12;

  public SearchParameters()
  {
    put(SearchText_, "");
    put(ContainsTextSearch_, "off");
    put(SearchLowerYear_, "0");
    put(SearchUpperYear_, "0");
    put(ItemType_, "");
    put(NoThumbnails_, "off");
    put(KeyWord_, "");
  }

  /**
   * get parameters
   */
  public void getParameters(Parameters param)
  {
    Enumeration keys=keys();
    while (keys.hasMoreElements())
    {
      String keyName=(String) keys.nextElement();
      if (param.containsKey(keyName))
        put(keyName, param.get(keyName));
    }
  }

  /**
   * search text..since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public String searchText()
  {
    return getString(SearchText_, "");
  }

  /**
   * the modified, correctly "quoted" search text to avoid search quoting syntax errors
   */
  private String querySearchText()
  {
    String text = searchText();
    text = removeChar(text, '\"');
    text = StringUtils.substitute(text, "'", "''");
    StringBuffer buf = new StringBuffer();
    if(containsTextSearch()) // CONTAINS -- exact phrase OR boolean expression
    {
      String [] boolWords = {" or "," OR "," and "," AND "};
      for(int i=0; i<boolWords.length; i++)
      {
        String search = boolWords[i];
        int the = text.indexOf(search);
        text = StringUtils.substitute(text, boolWords[i], "\" "+boolWords[i]+" \"");
      }
    }
    buf.append("'\"");
    buf.append(text);
    buf.append("\"'");
    return buf.toString();
  }

  /**
   * remove a character from a String
   */
  public static String removeChar(String text, char c)
  {
    String save = "";
    for(int i=0; i<text.length(); i++)
    {
      if(text.charAt(i) != '\"')
        save += text.charAt(i);
    }
    return save;
  }

  /**
   * free text search...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public boolean containsTextSearch()
  {
    return getString(ContainsTextSearch_, "off").compareTo("on")==0;
  }

  /**
   * text search operator - 'contains' if fundamental text search, 'freetext' otherwise
   * //text search operator - 'freetext' if free text search, 'contains' otherwise
   */
  protected String textSearchOperator()
  {
    return containsTextSearch() ? "contains" : "freetext";
  }

  /**
   * text search clause for given table, search text
   */
  protected String textSearchClause(String table) {
    StringBuffer buf=new StringBuffer();
    buf.append(textSearchOperator()).append("(").append(table).append(".*, ").append(querySearchText()).append(")");
    return buf.toString();
  }

  /**
   * text search clause for given table, field, search text
   */
  protected String textSearchClause(String table, String field) {
    StringBuffer buf=new StringBuffer();
    buf.append(textSearchOperator()).append("(").append(table).append(".").append(field).append(", ").append(querySearchText()).append(")");
    return buf.toString();
  }

  /**
   * search year...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public int searchLowerYear() {
    return getInt(SearchLowerYear_, 0);
  }

  public int searchUpperYear() {
    return getInt(SearchUpperYear_, 0);
  }

  /**
   * item type...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public String itemType() {
    return getString(ItemType_, "");
  }

  /**
   * show thumbnails...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public boolean noThumbnails() {
    return getString(NoThumbnails_, "off").compareTo("on")==0;
  }

  /**
   * key word...since value is set in constructor, this will never return
   * the default value passed with get(String|Int|Boolean) and will never
   * throw an exception
   */
  public int keyWord() {
    return getInt(KeyWord_, 0);
  }

  /**
   * save to session
   */
  public synchronized void saveToSession(HttpSession session) {
    JspSession.save(session, SessionAttribute_, this);
  }

  /**
   * main select statement
   */
  public String select()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("select Main.ItemID from Main");
    return buf.toString();
  }

  /**
   * formulates search query from parameters in this object...this uses full-text
   * indexing features of MSSQL...note that the join must be done in the query because
   * views can't be full-text indexed..
   */
  public String query()
  {
    StringBuffer buf=new StringBuffer();
    buf.append("SET CONCAT_NULL_YIELDS_NULL OFF ");
    buf.append(select());
    return buf.toString();
  }

  /**
   * union query
   */
  public String unionQuery() {
    StringBuffer buf=new StringBuffer();
    buf.append(" union ");
    buf.append(select());
    return buf.toString();
  }

  /**
   * searchable constraint
   */
  public String searchableConstraint() {
    StringBuffer buf=new StringBuffer();
    buf.append(" WHERE Main.Searchable=1 ");
    return buf.toString();
  }

  /**
   * query constraints for basic date search
   */
  public String searchBasicDate() {
    int lower_year = (searchLowerYear()!=0) ? searchLowerYear() : 0;
    int upper_year = (searchUpperYear()!=0) ? searchUpperYear() : 0;
    if(lower_year==0)
      return "";
    String circaOp[]={"+","-"};
    StringBuffer buf = new StringBuffer();
    if(lower_year<0) {
      circaOp[0]="-";
      circaOp[1]="+";
    }
    if(upper_year==0) {
      buf.append(" and (");
      buf.append("  (");
      buf.append("    (CreationDateYear is not null)");
      buf.append("    and (CreationDateYear =").append(lower_year).append(")");
      buf.append("  )");
      buf.append("  or");
      buf.append("  (");
      buf.append("    (LowDateRangeYear is not null)");
      buf.append("    and (HighDateRangeYear is not null)");
      buf.append("    and (").append(lower_year).append(" BETWEEN LowDateRangeYear AND HighDateRangeYear)");
      buf.append("  )");
      buf.append("  or");
      buf.append("  (");
      buf.append("    (CircaYear is not null)");
      buf.append("    and (").append(lower_year).append(" BETWEEN (CircaYear").append(circaOp[1]).append(circa_);
      buf.append(") AND (CircaYear").append(circaOp[0]).append(circa_).append("))");
      buf.append("  )");
      buf.append(")");
    }
    else
    {
      buf.append(" and (");
      buf.append("  (");
      buf.append("    (CreationDateYear is not null)");
      buf.append("    and (CreationDateYear BETWEEN ").append(lower_year).append(" AND ").append(upper_year).append(")");
      buf.append("  )");
      buf.append("  or");
      buf.append("  (");
      buf.append("    (LowDateRangeYear is not null)");
      buf.append("    and (HighDateRangeYear is not null)");
      buf.append("    and (");
      // A=upper_year; B=lower_year; C=highDateRangeYear; D=lowDateRangeYear;
      int A = upper_year;
      int B = lower_year;
      String C = "highDateRangeYear";
      String D = "lowDateRangeYear";
      // Accepting Cases:
      // A > C > D > B
      // C > A > B > D
      // A > C > B > D
      // C > A > D > B
      buf.append("(").append(A).append(">=").append(C).append(") and (").append(C).append(">=").append(D).append(") and (").append(D).append(">=").append(B).append(")");
      buf.append(" or ");
      buf.append("(").append(C).append(">=").append(A).append(") and (").append(A).append(">=").append(B).append(") and (").append(B).append(">=").append(D).append(")");
      buf.append(" or ");
      buf.append("(").append(A).append(">=").append(C).append(") and (").append(C).append(">=").append(B).append(") and (").append(B).append(">=").append(D).append(")");
      buf.append(" or ");
      buf.append("(").append(C).append(">=").append(A).append(") and (").append(A).append(">=").append(D).append(") and (").append(D).append(">=").append(B).append(")");
      buf.append("        )");
      buf.append("  )");
      buf.append("  or");
      buf.append("  (");
      buf.append("    (CircaYear is not null)");
      buf.append("    and (");
      // same accepting cases as above
      C="(CircaYear"+circaOp[0];
      D="(CircaYear"+circaOp[1];
      buf.append("(").append(A).append(">=").append(C+circa_).append(") and ").append(C+circa_).append(")>=").append(D+circa_).append(") and ").append(D+circa_).append(")>=").append(B).append(")");
      buf.append(" or ");
      buf.append("(").append(C+circa_).append(")>=").append(A).append(" and ").append(A).append(">=").append(B).append(" and ").append(B).append(">=").append(D+circa_).append("))");
      buf.append(" or ");
      buf.append("(").append(A).append(">=").append(C+circa_).append(") and ").append(C+circa_).append(")>=").append(B).append(" and ").append(B).append(">=").append(D+circa_).append("))");
      buf.append(" or ");
      buf.append("(").append(C+circa_).append(")>=").append(A).append(" and ").append(A).append(">=").append(D+circa_).append(") and ").append(D+circa_).append(")>=").append(B).append(")");
      buf.append("  ))");
      buf.append(")");
    }
    return buf.toString();
  }

  /**
   * template for text search query that searches full-text indexed table in conjunction
   * with main table..returns query of this form:
   *
   *  select Main.ItemID from Main, <table> where Main.Searchable=1
   *  and <joinOn>
   *  and [contains|freetext](<table>.*, '<searchText>')
   *
   * for query only involving main and webinfo, set joinOn=null
   *
   * the 'Searchable' field in the Main table is assumed to have been initialized at
   * startup time by the PvmaServletInit servlet.
   */
  protected String textSearchQuery(String table, String joinOn) {
    StringBuffer buf=new StringBuffer();
    buf.append("select distinct Main.ItemID, ");
    buf.append(MainSearchPage.tableRanks_.getTableValue(table)).append(" AS value, '");
    buf.append(table).append("' AS TableName from Main");
    
    if (joinOn != null) {
      buf.append(", ").append(table);
    }
    buf.append(searchableConstraint());
    
    if (joinOn != null) {
      buf.append(" and ").append(joinOn);
    }
    
    if (searchText().length() != 0) {
      buf.append(" and ").append(textSearchClause(table));
    }
    buf.append(searchBasicDate());
    return buf.toString();
  }

  /**
   * template for text search query that searches full-text indexed table in conjunction
   * with main table, and table that links with main..returns query of this form:
   *
   * select Main.ItemID from Main, <table>, <linktable> where Main.Searchable=1
   * and Main.ItemID=<linktable>.ItemID
   * and <joinOn>
   * and contains(<table>.*, '<searchText>')
   *
   * the 'Searchable' field in the Main table is assumed to have been initialized at
   * startup time by the PvmaServletInit servlet
   */
  protected String textSearchQuery(String table, String linkTable, String joinOn) {
    StringBuffer buf=new StringBuffer();
    buf.append("select distinct Main.ItemID, ");
    buf.append(MainSearchPage.tableRanks_.getTableValue(table)).append(" AS value, '");
    buf.append(table).append("' AS TableName from Main, ");
    buf.append(table).append(", ");
    buf.append(linkTable);
    buf.append(searchableConstraint());
    buf.append(" and Main.ItemID=").append(linkTable).append(".ItemID and ");
    buf.append(joinOn);
    if(searchText().length()>0)
      buf.append(" and ").append(textSearchClause(table));
    buf.append(searchBasicDate());
    return buf.toString();
  }

  protected String fieldSearchQuery(String field) {
    StringBuffer buf=new StringBuffer();
    buf.append("select distinct Main.ItemID ");
    buf.append(", ").append(MainSearchPage.tableRanks_.getTableValue(field)).append(" AS value ");
    buf.append(", '").append(field).append("' AS TableName ");
    buf.append(" from Main");
    buf.append(searchableConstraint());
    if (searchText().length() != 0) {
      buf.append(" and ").append(textSearchClause("Main", field));
    }
    buf.append(searchBasicDate());
    return buf.toString();
  }

  /**
   * returns a vector of searches
   */
  public Vector textSearchQueryVector() {
    Vector searches = new Vector();
    if(searchText().length()>0 || (searchUpperYear() != 0 && searchLowerYear() != 0) || searchLowerYear() != 0) {
      searches.add(textSearchQuery("Main", null));
      searches.add(fieldSearchQuery("itemname"));
      searches.add(textSearchQuery("WebInfo",           "Main.ItemID=WebInfo.ItemID"));
      searches.add(textSearchQuery("TranscriptionPage", "Main.ItemID=TranscriptionPage.ItemID"));
      
      /* 20031208 KRU 
       *
       * Disabled search of CollectionPeople and CollectionPlaces, which were returning unexpected and obscure
       * results.  For example, a search for exact phrase "town meeting" returned a reticule from Shelburne Falls,
       * simply because the CollectionPlaces record for Shelburne Falls contains the phrase "town meeting".
      searches.add(textSearchQuery("CollectionPlaces",  "Main.PlaceOfOriginID=CollectionPlaces.CollectionPlaceID"));
      searches.add(textSearchQuery("CollectionPeople",  "ObjectCreatorLink", "ObjectCreatorLink.CollectionPersonID=CollectionPeople.CollectionPersonID"));
       */
       
      searches.add(textSearchQuery("ProcessMaterials",  "Main.ProcessMaterialID=ProcessMaterials.ProcessMaterialID"));
      searches.add(textSearchQuery("Keywords",          "KeywordLink", "KeywordLink.KeywordID=Keywords.KeywordID"));
      searches.add(textSearchQuery("Topics",            "TopicLink", "TopicLink.TopicID=Topics.TopicID"));
      searches.add(textSearchQuery("Nomenclature",      "Main.NomenclatureID=Nomenclature.NomenclatureID"));
    }
    return searches;
  }

  /**
   * formulates text search query
   */
  public String textSearchQuery()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SET CONCAT_NULL_YIELDS_NULL OFF ");
    buf.append(textSearchQuery("Main", null));
    if (searchText().length() > 0)
    {
      buf.append(" union ");
      buf.append(fieldSearchQuery("itemname"));
      buf.append(" union ");
      buf.append(textSearchQuery("WebInfo", "Main.ItemID=WebInfo.ItemID"));
      buf.append(" union ");
      buf.append(textSearchQuery("TranscriptionPage",
                                 "Main.ItemID=TranscriptionPage.ItemID"));
      /* 20031208 KRU 
       *
       * Disabled search of CollectionPeople and CollectionPlaces, which were returning unexpected and obscure
       * results.  For example, a search for exact phrase "town meeting" returned a reticule from Shelburne Falls,
       * simply because the CollectionPlaces record for Shelburne Falls contains the phrase "town meeting".
       
      buf.append(" union ");
      buf.append(textSearchQuery("CollectionPlaces",
                                 "Main.PlaceOfOriginID=CollectionPlaces.CollectionPlaceID"));
      buf.append(" union ");
      buf.append(textSearchQuery("CollectionPeople", "ObjectCreatorLink",
                                 "ObjectCreatorLink.CollectionPersonID=CollectionPeople.CollectionPersonID"));
       */
      buf.append(" union ");
      buf.append(textSearchQuery("ProcessMaterials",
                                 "Main.ProcessMaterialID=ProcessMaterials.ProcessMaterialID"));
      buf.append(" union ");
      buf.append(textSearchQuery("Keywords", "KeywordLink",
                                 "KeywordLink.KeywordID=Keywords.KeywordID"));
      buf.append(" union ");
      buf.append(textSearchQuery("Topics", "TopicLink",
                                 "TopicLink.TopicID=Topics.TopicID"));
      buf.append(" union ");
      buf.append(textSearchQuery("Nomenclature",
                                 "Main.NomenclatureID=Nomenclature.NomenclatureID"));
    }
    else
    {
      buf.append(searchBasicDate());
    }
    return buf.toString();
  }

  /**
   * formatted search summary output
   *     SearchText_
   *     SearchLowerYear_
   *     SearchUpperYear_
   */
  public String searchSummary()
  {
    StringBuffer buf = new StringBuffer();
    if(searchText()!="")
      buf.append(searchText());
    if(searchLowerYear()!=0)
    {
      if(buf.length()!=0)
        buf.append(", ");
      if(searchUpperYear()!=0)
        buf.append("from ").append(searchLowerYear()).append(" to ").append(searchUpperYear());
      else
        buf.append("around ").append(searchLowerYear());
    }
    return buf.toString();
  }

  /**
   * load from session
   */
  public synchronized static SearchParameters loadFromSession(HttpSession session)
  {
    SearchParameters param=(SearchParameters) JspSession.load(session, SessionAttribute_);
    // if not found in session, return a new (empty) instance
    if (param==null)
      param=new SearchParameters();
    return param;
  }

  /* -------------------------------------------  Unused -------------------------------------------- */
  
  /**
   * template for text search query that uses simple 'like' clause on table that links with main..
   * returns query of this form:
   *
   * select Main.ItemID from Main, <table>, <linktable> where Main.Searchable=1
   * and Main.ItemID=<linktable>.ItemID
   * and <joinOn>
   * and <table>.<fields[0]> like '%<searchText>%' [and <table>.<fields[1]> like '%<searchText>%'....]
   *
   * the 'Searchable' field in the Main table is assumed to have been initialized at
   * startup time by the PvmaServletInit servlet
   */
  protected String simpleSearchQuery(String table, String linkTable, String joinOn, String [] fields)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("select Main.ItemID from Main, ");
    buf.append(table).append(", ");
    buf.append(linkTable);
    buf.append(searchableConstraint());
    buf.append(" and Main.ItemID=").append(linkTable).append(".ItemID");
    buf.append(" and ").append(joinOn);
    buf.append(likeClause(table, fields));
    return buf.toString();
  }

  /**
   * template for text search query that uses simple 'like' statement for text matching
   *
   *  select Main.ItemID from Main, <table> where Main.Searchable=1
   *  and <joinOn>
   *  and <table>.<fields[0]> like '%<searchText>%' [and <table>.<fields[1]> like '%<searchText>%']
   *
   * the 'Searchable' field in the Main table is assumed to have been initialized at
   * startup time by the PvmaServletInit servlet.
   */
  protected String simpleSearchQuery(String table, String joinOn, String[] fields)
  {
    StringBuffer buf=new StringBuffer();
    buf.append("select Main.ItemID from Main");
    if (joinOn != null)
    {
      buf.append(", ").append(table);
    }
    buf.append(searchableConstraint());
    if (joinOn != null)
    {
      buf.append(" and ").append(joinOn);
    }
    buf.append(likeClause(table, fields));
    return buf.toString();
  }
  
  /**
   * formats like clause given search text, table name, field names
   */
  protected String likeClause(String table, String [] fields)
  {
    StringBuffer buf=new StringBuffer();
    for (int i=0; i < fields.length; i++)
    {
      buf.append(" and ");
      buf.append(table).append(".").append(fields[i]);
      buf.append(" like '%").append(searchText()).append("%'");
    }
    return buf.toString();
  }

  /**
   * quotes strings as needed
   */
  protected String quoter(String text)
  {
    if(text.startsWith("\"") || textEvaluator(text)!=-1)
      return text;
    return "\""+text+"\"";
  }

  /**
   * text evaluator
   * returns 0 if empty, -1 if multiword containing spaces, 1 if it is a single word
   */
  private int textEvaluator(String text)
  {
    StringTokenizer toks = new StringTokenizer(searchText(), " ");
    if(text.length()>0 && toks.countTokens()==1)
      return 1;
    else if(text.length()>0)
      return -1;
    return 0;
  }
}
