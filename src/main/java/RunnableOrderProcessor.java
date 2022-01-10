import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class RunnableOrderProcessor implements Runnable {
  private static final String QUEUE_NAME = "store_orders";
  private static final int PRE_FETCH_COUNT=1;
  public int numMessageConsumed=0;

  private final ConnectionFactory factory;
  private Channel channel;
  private String consumerTagStr;
  public final int threadID;


  public RunnableOrderProcessor(int threadID) {
    this.threadID = threadID;
    this.factory = new ConnectionFactory();
    try {
      factory.setUri(CredentialConfig.getMqURI());
      factory.setUsername(CredentialConfig.getMqUsername());
      factory.setPassword(CredentialConfig.getMqPassword());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run(){
    try {
      channel = factory.newConnection().createChannel();
      //durable=true
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      //prefetch=1,sender won't dispatch a new message to a receiver until it has processed and acknowledged the previous one.
      channel.basicQos(PRE_FETCH_COUNT);
      DeliverCallback deliverCallback = (consumerTag, delivery) -> {
        List<OrderedItem> message = null;
        try {
          message = RunnableOrderProcessor.fromByteArray(delivery.getBody());
          addOrdersToDB(message);
          this.numMessageConsumed ++;
        } catch (ClassNotFoundException | SQLException e) {
          e.printStackTrace();
        }
        finally {
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
        System.out.println("Consumer thread " + this.threadID + " received a message (total: " + this.numMessageConsumed + ")");
      };

      //no auto ack
      consumerTagStr = channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });

    } catch (IOException | TimeoutException e ) {
      e.printStackTrace();
    }
//    finally {
//      if(consumerTagStr != null && channel != null){
//        try {
//          channel.basicCancel(consumerTagStr);
//        } catch (IOException e) {
//          e.printStackTrace();
//        }
//      }
//    }
  }

  private void addOrdersToDB(List<OrderedItem> orderedItems) throws SQLException {
    StoresOrdersDAO dao = new StoresOrdersDAO();
    dao.addNewOrderedItem(orderedItems);
  }

  private static List<OrderedItem> fromByteArray(byte[] serialized)
      throws IOException, ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
    ObjectInputStream ois = new ObjectInputStream(bis);
    List<OrderedItem> deSerialized = (List<OrderedItem>) ois.readObject();
    return deSerialized;
  }

}
