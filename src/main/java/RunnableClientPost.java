import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class RunnableClientPost implements Runnable {

  private int id;
  private TimeZone timeZone;
  private CountDownLatch completed;
  private Map<OptionsFlags, Object> options;
  private LocalDateTime startTimeStamp;
  private final int timeDiffMin;
  private RequestStats requestCounter;
  private BlockingQueue queue;
  private static final int SLEEP_TIME_MS = 30000;


  public RunnableClientPost(int id, TimeZone timeZone, CountDownLatch completed,
      Map<OptionsFlags, Object> options, LocalDateTime startTimeStamp, int timeDiffMin,
      RequestStats requestCounter, BlockingQueue queue) {
    this.id = id;
    this.timeZone = timeZone;
    this.completed = completed;
    this.options = options;
    this.startTimeStamp = startTimeStamp;
    this.timeDiffMin = timeDiffMin;
    this.requestCounter = requestCounter;
    this.queue = queue;

  }

  @Override
  public void run() {
    System.out.println("thread = " + this.id + " TimeZone = " + this.timeZone.toString() + " had been created!");
    HttpClientsPost client = new HttpClientsPost(this.timeZone, this.id, this.options,this.requestCounter,this.queue);
    while (true) {
      if (Duration.between(this.startTimeStamp, LocalDateTime.now()).toMinutes() >= timeDiffMin) {
        break;
      }
      try {
        client.post();
      } catch (Exception e) {
        System.out.println("client " + client.getClientId() + " " + e);
      }
      try {
        Thread.sleep(SLEEP_TIME_MS);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(this.id + " client stops");
    this.completed.countDown();
  }

}
