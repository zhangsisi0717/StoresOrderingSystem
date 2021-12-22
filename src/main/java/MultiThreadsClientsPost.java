import java.util.Map;
import org.apache.commons.cli.ParseException;

public class MultiThreadsClientsPost {

  public static void main(String[] args) throws InvalidArgumentException, ParseException {
    StoresSimulationCMLParser parser = new StoresSimulationCMLParser();
    Map<OptionsFlags,Object> options = parser.parse(args);


  }


}
