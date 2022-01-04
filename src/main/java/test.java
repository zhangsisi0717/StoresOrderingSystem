import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
public class test {
  public static void main(String[] args) {
    UUID uuid = UUID.randomUUID();
    System.out.println(uuid.toString());
    System.out.println(UUID.randomUUID().toString().replace("-","").substring(0,12));

    LocalDate d = LocalDate.now();
    System.out.println(d.toString());

  }
}
