package test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
        try {
            Connection conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(url);
    }
}
