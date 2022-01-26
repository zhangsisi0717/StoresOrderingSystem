import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class RunnableClientGet implements Runnable {

  private int id;
  private TimeZone timeZone;
  private CountDownLatch completed;
  private Map<OptionsFlags, Object> options;
  private LocalDateTime startTimeStamp;
  private final int timeDiffMin;
  private RequestStats requestStats;
  private BlockingQueue queue;
  private static int HOUR_TO_MS=1000*60*60;
  private int sleepTime;
  private List<String> orderIds;
//  private static final String NUM_PURCHASE_PER_HOUR_FLAG = "numPurchasesPerHour";

  public RunnableClientGet(int id, TimeZone timeZone, CountDownLatch completed,
      Map<OptionsFlags, Object> options, LocalDateTime startTimeStamp, int timeDiffMin,
      RequestStats requestStats, BlockingQueue queue, List<String> orderIds) {
    this.id = id;
    this.timeZone = timeZone;
    this.completed = completed;
    this.options = options;
    this.startTimeStamp = startTimeStamp;
    this.timeDiffMin = timeDiffMin;
    this.requestStats = requestStats;
    this.queue = queue;
    this.sleepTime = this.genSleepTime();
    this.orderIds = orderIds;

  }

//  @Override
//  public void run() {
//    System.out.println("thread = " + this.id + " had been created!");
////    HttpClientsGet client = new HttpClientsGet(this.id);
//    try{
////      client.get();
//    }catch (Exception e){
////      System.out.println("error!");
////      System.out.println("client " + client.getClientID() + " " + e);
//    }
//    this.completed.countDown();
//  }

  @Override
  public void run() {
    System.out.println("thread = " + this.id + " TimeZone = " + this.timeZone.toString() + " had been created!");
    HttpClientsGet client = new HttpClientsGet(this.timeZone, this.id, this.options,this.requestStats,this.queue,this.orderIds);
    System.out.println("thread = " + this.id + "HttpClientsGet has been created");
    while (true) {
      if (Duration.between(this.startTimeStamp, LocalDateTime.now()).toMinutes() >= timeDiffMin) {
        break;
      }
      try {
        client.get();
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
