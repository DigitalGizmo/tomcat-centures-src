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
 * extended all references should be to ContentAreaLabel
 */
public abstract class BaseContentAreaLabel extends BaseObject
{
    /** The Peer class */
    private static final ContentAreaLabelPeer peer =
        new ContentAreaLabelPeer();

        
    /** The value for the contentID field */
    private int contentID;
      
    /** The value for the label field */
    private String label;
  
    
    /**
     * Get the ContentID
     *
     * @return int
     */
    public int getContentID()
    {
        return contentID;
    }

                                              
    /**
     * Set the value of ContentID
     *
     * @param v new value
     */
    public void setContentID(int v) throws TorqueException
    {
    
                  if (this.contentID != v)
              {
            this.contentID = v;
            setModified(true);
        }
    
          
                                  
        // update associated ContentArea
        if (collContentAreas != null)
        {
            for (int i = 0; i < collContentAreas.size(); i++)
            {
                ((ContentArea) collContentAreas.get(i))
                    .setContentID(v);
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
     * Collection to store aggregation of collContentAreas
     */
    protected List collContentAreas;

    /**
     * Temporary storage of collContentAreas to save a possible db hit in
     * the event objects are add to the collection, but the
     * complete collection is never requested.
     */
    protected void initContentAreas()
    {
        if (collContentAreas == null)
        {
            collContentAreas = new ArrayList();
        }
    }

    /**
     * Method called to associate a ContentArea object to this object
     * through the ContentArea foreign key attribute
     *
     * @param l ContentArea
     * @throws TorqueException
     */
    public void addContentArea(ContentArea l) throws TorqueException
    {
        getContentAreas().add(l);
        l.setContentAreaLabel((ContentAreaLabel) this);
    }

    /**
     * The criteria used to select the current contents of collContentAreas
     */
    private Criteria lastContentAreasCriteria = null;

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getContentAreas(new Criteria())
     *
     * @throws TorqueException
     */
    public List getContentAreas() throws TorqueException
    {
        if (collContentAreas == null)
        {
            collContentAreas = getContentAreas(new Criteria(10));
        }
        return collContentAreas;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this ContentAreaLabel has previously
     * been saved, it will retrieve related ContentAreas from storage.
     * If this ContentAreaLabel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     *
     * @throws TorqueException
     */
    public List getContentAreas(Criteria criteria) throws TorqueException
    {
        if (collContentAreas == null)
        {
            if (isNew())
            {
               collContentAreas = new ArrayList();
            }
            else
            {
                      criteria.add(ContentAreaPeer.CONTENTID, getContentID() );
                      collContentAreas = ContentAreaPeer.doSelect(criteria);
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
                      criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                      if (!lastContentAreasCriteria.equals(criteria))
                {
                    collContentAreas = ContentAreaPeer.doSelect(criteria);
                }
            }
        }
        lastContentAreasCriteria = criteria;

        return collContentAreas;
    }

    /**
     * If this collection has already been initialized, returns
     * the collection. Otherwise returns the results of
     * getContentAreas(new Criteria(),Connection)
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getContentAreas(Connection con) throws TorqueException
    {
        if (collContentAreas == null)
        {
            collContentAreas = getContentAreas(new Criteria(10), con);
        }
        return collContentAreas;
    }

    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this ContentAreaLabel has previously
     * been saved, it will retrieve related ContentAreas from storage.
     * If this ContentAreaLabel is new, it will return
     * an empty collection or the current collection, the criteria
     * is ignored on a new object.
     * This method takes in the Connection also as input so that
     * referenced objects can also be obtained using a Connection
     * that is taken as input
     */
    public List getContentAreas(Criteria criteria, Connection con)
            throws TorqueException
    {
        if (collContentAreas == null)
        {
            if (isNew())
            {
               collContentAreas = new ArrayList();
            }
            else
            {
                       criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                       collContentAreas = ContentAreaPeer.doSelect(criteria, con);
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
                       criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                       if (!lastContentAreasCriteria.equals(criteria))
                 {
                     collContentAreas = ContentAreaPeer.doSelect(criteria, con);
                 }
             }
         }
         lastContentAreasCriteria = criteria;

         return collContentAreas;
     }

                        
              
                    
                    
                                
                                                              
                                        
                    
                    
          
    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this ContentAreaLabel is new, it will return
     * an empty collection; or if this ContentAreaLabel has previously
     * been saved, it will retrieve related ContentAreas from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in ContentAreaLabel.
     */
    protected List getContentAreasJoinActivity(Criteria criteria)
        throws TorqueException
    {
        if (collContentAreas == null)
        {
            if (isNew())
            {
               collContentAreas = new ArrayList();
            }
            else
            {
                            criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                            collContentAreas = ContentAreaPeer.doSelectJoinActivity(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            boolean newCriteria = true;
                        criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                        if (!lastContentAreasCriteria.equals(criteria))
            {
                collContentAreas = ContentAreaPeer.doSelectJoinActivity(criteria);
            }
        }
        lastContentAreasCriteria = criteria;

        return collContentAreas;
    }
                  
                    
                              
                                
                                                              
                                        
                    
                    
          
    /**
     * If this collection has already been initialized with
     * an identical criteria, it returns the collection.
     * Otherwise if this ContentAreaLabel is new, it will return
     * an empty collection; or if this ContentAreaLabel has previously
     * been saved, it will retrieve related ContentAreas from storage.
     *
     * This method is protected by default in order to keep the public
     * api reasonable.  You can provide public methods for those you
     * actually need in ContentAreaLabel.
     */
    protected List getContentAreasJoinContentAreaLabel(Criteria criteria)
        throws TorqueException
    {
        if (collContentAreas == null)
        {
            if (isNew())
            {
               collContentAreas = new ArrayList();
            }
            else
            {
                            criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                            collContentAreas = ContentAreaPeer.doSelectJoinContentAreaLabel(criteria);
            }
        }
        else
        {
            // the following code is to determine if a new query is
            // called for.  If the criteria is the same as the last
            // one, just return the collection.
            boolean newCriteria = true;
                        criteria.add(ContentAreaPeer.CONTENTID, getContentID());
                        if (!lastContentAreasCriteria.equals(criteria))
            {
                collContentAreas = ContentAreaPeer.doSelectJoinContentAreaLabel(criteria);
            }
        }
        lastContentAreasCriteria = criteria;

        return collContentAreas;
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
              fieldNames.add("ContentID");
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
          if (name.equals("ContentID"))
        {
                return new Integer(getContentID());
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
          if (name.equals(ContentAreaLabelPeer.CONTENTID))
        {
                return new Integer(getContentID());
            }
          if (name.equals(ContentAreaLabelPeer.LABEL))
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
                return new Integer(getContentID());
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
          save(ContentAreaLabelPeer.getMapBuilder()
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
                    ContentAreaLabelPeer.doInsert((ContentAreaLabel) this, con);
                    setNew(false);
                }
                else
                {
                    ContentAreaLabelPeer.doUpdate((ContentAreaLabel) this, con);
                }
            }

                                      
                
            if (collContentAreas != null)
            {
                for (int i = 0; i < collContentAreas.size(); i++)
                {
                    ((ContentArea) collContentAreas.get(i)).save(con);
                }
            }
                          alreadyInSave = false;
        }
      }


                          
      /**
     * Set the PrimaryKey using ObjectKey.
     *
     * @param  contentID ObjectKey
     */
    public void setPrimaryKey(ObjectKey key)
        throws TorqueException
    {
            setContentID(((NumberKey) key).intValue());
        }

    /**
     * Set the PrimaryKey using a String.
     *
     * @param key
     */
    public void setPrimaryKey(String key) throws TorqueException
    {
            setContentID(Integer.parseInt(key));
        }

  
    /**
     * returns an id that differentiates this object from others
     * of its class.
     */
    public ObjectKey getPrimaryKey()
    {
          return SimpleKey.keyFor(getContentID());
      }

 

    /**
     * Makes a copy of this object.
     * It creates a new object filling in the simple attributes.
       * It then fills all the association collections and sets the
     * related objects to isNew=true.
       */
      public ContentAreaLabel copy() throws TorqueException
    {
        return copyInto(new ContentAreaLabel());
    }
  
    protected ContentAreaLabel copyInto(ContentAreaLabel copyObj) throws TorqueException
    {
          copyObj.setContentID(contentID);
          copyObj.setLabel(label);
  
                    copyObj.setContentID(0);
                  
                                      
                
        List v = getContentAreas();
        for (int i = 0; i < v.size(); i++)
        {
            ContentArea obj = (ContentArea) v.get(i);
            copyObj.addContentArea(obj.copy());
        }
                    
        return copyObj;
    }

    /**
     * returns a peer instance associated with this om.  Since Peer classes
     * are not to have any instance attributes, this method returns the
     * same instance for all member of this class. The method could therefore
     * be static, but this would prevent one from overriding the behavior.
     */
    public ContentAreaLabelPeer getPeer()
    {
        return peer;
    }

    public String toString()
    {
        StringBuffer str = new StringBuffer();
        str.append("ContentAreaLabel:\n");
        str.append("ContentID = ")
           .append(getContentID())
           .append("\n");
        str.append("Label = ")
           .append(getLabel())
           .append("\n");
        return(str.toString());
    }
}
