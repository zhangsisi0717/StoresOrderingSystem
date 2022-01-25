import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultFileParser {
  private String path;
  private List<String> headers = new ArrayList<>();
  private Map<String,List<String>> result = new HashMap<>();
  private static final String COMMA_SEPARATOR=",";


  public void processCSVResultFile(String resultFilePath) throws FileNotFoundException {
    path = resultFilePath;
    try (BufferedReader csvReader = new BufferedReader(new FileReader(resultFilePath))){
      String line;
      int index=0;
      while((line = csvReader.readLine())!= null){
        String[] splitLine = line.split(COMMA_SEPARATOR);
        if(index==0){
          headers = Arrays.asList(splitLine);
          for(int j=0; j<headers.size();j++){
            result.put(headers.get(j),new ArrayList<>());
          }
        }else{
          for(int i=0;i<headers.size();i++){
            result.get(headers.get(i)).add(splitLine[i]);
          }
        }
        index++;
      }

    }catch (IOException e){
      e.printStackTrace();
    }

  }

  public String getPath() {
    return path;
  }

  public List<String> getHeaders() {
    return headers;
  }

  public Map<String, List<String>> getResult() {
    return result;
  }

  @Override
  public String toString() {
    return "ResultFileParser{" +
        "path='" + path + '\'' +
        '}';
  }
}
