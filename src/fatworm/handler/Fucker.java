package fatworm.handler;

import fatworm.constant.Const;
import fatworm.memory.Memory;
import fatworm.planner.Sucker;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Fucker {
    private static File metaFile, root;
    private static DBFucker dbManager;

    public static void init(File f) {
//        System.out.println(f.getAbsolutePath());
        root = f;
        String fileName = "meta";
        metaFile = new File(root, fileName);
        if (metaFile.exists()) {
            try {
                ObjectInputStream ooi = new ObjectInputStream(new FileInputStream(metaFile));
                dbManager = (DBFucker) ooi.readObject();
                ooi.close();
            } catch (Exception e) {
                System.err.println("init error!");
                e.printStackTrace();
            }
        }
        if (dbManager == null)
            dbManager = new DBFucker();
    }

    public static void close() {
        try {
            if (metaFile.exists())
                metaFile.delete();

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(metaFile));
            oos.writeObject(dbManager);
            oos.close();

            Set<Map.Entry<String, HashMap<String, List<List<Const>>>>> s = Memory.dbs.entrySet();
            for (Map.Entry<String, HashMap<String, List<List<Const>>>> entry : s) {
                String dbName = entry.getKey();
                File dataFile = new File(root, getDBFileName(dbName));
                if (dataFile.exists())
                    dataFile.delete();

                oos = new ObjectOutputStream(new FileOutputStream(dataFile));
                oos.writeObject(entry.getValue());
                oos.close();

                File logFile = new File(root, getDBLogFileName(dbName));
                if (logFile.exists())
                    logFile.delete();
            }
        } catch (Exception e) {
            System.err.println("exit error!");
            e.printStackTrace();
        }
    }

    public static File getRootFile() {
        return root;
    }

    public static String getDBFileName(String dbName) {
        return "data_" + dbName;
    }

    public static String getDBLogFileName(String dbName) {
        return getDBFileName(dbName) + ".log";
    }

    public static void storeSQL(String sql) {
        storeSQL(sql, dbManager.getCurrentDBName());
    }

    public static Sucker createPlanner() {
        return new Sucker();
    }

    public static DBFucker getDBManager() {
        return dbManager;
    }

    public static void restoreDB(String dbName) {
        try {
//                System.out.println("read from file " + dbName);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    new File(Fucker.getRootFile(), Fucker.getDBFileName(dbName))));
            Memory.dbs.put(dbName, (HashMap<String, List<List<Const>>>) ois.readObject());
            Memory.records = Memory.dbs.get(dbName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        File logFile = new File(root, getDBLogFileName(dbName));
        try {
            BufferedReader reader = new BufferedReader(new FileReader(logFile));
            String sql;
            while((sql = reader.readLine()) != null){
                Sucker sucker = createPlanner();
                sucker.restore(sql);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void storeSQL(String sql, String dbName) {
        File logFile = new File(root, getDBLogFileName(dbName));
        try {
            FileWriter writer = new FileWriter(logFile, true);
//            String s =  + "\n";
            writer.write(sql.replace("\n", " ").trim() + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
