public class RequestCounter {
  private int numSuccessfulRequest=0;
  private int attemptedRequest=0;

  synchronized public  void incSuccess() {
    numSuccessfulRequest++;
  }

  synchronized public void incAttemptedRequest() {
    attemptedRequest++;
  }

  public int getNumSuccessfulRequest() {
    return numSuccessfulRequest;
  }

  public int getAttemptedRequest() {
    return attemptedRequest;
  }

  @Override
  public String toString() {
    return "RequestCounter{" +
        "numSuccessfulRequest=" + numSuccessfulRequest +
        ", attemptedRequest=" + attemptedRequest +
        '}';
  }
}
