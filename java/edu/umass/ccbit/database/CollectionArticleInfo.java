package edu.umass.ccbit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectionArticleInfo extends DbLoadableByIDArticle {
  protected int    articleID_;
  protected int    year_;
  protected String description_;
  protected String person_;
  protected String corporation_;
  protected String primarySubject_;
  protected String secondarySubject_;
  protected String tertiarySubject_;
  protected String place_;
  protected String publication_;
  
  public static final String view_ = "NewsIndex";
        
  /**
   * view
   */
  protected String view() {
    return view_;
  }

  public int    articleID()        {return (articleID_);}
  public String description()      {return (description_);}
  public String primarySubject()   {return (primarySubject_);}
  public String secondarySubject() {return (secondarySubject_);}
  public String tertiarySubject()  {return (tertiarySubject_);}
  public String publication()      {return (publication_);}

  public interface fields {
  	public static final String ArticleID_        = "ArticleID";
  	public static final String Year_             = "Dates";
  	public static final String Description_      = "500";
  	public static final String Person_           = "600";
  	public static final String Corporation_      = "610";
  	public static final String PrimarySubject_   = "650a";
  	public static final String SecondarySubject_ = "650b";
  	public static final String TertiarySubject_  = "650c";
  	public static final String Place_            = "651";
  	public static final String Publication_      = "730";
  }

  /**
   * init
   */
  public void init( ResultSet result ) throws SQLException {
    articleID_        = result.getInt(    fields.ArticleID_ );
    year_             = result.getInt(    fields.Year_);
    description_      = result.getString( fields.Description_ );
    person_           = result.getString( fields.Person_ );
    corporation_      = result.getString( fields.Corporation_ );
    primarySubject_   = result.getString( fields.PrimarySubject_ );
    secondarySubject_ = result.getString( fields.SecondarySubject_ );
    tertiarySubject_  = result.getString( fields.TertiarySubject_ );
    place_            = result.getString( fields.Place_ );
    publication_      = result.getString( fields.Publication_ );
  }
}
