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
  private static int HOUR_TO_MS=1000*60*60;
  private int sleepTime;
//  private static final String NUM_PURCHASE_PER_HOUR_FLAG = "numPurchasesPerHour";


  public RunnableClientPost(int id, TimeZone timeZone, CountDownLatch completed,
      Map<OptionsFlags, Object> options, LocalDateTime startTimeStamp, int timeDiffMin,
      RequestStats requestStats, BlockingQueue queue) {
    this.id = id;
    this.timeZone = timeZone;
    this.completed = completed;
    this.options = options;
    this.startTimeStamp = startTimeStamp;
    this.timeDiffMin = timeDiffMin;
    this.requestCounter = requestStats;
    this.queue = queue;
    this.sleepTime = this.genSleepTime();

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
        Thread.sleep(this.sleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    System.out.println(this.id + " client stops");
    this.completed.countDown();
  }

  private int genSleepTime(){
    return HOUR_TO_MS / Integer.valueOf(this.options.get(OptionsFlags.numPurchasesPerHour).toString());
  }

}
