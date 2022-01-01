import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class testSender {
  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception{
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost"); //connect to a RabbitMQ on a local machine

    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {

      //Declaring a queue is idempotent - it will only be created if it doesn't exist already
      channel.queueDeclare(QUEUE_NAME, false, false, false, null); //declare a queue for us to send to then we can publish a message to the queue

      String message = "Hello World!";
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes()); //publish a message to the queue

      System.out.println(" [x] Sent '" + message + "'");


    }

  }




}
