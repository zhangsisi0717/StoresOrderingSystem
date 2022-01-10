import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MQPooledChannelFactory extends BasePooledObjectFactory<Channel> {

  private final com.rabbitmq.client.ConnectionFactory factory;

  public MQPooledChannelFactory()
      throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
      factory = new ConnectionFactory();
      factory.setUri(CredentialConfig.getMqURI());
      factory.setUsername(CredentialConfig.getMqUsername());
      factory.setPassword(CredentialConfig.getMqPassword());
  }

  @Override
  public Channel create() throws Exception {
    return factory.newConnection().createChannel();
  }

  @Override
  public PooledObject<Channel> wrap(Channel channel) {
    return new DefaultPooledObject<>(channel);
  }

  @Override
  public void destroyObject(PooledObject<Channel> pooledObjectChannel) throws Exception {
    try {
      Channel channel = pooledObjectChannel.getObject();
      Connection connection = channel.getConnection();
      channel.close();
      connection.close();
    }
    catch (IOException | TimeoutException e){
      e.printStackTrace();
    }
    finally {
      super.destroyObject(pooledObjectChannel);
    }
  }

}
