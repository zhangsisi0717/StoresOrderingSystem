public class testMQ {

  public static void main(String[] args) {

    for (int i = 0; i < 5; i++) {
      Thread t = new Thread(new OrderProcessor(i));
      t.start();
    }
    Object monitor = new Object();
    while (true) {
      synchronized (monitor) {
        try {
          System.out.println("main thread waiting for interruption ...");
          monitor.wait();
        } catch (InterruptedException e) {
          System.exit(0);
        }
      }
    }
  }

}
