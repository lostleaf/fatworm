package fatworm.driver;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by lostleaf on 14-6-5.
 */
public class Driver implements java.sql.Driver {
    static {
        try {
            java.sql.DriverManager.registerDriver(new Driver());
//            System.out.println("register driver");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't register driver!");
        }
    }
    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (acceptsURL(url)) {
            String path = url.substring(13);
            File file = (new File(path)).getAbsoluteFile();
            try {
                return new FatConnection(file);
            } catch (Exception e) {
                throw new SQLException(e);
            }
        }
        throw new SQLException("URL not accepted!");
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        if (!url.startsWith("jdbc:fatworm:")) return false;
        String path = url.substring(13);
        File file = (new File(path)).getAbsoluteFile();
        return (file.isDirectory()) || file.mkdirs();
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
