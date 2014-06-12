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

    public static String getCurDBLogFileName() {
        return getDBLogFileName(dbManager.getCurrentDBName());
    }

    public static void storeSQL(String sql) {
//        File logFile = new File(root, getCurDBLogFileName());
//        try {
//            FileWriter writer = new FileWriter(logFile, true);
//            writer.write(sql);
//            writer.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static Sucker createPlanner() {
        return new Sucker();
    }

    public static DBFucker getDBManager() {
        return dbManager;
    }
}
