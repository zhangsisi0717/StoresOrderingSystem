import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.dbcp2.*;

public class CredentialConfig {

  private static BasicDataSource dataSource;

  private static final String CONFIG_FILE = "/etc/store_proj_cred.properties";
  // NEVER store sensitive information below in plain text!
  private static String HOST_NAME;
  private static String PORT;
  private static String DATABASE;
  private static String USERNAME;
  private static String PASSWORD;
  private static String MQ_URI;
  private static String MQ_USERNAME;
  private static String MQ_PASSWORD;

  static {

    try (InputStream input = new FileInputStream(CONFIG_FILE)){
      Properties prop = new Properties();
      prop.load(input);
      HOST_NAME=prop.getProperty("MySQL_IP_ADDRESS");
      PORT = prop.getProperty("MySQL_PORT");
      DATABASE = prop.getProperty("DB_NAME");
      USERNAME = prop.getProperty("DB_USERNAME");
      PASSWORD = prop.getProperty("DB_PASSWORD");
      MQ_URI = prop.getProperty("MQ_URI");
      MQ_USERNAME = prop.getProperty("MQ_USERNAME");
      MQ_PASSWORD = prop.getProperty("MQ_PASSWORD");

    }
    catch (Exception e){
      e.printStackTrace();
    }



    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT,
        DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setInitialSize(10);
    dataSource.setMaxTotal(60);
  }




  public static BasicDataSource getDataSource() {
    return dataSource;
  }

  public static String getMqURI(){
    return MQ_URI;
  }

  public static String getMqUsername(){
    return MQ_USERNAME;
  }

  public static String getMqPassword(){
    return MQ_PASSWORD;
  }
}
