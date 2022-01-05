import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.cli.ParseException;

public class MultiThreadsClientsPost {

  private static int TIME_ZONE_DIFF = 1;
  private static int REQUEST_DURATION = 2;

  private static final String[] HEADERS = {"clientId", "timeZone", "statusCode", "startTime","latency"};
  private static BlockingQueue queue = new LinkedBlockingQueue();
  private static final int numTimeZones = 3;

  public static void main(String[] args)
      throws InvalidArgumentException, ParseException, InterruptedException {
    //{clientId, timeZone, status_code, timeStampBeforeRequest,latency}//

    String writeOutPath = "output" + File.separator + "result.csv";
    StoresSimulationCMLParser parser = new StoresSimulationCMLParser();
    Map<OptionsFlags, Object> options = parser.parse(args);

    int numOfStores = (int) options.get(OptionsFlags.maxNumStore);
    int numStoresWest = numOfStores / numTimeZones;
    int numStoresCentral = numOfStores / numTimeZones;

    queue.add(HEADERS);
    RequestStats requestStats = new RequestStats();
    CountDownLatch completed = new CountDownLatch(numOfStores);
    LocalDateTime startTimeStampWest = LocalDateTime.now();
    // create file writer thread
    RunnableFileWriter fileWriterRunnable = new RunnableFileWriter(queue,writeOutPath);
    Thread fileWriterThread = new Thread(fileWriterRunnable);
    fileWriterThread.start();

    //launch stores in west
    for (int i = 0; i < numStoresWest; i++) {
      Runnable thread = new RunnableClientPost(i + 1, TimeZone.WEST, completed, options,
          startTimeStampWest, REQUEST_DURATION,requestStats,queue);
      new Thread(thread).start();
    }
    ;

    while (true) {
      if (Duration.between(startTimeStampWest, LocalDateTime.now()).toMinutes() >= TIME_ZONE_DIFF) {
        break;
      }
    }
    //launch stores in central
    LocalDateTime startTimeStampCentral = LocalDateTime.now();
    for (int j = numStoresWest; j < numStoresWest + numStoresCentral; j++) {
      Runnable thread = new RunnableClientPost(j + 1, TimeZone.CENTRAL, completed, options,
          startTimeStampCentral, REQUEST_DURATION,requestStats,queue);
      new Thread(thread).start();
    }
    ;

    while (true) {
      if (Duration.between(startTimeStampCentral, LocalDateTime.now()).toMinutes()
          >= TIME_ZONE_DIFF) {
        break;
      }
    }

    //launch stores in east
    LocalDateTime startTimeStampEast = LocalDateTime.now();
    for (int k = numStoresWest + numStoresCentral; k < numOfStores; k++) {
      Runnable thread = new RunnableClientPost(k + 1, TimeZone.EAST, completed, options,
          startTimeStampEast, REQUEST_DURATION,requestStats,queue);
      new Thread(thread).start();
    }
    ;

    completed.await();
    fileWriterRunnable.requestStop();
    fileWriterThread.join();

    LocalDateTime endTimeStamp = LocalDateTime.now();
    Long totalTimeInMin = Duration.between(startTimeStampWest, endTimeStamp).toMinutes();
    Long totalTimeInSeconds = Duration.between(startTimeStampWest, endTimeStamp).toSeconds();
    System.out.println("total run time = " + totalTimeInMin + " mins");

    System.out.println(requestStats);
    System.out.println("throughput = " + requestStats.getNumSuccessfulRequest()/totalTimeInSeconds + " per second");

    System.out.println(requestStats);
    int numRequest = requestStats.getLatencyList().size();
//    System.out.println("throughput= " + requestStats.getNumSuccessfulRequest()/totalTimeInSeconds*1000 + " ms");
    System.out.println("mean response time: " + requestStats.getCumulativeLatencySum()/numRequest + " ms");
    Collections.sort(requestStats.getLatencyList());

    System.out.println("99 percentile response time = " + requestStats.getLatencyList().get((int)(numRequest*0.99)) + " ms");
    System.out.println("95 percentile response time = " + requestStats.getLatencyList().get((int)(numRequest*0.95)) + " ms");


  }

}

