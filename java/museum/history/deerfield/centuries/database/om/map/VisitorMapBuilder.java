package museum.history.deerfield.centuries.database.om.map;

import java.util.Date;
import java.math.BigDecimal;

import org.apache.torque.Torque;
import org.apache.torque.TorqueException;
import org.apache.torque.map.MapBuilder;
import org.apache.torque.map.DatabaseMap;
import org.apache.torque.map.TableMap;

/**
  *  This class was autogenerated by Torque on:
  *
  * [Thu Sep 09 17:11:01 EDT 2010]
  *
  */
public class VisitorMapBuilder implements MapBuilder
{
    /**
     * The name of this class
     */
    public static final String CLASS_NAME =
        "museum.history.deerfield.centuries.database.om.map.VisitorMapBuilder";


    /**
     * The database map.
     */
    private DatabaseMap dbMap = null;

    /**
     * Tells us if this DatabaseMapBuilder is built so that we
     * don't have to re-build it every time.
     *
     * @return true if this DatabaseMapBuilder is built
     */
    public boolean isBuilt()
    {
        return (dbMap != null);
    }

    /**
     * Gets the databasemap this map builder built.
     *
     * @return the databasemap
     */
    public DatabaseMap getDatabaseMap()
    {
        return this.dbMap;
    }

    /**
     * The doBuild() method builds the DatabaseMap
     *
     * @throws TorqueException
     */
    public void doBuild() throws TorqueException
    {
        dbMap = Torque.getDatabaseMap("centuriesForum");

        dbMap.addTable("Visitor");
        TableMap tMap = dbMap.getTable("Visitor");

        tMap.setPrimaryKeyMethod(TableMap.NATIVE);


              tMap.addPrimaryKey("Visitor.VISITORID", new Integer(0));
                    tMap.addColumn("Visitor.VISITORNAME", new String());
                    tMap.addColumn("Visitor.PASSWORD", new String());
                    tMap.addForeignKey(
                "Visitor.ROLEID", new Integer(0) , "Role" ,
                "roleID");
                    tMap.addColumn("Visitor.LASTNAME", new String());
                    tMap.addColumn("Visitor.FIRSTNAME", new String());
          }
}
