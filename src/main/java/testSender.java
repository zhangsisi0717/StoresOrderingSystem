import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class testSender {
  private final static String QUEUE_NAME = "hello2";

  public static void main(String[] argv) throws Exception{

    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost"); //connect to a RabbitMQ on a local machine

    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {

      //Declaring a queue is idempotent - it will only be created if it doesn't exist already
      //https://rabbitmq.github.io/rabbitmq-java-client/api/4.x.x/com/rabbitmq/client/Channel.html#queueDeclare(java.lang.String,boolean,boolean,boolean,java.util.Map)

//      boolean durable = true;
      channel.queueDeclare(QUEUE_NAME, true, false, false, null); //declare a queue for us to send to then we can publish a message to the queue

      //basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
      String message = String.join(" ", argv);

      // if exchange = "", then just use the default exchange aka nameless exchange,
      // each queue is automatically bound to the default exchange with the name of queue as the binding key.
      channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8")); //publish a message to the queue

      System.out.println(" [x] Sent '" + message + "'");

    }

  }

}
