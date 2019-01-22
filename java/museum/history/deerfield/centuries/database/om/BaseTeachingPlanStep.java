package museum.history.deerfield.centuries.database.om;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.torque.TorqueException;
import org.apache.torque.om.BaseObject;
import org.apache.torque.om.ComboKey;
import org.apache.torque.om.DateKey;
import org.apache.torque.om.NumberKey;
import org.apache.torque.om.ObjectKey;
import org.apache.torque.om.SimpleKey;
import org.apache.torque.om.StringKey;
import org.apache.torque.om.Persistent;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Transaction;

  
  
/**
 * This class was autogenerated by Torque on:
 *
 * [Thu Sep 09 17:11:01 EDT 2010]
 *
 * You should not use this class directly.  It should not even be
 * extended all references should be to TeachingPlanStep
 */
public abstract class BaseTeachingPlanStep extends BaseObject
{
    /** The Peer class */
    private static final TeachingPlanStepPeer peer =
        new TeachingPlanStepPeer();

        
    /** The value for the activityID field */
    private int activityID;
      
    /** The value for the stepNum field */
    private int stepNum;
      
    /** The value for the description field */
    private String description;
  
    
    /**
     * Get the ActivityID
     *
     * @return int
     */
    public int getActivityID()
    {
        return activityID;
    }

                              
    /**
     * Set the value of ActivityID
     *
     * @param v new value
     */
    public void setActivityID(int v) throws TorqueException
    {
    
                  if (this.activityID != v)
              {
            this.activityID = v;
            setModified(true);
        }
    
                          
                if (aActivity != null && !(aActivity.getActivityID() == v))
                {
            aActivity = null;
        }
      
              }
  
    /**
     * Get the StepNum
     *
     * @return int
     */
    public int getStepNum()
    {
        return stepNum;
    }

                        
    /**
     * Set the value of StepNum
     *
     * @param v new value
     */
    public void setStepNum(int v) 
    {
    
                  if (this.stepNum != v)
              {
            this.stepNum = v;
            setModified(true);
        }
    
          
              }
  
    /**
     * Get the Description
     *
     * @return String
     */
    public String getDescription()
    {
        return description;
    }

                        
    /**
     * Set the value of Description
     *
     * @param v new value
     */
    public void setDescription(String v) 
    {
    
                  if (!ObjectUtils.equals(this.description, v))
              {
            this.description = v;
            setModified(true);
        }
    
          
              }
  
      
    
                  
    
        private Activity aActivity;

    /**
     * Declares an association between this object and a Activity object
     *
     * @param v Activity
     * @throws TorqueException
     */
    public void setActivity(Activity v) throws TorqueException
    {
            if (v == null)
        {
                    setActivityID(0);
                  }
        else
        {
            setActivityID(v.getActivityID());
        }
                aActivity = v;
    }

                                            
    /**
     * Get the associated Activity object
     *
     * @return the associated Activity object
     * @throws TorqueException
     */
    public Activity getActivity() throws TorqueException
    {
        if (aActivity == null && (this.activityID > 0))
        {
                          aActivity = ActivityPeer.retrieveByPK(SimpleKey.keyFor(this.activityID));
              
            /* The following can be used instead of the line above to
               guarantee the related object contains a reference
               to this object, but this level of coupling
               may be undesirable in many circumstances.
               As it can lead to a db query with many results that may
               never be used.
               Activity obj = ActivityPeer.retrieveByPK(this.activityID);
               obj.addTeachingPlanSteps(this);
            */
        }
        return aActivity;
    }

    /**
     * Provides convenient way to set a relationship based on a
     * ObjectKey.  e.g.
     * <code>bar.setFooKey(foo.getPrimaryKey())</code>
     *
           */
    public void setActivityKey(ObjectKey key) throws TorqueException
    {
      
                        setActivityID(((NumberKey) key).intValue());
                  }
       
                
    private static List fieldNames = null;

    /**
     * Generate a list of field names.
     *
     * @return a list of field names
     */
    public static synchronized List getFieldNames()
    {
        if (fieldNames == null)
        {
            fieldNames = new ArrayList();
              fieldNames.add("ActivityID");
              fieldNames.add("StepNum");
              fieldNames.add("Description");
              fieldNames = Collections.unmodifiableList(fieldNames);
        }
        return fieldNames;
    }

