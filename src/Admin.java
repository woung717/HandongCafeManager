import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Shin on 2017-05-31.
 */
public class Admin {
    private Connection conn;
    private Manager manager;

    public Admin(Connection conn) {
        this.conn = conn;
    }

    public boolean addNewManager() {
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

        System.out.println("Cafe Name : ");
        String cafe = (new Scanner(System.in)).nextLine();
        System.out.println("Phone : ");
        String phone = (new Scanner(System.in)).nextLine();
        System.out.println("Location : ");
        String location = (new Scanner(System.in)).nextLine();

        PreparedStatement pstmt = null;

        try {
            pstmt = this.conn.prepareStatement("START TRANSACTION");
            pstmt.execute();

            String query = "INSERT INTO USERS (ID, PASSWORD, UNAME, BIRTH, SEX, TYPE) VALUES (?, ?, ?, ?, ?, ?)";

            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, ID);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, birthday);
            pstmt.setString(5, gender);
            pstmt.setString(6, "M");

            pstmt.executeUpdate();

            query = "INSERT INTO CAFES (CNAME, PHONE, LOCATION, MANAGER) VALUES (?, ?, ?, ?)";

            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, cafe);
            pstmt.setString(2, phone);
            pstmt.setString(3, location);
            pstmt.setString(4, ID);

            pstmt.executeUpdate();

            pstmt = this.conn.prepareStatement("COMMIT");
            pstmt.execute();

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to create new user.");

            try {
                pstmt = this.conn.prepareStatement("ROLLBACK");
                pstmt.execute();
            } catch (Exception e1) {;}
            return false;
        }

        return true;
    }

    public boolean setManagerOnCafe() {
        System.out.println("Cafe Name : ");
        String cafe = (new Scanner(System.in)).nextLine();
        System.out.println("Input ID : ");
        String ID = (new Scanner(System.in)).nextLine();

        try {
            String query = null;
            PreparedStatement pstmt = this.conn.prepareStatement("START TRANSACTION");
            pstmt.execute();

            query = "SELECT TYPE FROM USERS WHERE ID=?";
            pstmt = this.conn.prepareStatement(query);

            pstmt.setString(1, ID);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next() && rs.getString("TYPE").equals("M")) {
                query = "UPDATE CAFES SET MANAGER=? WHERE CNAME=?";
                pstmt = this.conn.prepareStatement(query);

                pstmt.setString(1, ID);
                pstmt.setString(2, cafe);

                pstmt.executeUpdate();
            }
        } catch(SQLException e) {
            System.out.println("Wrong Input");
            try {
                PreparedStatement pstmt = this.conn.prepareStatement("ROLLBACK");
                pstmt.execute();
            } catch (SQLException e1) { ; }

            return false;
        }

        return true;
    }

    public boolean removeUser() {
        System.out.println("Input ID : ");
        String ID = (new Scanner(System.in)).nextLine();

        String query = "DELETE FROM USERS WHERE ID=?";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, ID);

            pstmt.executeUpdate();
            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to remove the user.");

            return false;
        }

        return true;
    }

    public String[] getAllCafe() {
        String[] cafes = null;

        String query = "SELECT * FROM CAFES";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            cafes = new String[Customer.getRowFromRS(rs)];
            while(rs.next()) {
                cafes[i++] = rs.getString("CNAME") + "/" + rs.getString("PHONE") + "/" +
                        rs.getString("LOCATION") + "/" + rs.getString("MANAGER");
            }

            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Fail to get cafe list.");
        }

        return cafes;
    }

    public String[] getAllUser() {
        String[] users = null;

        String query = "SELECT * FROM USERS";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            users = new String[Customer.getRowFromRS(rs)];
            while(rs.next()) {
                users[i++] = rs.getString("ID") + "/" + rs.getString("PASSWORD") + "/" +
                        rs.getString("UNAME") + "/" + rs.getString("BIRTH") + "/" +
                        rs.getString("SEX") + "/" + (rs.getString("TYPE").equals("M") ? "Manager" :
                        (rs.getString("TYPE").equals("C")) ? "Customer" : "Administrator");
            }

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to get user list.");
        }

        return users;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
