import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.cli.ParseException;

public class MultiThreadsClientsPost {

  private static int TIME_ZONE_DIFF = 2;
  private static int REQUEST_DURATION = 7;

  public static void main(String[] args)
      throws InvalidArgumentException, ParseException, InterruptedException {
    StoresSimulationCMLParser parser = new StoresSimulationCMLParser();
    Map<OptionsFlags, Object> options = parser.parse(args);

    int numOfStores = (int) options.get(OptionsFlags.maxNumStore);

    int numStoresWest = numOfStores / 3;
    int numStoresCentral = numOfStores / 3;

    RequestCounter requestCounter = new RequestCounter();
    CountDownLatch completed = new CountDownLatch(numOfStores);
    LocalDateTime startTimeStampWest = LocalDateTime.now();

    //launch stores in west
    for (int i = 0; i < numStoresWest; i++) {
      Runnable thread = new RunnableClientPost(i + 1, TimeZone.WEST, completed, options,
          startTimeStampWest, REQUEST_DURATION,requestCounter);
      new Thread(thread).start();
    }
    ;

    while (true) {
      if (Duration.between(startTimeStampWest, LocalDateTime.now()).toMinutes() >= TIME_ZONE_DIFF) {
        break;
      }
    }
    //launch stores in west
    LocalDateTime startTimeStampCentral = LocalDateTime.now();
    for (int j = numStoresWest; j < numStoresWest + numStoresCentral; j++) {
      Runnable thread = new RunnableClientPost(j + 1, TimeZone.CENTRAL, completed, options,
          startTimeStampCentral, REQUEST_DURATION,requestCounter);
      new Thread(thread).start();
    }
    ;

    while (true) {
      if (Duration.between(startTimeStampCentral, LocalDateTime.now()).toMinutes()
          >= TIME_ZONE_DIFF) {
        break;
      }
    }
    LocalDateTime startTimeStampEast = LocalDateTime.now();
    for (int k = numStoresWest + numStoresCentral; k < numOfStores; k++) {
      Runnable thread = new RunnableClientPost(k + 1, TimeZone.EAST, completed, options,
          startTimeStampEast, REQUEST_DURATION,requestCounter);
      new Thread(thread).start();
    }
    ;

    completed.await();
    LocalDateTime endTimeStamp = LocalDateTime.now();
    Long totalTimeInMin = Duration.between(startTimeStampWest, endTimeStamp).toMinutes();
    Long totalTimeInSeconds = Duration.between(startTimeStampWest, endTimeStamp).toSeconds();
    System.out.println(
        "total run time = " + totalTimeInMin + " mins");

    System.out.println(requestCounter);
    System.out.println("throughput= " + requestCounter.getNumSuccessfulRequest()/totalTimeInSeconds);
  }

}

