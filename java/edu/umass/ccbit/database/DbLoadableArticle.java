package edu.umass.ccbit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbLoadableArticle extends DbLoadable implements InstanceFromResult {
  /**
   * initialize data in this object based on contents of result set
   */
  protected final void initFromResult(ResultSet result) throws SQLException {
    if (result.next()) init(result);
  }
}

  
