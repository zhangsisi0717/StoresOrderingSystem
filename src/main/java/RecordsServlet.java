import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "RecordsServlet", value = "/RecordsServlet")
public class RecordsServlet extends HttpServlet {
  private static final int VALID_URL_LENGTH_FOR_GET=3;
  private static final int STORE_ITEM_ID_INDEX=2;
  private static final int STORE_FLAG_INDEX=1;
  private static final int Top10_FLAG_INDEX=1;
  private static final String STORE_FLAG="store";
  private static final String TOP10_FLAG="top5";
  private static final String NUM_RE_PATTERN="^\\d+$";
  private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> recordsStoreToItem= SoldItemsRecordsStats.getRecordsStoreToItem();
  private static ConcurrentHashMap<Integer, ConcurrentHashMap<Integer,Integer>> recordsItemToStore= SoldItemsRecordsStats.getRecordsItemToStore();



  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    System.out.println("do get!");
    response.setContentType("text/html");
    String urlPath = request.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    String[] urlParts = urlPath.split("/");
    System.out.println("urlParts= ");
    this.printStringArray(urlParts);
    System.out.println("len= " + urlParts.length);
    if(urlParts.length != VALID_URL_LENGTH_FOR_GET || !urlParts[0].equals("")){
//      System.out.println("run here1 not found");
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    else if( urlParts[Top10_FLAG_INDEX].equals(TOP10_FLAG) && urlParts[STORE_ITEM_ID_INDEX].matches(NUM_RE_PATTERN)){
      response.setStatus(HttpServletResponse.SC_OK);
      System.out.println("items to store= " + recordsItemToStore);
      response.getWriter().println(recordsItemToStore.toString());
    }

    else if( urlParts[STORE_FLAG_INDEX].equals(STORE_FLAG) && urlParts[STORE_ITEM_ID_INDEX].matches(NUM_RE_PATTERN)){
      response.setStatus(HttpServletResponse.SC_OK);
      System.out.println("store to items= " + recordsStoreToItem);
      response.getWriter().println(recordsStoreToItem.toString());
    }

    else{
      System.out.println("run here2 not found");
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response){

  }

  private void printStringArray(String[] arr){
    for(int i=0;i<arr.length;i++){
      System.out.print(arr[i]+" ");
    }
    System.out.println();
  }

}


