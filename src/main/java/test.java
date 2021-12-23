import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
public class test {
  public static void main(String[] args) throws InterruptedException {
    LocalDateTime cur = LocalDateTime.now();
    System.out.println(cur);
    Thread.sleep(10000);
    LocalDateTime cur2 = LocalDateTime.now();
    System.out.println(cur2);
    Duration dur = Duration.between(cur,cur2);
    System.out.println(dur.toSeconds());
//    String date = "20211212";
//    DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
//    LocalDate parsedDate = LocalDate.parse(date, dateFormatter);
//    String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//    int storeId = 111;
//    int custId = 1212;
//    String ip = "localhost:8080";
//
//    String temp = "http://%s/StoresOrderingSystem/purchase/%d/customer/%d/date/%s";
//
//    String sf1=String.format(temp,ip,storeId,custId,formattedDate);
//    System.err.println(sf1);
//
//    System.out.println((int)(Math.random()*1000)+1);
  }

}
