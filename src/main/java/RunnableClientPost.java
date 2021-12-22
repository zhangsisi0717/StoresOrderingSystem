import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class RunnableClientPost implements Runnable{
  private int id;
  private TimeZone timeZone;
  private CountDownLatch completed;
  private Map<OptionsFlags, Object> options;

  public RunnableClientPost(int id, TimeZone timeZone, CountDownLatch completed,
      Map<OptionsFlags, Object> options) {
    this.id = id;
    this.timeZone = timeZone;
    this.completed = completed;
    this.options = options;
  }

  @Override
  public void run() {
    System.out.println("thread = " + this.id + " had been created!");
    HttpClientsPost client = new HttpClientsPost(this.timeZone,this.id,this.options);
    try{
      client.post();
    }catch (Exception e){
//      System.out.println("error!");
      System.out.println("client " + client.getClientId() + " " + e);
    }
    this.completed.countDown();
  }
}
