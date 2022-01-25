import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class testMongoDB {

  public static void main(String[] args) {

    try {
      MongoCollection collection = MongoDAO.getCollection();

      // a json record; see
      // https://mongodb.github.io/mongo-java-driver/4.1/driver/getting-started/quick-start/
      // for more
      Document doc = new Document("OrderID", "test_mongodb_6")
          .append("CustomerID", 3692108)
          .append("StoreID", 170)
          .append("Date", "2022-01-06")
          .append("ItemID", 19238).append("NumOfItems", 1).append("Price", 749.97);

      collection.insertOne(doc);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }

}
