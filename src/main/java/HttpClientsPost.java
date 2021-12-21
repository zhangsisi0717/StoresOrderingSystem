import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;


public class HttpClientsPost {

  private static final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(10))
      .build();

  public static void main(String[] args)
      throws URISyntaxException, IOException, InterruptedException {
    JsonObject item1 = Json.createObjectBuilder().add("ItemID", "3847").add("numberOfItems", 2).build();
    JsonObject item2 = Json.createObjectBuilder().add("ItemID", "4532").add("numberOfItems", 5).build();
    JsonArrayBuilder jsonArray = Json.createArrayBuilder().add(item1).add(item2);
    JsonObject allItems = Json.createObjectBuilder().add("items",jsonArray).build();


    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(allItems.toString()))
        .uri(new URI("http://localhost:8080/StoresOrderingSystem/purchase/12345/customer/111222/date/20211212"))
        .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
        .header("Content-Type", "application/json")
        .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // print status code
    System.out.println(response.statusCode());

    // print response body
    System.out.println(response.body());


  }

}
