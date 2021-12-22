import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.ParseException;

public class MultiThreadsClientsPost {
//  private static final int NUM_OF_THREADS=100;

  //int id, TimeZone timeZone, CountDownLatch completed,
  //      Map<OptionsFlags, Object> options

  public static void main(String[] args)
      throws InvalidArgumentException, ParseException, InterruptedException {
    StoresSimulationCMLParser parser = new StoresSimulationCMLParser();
    Map<OptionsFlags,Object> options = parser.parse(args);

    //maxNumStore,date,ip,customersIdRange,maxItemID,numPurchasesPerHour,numItemsEachPurchase
//    System.out.println(options.get(OptionsFlags.maxNumStore));
//    System.out.println(options.get(OptionsFlags.date));
//    System.out.println(options.get(OptionsFlags.ip));
//    System.out.println(options.get(OptionsFlags.customersIdRange));
//    System.out.println(options.get(OptionsFlags.maxItemID));
//    System.out.println(options.get(OptionsFlags.numPurchasesPerHour));
//    System.out.println(options.get(OptionsFlags.numItemsEachPurchase));

    int numOfStores = (int)options.get(OptionsFlags.maxNumStore);
    CountDownLatch completed = new CountDownLatch(numOfStores);

    long timeBefore = System.currentTimeMillis();
    for (int i = 0; i < numOfStores; i++) {
      // lambda runnable creation - interface only has a single method so lambda works fine
      Runnable thread =  new RunnableClientPost(i+1,TimeZone.WEST,completed,options);
      new Thread(thread).start();
    };
    completed.await();
    long timeAfter = System.currentTimeMillis();
    System.out.println("finish " + numOfStores + " requests!");
    System.out.println("timeBefore= " + timeBefore);
    System.out.println("timeAfter= " + timeAfter);
    System.out.println("total requesting time = "+ TimeUnit.MILLISECONDS.toSeconds(timeAfter-timeBefore) + " s");



  }


}
