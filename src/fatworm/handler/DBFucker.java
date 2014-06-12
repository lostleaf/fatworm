package fatworm.handler;

import fatworm.constant.Const;
import fatworm.memory.Memory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lostleaf on 14-6-5.
 */
public class DBFucker implements Serializable {
    private String currentDBName;
    private Database currentDB;
    private Map<String, Database> dbs;

    public DBFucker() {
        dbs = new HashMap<String, Database>();
        currentDB = null;
    }

    public Database getCurrentDB() {
        return currentDB;
    }
    public String getCurrentDBName(){return currentDBName;}

    public void useDatabase(String dbName) {
        dbName = dbName.toLowerCase();
        currentDBName = dbName;
        currentDB = dbs.get(dbName);
        if (Memory.dbs.containsKey(dbName))
            Memory.records = Memory.dbs.get(dbName);
        else {
            try {
                System.out.println("read from file " + dbName);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                        new File(Fucker.getRootFile(), Fucker.getDBFileName(dbName))));
                Memory.dbs.put(dbName, (HashMap<String, List<List<Const>>>) ois.readObject());
                Memory.records = Memory.dbs.get(dbName);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
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
