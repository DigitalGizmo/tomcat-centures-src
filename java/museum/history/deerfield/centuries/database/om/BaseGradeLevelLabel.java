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
 * extended all references should be to GradeLevelLabel
 */
public abstract class BaseGradeLevelLabel extends BaseObject
{
    /** The Peer class */
    private static final GradeLevelLabelPeer peer =
        new GradeLevelLabelPeer();

        
    /** The value for the levelID field */
    private int levelID;
      
    /** The value for the label field */
    private String label;
  
    
    /**
     * Get the LevelID
     *
     * @return int
     */
    public int getLevelID()
    {
        return levelID;
    }

                                              
    /**
     * Set the value of LevelID
     *
     * @param v new value
     */
    public void setLevelID(int v) throws TorqueException
    {
    
                  if (this.levelID != v)
              {
            this.levelID = v;
            setModified(true);
        }
    
          
                                  
        // update associated GradeLevel
        if (collGradeLevels != null)
        {
            for (int i = 0; i < collGradeLevels.size(); i++)
            {
                ((GradeLevel) collGradeLevels.get(i))
                    .setLevelID(v);
            }
        }
                      }
  
    /**
     * Get the Label
     *
     * @return String
     */
    public String getLabel()
    {
        return label;
    }

                        
    /**
     * Set the value of Label
     *
     * @param v new value
     */
    public void setLabel(String v) 
    {
    
                  if (!ObjectUtils.equals(this.label, v))
              {
            this.label = v;
            setModified(true);
        }
    
          
              }
  
         
                                
            
    /**
     * Collection to store aggregation of collGradeLevels
     */
    protected List collGradeLevels;

    /**
     * Temporary storage of collGradeLevels to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initGradeLevels()
    {
        if (collGradeLevels == null)
        {
            collGradeLevels = new ArrayList();
        }
    }

    /**
     * Method called to associate a GradeLevel object to this object
     * through the GradeLevel foreign key attribute
     *
     * @param l GradeLevel
     * @throws TorqueException
     */
    public void addGradeLevel(GradeLevel l) throws TorqueException
    {
        getGradeLevels().add(l);
        l.setGradeLevelLabel((GradeLevelLabel) this);
    }

