
/**
 * Title:        ExhibitQuery<p>
 * Description:  base class for a query db for the matrix page<p>
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author tarmstro
 * @version 1.0
 */
package edu.umass.ccbit.database;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.lang.StringBuffer;
import java.util.Vector;

public class ExhibitMatrixQuery
{
  public Vector eras_;
  public static final String exhibitView_ = "Web_ExhibitMatrixView";
  public int xMax_;
  public int yMax_;
  public String exhibitName_;
  //public String xThemeName_[] = new String[50];
  //public String yThemeName_[] = new String[50];
  //public String xThemeURL_[] = new String[50];
  //public String yThemeURL_[] = new String[50];

  /*
  * strings used in the query
  */
  public interface fields
  {
    static final String xPos_="XPos";
    static final String yPos_="YPos";
    static final String xMax_="XMax";
    static final String yMax_="YMax";
    static final String xThemeID_="XThemeID";
    static final String yThemeID_="YThemeID";
    static final String xThemeURL_="XThemeURL";
    static final String yThemeURL_="YThemeURL";
    static final String xThemeName_="XThemeName";
    static final String yThemeName_="YThemeName";
    //static final String client_="Client";
    //static final String fileName_="Filename";
    static final String imgPath_="ImagePath";
    static final String exhibitID_="ExhibitID";
    static final String exhibitName_="ExhibitName";
  }

  public CollectionMatrixItem item(int xIndex, int yIndex)
  {
    // access some ExhibitObject based on its index in the data structure
    return (CollectionMatrixItem) ((Vector) eras_.elementAt(xIndex)).elementAt(yIndex);
  }

  /**
   * construct a query
   * use the parameter to determine the exhibit that we are in
   * @param exhibitID the exhibit id
   */
  public String query(int exhibitID)
  {
    // currently using a string defined from above
    // break up the parts: SELECT, FROM, SHOW BY, etc...
    // we use all the fields in the view, so when
    // doing the query, SELECT * FROM exhibitView_
    StringBuffer buf = new StringBuffer();
    buf.append("SELECT * FROM ").append(exhibitView_);
    buf.append(" WHERE ").append(fields.exhibitID_).append("=").append(exhibitID);
    buf.append(" ORDER BY ").append(fields.xPos_).append(", ").append(fields.yPos_);
    return buf.toString();
  }

  /**
   * run the query, store in vector of vectors
   * the vector containing vectors is considered the 'era' vector
   * or the topics on the x-axis.  The internal vectors are the 'themes'
   * vectors, or the topics on the y-axis.
   * @param conn the database connection
   * @param exhibitID the id of the exhibit
   */
  public void execute(Connection conn, int exhibitID) throws SQLException
  {
    eras_ = new Vector();
    Statement st = conn.createStatement();
    ResultSet result = st.executeQuery(query(exhibitID));
    result.next();
    base_init(result);
    /*
    // works but is error prone if no info exists...
    for(int i=0; i<xMax_; i++)
    {
      eras_.insertElementAt(new Vector(), i);
      for(int j=0; j<yMax_; j++)
      {
        // get to the specific era vector and then insert a
        // CollectionMatrixItem at its proper y position
        ((Vector) eras_.elementAt(i)).insertElementAt(CollectionMatrixItem.new_ExhibitQuery(result), j);
        // initially this we before the previous statement in an if expression
        // however, this did not work logically
        // this will advance the cursor which points to the current row one more
        // than there are rows, but this doesn't cause a problem(but it might?)
        result.next();
      }
    }
    */
    for(int i=0; i<xMax_; i++)
    {
      eras_.insertElementAt(new Vector(), i);
      for(int j=0; j<yMax_; j++)
      {
        ((Vector) eras_.elementAt(i)).insertElementAt(new CollectionMatrixItem(), j);
      }
    }
    CollectionMatrixItem tempItem = CollectionMatrixItem.new_ExhibitQuery(result);
    int tempX = tempItem.xPos_;
    int tempY = tempItem.yPos_;
    ((Vector) eras_.elementAt(tempX-1)).insertElementAt(CollectionMatrixItem.new_ExhibitQuery(result), tempY-1);
    while(result.next())
    {
      tempItem = CollectionMatrixItem.new_ExhibitQuery(result);
      tempX = tempItem.xPos_;
      tempY = tempItem.yPos_;
      ((Vector) eras_.elementAt(tempX-1)).insertElementAt(CollectionMatrixItem.new_ExhibitQuery(result), tempY-1);
    }
    result.close();
    st.close();
  }

  /**
   * initialize non grid item variables
   * @param result the result set generated from the query
   */
  protected void base_init(ResultSet result)
   throws SQLException
  {
    xMax_=result.getInt(fields.xMax_);
    yMax_=result.getInt(fields.yMax_);
    exhibitName_=result.getString(fields.exhibitName_);
  }
}
