import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.InsertManyOptions;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Date;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.bson.Document;
import org.bson.conversions.Bson;

@WebServlet(name = "StoresServlet", value = "/StoresServlet")
public class StoresServlet extends HttpServlet {


  private static final int VALID_URL_LENGTH_FOR_POST = 7;
  private static final int VALID_URL_LENGTH_FOR_GET = 4;
  private static final String ORDER_ID_FLAG = "OrderID";
  private static final String ORDER_ID_URL_FLAG = "orderID";
  private static final String CUSTOMER_ID_FLAG = "CustomerID";
  private static final String STORE_ID_FLAG = "StoreID";
  private static final String PRICE_FLAG = "Price";
  private static final String DATE_FLAG = "Date";
  private static final String QUEUE_NAME = "store_orders";

  private static final String PURCHASE = "purchase";
  private static final String CUSTOMER = "customer";
  private static final String DATE = "date";
  private static final String ITEMS = "Items";
  private static final String ITEMS_ID = "ItemID";
  private static final String NUM_OF_ITEMS = "NumOfItems";
  private static final int PURCHASE_IDX = 1;
  private static final int STORE_ID_IDX = 2;
  private static final int CUSTOMER_IDX = 3;
  private static final int CUSTOMER_ID_IDX = 4;
  private static final int DATE_IDX = 5;
  private static final int DATE_CONTENT_IDX = 6;
  private static final int ORDER_ID_URL_IDX = 2;
  private static final boolean DURABLE=true;
  private static final String NUM_RE_PATTERN="^\\d+$";
  private static final String SLASH_SEPARATOR="/";
  private static final String EMPTY_STRING="";
  private static GenericObjectPool<Channel> pool = MQChannelPool.getPool();
  private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> recordsStoreToItem= SoldItemsRecordsStats.getRecordsStoreToItem();
  private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> recordsItemToStore= SoldItemsRecordsStats.getRecordsItemToStore();
//  private static MongoClient client = DAO.MongoDAO.getClient();

  private static MongoCollection collection = MongoDAO.getCollection();

  private InsertManyOptions insertManyOptions;

