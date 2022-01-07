import java.sql.*;
import java.util.List;
import org.apache.commons.dbcp2.*;

public class StoresOrdersDAO {

  private static BasicDataSource dataSource;

  public StoresOrdersDAO() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public void addNewOrderedItem(List<OrderedItem> orderedItems) throws SQLException {
    Connection conn = null;
    PreparedStatement preparedStatement = null;
    String insertQueryStatement =
        "INSERT INTO StoresOrderedItems (OrderID, ItemID, CustomerID, StoreID, NumOfItems, Price,Date,Status) "
            +
            "VALUES (?,?,?,?,?,?,?,?)";
    try {
      conn = dataSource.getConnection();
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      for (OrderedItem item : orderedItems) {
        preparedStatement.setString(1, item.getOrderId());
        preparedStatement.setInt(2, item.getItemID());
        preparedStatement.setInt(3, item.getCustomerId());
        preparedStatement.setInt(4, item.getStoreId());
        preparedStatement.setInt(5, item.getNumOfItems());
        preparedStatement.setDouble(6, item.getPrice());
        preparedStatement.setDate(7, (Date) item.getDate());
        preparedStatement.setString(8, item.getStatus().toString());
        preparedStatement.addBatch();
      }

      // execute insert SQL statement
//      preparedStatement.executeUpdate();
      preparedStatement.executeBatch();
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }

//  public static void main(String[] args) throws SQLException {
//    StoresOrdersDAO test = new StoresOrdersDAO();
//    Date date = Date.valueOf(LocalDate.now());
//    OrderedItem item = new OrderedItem(12313,193225,"231231234",242,5,33.0,date);
//    OrderedItem item2 = new OrderedItem(12313,124325,"231234",242,5,33.0,date);
//    List<OrderedItem> items = Arrays.asList(item,item2);
//    test.addNewOrderedItem(items);
//  }

}
