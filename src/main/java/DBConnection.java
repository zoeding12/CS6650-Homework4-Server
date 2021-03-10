import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.*;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    // private static BasicDataSource dataSource;
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    // NEVER store sensitive information below in plain text!
    private static final String HOST_NAME = System.getProperty("MYSQL_IP_ADDRESS");
    private static final String PORT = System.getProperty("MYSQL_PORT");
    private static final String DATABASE = "supermarket";
    private static final String USERNAME = System.getProperty("DB_USERNAME");
    private static final String PASSWORD = System.getProperty("DB_PASSWORD");

    static {
        try {
            // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
//        dataSource = new BasicDataSource();
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
            String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
//        dataSource.setUrl(url);
//        dataSource.setUsername(USERNAME);
//        dataSource.setPassword(PASSWORD);
//        dataSource.setInitialSize(10);
//        dataSource.setMaxTotal(60);
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
            config.setJdbcUrl(url);
            config.setUsername(USERNAME);
            config.setPassword(PASSWORD);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            ds = new HikariDataSource(config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private DBConnection(){}

//    public static BasicDataSource getDataSource() {
//        return dataSource;
//    }
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
