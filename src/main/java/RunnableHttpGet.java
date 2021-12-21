import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.servlet.http.HttpServletResponse;

public class RunnableHttpGet implements Runnable {

  private int id;
  private CountDownLatch completed;
  public RunnableHttpGet(int id,  CountDownLatch completed) {
    this.id = id;
    this.completed = completed;
  }

  @Override
  synchronized public void run() {
//    System.out.println("thread = " + this.id + " had been created!");
    try{
      HttpClientsGet client = new HttpClientsGet(this.id);
      client.get();
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
    this.completed.countDown();
  }
}