    /**
     * The criteria used to select the current contents of collGradeLevels
     */
    private Criteria lastGradeLevelsCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getGradeLevels(new Criteria())
     *
     * @throws TorqueException
     */
    public List getGradeLevels() throws TorqueException
    {
        if (collGradeLevels == null)
        {
            collGradeLevels = getGradeLevels(new Criteria(10));
        }
        return collGradeLevels;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this GradeLevelLabel has previously
     * been saved, it will retrieve related GradeLevels from storage.
     * If this GradeLevelLabel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getGradeLevels(Criteria criteria) throws TorqueException
    {
        if (collGradeLevels == null)
        {
            if (isNew())
            {
               collGradeLevels = new ArrayList();
            }
            else
            {
                      criteria.add(GradeLevelPeer.LEVELID, getLevelID() );
                      collGradeLevels = GradeLevelPeer.doSelect(criteria);
            }
        }
        else
        {
            // criteria has no effect for a new object
            if (!isNew())
            {
                // the following code is to determine if a new query is
                // called for.  If the criteria is the same as the last
                // one, just return the collection.
                      criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                      if (!lastGradeLevelsCriteria.equals(criteria))
                {
                    collGradeLevels = GradeLevelPeer.doSelect(criteria);
                }
            }
        }
        lastGradeLevelsCriteria = criteria;

        return collGradeLevels;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getGradeLevels(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getGradeLevels(Connection con) throws TorqueException
    {
        if (collGradeLevels == null)
        {
            collGradeLevels = getGradeLevels(new Criteria(10), con);
        }
        return collGradeLevels;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this GradeLevelLabel has previously
     * been saved, it will retrieve related GradeLevels from storage.
     * If this GradeLevelLabel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getGradeLevels(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collGradeLevels == null)
        {
            if (isNew())
            {
               collGradeLevels = new ArrayList();
            }
            else
            {
                       criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                       collGradeLevels = GradeLevelPeer.doSelect(criteria, con);
             }
         }
         else
         {
             // criteria has no effect for a new object
             if (!isNew())
             {
                 // the following code is to determine if a new query is
                 // called for.  If the criteria is the same as the last
                 // one, just return the collection.
                       criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                       if (!lastGradeLevelsCriteria.equals(criteria))
                 {
                     collGradeLevels = GradeLevelPeer.doSelect(criteria, con);
                 }
             }
         }
         lastGradeLevelsCriteria = criteria;

         return collGradeLevels;
     }

                        
              
                    
                    
                                
                                                              
                                        
                    
                    
          
    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this GradeLevelLabel is new, it will return
     * an empty collection; or if this GradeLevelLabel has previously
     * been saved, it will retrieve related GradeLevels from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in GradeLevelLabel.
     */
    protected List getGradeLevelsJoinActivity(Criteria criteria)
        throws TorqueException
    {
        if (collGradeLevels == null)
        {
            if (isNew())
            {
               collGradeLevels = new ArrayList();
            }
            else
            {
                            criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                            collGradeLevels = GradeLevelPeer.doSelectJoinActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            boolean newCriteria = true;
                        criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                        if (!lastGradeLevelsCriteria.equals(criteria))
            {
                collGradeLevels = GradeLevelPeer.doSelectJoinActivity(criteria);
            }
        }
        lastGradeLevelsCriteria = criteria;

        return collGradeLevels;
    }
                  
                    
                              
                                
                                                              
                                        
                    
                    
          
    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this GradeLevelLabel is new, it will return
     * an empty collection; or if this GradeLevelLabel has previously
     * been saved, it will retrieve related GradeLevels from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in GradeLevelLabel.
     */
    protected List getGradeLevelsJoinGradeLevelLabel(Criteria criteria)
        throws TorqueException
    {
        if (collGradeLevels == null)
        {
            if (isNew())
            {
               collGradeLevels = new ArrayList();
            }
            else
            {
                            criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                            collGradeLevels = GradeLevelPeer.doSelectJoinGradeLevelLabel(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            boolean newCriteria = true;
                        criteria.add(GradeLevelPeer.LEVELID, getLevelID());
                        if (!lastGradeLevelsCriteria.equals(criteria))
            {
                collGradeLevels = GradeLevelPeer.doSelectJoinGradeLevelLabel(criteria);
            }
        }
        lastGradeLevelsCriteria = criteria;

        return collGradeLevels;
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
              fieldNames.add("LevelID");
              fieldNames.add("Label");
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
          if (name.equals("LevelID"))
        {
                return new Integer(getLevelID());
            }
          if (name.equals("Label"))
        {
                return getLabel();
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
          if (name.equals(GradeLevelLabelPeer.LEVELID))
        {
                return new Integer(getLevelID());
            }
          if (name.equals(GradeLevelLabelPeer.LABEL))
        {
                return getLabel();
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
                return new Integer(getLevelID());
            }
              if (pos == 1)
        {
                return getLabel();
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
          save(GradeLevelLabelPeer.getMapBuilder()
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
                    GradeLevelLabelPeer.doInsert((GradeLevelLabel) this, con);
                    setNew(false);
                }
                else
                {
                    GradeLevelLabelPeer.doUpdate((GradeLevelLabel) this, con);
                }
            }

                                      
                
            if (collGradeLevels != null)
            {
                for (int i = 0; i < collGradeLevels.size(); i++)
                {
                    ((GradeLevel) collGradeLevels.get(i)).save(con);
                }
            }
                          alreadyInSave = false;
        }
      }


                          
      /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param  levelID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
            setLevelID(((NumberKey) key).intValue());
        }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
            setLevelID(Integer.parseInt(key));
        }

  
    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
          return SimpleKey.keyFor(getLevelID());
      }

 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
       * It then fills all the association collections and sets the
     * related objects to isNew=true.
       */
      public GradeLevelLabel copy() throws TorqueException
    {
        return copyInto(new GradeLevelLabel());
    }
  
    protected GradeLevelLabel copyInto(GradeLevelLabel copyObj) throws TorqueException
    {
          copyObj.setLevelID(levelID);
          copyObj.setLabel(label);
  
                    copyObj.setLevelID(0);
                  
                                      
                
        List v = getGradeLevels();
        for (int i = 0; i < v.size(); i++)
        {
            GradeLevel obj = (GradeLevel) v.get(i);
            copyObj.addGradeLevel(obj.copy());
        }
                    
        return copyObj;
    }

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public GradeLevelLabelPeer getPeer()
    {
        return peer;
    }

    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("GradeLevelLabel:\n");
        str.append("LevelID = ")
           .append(getLevelID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        return(str.toString());
    }
}