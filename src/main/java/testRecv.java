import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import javax.imageio.IIOException;

public class testRecv {
  //https://www.rabbitmq.com/tutorials/tutorial-one-java.html
  private final static String QUEUE_NAME = "hello";
  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    boolean durable = true;
    channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

    int preFetchCount = 1; // means sender wont dispatch a new message to a receiver until it has processed and acknowledged the previous one.
    channel.basicQos(preFetchCount);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received '" + message + "'");

      try{
        testRecv.doWork(message);
      }catch (InterruptedException e){}finally {
        System.out.println(" [x] Done");
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      }

    };

    boolean autoAck = false; // acknowledgment is covered below

    //String queue, boolean autoAck,
    channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, consumerTag -> { });

  }

  private static void doWork(String task) throws InterruptedException {
    for (char ch: task.toCharArray()) {
      if (ch == '.') Thread.sleep(1000);
    }
  }


}