    /**
     * Retrieves a field from the object by name passed in as a String.
     *
     * @param name field name
     * @return value
     */
    public Object getByName(String name)
    {
          if (name.equals("ActivityID"))
        {
                return new Integer(getActivityID());
            }
          if (name.equals("StepNum"))
        {
                return new Integer(getStepNum());
            }
          if (name.equals("Description"))
        {
                return getDescription();
            }
          return null;
    }
    
    /**
     * Retrieves a field from the object by name passed in
     * as a String.  The String must be one of the static
     * Strings defined in this Class' Peer.
     *
     * @param name peer name
     * @return value
     */
    public Object getByPeerName(String name)
    {
          if (name.equals(TeachingPlanStepPeer.ACTIVITYID))
        {
                return new Integer(getActivityID());
            }
          if (name.equals(TeachingPlanStepPeer.STEPNUM))
        {
                return new Integer(getStepNum());
            }
          if (name.equals(TeachingPlanStepPeer.DESCRIPTION))
        {
                return getDescription();
            }
          return null;
    }

    /**
     * Retrieves a field from the object by Position as specified
     * in the xml schema.  Zero-based.
     *
     * @param pos position in xml schema
     * @return value
     */
    public Object getByPosition(int pos)
    {
            if (pos == 0)
        {
                return new Integer(getActivityID());
            }
              if (pos == 1)
        {
                return new Integer(getStepNum());
            }
              if (pos == 2)
        {
                return getDescription();
            }
              return null;
    }
     
    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
     *
     * @throws Exception
     */
    public void save() throws Exception
    {
          save(TeachingPlanStepPeer.getMapBuilder()
                .getDatabaseMap().getName());
      }

    /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.
       * Note: this code is here because the method body is
     * auto-generated conditionally and therefore needs to be
     * in this file instead of in the super class, BaseObject.
       *
     * @param dbName
     * @throws TorqueException
     */
    public void save(String dbName) throws TorqueException
    {
        Connection con = null;
          try
        {
            con = Transaction.begin(dbName);
            save(con);
            Transaction.commit(con);
        }
        catch(TorqueException e)
        {
            Transaction.safeRollback(con);
            throw e;
        }
      }

      /** flag to prevent endless save loop, if this object is referenced
        by another object which falls in this transaction. */
    private boolean alreadyInSave = false;
      /**
     * Stores the object in the database.  If the object is new,
     * it inserts it; otherwise an update is performed.  This method
     * is meant to be used as part of a transaction, otherwise use
     * the save() method and the connection details will be handled
     * internally
     *
     * @param con
     * @throws TorqueException
     */
    public void save(Connection con) throws TorqueException
    {
          if (!alreadyInSave)
        {
            alreadyInSave = true;


  
            // If this object has been modified, then save it to the database.
            if (isModified())
            {
                if (isNew())
                {
                    TeachingPlanStepPeer.doInsert((TeachingPlanStep) this, con);
                    setNew(false);
                }
                else
                {
                    TeachingPlanStepPeer.doUpdate((TeachingPlanStep) this, con);
                }
            }

                      alreadyInSave = false;
        }
      }


    
  
    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
          return null;
      }

 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
       * It then fills all the association collections and sets the
     * related objects to isNew=true.
       */
      public TeachingPlanStep copy() throws TorqueException
    {
        return copyInto(new TeachingPlanStep());
    }
  
    protected TeachingPlanStep copyInto(TeachingPlanStep copyObj) throws TorqueException
    {
          copyObj.setActivityID(activityID);
          copyObj.setStepNum(stepNum);
          copyObj.setDescription(description);
  
                    
        
        return copyObj;
    }

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public TeachingPlanStepPeer getPeer()
    {
        return peer;
    }

    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("TeachingPlanStep:\n");
        str.append("ActivityID = ")
           .append(getActivityID())
           .append("\n");
        str.append("StepNum = ")
           .append(getStepNum())
           .append("\n");
        str.append("Description = ")
           .append(getDescription())
           .append("\n");
        return(str.toString());
    }
}