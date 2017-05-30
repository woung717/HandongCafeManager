import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    static final String SERVER_ADDR = "peace.handong.edu";
    static final String DB_ID = "db21100380";
    static final String DB_PASSWORD = "21100380";
    static final String DB_NAME = "21100380_HyeonUng_Shin";

    public static void main(String[] args) {
        Connection conn = initDatabase(SERVER_ADDR, DB_ID, DB_PASSWORD);

        Customer customer = new Customer(conn);
    }

    private static Connection initDatabase(String addr, String id, String password) {
        Connection con = null;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            con = DriverManager.getConnection("jdbc:mysql://" + addr, id, password);
            if (!con.isClosed())
                System.out.println("Successfully connected to MySQL server.");

            Statement stmt = (Statement) con.createStatement();

            stmt.execute("USE " + DB_NAME);
        }
        catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }

        return con;
    }
}
