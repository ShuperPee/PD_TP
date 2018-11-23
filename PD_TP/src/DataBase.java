/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pedro
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String HOST = "localhost:3306";
    static final String DB_NAME = "PD_TP";
    static final String DB_URL = "jdbc:mysql://" + HOST + "/" + DB_NAME;
    // Database credentials
    static final String USER = "root";
    static final String PASS = "password";
    static private Connection conn;
    static private Statement stmt;

    public DataBase() throws ClassNotFoundException, SQLException {
        conn = null;
        stmt = null;

        //Register JDBC driver
        Class.forName(JDBC_DRIVER);
        //Open a connection
        System.out.println("Liga a Base de dados...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
    }

    public void getDefault() {
        try {
            System.out.println("Creating statement...");
            //Criar a query
            String sql;
            sql = "SELECT * FROM Tabela";
            //Executar a query
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            //Extrair informações do resultado da query
            while (rs.next()) {
                //Recebe uma linha que representa uma Funcionário
                System.out.println("ID Utilizador = " + rs.getInt("id_utilizador") + " PIN = " + rs.getInt("pin"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}
