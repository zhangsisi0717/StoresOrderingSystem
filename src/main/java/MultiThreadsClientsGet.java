import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.cli.ParseException;

public class MultiThreadsClientsGet {

  private static final String ORDER_ID_FLAG = "orderId";
  private static final String CLIENT_ID_FLAG="clientId";
  private static final String TIME_ZONE_FLAG="timeZone";
  private static final String STATUS_CODE_FLAG="statusCode";
  private static final String LATENCY_FLAG="latency";
  private static final String WRITE_OUT_PATH= "output" + File.separator + "get_results.csv";
  private static final String READ_PATH= "output" + File.separator + "result.csv";

  private static int TIME_ZONE_DIFF = 1;
  private static int REQUEST_DURATION = 3;

  private static final String[] HEADERS = {CLIENT_ID_FLAG,TIME_ZONE_FLAG,STATUS_CODE_FLAG,LATENCY_FLAG};
  private static BlockingQueue queue = new LinkedBlockingQueue();
  private static final int numTimeZones = 3;

  public static void main(String[] args)
      throws InterruptedException, FileNotFoundException, InvalidArgumentException, ParseException {
    ResultFileParser fileParser = new ResultFileParser();
    fileParser.processCSVResultFile(READ_PATH);

    StoresSimulationCMLParser cmdParser = new StoresSimulationCMLParser();
    Map<OptionsFlags, Object> options = cmdParser.parse(args);

    int numOfStores = (int) options.get(OptionsFlags.maxNumStore);
    int numStoresWest = numOfStores / numTimeZones;
    int numStoresCentral = numOfStores / numTimeZones;

    queue.add(HEADERS);
    RequestStats requestStats = new RequestStats();
    CountDownLatch completed = new CountDownLatch(numOfStores);


    // create file writer thread
    RunnableFileWriter fileWriterRunnable = new RunnableFileWriter(queue,WRITE_OUT_PATH);
    Thread fileWriterThread = new Thread(fileWriterRunnable);
    fileWriterThread.start();

    //launch stores in west
    LocalDateTime startTimeStampWest = LocalDateTime.now();
    StoresRequestsLauncher.launchMultipleStoresGet(0,numStoresWest, TimeZone.WEST, completed, options,
          startTimeStampWest, REQUEST_DURATION,requestStats,queue,fileParser.getResult().get(ORDER_ID_FLAG));

    while (true) {
      if (Duration.between(startTimeStampWest, LocalDateTime.now()).toMinutes() >= TIME_ZONE_DIFF) {
        break;
      }
    }

    //launch stores in central
    LocalDateTime startTimeStampCentral = LocalDateTime.now();
    StoresRequestsLauncher.launchMultipleStoresGet(numStoresWest,numStoresWest+numStoresCentral, TimeZone.CENTRAL, completed, options,
          startTimeStampCentral, REQUEST_DURATION,requestStats,queue,fileParser.getResult().get(ORDER_ID_FLAG));


    while (true) {
      if (Duration.between(startTimeStampCentral, LocalDateTime.now()).toMinutes()
          >= TIME_ZONE_DIFF) {
        break;
      }
    }

    //launch stores in east
    LocalDateTime startTimeStampEast = LocalDateTime.now();
    StoresRequestsLauncher.launchMultipleStoresGet(numStoresWest+numStoresCentral,numOfStores, TimeZone.EAST, completed, options,
          startTimeStampEast, REQUEST_DURATION,requestStats,queue,fileParser.getResult().get(ORDER_ID_FLAG));


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

    System.out.println("99 percentile response time = " + requestStats.get99PResponseTime() + " ms");
    System.out.println("95 percentile response time = " + requestStats.get99PResponseTime() + " ms");
  }



}
