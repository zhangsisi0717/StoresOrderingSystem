import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class StoresSimulationCMLParser{
  private static final String MAX_NUM_STORES_FLAG = "maxNumStore";
  private static final String MAX_NUM_STORES_DESC = "maximum number of stores to simulate";
  private static final String DATE_FLAG = "date";
  private static final String DATE_DESC ="date of the order";
  private static final String IP_FLAG = "ip";
  private static final String IP_DESC = "IP/port address of the server";
  private static final String CUSTOMER_ID_RANGE_FLAG = "customersIdRange";
  private static final String CUSTOMER_ID_RANGE_DESC = "This is the range of customerIDs per store -default 2000";
  private static final int CUSTOMER_ID_RANGE_DEFAULT = 2000;
  private static final String MAX_ITEM_ID_FLAG = "maxItemID";
  private static final String MAX_ITEM_ID_DESC ="maximum itemID - default 200000";
  private static final int MAX_ITEM_ID_DEFAULT = 200000;
  private static final String NUM_PURCHASE_PER_HOUR_FLAG = "numPurchasesPerHour";
  private static final String NUM_PURCHASE_PER_HOUR_DESC = "number of purchases per hour - default 60";
  private static final int NUM_PURCHASE_PER_HOUR_DEFAULT = 60;
  private static final String NUM_ITEMS_EACH_PURCHASE_FLAG = "numItemsEachPurchase";
  private static final String NUM_ITEMS_EACH_PURCHASE_DESC = "number of items for each purchase, larger than 0 and default 5";
  private static final int NUM_ITEMS_EACH_PURCHASE_DEFAULT = 5;
  private Map<String,Object> optionToArgs;
  private String[] originArgs;

  public StoresSimulationCMLParser(String[] originArgs) {
    this.originArgs = originArgs;
  }



  private static Options genOptionsForPurchaseOrder(){
    Options options = new Options();
    options.addOption(Option.builder(MAX_NUM_STORES_FLAG).required().hasArg().desc(MAX_NUM_STORES_DESC).build());
    options.addOption(Option.builder(IP_FLAG).required().hasArg().desc(IP_DESC).build());
    options.addOption(Option.builder(DATE_FLAG).required().hasArg().desc(DATE_DESC).build());
    options.addOption(Option.builder(CUSTOMER_ID_RANGE_FLAG).optionalArg(true).hasArg().desc(CUSTOMER_ID_RANGE_DESC).build());
    options.addOption(Option.builder(MAX_ITEM_ID_FLAG).optionalArg(true).hasArg().desc(MAX_ITEM_ID_DESC).build());
    options.addOption(Option.builder(NUM_PURCHASE_PER_HOUR_FLAG).optionalArg(true).hasArg().desc(NUM_PURCHASE_PER_HOUR_DESC).build());
    options.addOption(Option.builder(NUM_ITEMS_EACH_PURCHASE_FLAG).optionalArg(true).hasArg().desc(NUM_ITEMS_EACH_PURCHASE_DESC).build());
    return options;
  }

//  private Map<String,Object> parse(String[] args) throws ParseException {
//    CommandLineParser cmlParser = new DefaultParser();
//    Options cmdLineOptions = StoresSimulationCMLParser.genOptionsForPurchaseOrder();
//    CommandLine cml = cmlParser.parse(cmdLineOptions,args);
//    this.optionToArgs.put(MAX_NUM_STORES_FLAG)
//    this.optionToArgs.put(DATE_FLAG)
//    this.optionToArgs.put(MAX_NUM_STORES_FLAG)
//    this.optionToArgs.put(MAX_NUM_STORES_FLAG)

//    private static final String MAX_NUM_STORES_DESC = "maximum number of stores to simulate";
//    private static final String DATE_FLAG = "date";
//    private static final String DATE_DESC ="date of the order";
//    private static final String IP_FLAG = "ip";
//    private static final String IP_DESC = "IP/port address of the server";
//    private static final String CUSTOMER_ID_RANGE_FLAG = "customersIdRange";
//    private static final String CUSTOMER_ID_RANGE_DESC = "This is the range of customerIDs per store -default 2000";
//    private static final int CUSTOMER_ID_RANGE_DEFAULT = 2000;
//    private static final String MAX_ITEM_ID_FLAG = "maxItemID";
//    private static final String MAX_ITEM_ID_DESC ="maximum itemID - default 200000";
//    private static final int MAX_ITEM_ID_DEFAULT = 200000;
//    private static final String NUM_PURCHASE_PER_HOUR_FLAG = "numPurchasesPerHour";
//    private static final String NUM_PURCHASE_PER_HOUR_DESC = "number of purchases per hour - default 60";
//    private static final int NUM_PURCHASE_PER_HOUR_DEFAULT = 60;
//    private static final String NUM_ITEMS_EACH_PURCHASE_FLAG = "numItemsEachPurchase";
//    private static final String NUM_ITEMS_EACH_PURCHASE_DESC = "number of items for each purchase, larger than 0 and default 5";
//    private static final int NUM_ITEMS_EACH_PURCHASE_DEFAULT = 5;
//  }
}
