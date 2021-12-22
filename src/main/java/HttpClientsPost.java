import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;


public class HttpClientsPost {

  private static final String ITEM_ID_FLAG = "ItemID";
  private static final String ITEMS_FLAG = "items";
  private static final String NUM_ITEMS_FLAG = "numberOfItems";
  private static final int NUM_ITEMS_UB = 4;
  private static final int NUM_ITEMS_LB = 1;
  private static final int CONNECTION_TIME_OUT = 10;
  private static final String URI_FORMAT = "http://%s/StoresOrderingSystem/purchase/%d/customer/%d/date/%s";
  private static final String DATE_FORMAT = "yyyyMMdd";
  private static final String HEADER_1 = "User-Agent";
  private static final String HEADER_1_VALUE = "Java 11 HttpClient Bot";
  private static final String HEADER_2 = "Content-Type";
  private static final String HEADER_2_VALUE = "application/json";
  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(CONNECTION_TIME_OUT))
      .build();
  private JsonObject allItems;
  private HttpRequest request;

  private TimeZone timeZone;
  private int clientId;
  private Map<OptionsFlags, Object> options;
  private int storeId;
  private int customerId;
  private String formattedDate;
  private HttpResponse<String> response;

  public HttpClientsPost(TimeZone timeZone,int clientId, Map<OptionsFlags, Object> options) {
    this.timeZone = timeZone;
    this.clientId = clientId;
    this.options = options;
    this.storeId = this.genStoreId();
    this.customerId = this.genCustomerID();
    this.formattedDate = this.genFormattedDate();
  }

  public void post() throws URISyntaxException, IOException, InterruptedException {
//    System.out.println("run here!");
    this.genAllItems();
    System.out.println("all items: = " + this.genAllItems().toString());
//    System.out.println("run here2!");
    this.request = this.genRequest();
//    System.out.println("run here3!");
    HttpResponse<String> response = HTTP_CLIENT.send(this.request, HttpResponse.BodyHandlers.ofString());
//    System.out.println("run here4!");
    this.response = response;
//    System.out.println("run here5!");
    // print status code
//    System.out.println(response.statusCode());

    // print response body
    System.out.println(response.body());
  }

  private int genStoreId() {
    return (int) (Math.random() * (int) this.options.get(OptionsFlags.maxNumStore)) + NUM_ITEMS_LB;
  }

  private int genCustomerID() {
    return (int) (Math.random() * (int) this.options.get(OptionsFlags.customersIdRange))
            + NUM_ITEMS_LB;
  }

  private String genFormattedDate() {
    LocalDate date = (LocalDate) this.options.get(OptionsFlags.date);
    DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
    String formattedDate = date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    return formattedDate;
  }


  private JsonObject genItem() {
    String itemID = String.valueOf(
        (int) (Math.random() * ((int) this.options.get(OptionsFlags.maxItemID))));
    int numItems = (int) (Math.random() * NUM_ITEMS_UB) + NUM_ITEMS_LB;
    JsonObject item = Json.createObjectBuilder().add(ITEM_ID_FLAG, itemID)
        .add(NUM_ITEMS_FLAG, numItems).build();
    return item;
  }

  private JsonObject genAllItems() {
    int totalItems =
        (int) (Math.random() * (int) this.options.get(OptionsFlags.numItemsEachPurchase))
            + NUM_ITEMS_LB;
    JsonArrayBuilder jsonArray = Json.createArrayBuilder();
    for (int i = 0; i < totalItems; i++) {
      jsonArray = jsonArray.add(this.genItem());
    }
    JsonObject allItems = Json.createObjectBuilder().add(ITEMS_FLAG, jsonArray).build();
    this.allItems = allItems;
    return allItems;
  }

  private HttpRequest genRequest() throws URISyntaxException {
    String ip = (String) this.options.get(OptionsFlags.ip);
    String uri = String.format(URI_FORMAT, ip, this.storeId, this.customerId, this.formattedDate);
    System.out.println("uri = " + uri);
    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(this.allItems.toString()))
        .uri(new URI(uri))
        .setHeader(HEADER_1, HEADER_1_VALUE) // add request header
        .header(HEADER_2, HEADER_2_VALUE)
        .build();

    return request;
  }

  public int getClientId() {
    return clientId;
  }

    public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException {
    JsonObject item1 = Json.createObjectBuilder().add("ItemID", "3847").add("numberOfItems", 2)
        .build();
    JsonObject item2 = Json.createObjectBuilder().add("ItemID", "4532").add("numberOfItems", 5)
        .build();
    JsonArrayBuilder jsonArray = Json.createArrayBuilder().add(item1).add(item2);
    JsonObject allItems = Json.createObjectBuilder().add("items", jsonArray).build();

    System.out.println(allItems.toString());
    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(allItems.toString()))
        .uri(new URI(
            "http://localhost:8080/StoresOrderingSystem/purchase/12345/customer/111222/date/20211212"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
        .header("Content-Type", "application/json")
        .build();

      HttpClient http_client = HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .connectTimeout(Duration.ofSeconds(CONNECTION_TIME_OUT))
          .build();
    HttpResponse<String> response = http_client.send(request, HttpResponse.BodyHandlers.ofString());

    // print status code
    System.out.println(response.statusCode());

    // print response body
    System.out.println(response.body());


  }


}
