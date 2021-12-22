import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

import java.net.http.HttpClient;

@WebServlet(name = "StoresServlet", value = "/StoresServlet")
public class StoresServlet extends HttpServlet {

  private static final int VALID_URL_LENGTH_FOR_POST = 7;
  private static final String PURCHASE = "purchase";
  private static final String CUSTOMER = "customer";
  private static final String DATE = "date";
  private static final String ITEMS = "items";
  private static final String ITEMS_ID = "ItemID";
  private static final String NUM_OF_ITEMS = "numberOfItems";
  private static final int PURCHASE_IDX = 1;
  private static final int STORE_ID_IDX = 2;
  private static final int CUSTOMER_IDX = 3;
  private static final int CUSTOMER_ID_IDX = 4;
  private static final int DATE_IDX = 5;
  private static final int DATE_CONTENT_IDX = 6;

//  private static final HttpClient client = null;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("text/html");
    response.getWriter().println("stores ordering system requests arrived!");
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


//    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    return;

  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("application/json");
    String urlPath = request.getPathInfo();


    // check we have a URL!
    if (urlPath == null || urlPath.isEmpty()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }

    String[] urlParts = urlPath.split("/");
    if (!this.isUrlValidForPost(urlParts)) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    // get the post body json
    String postBodyJSONString = request.getReader().lines().collect(Collectors.joining());
    try {
      this.processBodyData(postBodyJSONString);
    } catch (Exception e) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    response.setStatus(HttpServletResponse.SC_CREATED);
    response.getWriter().println("placed an order successfully!");

  }

  private boolean isUrlValidForPost(String[] urlParts) {
    // urlPath  = "/purchase/{storeID}/customer/{custID}/date/{date}"
    // urlParts = [, purchase,{storeID},customer,{custID},date,{date}]
    if (urlParts.length != VALID_URL_LENGTH_FOR_POST) {
      return false;
    }
    if (!urlParts[0].equals("")) {
      return false;
    }
    if (!urlParts[PURCHASE_IDX].equals(PURCHASE) || !urlParts[CUSTOMER_IDX].equals(CUSTOMER)
        || !urlParts[DATE_IDX].equals(
        DATE)) {
      return false;
    }

    if (!urlParts[STORE_ID_IDX].matches("^\\d+$") || !urlParts[CUSTOMER_ID_IDX].matches("^\\d+$")) {
      return false;
    }

    if (!this.isValidDate(urlParts[DATE_CONTENT_IDX])) {
      return false;
    }
    return true;
  }

  private boolean isValidDate(String dateStr) {
    try {
      DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
      LocalDate.parse(dateStr, dateFormatter);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  private void processBodyData(String postBodyJSONString) {
    //https://docs.oracle.com/javaee/7/api/javax/json/JsonObject.html
    JsonReader jsonReader = Json.createReader(new StringReader(postBodyJSONString));
    JsonObject postBodyJSON = jsonReader.readObject();

    for (JsonValue jv : postBodyJSON.getJsonArray(ITEMS)) {
      JsonObject jo = (JsonObject) jv;
      String itemID = jo.getString(ITEMS_ID);
      int numberOfItem = jo.getInt(NUM_OF_ITEMS);
      processOrder(itemID, numberOfItem);
    }


  }

  private void processOrder(String ItemID, int numberOfItem) {

  }
}