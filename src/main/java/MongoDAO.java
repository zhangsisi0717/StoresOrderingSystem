import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MongoDAO {

  private static final String CONFIG_FILE = "/etc/store_proj_cred.properties";
  private static String MONGODB_ENDPOINT;
  private static String MONGODB_USERNAME;
  private static String MONGODB_PASSWORD;
  private static String MONGODB_TRUSTSTORE;
  private static String MONGODB_TRUSTSTORE_PASSWORD;
  private static String MONGODB_DATABASE;
  private static String MONGODB_COLLECTION;

  private static String connectionString;
  private static MongoClient client;
  private static MongoCollection collection;

  static {

    try (InputStream input = new FileInputStream(CONFIG_FILE);){
      Properties prop = new Properties();
      prop.load(input);
      MONGODB_ENDPOINT = prop.getProperty("MONGODB_ENDPOINT");
      MONGODB_USERNAME = prop.getProperty("MONGODB_USERNAME");
      MONGODB_PASSWORD = prop.getProperty("MONGODB_PASSWORD");
      MONGODB_TRUSTSTORE = prop.getProperty("MONGODB_TRUSTSTORE");
      MONGODB_TRUSTSTORE_PASSWORD = prop.getProperty("MONGODB_TRUSTSTORE_PASSWORD");
      MONGODB_DATABASE = prop.getProperty("MONGODB_DATABASE");
      MONGODB_COLLECTION = prop.getProperty("MONGODB_COLLECTION");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    String template = "mongodb://%s:%s@%s/?tls=true&tlsAllowInvalidHostnames=true&readpreference=%s&maxPoolSize=100";
    String username = MONGODB_USERNAME;
    String password = MONGODB_PASSWORD;

    // use localhost:27017 when running Java locally;
    // must set up a ssh tunnel
    // run the following command (this command doesn't exit, so use screen to keep it running) before running this java program:
    //
    // ssh -i ~/.ssh/aws.pem -L 27017:stores-orders-docdb.cluster-cu1txvgpigr3.us-west-2.docdb.amazonaws.com:27017 ec2-user@ec2-54-188-144-96.us-west-2.compute.amazonaws.com -N
    String clusterEndpoint = MONGODB_ENDPOINT;

    String readPreference = "secondaryPreferred";
    String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

    String truststore = MONGODB_TRUSTSTORE;
    String truststorePassword = MONGODB_TRUSTSTORE_PASSWORD;

    System.setProperty("javax.net.ssl.trustStore", truststore);
    System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

    System.out.println("connection string is: "+ connectionString);
    client = MongoClients.create(connectionString);
    collection = client.getDatabase(MONGODB_DATABASE).getCollection(MONGODB_COLLECTION);
    System.out.println("connection to AWS DocumentDB established.");
    System.out.println("using database: " + MONGODB_DATABASE + ", collection: " + MONGODB_COLLECTION);
  }

  public static MongoClient getClient() {
    return client;
  }

  public static MongoCollection getCollection() {return collection;}

}
