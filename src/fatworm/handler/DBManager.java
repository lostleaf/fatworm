package fatworm.handler;

import fatworm.constant.Const;
import fatworm.memory.Memory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DBManager implements Serializable {
    private String currentDBName;
    private Database currentDB;
    private Map<String, Database> dbs;

    public DBManager() {
        dbs = new HashMap<String, Database>();
        currentDB = null;
    }

    public Database getCurrentDB() {
        return currentDB;
    }

    public String getCurrentDBName() {
        return currentDBName;
    }

    public void useDatabase(String dbName) {
        dbName = dbName.toLowerCase();
        currentDBName = dbName;
        currentDB = dbs.get(dbName);
        Memory.records = Memory.dbs.get(dbName);
    }

    public void addDatabase(String dbName) {
        dbName = dbName.toLowerCase();
        dbs.put(dbName, new Database());
        Memory.dbs.put(dbName, new HashMap<String, List<List<Const>>>());
    }

    public void dropDatabase(String name) {
        name = name.toLowerCase();
        if (name.equals(currentDBName)) {
            currentDB = null;
            currentDBName = null;
        }
        if (dbs.containsKey(name)) {
            dbs.get(name).dropAllTables();
            dbs.remove(name);
        }
        Memory.dbs.remove(name);
    }
}
