import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HttpClientsGet {
  private static final HttpClient httpClient = HttpClient.newBuilder()
      .version(HttpClient.Version.HTTP_2)
      .connectTimeout(Duration.ofSeconds(10))
      .build();
//  private static final String GET_URL = "http://localhost:8080/StoresOrderingSystem/purchase";
  private static final String GET_URL = "http://54.202.251.227:8080/StoresOrderingSystem/purchase";
  private final int clientID;

  public HttpClientsGet(int clientID) {
    this.clientID = clientID;
  }

  public void get()
      throws URISyntaxException, ExecutionException, InterruptedException, TimeoutException {
    HttpRequest request = HttpRequest.newBuilder()
        .GET()
        .uri(new URI(GET_URL))
        .setHeader("User-Agent", "Java 11 HttpClient Bot")
        .build();

    CompletableFuture<HttpResponse<String>> response =
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

    String result = response.thenApply(HttpResponse::body).get(30, TimeUnit.SECONDS);
    System.out.println("client " + this.clientID + " " + result);
  }

  public int getClientID() {
    return clientID;
  }
}
