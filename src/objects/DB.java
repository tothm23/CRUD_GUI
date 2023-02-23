package objects;

import java.sql.*;

/**
 *
 * @author Tóth Milán
 */
public class DB {

    private String URL;
    private String USERNAME;
    private String PASSWORD;

    /**
     * Helps in MySQL connection establishing
     *
     * @param URL
     * @param USERNAME
     * @param PASSWORD
     */
    public DB(String URL, String USERNAME, String PASSWORD) {
        this.URL = URL;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    private Connection con;

    private Statement stmt;

    /**
     * Tries import the Driver from mysql-connector-j-8.0.jar
     */
    public void importDriver() {
        // Deprecated  "com.mysql.jdbc.Driver"
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            System.out.println(driver + " imported");
        } catch (ClassNotFoundException err) {
            System.err.println(driver + " not found");
        }
    }

    /**
     * Tries to connect to the MySQL database
     */
    public void connect() {
        try {
            con = DriverManager.getConnection(
                    URL, USERNAME, PASSWORD);
            stmt = con.createStatement();
            System.out.println("Connected to " + URL.substring(URL.indexOf("/") + 2));
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    /**
     * Tries execute an query
     */
    public void executeQuery() {
        try {
            ResultSet rs = stmt.executeQuery("select * from pizza");
            while (rs.next()) {
                System.out.println(rs.getString("Name"));
            }
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    /**
     * Tries disconnect from the MySQL database
     */
    public void disconnect() {
        try {
            con.close();
            System.out.println("Disconnected from " + URL.substring(URL.indexOf("/") + 2));
        } catch (SQLException err) {
            System.err.println(err.getMessage());
        }
    }

    public Statement getStmt() {
        return stmt;
    }

    public Connection getCon() {
        return con;
    }

}
