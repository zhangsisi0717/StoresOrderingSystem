import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class testPublisher {
  private static final String EXCHANGE_NAME = "store-orders";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUri("amqps://b-7fe29ba2-703e-48ef-9646-81c7fb24558b.mq.us-west-2.amazonaws.com:5671");
    factory.setUsername("sisizhang");
    factory.setPassword("19920903Xyc!");
    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {

      //declare a fanout exchange named "logs"
      channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//      String message = argv.length < 1 ? "info: Hello World!" :
//          String.join(" ", argv);
      String message = "info: Hello World!";

      //routing key == "", which means exchange sends message to all queues
      channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
      System.out.println(" [x] Sent '" + message + "'");
    }
  }


}
