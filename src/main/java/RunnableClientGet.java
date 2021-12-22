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

public class RunnableClientGet implements Runnable {

  private int id;
  private CountDownLatch completed;
  public RunnableClientGet(int id,  CountDownLatch completed) {
    this.id = id;
    this.completed = completed;
  }

  @Override
  public void run() {
    System.out.println("thread = " + this.id + " had been created!");
    HttpClientsGet client = new HttpClientsGet(this.id);
    try{
      client.get();
    }catch (Exception e){
//      System.out.println("error!");
      System.out.println("client " + client.getClientID() + " " + e);
    }
    this.completed.countDown();
  }
}
