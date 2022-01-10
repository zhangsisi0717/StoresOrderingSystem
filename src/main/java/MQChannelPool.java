import com.rabbitmq.client.Channel;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class MQChannelPool {
  private static GenericObjectPool<Channel> channelPool;
  private static final int MIN_IDLE=2;
  private static final int MAX_IDLE=20;
  private static final int MAX_TOTAL=400;

  static {
    try {
      GenericObjectPoolConfig<Channel> config = new GenericObjectPoolConfig<>();
      config.setMinIdle(MIN_IDLE);
      config.setMaxIdle(MAX_IDLE);
      config.setMaxTotal(MAX_TOTAL);
      channelPool = new GenericObjectPool<>(new MQPooledChannelFactory(), config);
    } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
      e.printStackTrace();
    }
  }

  public static GenericObjectPool<Channel> getPool(){
    return channelPool;
  }

}
