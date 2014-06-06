package fatworm.handler;

import fatworm.planner.Planner;

import java.io.*;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Manager {
    private static File file;
    private static DBManager dbManager;
    private static String fileName = "meta";

    public static void init(File f) {
        file = new File(f, fileName);
        if (file.exists()) {
            try {
                FileInputStream ins = new FileInputStream(file);
                ObjectInputStream ooi = new ObjectInputStream(ins);
                dbManager = (DBManager)ooi.readObject();
                ooi.close();
            } catch (Exception e) {
                System.err.println("init error!");
                e.printStackTrace();
            }
        } else {
            dbManager = new DBManager();
        }
    }

    public static void close() {
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dbManager);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            System.err.println("exit error!");
            e.printStackTrace();
        }
    }
    public static String getFileName() {
        return fileName;
    }

    public static Planner createPlanner() {
        return new Planner();
    }

    public static DBManager getDBManager() {
        return null;
    }
}
