import java.util.ArrayList;
import java.util.List;

public class RequestStats {
  private int numSuccessfulRequest=0;
  private int attemptedRequest=0;
  private List<Long> latencyList= new ArrayList<>();
  private Long cumulativeLatencySum=(long) 0;
  private int totalNumOfNewItems=0;
  private int attemptedAddNewItems=0;

  synchronized public  void incSuccess() {
    numSuccessfulRequest++;
  }

  synchronized public void incAttemptedRequest() {
    attemptedRequest++;
  }

  public int getNumSuccessfulRequest() {
    return numSuccessfulRequest;
  }

  public List<Long> getLatencyList(){return latencyList;}

  public int getAttemptedRequest() {
    return attemptedRequest;
  }

  synchronized public void addToLatencyList(Long latency){
    this.latencyList.add(latency);
    this.cumulativeLatencySum += latency;
  }

  public Long getCumulativeLatencySum() {
    return cumulativeLatencySum;
  }

  synchronized public void incItems(int numOfNewItems){
    this.totalNumOfNewItems += numOfNewItems;
  }

  synchronized public void incAttemptAddItems(int numOfNewItems){
    this.attemptedAddNewItems+=numOfNewItems;
  }

  public int getTotalNumOfNewItems() {
    return totalNumOfNewItems;
  }

  @Override
  public String toString() {
    return "RequestStats{" +
        "numSuccessfulRequest=" + numSuccessfulRequest +
        ", attemptedRequest=" + attemptedRequest +
        ", totalNumOfNewItems=" + totalNumOfNewItems +
        ", attemptedAddNewItems=" + attemptedAddNewItems +
        '}';
  }
}
