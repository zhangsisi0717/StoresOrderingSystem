import java.time.LocalDate;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;
import java.sql.Date;
import javax.json.Json;
import javax.json.JsonObject;

public class test {
  public static void main(String[] args) {
    JsonObject item = Json.createObjectBuilder().add("Price", 22.4).build();
    Double v = Double.valueOf(item.get("Price").toString());

    System.out.println(item.get("Price"));
    System.out.println(LocalDate.now().toString());
    Date d = Date.valueOf(LocalDate.now().toString());
    System.out.println(d);



  }
}
