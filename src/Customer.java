import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public String[] getPostTitleList() {
        String[] posts = null;

        String query = "SELECT * FROM POST ORDER BY POST_DATE ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            posts = new String[getRowFromRS(rs)];
            while(rs.next()) {
                posts[i++] = rs.getInt("POST_ID") + ". " + rs.getString("TITLE") + " (" +
                            rs.getString("CAFE") + ")" + " - " + rs.getString("POST_DATE");
            }

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to get post list.");
        }

        return posts;
    }

    public String getPost(int postNumber) {
        String post = null;

        String query = "SELECT * FROM POST WHERE POST_ID=?";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setInt(1, postNumber);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                post = rs.getString("TITLE") + " (" + rs.getString("CAFE") + ")" + " - " +
                        "\n" + rs.getString("CONTENT") + " / " + rs.getString("POST_DATE");
            }

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to get post list.");
        }

        return post;
    }

    public String[] getCafeList() {
        String[] cafes = null;

        String query = "SELECT * FROM CAFES ORDER BY CNAME ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            cafes = new String[getRowFromRS(rs)];
            while(rs.next()) {
                cafes[i++] = rs.getString("CNAME") + " - " + rs.getString("LOCATION") +
                        "(" + rs.getString("PHONE") + ")";
            }

            pstmt.close();
        } catch(SQLException e) {
            System.out.println(e);
            System.out.println("Fail to get cafe list.");
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
            coupons = new String[getRowFromRS(rs)];
            while(rs.next()) {
                coupons[i++] = rs.getString("CAFE") + " - " + rs.getInt("CNUMBER");
            }

            pstmt.close();
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
            menu = new String[getRowFromRS(rs)];
            while(rs.next()) {
                menu[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal) - " + rs.getInt("PRICE") + " won" +
                        ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }

            pstmt.close();
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
            beverages = new String[getRowFromRS(rs)];
            while(rs.next()) {
                beverages[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal" + ", " + rs.getString("TEMPERATURE") +
                        ", " + rs.getString("SIZE") +") - " + rs.getInt("PRICE") + " won" +
                        ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to get menu list.");
        }

        return beverages;
    }

    public String[] getAllBeverageByTemperature(String temp) {
        String beverages[] = null;

        String query = "SELECT MENUS.*, BEVERAGES.* FROM MENUS " +
                        "INNER JOIN BEVERAGES ON MENUS.MENUID= BEVERAGES.MENU " +
                        "WHERE MENUS.CAFE=? AND BEVERAGES.TEMPERATURE=? ORDER BY MENUS.PRICE ASC";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, temp);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            beverages = new String[getRowFromRS(rs)];
            while(rs.next()) {
                beverages[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal" + ", " + rs.getString("SIZE") +") - " +
                        rs.getInt("PRICE") + " won" + ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }

            pstmt.close();
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
            menu = new String[getRowFromRS(rs)];
            while(rs.next()) {
                menu[i++] = rs.getString("MNAME") + " (" + rs.getString("SORT") + ", " +
                        rs.getInt("CALORIE") + " kcal) - " + rs.getInt("PRICE") + " won" +
                        ((rs.getInt("STOCK") < 1) ? " Sold Out" : "");
            }


            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to get menu list.");
        }

        return menu;
    }

    public String[] getIngredients(String menu) {
        String[] ingredients = null;

        String query = "SELECT * FROM INGREDIENT WHERE MENU=(SELECT MENUID FROM MENUS WHERE CAFE=? AND MNAME=?)";

        PreparedStatement pstmt;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, menu);

            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            ingredients = new String[getRowFromRS(rs)];
            while(rs.next()) {
                ingredients[i++] = rs.getString("NAME");
            }

            pstmt.close();
        } catch(Exception e) {
            System.out.println("Fail to get ingredient list.");
        }

        return ingredients;
    }
    
    public static int getRowFromRS(ResultSet rs) {
        int row = 0;
        
        try {
            rs.last();
            row = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {;}
        
        return row;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCafe() {
        return cafe;
    }

    public void setCafe(String cafe) {
        this.cafe = cafe;
    }
}
