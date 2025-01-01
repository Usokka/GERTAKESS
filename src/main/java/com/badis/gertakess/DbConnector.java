package com.badis.gertakess;
import java.sql.*;

public class DbConnector {
    private static String url = "jdbc:mysql://root@localhost:3306/gestion_caisse_db?serverTimezone=UTC";
    private static String username = "";
    private static String password = "";
    private static Connection conn;
    public static Connection connect() {
        try {
            conn = DriverManager.getConnection(url,username,password);

            return conn;
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            return null;
        }
    }

    public static void close(){
        try {
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