  @Override
  public void init() throws ServletException {

    insertManyOptions = new InsertManyOptions();
    insertManyOptions.ordered(false);
    System.out.println("Servlet init() called");
  }


  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    // urlPath  = "/purchase/orderID/{orderID}"
    // urlParts = [,purchase,orderID,{orderID}]
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    String urlPath = request.getPathInfo();
    String[] urlParts = urlPath.split(SLASH_SEPARATOR);
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    try{
      String orderID = this.processGetURL(urlParts);
      if(orderID.equals(EMPTY_STRING)){
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      }
      else{
        JsonObject findResult = this.findOrderFromMongoDB(orderID);
        response.getWriter().write(findResult.toString());
      }
    } catch (Exception e){
      e.printStackTrace();
    }

//    response.setStatus(HttpServletResponse.SC_OK);
//    response.setContentType("text/html");
//    response.getWriter().println("stores ordering system requests arrived!");
//    try {
//      Thread.sleep(30000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//
////    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//    return;

  }

  // urlPath  = "/purchase/orderID/{orderID}"
  // urlParts = [,purchase,orderID,{orderID}]
  private String processGetURL(String[] urlParts) {
    if (urlParts.length != VALID_URL_LENGTH_FOR_GET || !urlParts[0].equals("") || !urlParts[ORDER_ID_URL_IDX].equals(ORDER_ID_URL_FLAG)){
      return EMPTY_STRING;
    }
    return urlParts[ORDER_ID_URL_IDX+1];
  }

  private JsonObject findOrderFromMongoDB(String orderId){
    MongoCollection<Document> collection = MongoDAO.getCollection();
    Bson filter = Filters.eq(ORDER_ID_FLAG, orderId);
    MongoCursor<Document> res = collection.find(filter).iterator();
    JsonArrayBuilder jsonArray = Json.createArrayBuilder();
    while(res.hasNext()){
      jsonArray.add(res.next().toJson());
    }
    JsonObject findResult = Json.createObjectBuilder().add("findResult", jsonArray).build();
    return findResult;
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/plain");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    String[] urlParts = urlPath.split(SLASH_SEPARATOR);
    if (!this.isUrlValidForPost(urlParts)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    // get the post body json
    String postBodyJSONString = request.getReader().lines().collect(Collectors.joining());
    try {
      String orderID = this.processBodyData(postBodyJSONString);
      response.setStatus(HttpServletResponse.SC_CREATED);
      response.getWriter().println(orderID);
    } catch (Exception e) {
      e.printStackTrace();
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }
  }

  private boolean isUrlValidForPost(String[] urlParts) {
    // urlPath  = "/purchase/{storeID}/customer/{custID}/date/{date}"
    // urlParts = [, purchase,{storeID},customer,{custID},date,{date}]
    if (urlParts.length != VALID_URL_LENGTH_FOR_POST) {
      return false;
    }
    if (!urlParts[0].equals("")) {
      return false;
    }
    if (!urlParts[PURCHASE_IDX].equals(PURCHASE) || !urlParts[CUSTOMER_IDX].equals(CUSTOMER)
        || !urlParts[DATE_IDX].equals(
        DATE)) {
      return false;
    }

    if (!urlParts[STORE_ID_IDX].matches(NUM_RE_PATTERN) || !urlParts[CUSTOMER_ID_IDX].matches(NUM_RE_PATTERN)) {
      return false;
    }

    if (!this.isValidDate(urlParts[DATE_CONTENT_IDX])) {
      return false;
    }
    return true;
  }

  private boolean isValidDate(String dateStr) {
    try {
      DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
      LocalDate.parse(dateStr, dateFormatter);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  private String processBodyData(String postBodyJSONString)
      throws Exception {
    //https://docs.oracle.com/javaee/7/api/javax/json/JsonObject.html

    List<OrderedItem> allOrderedItems = new ArrayList<>();
    JsonReader jsonReader = Json.createReader(new StringReader(postBodyJSONString));
    JsonObject postBodyJSON = jsonReader.readObject();

    String orderID = postBodyJSON.getString(ORDER_ID_FLAG);
    Integer customerID = postBodyJSON.getInt(CUSTOMER_ID_FLAG);
    Integer storeID = postBodyJSON.getInt(STORE_ID_FLAG);
    Date date = Date.valueOf(postBodyJSON.getString(DATE_FLAG));

    for (JsonValue jv : postBodyJSON.getJsonArray(ITEMS)) {
      JsonObject jo = (JsonObject) jv;
      int itemID = jo.getInt(ITEMS_ID);
      int numberOfItem = jo.getInt(NUM_OF_ITEMS);
      Double price = Double.valueOf(jo.get(PRICE_FLAG).toString());
      OrderedItem newItem = new OrderedItem(storeID, customerID, orderID, itemID, numberOfItem,
          price, date);
      allOrderedItems.add(newItem);
    }

//    this.addToRecords(allOrderedItems);
//    System.out.println("after adding to records stats==");
//    System.out.println("ItemToStore static = " + SoldItemsRecordsStats.getRecordsItemToStore());
//    System.out.println("StoreToItem static = " + SoldItemsRecordsStats.getRecordsStoreToItem());
//
//    System.out.println("ItemToStore  = " + recordsItemToStore);
//    System.out.println("StoreToItem  = " + recordsStoreToItem);

//    this.sendMessageToMQ(allOrderedItems);

      this.addToMongoDB(allOrderedItems);
      return orderID;
  }

  private void addOrdersToDB(List<OrderedItem> orderedItems) throws SQLException {
    StoresOrdersDAO dao = new StoresOrdersDAO();
    dao.addNewOrderedItem(orderedItems);
  }

  //  recordsStoreToItem key:StoreId, Value Map<ItemId, numSold>;
  //  recordsItemToStore key: ItemId, Value Map<StoreId, numSold>;
  private void addToRecords(List<OrderedItem> orderedItems){
    for(OrderedItem item: orderedItems){
      this.addToItemToStoreRecords(item);
      this.addToStoreToItemRecords(item);
    }
  }

  private void addToMongoDB(List<OrderedItem> orderedItems){
    List<Document> docs = new ArrayList<>();
    for(OrderedItem item: orderedItems){
      Document doc = new Document(ORDER_ID_FLAG, item.getOrderId())
          .append(CUSTOMER_ID_FLAG, item.getCustomerId())
          .append(STORE_ID_FLAG, item.getStoreId())
          .append(DATE_FLAG, item.getDate())
          .append(ITEMS_ID, item.getItemID())
          .append(NUM_OF_ITEMS, item.getNumOfItems())
          .append(PRICE_FLAG, item.getPrice());
      docs.add(doc);
    }
//    MongoCollection collection = client.getDatabase("storeOrders").getCollection("orders");
    collection.insertMany(docs, insertManyOptions);
  }

  private void addToStoreToItemRecords(OrderedItem item){
    if(recordsStoreToItem.get(item.getStoreId()) == null){
      ConcurrentHashMap<Integer,Integer> itemIdToFre = new ConcurrentHashMap<>(){{put(item.getItemID(),1);}};
      recordsStoreToItem.put(item.getStoreId(),itemIdToFre);
    }
    else{
        int fre = recordsStoreToItem.get(item.getStoreId()).getOrDefault(item.getItemID(),0);
        recordsStoreToItem.get(item.getStoreId()).put(item.getItemID(),fre+1);
    }
  }

  private void addToItemToStoreRecords(OrderedItem item){
    if(recordsItemToStore.get(item.getItemID()) == null){
      ConcurrentHashMap<Integer,Integer> storeIdToFre = new ConcurrentHashMap<>(){{put(item.getStoreId(),1);}};
      recordsItemToStore.put(item.getItemID(),storeIdToFre);
    }
    else{
      int fre = recordsItemToStore.get(item.getItemID()).getOrDefault(item.getStoreId(),0);
      recordsItemToStore.get(item.getItemID()).put(item.getStoreId(),fre+1);
    }
  }

  private static byte[] writeToByteArray(List<OrderedItem> allOrderedItems) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(allOrderedItems);
    return bos.toByteArray();
  }

  private void sendMessageToMQ(List<OrderedItem> allOrderedItems)
      throws Exception {

    Channel channel = pool.borrowObject();
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, writeToByteArray(allOrderedItems));
    pool.returnObject(channel);
  }

//  private GenericObjectPool createConnectionPool(int size){
//
//  }

}