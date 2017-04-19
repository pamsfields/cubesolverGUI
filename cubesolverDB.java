/**
 * Created by Pam on 4/9/2017.
 */
import java.sql.*;
import java.util.Scanner;

public class cubesolverDB {
    static Scanner stringScanner = new Scanner(System.in);
    static Scanner numberScanner = new Scanner(System.in);

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";//Configure the driver needed
    static final String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/cube";//Connection string – where's the database?

    static final String USER = "PamFields";   //username selected on MySQL
    static final String PASSWORD = System.getenv("SQL_Test");   //Set as environmental variable

    public final static String SOLVER_COLUMN = "Solver's Name";
    public final static String SOLVER_TIME = "Time (in seconds)";
    public final static String SOLVER_TABLE_NAME = "Rublik's cube Solvers";


    public cubesolverDB() throws SQLException {
    }

    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Can't instantiate driver class; check you have driver and classpath configured correctly");
            cnfe.printStackTrace();
            System.exit(-1);
        }
        //You should have already created a database via terminal/command prompt OR MySQL Workbench
        Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();
        //Create a table in the database, if it does not exist already
        String createTableSQL = "CREATE TABLE IF NOT EXISTS rubikscube (Cube_Solver varchar(30), Solved_Seconds double)";
        statement.executeUpdate(createTableSQL);
        System.out.println("Created rubikscube table");

        //Add some data
        String addDataSQL = "INSERT INTO rubikscube VALUES ('Cubestormer II robot', 5.270)";
        statement.executeUpdate(addDataSQL);

        addDataSQL = "INSERT INTO rubikscube VALUES ('Fakhri Raihaan', 27.93)";
        statement.executeUpdate(addDataSQL);

        addDataSQL = "INSERT INTO rubikscube VALUES ('Ruxin Liu', 99.33)";
        statement.executeUpdate(addDataSQL);

        addDataSQL = "INSERT INTO rubikscube VALUES ('Mats Valk', 6.27)";
        statement.executeUpdate(addDataSQL);
        System.out.println("Added four rows of data");

        String fetchAllDataSQL = "SELECT * FROM rubikscube";
        ResultSet rs = statement.executeQuery(fetchAllDataSQL);

        String updateDataSQL = "update rubikscube set Solved_Seconds = 5.55 where Cube_Solver = 'Mats Valk'";
        statement.executeUpdate(updateDataSQL);

        String addMore;
        System.out.println("Would you like to add more data to the table?");
        addMore = stringScanner.nextLine();
        if (!addMore.equals("yes")) {
            System.out.println("End of Program");
        } else {
            String solver;
            System.out.println("Please enter the solver's name");
            solver = stringScanner.nextLine();
            Double time;
            System.out.println("Please enter the time in seconds");
            time = numberScanner.nextDouble();
            addDataSQL = "Insert INTO rubikscube VALUES (" + solver + ", " + time + ")";
        }

        while (rs.next()) {
            String cube_solver = rs.getString("Cube_Solver");
            double solved_seconds = rs.getDouble("Solved_Seconds");
            System.out.println("Cube_Solver = " + cube_solver + " Solved_Seconds = " + solved_seconds);
        }
    }

    public static void shutdown() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();
        String fetchAllDataSQL = "SELECT * FROM rubikscube";
        ResultSet rs = statement.executeQuery(fetchAllDataSQL);
        try {
            if (rs != null) {
                rs.close();
                System.out.println("Result set closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se) {
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }

        try {
            if (conn != null) {
                conn.close();
                System.out.println("Database connection closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    public static boolean loadAllSolvers() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASSWORD);
        Statement statement = conn.createStatement();
        String fetchAllDataSQL = "SELECT * FROM rubikscube";
        ResultSet rs = statement.executeQuery(fetchAllDataSQL);
        try{

            if (rs!=null) {
                rs.close();
            }

            String getAllData = "SELECT * FROM " + SOLVER_TABLE_NAME;
            rs = statement.executeQuery(getAllData);

            if (CubeSolverModel == null) {
                //If no current movieDataModel, then make one
                CubeSolverModel = new CubeSolverModel(rs);
            } else {
                //Or, if one already exists, update its ResultSet
                CubeSolverModel.updateResultSet(rs);
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error loading or reloading movies");
            System.out.println(e);
            e.printStackTrace();
            return false;
        }

    }
}
