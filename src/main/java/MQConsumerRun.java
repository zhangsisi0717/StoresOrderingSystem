public class MQConsumerRun {
  private static final int NUM_OF_CONSUMERS=5;

  public static void main(String[] args) {

    for (int i = 0; i < NUM_OF_CONSUMERS; i++) {
      Thread t = new Thread(new RunnableOrderProcessor(i));
      t.start();
    }
    Object monitor = new Object();
    while (true) {
      synchronized (monitor) {
        try {
          System.out.println("consumers start waiting for messages, Ctrl+C to interrupt...");
          monitor.wait();
        } catch (InterruptedException e) {
          System.exit(0);
        }
      }
    }
  }

}
