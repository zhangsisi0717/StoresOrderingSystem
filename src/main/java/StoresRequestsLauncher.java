import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;


//i + 1, TimeZone.WEST, completed, options,
//          startTimeStampWest, REQUEST_DURATION,requestStats,queue
public class StoresRequestsLauncher {

  public static void launchMultipleStoresPost(int startIdx, int endIdx, TimeZone timezone, CountDownLatch completed,Map<OptionsFlags, Object> options, LocalDateTime startTimeStamp, int requestDuration, RequestStats requestStats, BlockingQueue queue){
    for (int k = startIdx; k < endIdx; k++) {
      Runnable thread = new RunnableClientPost(k + 1, timezone, completed, options,
          startTimeStamp,  requestDuration,requestStats,queue);
      new Thread(thread).start();
    }
  }

  public static void launchMultipleStoresGet(int startIdx, int endIdx, TimeZone timezone, CountDownLatch completed,Map<OptionsFlags, Object> options, LocalDateTime startTimeStamp, int requestDuration, RequestStats requestStats, BlockingQueue queue,
      List<String> orderIds){
    for (int k = startIdx; k < endIdx; k++) {
      Runnable thread = new RunnableClientGet(k + 1, timezone, completed, options,
          startTimeStamp,  requestDuration,requestStats,queue,orderIds);
      new Thread(thread).start();
    }
  }

}
