import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * Created by Shin on 2017-05-31.
 */
public class Customer {
    private Connection conn;

    private String ID;
    private String cafe;

    public Customer(Connection conn) {
        this.conn = conn;
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

        String query = "INSERT INTO USER (ID, PASSWORD, UNAME, BIRTH, SEX, TYPE) VALUES (?, ?, ?, ?, ?, ?)";

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

        String query = "SELECT * FROM USER WHERE ID=? AND PASSWORD=?";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, ID);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getString("ID");
            } else {
                System.out.println("There is no such user.");
            }
        } catch(Exception e) {
            System.out.println("Fail to login.");
        }

        return null;
    }

    public String[] getCafeList() {
        String[] cafes = null;

        String query = "SELECT * FROM CAFES ORDER BY CNAME ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            cafes = new String[rs.getRow()];
            while(rs.next()) {
                cafes[i++] = rs.getString("CNAME") + " - " + rs.getString("LOCATION") +
                        "(" + rs.getString("PHONE") + ")";
            }
        } catch(Exception e) {
            System.out.println("Fail to login.");
        }

        return cafes;
    }

    public String[] getCouponList() {
        String[] coupons = null;

        String query = "SELECT * FROM COUPON WHERE CUSTOMER=?";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.ID);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            coupons = new String[rs.getRow()];
            while(rs.next()) {
                coupons[i++] = rs.getString("CAFE") + " - " + rs.getInt("CNUMBER");
            }
        } catch(Exception e) {
            System.out.println("Fail to get coupon list.");
        }

        return coupons;
    }

    public String[] getAllMenu() {
        String[] menu = null;

        String query = "SELECT * FROM MENUS WHERE CAFE=? ORDER BY PRICE ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            menu = new String[rs.getRow()];
            while(rs.next()) {
                menu[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal) - " + rs.getInt("PRICE") + " won" +
                        ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }
        } catch(Exception e) {
            System.out.println("Fail to get menu list.");
        }

        return menu;
    }

    public String[] getAllBeverageByCafe() {
        String beverages[] = null;

        String query = "SELECT MENUS.*, BEVERAGES.* FROM MENUS " +
                        "INNER JOIN BEVERAGES ON MENUS.MENUID= BEVERAGES.MENU " +
                        "WHERE MENUS.SORT!=? AND MENUS.CAFE=? ORDER BY MENUS.PRICE ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, "Dessert");
            pstmt.setString(2, this.cafe);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            beverages = new String[rs.getRow()];
            while(rs.next()) {
                beverages[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal" + ", " + rs.getString("TEMPERATURE") +
                        ", " + rs.getString("SIZE") +") - " + rs.getInt("PRICE") + " won" +
                        ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }
        } catch(Exception e) {
            System.out.println("Fail to get menu list.");
        }

        return beverages;
    }

    public String[] getAllBeverageByTemperature(String temp) {
        String beverages[] = null;

        String query = "SELECT MENUS.*, BEVERAGES.* FROM MENUS " +
                        "INNER JOIN BEVERAGES ON MENUS.MENUID= BEVERAGES.MENU " +
                        "WHERE MENUS.SORT!=? AND BEVERAGES.TEMPERATURE=? ORDER BY MENUS.PRICE ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, "Dessert");
            pstmt.setString(2, temp);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            beverages = new String[rs.getRow()];
            while(rs.next()) {
                beverages[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal" + ", " + rs.getString("SIZE") +") - " +
                        rs.getInt("PRICE") + " won" + ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }
        } catch(Exception e) {
            System.out.println("Fail to get menu list.");
        }

        return beverages;
    }

    public String[] getMenuWithNonCertainIngredient(String ingredient) {
        String[] menu = null;

        String query = "SELECT MENUS.* FROM MENUS WHERE MENUS.CAFE=? AND MENUS.MENUID NOT IN " +
                        "(SELECT MENU FROM INGREDIENT WHERE NAME=? GROUP BY MENU) ORDER BY MENUID ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, ingredient);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            menu = new String[rs.getRow()];
            while(rs.next()) {
                menu[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal) - " + rs.getInt("PRICE") + " won" +
                        ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }
        } catch(Exception e) {
            System.out.println("Fail to get menu list.");
        }

        return menu;
    }

    public String[] getIngredients(String menu) {
        String[] ingredients = null;

        String query = "SELECT * FROM INGREDIENT WHERE MENU=(SELECT MENUID FROM MENU WHERE CAFE=? AND MNAME=?)";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, menu);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            ingredients = new String[rs.getRow()];
            while(rs.next()) {
                ingredients[i++] = rs.getString("NAME");
            }
        } catch(Exception e) {
            System.out.println("Fail to get ingredient list.");
        }

        return ingredients;
    }
}
