package test;

import fatworm.memory.Memory;

import java.io.File;
import java.sql.*;
import java.util.Scanner;

/**
 * Created by lostleaf on 14-5-31.
 */
public class Test {
    static {
        try {
            Class.forName("fatworm.driver.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String url = "jdbc:fatworm:/"
            + System.getProperty("user.dir") + File.separator + "db";

    public static void main(String args[]) {
        String query = "";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            Scanner scanner = new Scanner(new File("test.sql"));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.trim().equals(";")) {
                    System.out.println(query);
                    if (stmt.execute(query)) {
                        ResultSet rs = stmt.getResultSet();
                        rs.beforeFirst();
                        ResultSetMetaData meta = rs.getMetaData();
                        System.out.println(meta.getColumnCount());
                        System.out.println(Memory.records.get("test1").size());
                        while (rs.next()) {
                            String s = "(";
                            for (int i = 1; i <= meta.getColumnCount(); i++)
                                s = s + (i == 1 ? "" : ", ") + rs.getObject(i);
                            System.out.println(s + ")");
                        }
                    }
                    query = "";
                } else if (!line.startsWith("@"))
                    query = query + line;
            }
        } catch (Exception e) {
            System.err.println(query + " error");
//            e.printStackTrace();
        }
//        System.out.println(url);
    }
}
