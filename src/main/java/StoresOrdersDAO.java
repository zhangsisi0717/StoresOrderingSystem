import java.sql.*;
import org.apache.commons.dbcp2.*;
public class StoresOrdersDAO {
  private static BasicDataSource dataSource;

  public StoresOrdersDAO() {
    dataSource = DBCPDataSource.getDataSource();
  }

  public void addNewOrderedItem(OrderedItem orderedItem){


  }

}
