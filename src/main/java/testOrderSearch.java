import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import java.io.File;
import java.io.FileNotFoundException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import static com.mongodb.client.model.Filters.eq;

public class testOrderSearch {
  private static final String PATH = "output" + File.separator + "result.csv";
  public static void main(String[] args) throws FileNotFoundException {
    MongoCollection<Document> collection = MongoDAO.getCollection();
    Bson filter = Filters.eq("OrderID", "3de2a22d1b6f");
//    Bson filter = Filters.eq("OrderID", "3de2a22d1b6f");
    MongoCursor<Document> res = collection.find(filter).iterator();
    JsonArrayBuilder jsonArray = Json.createArrayBuilder();
    while(res.hasNext()){
      jsonArray.add(res.next().toJson());
    }
    JsonObject findResult = Json.createObjectBuilder().add("findResult", jsonArray).build();
    System.out.println(findResult.toString());

//    while(cursor) {
//      System.out.println(cursor.next());
//    }
//    JsonObject item = Json.createObjectBuilder().add("OrderId", "test_mongodb_6").build();
//
//
//    FindIterable<Document> fi = collection.find(toDo);
//    if (limit > 0)
//      fi.limit(limit);
//    if (skip > 0)
//      fi.skip(skip);
//
//    JSONArray array = new JSONArray();
//    for (Document document : fi)
//      array.add(JSON.parseObject(document.toJson()));

//      ResultFileParser parser = new ResultFileParser();
//      parser.processCSVResultFile(PATH);
//      System.out.println(parser.getHeaders());
//      System.out.println(parser.getResult().get("orderId").size());

//      try{
//        MongoCollection collection = MongoDAO.getCollection();
//        Bson projectionFields = Projections.fields(Projections.include("OrderId"));
//        Document doc = (Document) collection.find(eq("OrderId", "test_mongodb_6"));
////        Document filter = new Document("OrderId", "test_mongodb_6");
////        System.out.println(collection.find((filter)).first());
//
//      }catch (Exception e){
//        e.printStackTrace();
//      }

  }

}
