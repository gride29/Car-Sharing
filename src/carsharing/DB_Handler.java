package carsharing;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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
                    ");" +
                    "CREATE TABLE IF NOT EXISTS CAR (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR NOT NULL UNIQUE," +
                    "COMPANY_ID INT NOT NULL," +
                    "CONSTRAINT fk_companyId FOREIGN KEY (COMPANY_ID)" +
                    "REFERENCES COMPANY(ID)" +
                    ");" +
                    "CREATE TABLE IF NOT EXISTS CUSTOMER (" +
                    "ID INT PRIMARY KEY AUTO_INCREMENT," +
                    "NAME VARCHAR NOT NULL UNIQUE," +
                    "RENTED_CAR_ID INT DEFAULT NULL," +
                    "CONSTRAINT fk_rentId FOREIGN KEY (RENTED_CAR_ID)" +
                    "REFERENCES CAR(ID)" +
                    ");";
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

    static ArrayList<Car> getCarList(int companyId) {
        ArrayList<Car> carList = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {

                connection.setAutoCommit(true);
                try (Statement statement = connection.createStatement()) {
                    String countQuery = "SELECT COUNT(*) FROM CAR";
                    var res = statement.executeQuery(countQuery);
                    int count = -1;

                    while (res.next()) {
                        count = res.getInt(1);
                    }

                    if (count > 0) {
                        String listQuery = "SELECT * " +
                                "FROM CAR " +
                                "LEFT JOIN CUSTOMER ON CAR.ID = CUSTOMER.RENTED_CAR_ID " +
                                "WHERE " +
                                "CAR.COMPANY_ID = " + companyId + " AND CUSTOMER.RENTED_CAR_ID IS NULL;";
                        res = statement.executeQuery(listQuery);

                        while (res.next()) {
                            carList.add(new Car(res.getInt(1), res.getString(2), companyId));
                        }
                    }
                }

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return carList;
    }

    static void createCar(String carName, int companyId) {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                connection.setAutoCommit(true);

                try (Statement statement = connection.createStatement()) {
                    String sql = "INSERT INTO CAR(NAME, COMPANY_ID) VALUES('" + carName + "', " + companyId + ");";
                    statement.executeUpdate(sql);
                    System.out.println("The car was added!");
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static ArrayList<Account> getCustomersList() {
        ArrayList<Account> customers = new ArrayList<>();

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                connection.setAutoCommit(true);

                try (Statement statement = connection.createStatement()) {

                    String countQuery = "SELECT COUNT(*) FROM CUSTOMER";
                    var res = statement.executeQuery(countQuery);
                    int count = -1;

                    while (res.next()) {
                        count = res.getInt(1);
                    }

                    if (count > 0) {
                        String listQuery = "SELECT * FROM CUSTOMER";
                        res = statement.executeQuery(listQuery);

                        while (res.next()) {
                            Account customer = new Account(res.getInt(1), res.getString(2), -1);
                            customers.add(customer);
                        }

                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    static void createCustomer(String name) {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {

                connection.setAutoCommit(true);
                try (Statement statement = connection.createStatement()) {
                    String sql = "INSERT INTO CUSTOMER(NAME) VALUES ('" + name + "')";
                    statement.executeUpdate(sql);
                    System.out.println("The customer was added!");
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static void rentCar(Account customerAccount) {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                connection.setAutoCommit(true);
                try (Statement statement = connection.createStatement()) {
                    String sql = "SELECT * FROM CUSTOMER WHERE ID = " + customerAccount.id + ";";
                    ResultSet customer = statement.executeQuery(sql);

                    int carId = -1;
                    String carName = "NULL";

                    while (customer.next()) {
                        if (customer.getObject(3) != null) {
                            System.out.println("You've already rented a car!");
                            System.out.println();
                            return;
                        } else {
                            Scanner scan = new Scanner(System.in);
                            ArrayList<Company> companies = DB_Handler.getCompanyList();

                            if (companies.size() == 0) {
                                return;
                            } else {
                                Manager.printCompanies(companies);
                                int selectedCompany = Integer.parseInt(scan.nextLine());
                                System.out.println();

                                if (selectedCompany == 0) {
                                    return;
                                }

                                int companyId = companies.get(selectedCompany - 1).id;
                                String companyName = companies.get(selectedCompany - 1).companyName;

                                ArrayList<Car> carList = DB_Handler.getCarList(companyId);

                                if (carList.size() == 0) {
                                    System.out.println("No available cars in the '" + companyName + "' company");
                                    System.out.println();
                                    return;
                                } else {
                                    Company.printCars(carList);
                                    System.out.println("0. Back");

                                    int selectedCar = Integer.parseInt(scan.nextLine());
                                    System.out.println();

                                    if (selectedCar == 0) {
                                        return;
                                    }
                                    carId = carList.get(selectedCar - 1).id;
                                    carName = carList.get(selectedCar - 1).carName;
                                }
                            }
                        }
                    }
                    sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = " +
                            carId + " WHERE ID = " + customerAccount.id;
                    statement.executeUpdate(sql);
                    System.out.println("You rented '" + carName + "'");
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static void returnCar(Account customerAccount) {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                connection.setAutoCommit(true);
                try (Statement statement = connection.createStatement()) {
                    String sql = "SELECT * FROM CUSTOMER WHERE ID = " + customerAccount.id + ";";
                    ResultSet customer = statement.executeQuery(sql);

                    while (customer.next()) {
                        if (customer.getObject(3) == null) {
                            System.out.println("You didn't rent a car!\n");
                            return;
                        }
                    }

                    sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = NULL WHERE ID = " + customerAccount.id;
                    statement.executeUpdate(sql);
                    System.out.println("You've returned a rented car!");
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    static void getCar(Account customerAccount) {
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection connection = DriverManager.getConnection(DB_URL)) {
                connection.setAutoCommit(true);
                try (Statement statement = connection.createStatement()) {
                    String query = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE ID=" + customerAccount.id + ";";
                    ResultSet res = statement.executeQuery(query);

                    String carName = "NULL";
                    String companyName = "NULL";
                    int companyId = 0;
                    int rentedCarId = 0;

                    while (res.next()) {

                        if (res.getObject(1) == null) {
                            System.out.println("You didn't rent a car!\n");
                            return;
                        } else {
                            rentedCarId = res.getInt(1);
                        }
                    }

                    query = "SELECT * FROM CAR WHERE ID=" + rentedCarId;
                    ResultSet car = statement.executeQuery(query);

                    while (car.next()) {

                        carName = car.getString("NAME");
                        companyId = car.getInt("COMPANY_ID");
                    }

                    query = "SELECT NAME FROM COMPANY WHERE ID=" + companyId;
                    ResultSet company = statement.executeQuery(query);

                    while (company.next()) {
                        companyName = company.getString("NAME");
                    }

                    System.out.println("Your rented car:");
                    System.out.println(carName);
                    System.out.println("Company:");
                    System.out.println(companyName);
                    System.out.println();
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
