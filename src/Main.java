import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Scanner;

public class Main {
    static final String SERVER_ADDR = "peace.handong.edu";
    static final String DB_ID = "db21100380";
    static final String DB_PASSWORD = "21100380";
    static final String DB_NAME = "21100380_HyeonUng_Shin";

    static Connection conn;

    public static void main(String[] args) {
        conn = initDatabase(SERVER_ADDR, DB_ID, DB_PASSWORD);

        Customer customer = new Customer(conn);
        int s = (new Scanner(System.in)).nextInt();
    }

    private static int printMainMenu() {
        return 0;
    }

    private static Connection initDatabase(String addr, String id, String password) {
        Connection conn = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection("jdbc:mysql://" + addr, id, password);
            if (!conn.isClosed())
                System.out.println("Successfully connected to MySQL server.");

            Statement stmt = (Statement) conn.createStatement();

            stmt.execute("USE " + DB_NAME);
        }
        catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        return conn;
    }

    public String register() {
        System.out.println("Input ID : ");
        String ID = (new Scanner(System.in)).nextLine();
        System.out.println("Input Password : ");
        String password = (new Scanner(System.in)).nextLine();
        System.out.println("Name : ");
        String name = (new Scanner(System.in)).nextLine();
        System.out.println("Birthday(YYYY-MM-DD) : ");
        String birthday = (new Scanner(System.in)).nextLine();
        System.out.println("Gender(M or F) : ");
        String gender = (new Scanner(System.in)).nextLine();

        String query = "INSERT INTO USERS (ID, PASSWORD, UNAME, BIRTH, SEX, TYPE) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, ID);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, birthday);
            pstmt.setString(5, gender);
            pstmt.setString(6, "C");

            pstmt.executeUpdate();

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to create new user.");

            return null;
        }

        return ID;
    }

    public String login() {
        System.out.println("Input ID : ");
        String ID = (new Scanner(System.in)).nextLine();
        System.out.println("Input Password : ");
        String password = (new Scanner(System.in)).nextLine();

        String query = "SELECT * FROM USERS WHERE ID=? AND PASSWORD=?";

        PreparedStatement pstmt = null;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, ID);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                pstmt.close();
                return rs.getString("ID");
            } else {
                System.out.println("There is no such user.");
            }
            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to login.");
        }

        return null;
    }
}
