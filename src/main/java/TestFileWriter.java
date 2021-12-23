import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.cli.ParseException;

public class TestFileWriter {
  private static int REQUEST_DURATION = 1;

  private static final String[] HEADERS = {"clientId", "timeZone", "statusCode", "startTime","latency"};
  private static BlockingQueue queue = new LinkedBlockingQueue();
  public static void main(String[] args)
      throws InvalidArgumentException, ParseException, InterruptedException {
    String writeOutPath = "output" + File.separator + "result.csv";
    StoresSimulationCMLParser parser = new StoresSimulationCMLParser();
    Map<OptionsFlags, Object> options = parser.parse(args);

    int numOfStores = (int) options.get(OptionsFlags.maxNumStore);

    RequestCounter requestCounter = new RequestCounter();
    CountDownLatch completed = new CountDownLatch(numOfStores);
    LocalDateTime startTimeStampWest = LocalDateTime.now();
    // create file writer thread

    queue.add(HEADERS);
    RunnableFileWriter fileWriterRunnable = new RunnableFileWriter(queue,writeOutPath);
    Thread fileWriterThread = new Thread(fileWriterRunnable);
    fileWriterThread.start();

    //launch stores in west
    for (int i = 0; i < numOfStores; i++) {
      Runnable thread = new RunnableClientPost(i + 1, TimeZone.WEST, completed, options,
          startTimeStampWest, REQUEST_DURATION,requestCounter,queue);
      new Thread(thread).start();
    }

    completed.await();

    System.out.println("all thread stopped");

    fileWriterRunnable.requestStop();


    fileWriterThread.join();

    LocalDateTime endTimeStamp = LocalDateTime.now();
    Long totalTimeInMin = Duration.between(startTimeStampWest, endTimeStamp).toMinutes();
    Long totalTimeInSeconds = Duration.between(startTimeStampWest, endTimeStamp).toSeconds();
    System.out.println(
        "total run time = " + totalTimeInMin + " mins");

    System.out.println(requestCounter);
    System.out.println("throughput= " + requestCounter.getNumSuccessfulRequest()/totalTimeInSeconds);
  }
}
