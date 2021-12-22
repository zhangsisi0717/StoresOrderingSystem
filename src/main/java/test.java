import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class test {
  public static void main(String[] args){
    String date = "20211212";
    DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
    LocalDate parsedDate = LocalDate.parse(date, dateFormatter);
    String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    int storeId = 111;
    int custId = 1212;
    String ip = "localhost:8080";

    String temp = "http://%s/StoresOrderingSystem/purchase/%d/customer/%d/date/%s";

    String sf1=String.format(temp,ip,storeId,custId,formattedDate);
    System.err.println(sf1);

    System.out.println((int)(Math.random()*1000)+1);
  }

}
