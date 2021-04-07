import java.sql.*;
import org.apache.commons.dbcp2.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DAO {
    //private static BasicDataSource dataSource;

    public DAO() {
    }

    public void createPurchase(String purchase_id, String store_id, String customer_id,
                               String date, String jsonItems) {
        Connection conn = null;

        PreparedStatement purchaseStatement = null;
        String purchaseInsertQuery = "INSERT INTO purchase (purchase_id, store_id, customer_id, open_date, purchaseItems) VALUES (?,?,?,?,?)";
        try {
            conn = DBConnection.getConnection();
            purchaseStatement = conn.prepareStatement(purchaseInsertQuery);

            // preparation for purchase table
            purchaseStatement.setString(1, purchase_id);
            purchaseStatement.setString(2, store_id);
            purchaseStatement.setString(3, customer_id);
            purchaseStatement.setString(4, date);
            purchaseStatement.setString(5, jsonItems);

            // insert
            purchaseStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (purchaseStatement != null) {
                    purchaseStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
