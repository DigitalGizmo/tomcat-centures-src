package edu.umass.ccbit.database;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;
import edu.umass.ckc.util.StringUtils;

public class Lexicon extends DbLoadable
{
  private TreeMap lexicon_;
  private Vector wordsAndPhrases_;
  private static final String key_   = "keyname";
  private static final String value_ = "synonym";
  private static final String delim_ = " ";

  protected void initFromResult(ResultSet result) throws SQLException
  {
    while(result.next())
    {
      String key   = result.getString(key_).toLowerCase();
      String value = result.getString(value_).toLowerCase();
      Vector previous = (Vector) lexicon_.get(key);
      if(previous==null)
        previous = new Vector();
      previous.add(value);
      lexicon_.put(key, previous);
    }
  }

  public void load(Connection conn) throws SQLException
  {
    lexicon_ = new TreeMap();
    load(conn, query());
  }

  private String query()
  {
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM Lexicon");
    return buf.toString();
  }

  public String lexiconDisplay(String searchText)
  {
    searchText = searchText.toLowerCase();
    StringBuffer buf = new StringBuffer();
    StringTokenizer toks = new StringTokenizer(searchText, delim_);
    loadWordsAndPhrases(toks);
    for(int i=0; i<wordsAndPhrases_.size(); i++)
    {
      String word = (String) wordsAndPhrases_.get(i);
      String single = wordMatchLink(word);
      String phrase = phraseMatchLinks(word);
      if(!single.equalsIgnoreCase("") || !phrase.equalsIgnoreCase(""))
        buf.append("Your search for \"").append(word).append("\" can be expanded to include:<BR>");
      int num = 1;
      if(!phrase.equalsIgnoreCase(""))
      {
        buf.append(num).append(". ").append(phrase).append("<BR>");
        num++;
      }
      if(!single.equalsIgnoreCase(""))
      {
        buf.append(num).append(". ").append(single).append("<BR>");
        num++;
      }
    }
    return buf.toString();
  }

  private void loadWordsAndPhrases(StringTokenizer toks)
  {
    Vector words = new Vector();
    int maxLength=toks.countTokens();
    while(toks.hasMoreElements())
    {
      String singleton = toks.nextToken();
      words.add(singleton);
    }
    wordsAndPhrases_ = new Vector();
    int phraseLength=1;
    int numCounter=maxLength;
    while(phraseLength<=maxLength)
    {
      for(int i=0; i<numCounter; i++)
      {
        StringBuffer word = new StringBuffer();
        for(int j=0; j<phraseLength; j++)
          word.append((String)words.elementAt(i+j)).append(delim_);
        wordsAndPhrases_.add(word.toString().trim());
      }
      phraseLength++;
      numCounter--;
    }

  }

  private String phraseMatchLinks(String key)
  {
    StringBuffer buf = new StringBuffer();
    Vector synonyms = (Vector)lexicon_.get(key);
    if(synonyms==null)
      return "";
    Enumeration e = synonyms.elements();
    while(e.hasMoreElements())
    {
      String syn = (String) e.nextElement();
      StringTokenizer toks = new StringTokenizer(syn, delim_);
      if(toks.countTokens()>1)
        buf.append(phraseLink(syn, syn+" or "+key));
    }
    String phrases = buf.toString();
    if(phrases.endsWith(", "))
      phrases = phrases.substring(0, phrases.length()-2);
    return phrases;
  }

  private String phraseLink(String phrase, String search)
  {
    if(phrase.equalsIgnoreCase(""))
      return "";
    return "<a href=\"./results.jsp?lexicon=1&contains=on&searchtext="+StringUtils.formatURLText(search)+"\">"+phrase+"</a>, ";
  }

  private String wordMatchLink(String key)
  {
    StringBuffer buf = new StringBuffer();
    Vector synonyms = (Vector)lexicon_.get(key);
    if(synonyms==null)
      return "";
    Enumeration e = synonyms.elements();
    StringBuffer listOfWords = new StringBuffer();
    StringBuffer listOfCommaWords = new StringBuffer();
    while(e.hasMoreElements())
    {
      String syn = (String) e.nextElement();
      StringTokenizer toks = new StringTokenizer(syn, delim_);
      if(toks.countTokens()==1)
      {
        listOfWords.append(syn);
        listOfCommaWords.append(syn);
        if(e.hasMoreElements())
        {
          listOfWords.append(" ");
          listOfCommaWords.append(", ");
        }
      }
    }
    if(listOfWords.length()>0)
      listOfWords.append(" ").append(key);
    return wordsLink(listOfWords.toString(), listOfCommaWords.toString());
  }

  private String wordsLink(String words, String commas)
  {
    if(words.equalsIgnoreCase(""))
      return "";
    if(words.endsWith(" "))
      words = words.substring(0, words.length()-1);
    if(commas.endsWith(", "))
      commas = commas.substring(0, commas.length()-2);
    return "<a href=\"./results.jsp?lexicon=1&contains=off&searchtext="+StringUtils.formatURLText(words)+"\">"+commas+"</a>";
  }
}
