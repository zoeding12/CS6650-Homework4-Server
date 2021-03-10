import java.sql.*;
import org.apache.commons.dbcp2.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class DAO {
    //private static BasicDataSource dataSource;

    public DAO() {
        //dataSource = DBConnection.getDataSource();
    }

    public void createPurchase(String purchase_id, String store_id, String customer_id,
                               String date, String jsonItems) {
        Connection conn = null;
//        PreparedStatement itemsStatement = null;
        PreparedStatement purchaseStatement = null;
        String purchaseInsertQuery = "INSERT INTO purchase (purchase_id, store_id, customer_id, open_date, purchaseItems) VALUES (?,?,?,?,?)";
        try {
            conn = DBConnection.getConnection();
            purchaseStatement = conn.prepareStatement(purchaseInsertQuery);
//            itemsStatement = conn.prepareStatement(itemsInsertQuery);

            // preparation for purchase table
            purchaseStatement.setString(1, purchase_id);
            purchaseStatement.setString(2, store_id);
            purchaseStatement.setString(3, customer_id);
            purchaseStatement.setString(4, date);
            purchaseStatement.setString(5, jsonItems);

            // preparation for purchaseItems table
//            for(int i = 0; i < items.size(); i++){
//                JSONObject item = (JSONObject) items.get(i);
//                String item_id = (String) item.get("ItemID");
//                Long num_of_item_long = (Long) item.get("numberOfItems:");
//                int num_of_item = num_of_item_long.intValue();
//                itemsStatement.setString(1, purchase_id);
//                itemsStatement.setString(2, item_id);
//                itemsStatement.setInt(3, num_of_item);
//                itemsStatement.addBatch();
//            }

            // insert
            purchaseStatement.executeUpdate();
//            int[] numUpdates = itemsStatement.executeBatch();

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
