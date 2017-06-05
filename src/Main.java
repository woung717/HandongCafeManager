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

        String ID = null;
        do {
            ID = (door() == 1) ? login() : register();
        } while(ID == null);

        boolean mainLoop = true;
        while(mainLoop) {
            if(getUserType(ID).equals("C")) {
                Customer customer = new Customer(conn);

                customer.setID(ID);

                switch (printCustomterMainMenu()) {
                    case 1: {
                        String[] cafes = customer.getCafeList();

                        System.out.println("=============================");
                        for (String cafe : cafes) {
                            System.out.println(cafe);
                        }
                        System.out.println("=============================");
                        System.out.print("Choose a cafe : ");
                        customer.setCafe((new Scanner(System.in)).nextLine());

                        boolean cafeLoop = true;
                        while (cafeLoop) {

                            System.out.println("");
                            System.out.println(customer.getCafe());
                            switch (printCafeMenu()) {
                                case 1: {
                                    String[] menus = customer.getAllMenu();

                                    System.out.println("=============================");
                                    for (String menu : menus) {
                                        System.out.println(menu);
                                    }
                                    System.out.println("=============================");
                                    try { System.in.read(); } catch (Exception e) { ; }

                                    break;
                                }
                                case 2: {
                                    String[] menus = customer.getAllBeverageByCafe();

                                    System.out.println("=============================");
                                    for (String menu : menus) {
                                        System.out.println(menu);
                                    }
                                    System.out.println("=============================");
                                    try { System.in.read(); } catch (Exception e) { ; }

                                    break;
                                }
                                case 3: {
                                    System.out.print("Hot or Cool? (H or C or H/C) : ");
                                    String[] menus = customer.getAllBeverageByTemperature((new Scanner(System.in)).nextLine());

                                    System.out.println("=============================");
                                    for (String menu : menus) {
                                        System.out.println(menu);
                                    }
                                    System.out.println("=============================");
                                    try { System.in.read(); } catch (Exception e) { ; }

                                    break;
                                }
                                case 4: {
                                    System.out.print("Which beverage do you want? : ");

                                    String[] ingredients = customer.getIngredients((new Scanner(System.in)).nextLine());

                                    System.out.println("=============================");
                                    for (String ingredient : ingredients) {
                                        System.out.println(ingredient);
                                    }
                                    System.out.println("=============================");
                                    try { System.in.read(); } catch (Exception e) { ; }

                                    break;
                                }
                                case 5: {
                                    System.out.print("What ingredient do you want to exclude? : ");

                                    String[] menus = customer.getMenuWithNonCertainIngredient((new Scanner(System.in)).nextLine());

                                    System.out.println("=============================");
                                    for (String menu : menus) {
                                        System.out.println(menu);
                                    }
                                    System.out.println("=============================");
                                    try { System.in.read(); } catch (Exception e) { ; }

                                    break;
                                }
                                case 6: {
                                    cafeLoop = false;
                                    break;
                                }
                                default:
                                    break;
                            }
                        }

                        break;
                    }
                    case 2: {
                        String[] posts = customer.getPostTitleList();

                        System.out.println("=============================");
                        for (String post : posts) {
                            System.out.println(post);
                        }
                        System.out.println("=============================");
                        System.out.print("Choose a post number to view : ");
                        int postNumber = (new Scanner(System.in)).nextInt();

                        String post;
                        if ((post = customer.getPost(postNumber)) != null) {
                            System.out.println("=============================");
                            System.out.println(post);
                            System.out.println("=============================");
                            try { System.in.read(); } catch (Exception e) { ; }
                        } else {
                            System.out.print("No such post exist.");
                        }

                        break;
                    }
                    case 3: {
                        String[] coupons = customer.getCouponList();

                        System.out.println("=============================");
                        for (String coupon : coupons) {
                            System.out.println(coupon);
                        }
                        System.out.println("=============================");
                        try { System.in.read(); } catch (Exception e) { ; }

                        break;
                    }
                    case 4: {
                        mainLoop = false;
                        break;
                    }
                    default:
                        break;
                }
            } else if(getUserType(ID).equals("M")) {
                Manager manager = new Manager(conn, ID);

                System.out.println("");
                System.out.println(manager.getCafe());
                switch (printManagerMainMenu()) {
                    case 1: {
                        manager.addMenu();
                        break;
                    }
                    case 2: {
                        manager.deleteMenu();
                        break;
                    }
                    case 3: {
                        String[] menus = manager.getAllMenu();

                        System.out.println("=============================");
                        for (String menu : menus) {
                            System.out.println(menu);
                        }
                        System.out.println("=============================");
                        try { System.in.read(); } catch (Exception e) { ; }

                        break;
                    }
                    case 4: {
                        manager.addEvent();
                        break;
                    }
                    case 5: {
                        manager.makeOrder();
                        break;
                    }
                    case 6: {
                        String[] menus = manager.getTop3ByMonth();

                        System.out.println("=============================");
                        for (String menu : menus) {
                            System.out.println(menu);
                        }
                        System.out.println("=============================");
                        try { System.in.read(); } catch (Exception e) { ; }

                        break;
                    }
                    case 7: {
                        mainLoop = false;
                        break;
                    }
                    default:
                        break;
                }
            } else if(getUserType(ID).equals("A")) {
                Admin admin = new Admin(conn);

                switch (printAdminMainMenu()) {
                    case 1: {
                        String[] cafes = admin.getAllCafe();

                        System.out.println("=============================");
                        for (String cafe : cafes) {
                            System.out.println(cafe);
                        }
                        System.out.println("=============================");
                        try { System.in.read(); } catch (Exception e) { ; }

                        break;
                    }
                    case 2: {
                        String[] users = admin.getAllUser();

                        System.out.println("=============================");
                        for (String user : users) {
                            System.out.println(user);
                        }
                        System.out.println("=============================");
                        try { System.in.read(); } catch (Exception e) { ; }

                        break;
                    }
                    case 3: {
                        admin.addNewManager();
                        break;
                    }
                    case 4: {
                        admin.removeUser();
                        break;
                    }
                    case 5: {
                        admin.setManagerOnCafe();
                        break;
                    }
                    case 6: {
                        mainLoop = false;
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

    public static int door() {
        System.out.println("");
        System.out.println("Handong Cafe Manager");
        System.out.println("=============================");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("=============================");
        System.out.print("Choose a menu : ");

        return (new Scanner(System.in)).nextInt();
    }

    public static int printAdminMainMenu() {
        System.out.println("");
        System.out.println("=============================");
        System.out.println("1. View all cafe");
        System.out.println("2. View all user");
        System.out.println("3. Add manager(with cafe)");
        System.out.println("4. Remove user");
        System.out.println("5. Set manager on cafe");
        System.out.println("6. Quit");
        System.out.println("=============================");
        System.out.print("Choose a menu : ");

        return (new Scanner(System.in)).nextInt();
    }

    public static int printManagerMainMenu() {
        System.out.println("=============================");
        System.out.println("1. Add menu");
        System.out.println("2. Remove menu");
        System.out.println("3. Show all menu");
        System.out.println("4. Add event");
        System.out.println("5. Make order");
        System.out.println("6. Monthly Top 3 menu");
        System.out.println("7. Quit");
        System.out.println("=============================");
        System.out.print("Choose a menu : ");

        return (new Scanner(System.in)).nextInt();
    }

    public static int printCustomterMainMenu() {
        System.out.println("");
        System.out.println("=============================");
        System.out.println("1. Select a cafe");
        System.out.println("2. View all events");
        System.out.println("3. View Stamp Coupons");
        System.out.println("4. Quit");
        System.out.println("=============================");
        System.out.print("Choose a menu : ");

        return (new Scanner(System.in)).nextInt();
    }

    public static int printCafeMenu() {
        System.out.println("=============================");
        System.out.println("1. View all menu");
        System.out.println("2. View beverage menu");
        System.out.println("3. View beverage menu by temperature");
        System.out.println("4. View ingredients");
        System.out.println("5. View menu with filtering ingredient");
        System.out.println("6. Exit cafe menu");
        System.out.println("=============================");
        System.out.print("Choose a menu : ");

        return (new Scanner(System.in)).nextInt();
    }

    public static Connection initDatabase(String addr, String id, String password) {
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

    public static String register() {
        System.out.print("");
        System.out.print("Input ID : ");
        String ID = (new Scanner(System.in)).nextLine();
        System.out.print("Input Password : ");
        String password = (new Scanner(System.in)).nextLine();
        System.out.print("Name : ");
        String name = (new Scanner(System.in)).nextLine();
        System.out.print("Birthday(YYYY-MM-DD) : ");
        String birthday = (new Scanner(System.in)).nextLine();
        System.out.print("Gender(M or F) : ");
        String gender = (new Scanner(System.in)).nextLine();

        String query = "INSERT INTO USERS (ID, PASSWORD, UNAME, BIRTH, SEX, TYPE) VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt;
        try {
            pstmt = conn.prepareStatement(query);
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

    public static String login() {
        System.out.print("");
        System.out.print("Input ID : ");
        String ID = (new Scanner(System.in)).nextLine();
        System.out.print("Input Password : ");
        String password = (new Scanner(System.in)).nextLine();

        String query = "SELECT * FROM USERS WHERE ID=? AND PASSWORD=?";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, ID);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                pstmt.close();

                return ID;
            } else {
                System.out.println("There is no such user.");
            }
            pstmt.close();
        } catch(SQLException e) {
            System.out.println(e);
            System.out.println("Fail to login.");
        }

        return null;
    }

    public static String getUserType(String ID) {
        String query = "SELECT TYPE FROM USERS WHERE ID=?";

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, ID);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getString("TYPE");
            } else {
                System.out.println("There is no such user.");
            }
            pstmt.close();
        } catch(SQLException e) {
            System.out.println(e);
            System.out.println("Fail to login.");
        }

        return null;
    }
}
