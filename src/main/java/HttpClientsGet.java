import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.json.JsonObject;

public class HttpClientsGet {

  private HttpRequest request;
  private TimeZone timeZone;
  private int clientId;
  private Map<OptionsFlags, Object> options;
  private HttpResponse<String> response;
  private RequestStats requestStats;
  private LocalDateTime timeStampBeforeRequest;
  private LocalDateTime timeStampAfterRequest;
  private BlockingQueue queue;
  private List<String> orderIds;
  private static final int OK = 200;
  private static final String URI_FORMAT = "http://%s/StoresOrderingSystem/purchase/orderID/%s";
  private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofSeconds(10)).build();


  public HttpClientsGet(TimeZone timeZone,int clientId, Map<OptionsFlags, Object> options,
      RequestStats requestStats, BlockingQueue queue, List orderIds) {
    this.timeZone = timeZone;
    this.clientId = clientId;
    this.options = options;
    this.requestStats = requestStats;
    this.queue = queue;
    this.orderIds = orderIds;
  }

  //reference: https://mkyong.com/java/java-11-httpclient-examples/
  public void get()
      throws URISyntaxException, ExecutionException, InterruptedException, TimeoutException, IOException {
    this.requestStats.incAttemptedRequest();
    this.timeStampBeforeRequest = LocalDateTime.now();
    this.request = this.genRequest();
    HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    this.timeStampAfterRequest = LocalDateTime.now();
    if(response.statusCode() == OK){
      this.requestStats.incSuccess();

      //{clientId, timeZone, status_code, timeStampBeforeRequest,latency}
      Long latencyMillis = Duration.between(this.timeStampBeforeRequest,this.timeStampAfterRequest).toMillis();
      String latency = String.valueOf(latencyMillis);
      String[] singleRequestResult = {String.valueOf(this.clientId),this.timeZone.toString(),String.valueOf(response.statusCode()),latency};
      this.queue.add(singleRequestResult);
      this.requestStats.addToLatencyList(latencyMillis);
    }
    this.response = response;
    System.out.println("client " + this.clientId + ", timeZone: " + this.timeZone.toString() +": "+" status code: " + response.statusCode());

  }

  private HttpRequest genRequest() throws URISyntaxException {
    System.out.println("genRequest called");
    String ip = (String) this.options.get(OptionsFlags.ip);
    String uri = String.format(URI_FORMAT, ip, this.genOrderId());
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(new URI(uri))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .build();
    return request;
  }

  private String genOrderId(){
    int index = (int)(Math.random()*this.orderIds.size());
    return this.orderIds.get(index);
  }

  public TimeZone getTimeZone() {
    return timeZone;
  }

  public int getClientId() {
    return clientId;
  }

  public HttpResponse<String> getResponse() {
    return response;
  }

  public RequestStats getRequestStats() {
    return requestStats;
  }
}
