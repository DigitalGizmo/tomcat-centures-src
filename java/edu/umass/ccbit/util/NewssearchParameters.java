package edu.umass.ccbit.util;

import edu.umass.ccbit.jsp.MainNewssearchPage;
import edu.umass.ckc.util.Parameters;
import edu.umass.ckc.util.StringUtils;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

public class NewssearchParameters extends Parameters
{
  // session attribute
  public static final String SessionAttribute_="newssearch_parameters";
  // request parameter names
  public static final String SearchText_="searchtext";
  public static final String ContainsTextSearch_="contains";

  //odd quoting
  public static final String quote_= (new String("\"")).substring(1,1);

  public NewssearchParameters()
  {
    put(SearchText_, "");
    put(ContainsTextSearch_, "off");
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
    buf.append("select NewsIndex.ArticleID from NewsIndex");
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
   * template for text search query that searches full-text indexed table in conjunction
   * with main table..returns query of this form:
   *
   *  select NewsIndex.ArticleID from Main, <table> where 1=1
   *  and <joinOn>
   *  and [contains|freetext](<table>.*, '<searchText>')
   *
   * for query only involving NewsIndex, set joinOn=null
   */
  protected String textSearchQuery(String table, String joinOn) {
    StringBuffer buf=new StringBuffer();
    buf.append("select distinct NewsIndex.ArticleID, ");
    buf.append( 1 ).append(" AS value, '");
    buf.append(table).append("' AS TableName from NewsIndex");
    
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
    return buf.toString();
  }

  /**
   * template for text search query that searches full-text indexed table in conjunction
   * with NewsIndex table, and table that links with NewsIndex..returns query of this form:
   *
   * select NewsIndex.ArticleID from NewsIndex, <table>, <linktable> where 1=1
   * and NewsIndex.ArticleID=<linktable>.ArticleID
   * and <joinOn>
   * and contains(<table>.*, '<searchText>')
   */
  protected String textSearchQuery(String table, String linkTable, String joinOn) {
    StringBuffer buf=new StringBuffer();
    buf.append("select distinct NewsIndex.ArticleID, ");
    buf.append( 1 ).append(" AS value, '");
    buf.append(table).append("' AS TableName from NewsIndex, ");
    buf.append(table).append(", ");
    buf.append(linkTable);
    buf.append(searchableConstraint());
    buf.append(" and NewsIndex.ArticleID=").append(linkTable).append(".ArticleID and ");
    buf.append(joinOn);
    if(searchText().length()>0)
      buf.append(" and ").append(textSearchClause(table));
    return buf.toString();
  }

  /**
   * returns a vector of searches
   */
  public Vector textSearchQueryVector() {
    Vector searches = new Vector();
    if(searchText().length()>0) {
      searches.add(textSearchQuery("NewsIndex", null));
      searches.add(textSearchQuery("Newstopics",            "NewstopicLink", "NewstopicLink.TopicID=Newstopics.TopicID"));
    }
    return searches;
  }

  /**
   * searchable constraint
   */
  public String searchableConstraint() {
    StringBuffer buf=new StringBuffer();
    buf.append(" WHERE 1=1 ");
    return buf.toString();
  }

  /**
   * formatted search summary output
   */
  public String searchSummary()
  {
    StringBuffer buf = new StringBuffer();
    if(searchText()!="")
      buf.append(searchText());
    return buf.toString();
  }

  /**
   * load from session
   */
  public synchronized static NewssearchParameters loadFromSession(HttpSession session)
  {
    NewssearchParameters param=(NewssearchParameters) JspSession.load(session, SessionAttribute_);
    // if not found in session, return a new (empty) instance
    if (param==null)
      param=new NewssearchParameters();
    return param;
  }
}
