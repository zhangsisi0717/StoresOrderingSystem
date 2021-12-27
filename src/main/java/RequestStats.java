import java.util.ArrayList;
import java.util.List;

public class RequestStats {
  private int numSuccessfulRequest=0;
  private int attemptedRequest=0;
  private List<Long> latencyList= new ArrayList<>();
  private Long cumulativeLatencySum=(long) 0;

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

  public void addToLatencyList(Long latency){
    this.latencyList.add(latency);
    this.cumulativeLatencySum += latency;
  }

  public Long getCumulativeLatencySum() {
    return cumulativeLatencySum;
  }

  @Override
  public String toString() {
    return "RequestCounter{" +
        "numSuccessfulRequest=" + numSuccessfulRequest +
        ", attemptedRequest=" + attemptedRequest +
        '}';
  }
}
