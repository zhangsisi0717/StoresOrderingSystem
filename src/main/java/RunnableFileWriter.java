import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class RunnableFileWriter implements Runnable {

  private volatile boolean active = true;
  private static final String DELIMITER = ",";
  private BlockingQueue<String[]> queue;
  private String writeOutPath;
  private static final int  POLL_TIME_OUT=100;

  public RunnableFileWriter(BlockingQueue<String[]> queue, String writeOutPath) {
    this.queue = queue;
    this.writeOutPath = writeOutPath;
  }

  @Override
  public void run() {

    File file = new File(this.writeOutPath);
    try {
      if (!file.exists()) {
        file.createNewFile();
      }
      FileWriter fw = new FileWriter(file.getAbsoluteFile());
      BufferedWriter writer = new BufferedWriter(fw);

      while (active || !queue.isEmpty()) {
        String[] lines = queue.poll(POLL_TIME_OUT, TimeUnit.MILLISECONDS);
        if(lines != null){
          this.writeToCSV(lines, writer);
        }
//        System.out.println("writer thread: this.active = " + this.active + "; queue.empty = " + queue.isEmpty());
      }

      fw.close();
      writer.close();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void writeToCSV(String[] re, BufferedWriter writer) throws IOException {
    for (int i = 0; i < re.length; i++) {
      if (i == re.length - 1) {
        writer.write(re[i]);
      } else {
        writer.write(re[i] + DELIMITER);
      }
    }
    writer.write(System.lineSeparator());
    writer.flush();
  }

  private void printLines(String[] lines) {
    for (int i = 0; i < lines.length; i++) {
      System.out.print(lines[i] + "  ");
    }
    System.out.println();
  }

  public void requestStop() {
    this.active = false;
  }

  public boolean getActive() {
    return this.active;
  }

}






