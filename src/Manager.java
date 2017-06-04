import java.sql.*;
import java.util.Scanner;

public class Manager {
    private Connection conn;
    private String ID;
    private String cafe;

    public Manager(Connection conn, String ID) {
        this.conn = conn;
        this.ID = ID;
        this.cafe = this.resolveCafe();
    }

    public String resolveCafe() {
        String query = "SELECT CNAME FROM CAFES WHERE MANAGER=?";

        PreparedStatement pstmt;
        String cafe = null;
        try {
            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.ID);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                cafe = rs.getString("CNAME");
            }

            pstmt.close();

            return cafe;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Fail to get cafe.");
        }

        return cafe;
    }

    public void addMenu() {
        System.out.println("Menu name : ");
        String menu = (new Scanner(System.in)).nextLine();
        System.out.println("Menu type : ");
        String sort = (new Scanner(System.in)).nextLine();
        System.out.println("Calorie : ");
        String calorie = (new Scanner(System.in)).nextLine();
        System.out.println("Price : ");
        String price = (new Scanner(System.in)).nextLine();
        System.out.println("Stock : ");
        String stock = (new Scanner(System.in)).nextLine();
        System.out.println("Ingredients(Separate with semi-colon) : ");
        String ingredients = (new Scanner(System.in)).nextLine();

        try {
            String query = "INSERT INTO MENUS (CAFE, SORT, MNAME, CALORIE, PRICE, STOCK) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, sort);
            pstmt.setString(3, menu);
            pstmt.setString(4, calorie);
            pstmt.setString(5, price);
            pstmt.setString(6, stock);

            pstmt.executeUpdate();

            String[] ingredient = ingredients.split(";");

            for(int i = 0; i < ingredient.length; i++) {
                query = "INSERT INTO INGREDIENT (MENU, NAME) VALUES ((SELECT MENUID FROM MENUS WHERE MNAME=? AND CAFE=?), ?)";
                pstmt = this.conn.prepareStatement(query);

                pstmt.setString(1, menu);
                pstmt.setString(2, this.cafe);
                pstmt.setString(3, ingredient[i].replaceAll(" ", ""));
                pstmt.executeUpdate();
            }

            pstmt.close();

            System.out.println("menu successfully added!");
            System.out.println("done! new stock added!");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getStackTrace());
        }
    }

    public void deleteMenu() {
        System.out.println("Menu name : ");
        String menu = (new Scanner(System.in)).nextLine();

        try {
            String query = "DELETE FROM MENUS WHERE CAFE=? AND MNAME=?";

            PreparedStatement pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, menu);

            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("menu successfully deleted");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getStackTrace());
        }
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
            menu = new String[Customer.getRowFromRS(rs)];
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

    public void addEvent() {
        System.out.println("Title of the event : ");
        String title = (new Scanner(System.in)).nextLine();
        System.out.println("Content of the event : ");
        String content = (new Scanner(System.in)).nextLine();

        try {
            String query = "INSERT INTO POST (TITLE, CONTENT, CAFE) VALUES (?, ?, ?)";

            PreparedStatement pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, title);
            pstmt.setString(2, content);
            pstmt.setString(3, this.cafe);

            pstmt.executeUpdate();
            pstmt.close();

            System.out.println("event successfully posted!");
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getStackTrace());
        }
    }

    public String[] getTop3ByMonth() {
        String[] menus = null;

        String query = "SELECT *, COUNT(MENU) FROM ORDERS INNER JOIN MENUS ON MENUS.MENUID=ORDERS.MENU " +
                        "WHERE CAFE_NAME=? GROUP BY MONTH(ISSUE_TIME) ORDER BY COUNT(MENU) DESC LIMIT 3";

        try {
            PreparedStatement pstmt = this.conn.prepareStatement(query);

            pstmt.setString(1, this.cafe);
            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            menus = new String[Customer.getRowFromRS(rs)];
            while(rs.next()) {
                menus[i] = rs.getString("MNAME") + " - " + rs.getString("COUNT(MENU)");
            }

            pstmt.close();

            return menus;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return menus;
    }

    public void makeOrder() {
        System.out.println("Customer ID : ");
        String customer = (new Scanner(System.in)).nextLine();
        System.out.println("Menu name : ");
        String menu = (new Scanner(System.in)).nextLine();
        System.out.println("Use coupon?(Y/N) : ");
        String coupon = (new Scanner(System.in)).nextLine();

        try {
            PreparedStatement pstmt = this.conn.prepareStatement("START TRANSACTION");
            pstmt.execute();

            String query = "INSERT INTO ORDERS (CUSTOMER, MENU, CAFE_NAME) VALUES (?, (SELECT MENUID FROM MENUS WHERE MNAME=? AND CAFE=?), ?)";

            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, customer);
            pstmt.setString(2, menu);
            pstmt.setString(3, this.cafe);
            pstmt.setString(4, this.cafe);

            pstmt.executeUpdate();

            query = "SELECT CNUMBER FROM COUPON WHERE CUSTOMER=? AND CAFE=?";

            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, customer);
            pstmt.setString(2, this.cafe);

            ResultSet rs = pstmt.executeQuery();
            if(!rs.next()) {    // if customer doesn't have coupon
                query = "INSERT INTO COUPON (CUSTOMER, CAFE) VALUES (?, ?)";

                pstmt = this.conn.prepareStatement(query);
                pstmt.setString(1, customer);
                pstmt.setString(2, this.cafe);

                pstmt.executeUpdate();
            }

            if(coupon.equals("Y") || coupon.equals("y")) {
                query = "SELECT CNUMBER FROM COUPON WHERE CUSTOMER=? AND CAFE=?";

                pstmt = this.conn.prepareStatement(query);
                pstmt.setString(1, customer);
                pstmt.setString(2, this.cafe);

                rs = pstmt.executeQuery();
                if(rs.next()) {
                    if (rs.getInt("CNUMBER") >= 10) {
                        query = "UPDATE COUPON SET CNUMBER=CNUMBER-10 WHERE CUSTOMER=? AND CAFE=?";

                        pstmt = this.conn.prepareStatement(query);
                        pstmt.setString(1, customer);
                        pstmt.setString(2, this.cafe);

                        pstmt.executeUpdate();
                    } else {
                        System.out.println("Need more than 10 coupons");
                        pstmt = this.conn.prepareStatement("ROLLBACK");
                        pstmt.execute();

                        try { System.in.read(); } catch (Exception e) { ; }

                        return;
                    }
                }
            } else {
                query = "UPDATE COUPON SET CNUMBER=CNUMBER+1 WHERE CUSTOMER=? AND CAFE=?";

                pstmt = this.conn.prepareStatement(query);
                pstmt.setString(1, customer);
                pstmt.setString(2, this.cafe);

                pstmt.executeUpdate();
            }

            query = "UPDATE MENUS SET STOCK=STOCK-1 WHERE CAFE=? AND MNAME=? AND STOCK>0";

            pstmt = this.conn.prepareStatement(query);
            pstmt.setString(1, this.cafe);
            pstmt.setString(2, menu);

            pstmt.executeUpdate();

            pstmt = this.conn.prepareStatement("COMMIT");
            pstmt.execute();

            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e);
            try {
                PreparedStatement pstmt = this.conn.prepareStatement("ROLLBACK");
                pstmt.execute();
            } catch (SQLException e1) { ; }
        }
    }

    public String getCafe() {
        return cafe;
    }
}