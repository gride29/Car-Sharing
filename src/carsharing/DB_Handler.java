package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB_Handler {
    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:file:./src/carsharing/db/carsharing";

    static void createTables() {
        Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL);

            System.out.println("Creating table in given database...");
            stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR NOT NULL UNIQUE" +
                    ")";
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");

            conn.setAutoCommit(true);

            stmt.close();
            conn.close();
        } catch(Exception se) {
            se.printStackTrace();
        }
        finally {
            try{
                if(stmt!=null) stmt.close();
            } catch(SQLException ignored) {
            }
            try {
                if(conn!=null) conn.close();
            } catch(SQLException se){
                se.printStackTrace();
            }
        }
        System.out.println("Goodbye!");
    }

    static ArrayList<Company> getCompanyList() {
        ArrayList<Company> companyList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)){
                connection.setAutoCommit(true);

                try (Statement statement = connection.createStatement()){
                    String countQuery = "SELECT COUNT(*) FROM COMPANY";
                    var res = statement.executeQuery(countQuery);
                    int count = -1;

                    while (res.next()) {
                        count = res.getInt(1);
                    }

                    // If there are companies in database, add them to list
                    if (count > 0) {
                        String listQuery = "SELECT * FROM COMPANY";
                        res = statement.executeQuery(listQuery);

                        while (res.next()) {
                            Company company = new Company(res.getInt(1), res.getString(2));
                            companyList.add(company);
                        }
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return companyList;
    }

    static void createCompany(String companyName) {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                connection.setAutoCommit(true);

                try (Statement statement = connection.createStatement()) {
                    String sql = "INSERT INTO COMPANY(NAME) VALUES('" + companyName + "');";
                    statement.executeUpdate(sql);
                    System.out.println("The company was created!");
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
