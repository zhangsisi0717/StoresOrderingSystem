import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class testSer {


  private static byte[] writeToByteArray(List<OrderedItem> allOrderedItems) throws IOException {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(bos);
    oos.writeObject(allOrderedItems);
    return bos.toByteArray();
  }

  private static List<OrderedItem> fromByteArray(byte[] serialized)
      throws IOException, ClassNotFoundException {
    ByteArrayInputStream bis = new ByteArrayInputStream(serialized);
    ObjectInputStream ois = new ObjectInputStream(bis);
    List<OrderedItem> deSerialized = (List<OrderedItem>) ois.readObject();
    return deSerialized;
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {

    Date date = Date.valueOf(LocalDate.now());
    OrderedItem item = new OrderedItem(12313,193225,"231231234",242,5,33.0,date);
    OrderedItem item2 = new OrderedItem(12313,124325,"231234",242,5,33.0,date);
    List<OrderedItem> items = Arrays.asList(item,item2);

    System.out.println(fromByteArray(testSer.writeToByteArray(items)));

  }


}
