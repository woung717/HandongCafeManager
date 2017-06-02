import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            String query = "INSERT INTO USERS (ID, PASSWORD, UNAME, BIRTH, SEX, TYPE) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt;
            try {
                pstmt = this.conn.prepareStatement(query);
                pstmt.setString(1, ID);
                pstmt.setString(2, password);
                pstmt.setString(3, name);
                pstmt.setString(4, birthday);
                pstmt.setString(5, gender);
                pstmt.setString(6, "M");

                pstmt.executeUpdate();
            } catch(Exception e) {
                System.out.println("Fail to create new user.");

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
        } catch(Exception e) {
            System.out.println("Fail to remove the user.");

            return false;
        }

        return true;
    }

    public String[] showAllUser() {
        String[] users = null;

        String query = "SELECT * FROM USERS";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            users = new String[rs.getRow()];
            while(rs.next()) {
                users[i++] = rs.getString("ID") + "/" + rs.getString("PASSWORD") + "/" +
                        rs.getString("UNAME") + "/" + rs.getString("BIRTH") + "/" +
                        rs.getString("SEX") + "/" + (rs.getString("TYPE") == "M" ? "Manager" :
                        (rs.getString("TYPE") == "C") ? "Customer" : "Administrator");
            }
        } catch(Exception e) {
            System.out.println("Fail to get coupon list.");
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
