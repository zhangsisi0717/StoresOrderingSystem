import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class test2 {
  public static void main(String[] args) throws InterruptedException, IOException {
    String writeOutPath = "output" + File.separator + "result.csv";
    String[] HEADERS = {"clientId", "timeZone", "statusCode", "startTime","latency"};
    BlockingQueue<String[]> queue = new LinkedBlockingQueue();
    queue.add(HEADERS);
    System.out.println(queue.size());
    String[] re= queue.take();
//    System.out.println(a[0]);
    try(BufferedWriter writer = new BufferedWriter(new FileWriter(writeOutPath))){
    for(int i=0; i<re.length; i++){
      if(i==re.length-1){
        writer.write(re[i]);
      }
      else{
        writer.write(re[i] +",");
      }
    }
    writer.write(System.lineSeparator());}

//    String writeOutPath = "output" + File.separator + "result.csv";
//    RunnableFileWriter fileWriterRunnable = new RunnableFileWriter(queue,writeOutPath);
//    Thread fileWriterThread = new Thread(fileWriterRunnable);
//    fileWriterThread.start();
//
//    fileWriterThread.join();
//    System.out.println("finish");

  }



}
