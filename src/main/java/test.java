import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.ArrayList;

public class test {
  public static void main(String[] args) throws InterruptedException, IOException {
//    String currentPath = new java.io.File(".").getCanonicalPath();
//    System.out.println(currentPath);
//    File file = new File("/requestResult.csv");
//    if (!file.exists())
//    {
//      file.createNewFile();
//    }

    String[][] all = {{"sisi","yuchen","rourou","beibei"},{"sisi","yuchen","rourou","beibei"}};
    String writeOutPath = "output" + File.separator + "result.csv";
    try(BufferedWriter writer = new BufferedWriter(new FileWriter(writeOutPath))) {
      for(String[] names: all){
        for(String name: names){
          writer.write(name+ ",");
        }
        writer.write(System.lineSeparator());
      }
      }
    }
  }


