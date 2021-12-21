import java.util.concurrent.CountDownLatch;

public class HttpMultiThreadClientsGet {
  final static private int NUM_OF_THREADS = 100;

  public static void main(String[] args) throws InterruptedException {
    CountDownLatch completed = new CountDownLatch(NUM_OF_THREADS);
    long timeBefore = System.currentTimeMillis();
    for (int i = 0; i < NUM_OF_THREADS; i++) {
      // lambda runnable creation - interface only has a single method so lambda works fine
      Runnable thread =  new RunnableHttpGet(i,completed);
      new Thread(thread).start();
      };
    completed.await();
    long timeAfter = System.currentTimeMillis();
    System.out.println("finish " + NUM_OF_THREADS + " requests!");
    System.out.println("timeBefore= " + timeBefore);
    System.out.println("timeAfter= " + timeAfter);
    System.out.println();
  }

}
