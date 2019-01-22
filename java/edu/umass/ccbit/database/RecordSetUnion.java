package edu.umass.ccbit.database;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2002<p>
 * Company:    University of Massachusetts/Center for Computer-based Instructional Technology<p>
 * @author
 * @version 1.0
 */

import edu.umass.ckc.database.ColumnMetaData;
import edu.umass.ckc.database.RecordSet;
import edu.umass.ckc.database.RecordSetMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;


public class RecordSetUnion extends RecordSet
{
    public RecordSetUnion()
    {
      records_ = new Vector();
    }

    /**
     * load the record set
     */
    public void add(ResultSet results) throws SQLException
    {
        // load the meta data
        metaData_ = new RecordSetMetaData();
        metaData_.set(results);
        // load the results
        if(records_ == null)
          records_ = new Vector();

        int numFields = metaData_.getNumColumns();
        while(results.next())
        {
            Vector tmp = new Vector(numFields);
            for(int i=1; i<=numFields; ++i)
            {
                String value = results.getString(i);
                if(results.wasNull())
                    value = null;
                ColumnMetaData cmeta = metaData_.getColumnMetaData(i-1);
                if(cmeta.getSqlType() == java.sql.Types.CHAR)
                {
                    value = value.trim();
                }
                tmp.addElement(value);
            }
            records_.addElement(tmp);
        }
    }
}
