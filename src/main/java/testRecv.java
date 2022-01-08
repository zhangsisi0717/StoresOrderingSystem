import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import javax.imageio.IIOException;

public class testRecv {
  //https://www.rabbitmq.com/tutorials/tutorial-one-java.html
  private static final String EXCHANGE_NAME = "logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("amqps://b-154fe6f0-03ec-44e1-8632-6e2706e3e7da.mq.us-west-2.amazonaws.com:5671");
    factory.setUsername("sisizhang");
    factory.setPassword("1234567890!@");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

//    boolean durable = true;
    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    int preFetchCount = 1; // means sender wont dispatch a new message to a receiver until it has processed and acknowledged the previous one.
    channel.basicQos(preFetchCount);

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received '" + message + "'");

      try{
        testRecv.doWork(message);
      } finally {
        System.out.println(" [x] Done");
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      }

    };

//    boolean autoAck = false; // acknowledgment is covered below

    //String queue, boolean autoAck,
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

  }

//  private static void doWork(String task) throws InterruptedException {
//    for (char ch: task.toCharArray()) {
//      if (ch == '.') Thread.sleep(1000);
//    }
//  }

  private static void doWork(String task) {
    for (char ch : task.toCharArray()) {
      if (ch == '.') {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException _ignored) {
          Thread.currentThread().interrupt();
        }
      }
    }
  }

}
