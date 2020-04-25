package banking.database;

import java.sql.*;

/**
 * @author Dionysios Stolis 4/25/2020
 */
public class DatabaseService {

    private static final String ROOT = System.getProperty ("java.class.path");
    private static final String SQLITE_DRIVER = "jdbc:sqlite:";
    void createNewDatabase(String databaseName) {
        String url = SQLITE_DRIVER + ROOT  + databaseName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void createNewTable(String databaseName) {
        String url = "jdbc:sqlite:" + ROOT + "/" + databaseName;
        String sql = "CREATE TABLE card (\n"
                + "	id INTEGER PRIMARY KEY,\n"
                + "	number TEXT NOT NULL,\n"
                + "	pin TEXT NOT NULL,\n"
                + "	balance INTEGER\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void insert(String databaseName, String number, String pin) {
        String sql = "INSERT INTO card(number ,pin) VALUES(?,?)";
        String url = SQLITE_DRIVER + ROOT + databaseName;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    int selectBalance(String databaseName, String number) {
        String url = SQLITE_DRIVER + ROOT + "/" + databaseName;
        String sql = "SELECT balance FROM card WHERE number = ?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            ResultSet set = pstmt.executeQuery();
            return set.getInt("balance");
        } catch (SQLException e) {
            return -1;
        }
    }

    void delete(String databaseName, String number) {
        String sql = "DELETE FROM card WHERE number = ?";
        String url = SQLITE_DRIVER + ROOT + "/" + databaseName;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // set the corresponding param
            pstmt.setString(1, number);
            // execute the delete statement
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    void update(String databaseName, String number, int balance) {
        String sql = "UPDATE card SET balance = ? , "
                + "WHERE number = ?";
        String url = SQLITE_DRIVER + ROOT + "/" + databaseName;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, balance);
            pstmt.setString(2, number);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    int selectAccount(String databaseName, String number, String pin) {
        String url = SQLITE_DRIVER + ROOT + "/" + databaseName;
        String sql = "SELECT balance FROM card WHERE number = ? AND pin = ?;";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, number);
            pstmt.setString(2, pin);
            ResultSet set = pstmt.executeQuery();
            return set.getInt("balance");
        } catch (SQLException e) {
            return -1;
        }
    }
}
