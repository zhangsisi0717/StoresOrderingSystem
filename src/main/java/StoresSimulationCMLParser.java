import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class StoresSimulationCMLParser {

  //  private static final String MAX_NUM_STORES_FLAG = "maxNumStore";
  private static final String MAX_NUM_STORES_DESC = "maximum number of stores to simulate";
  //  private static final String DATE_FLAG = "date";
  private static final String DATE_DESC = "date of the order";
  //  private static final String IP_FLAG = "ip";
  private static final String IP_DESC = "IP/port address of the server";
  //  private static final String CUSTOMER_ID_RANGE_FLAG = "customersIdRange";
  private static final String CUSTOMER_ID_RANGE_DESC = "This is the range of customerIDs per store -default 2000";
  private static final int CUSTOMER_ID_RANGE_DEFAULT = 2000;
  //  private static final String MAX_ITEM_ID_FLAG = "maxItemID";
  private static final String MAX_ITEM_ID_DESC = "maximum itemID - default 200000";
  private static final int MAX_ITEM_ID_DEFAULT = 200000;
  //  private static final String NUM_PURCHASE_PER_HOUR_FLAG = "numPurchasesPerHour";
  private static final String NUM_PURCHASE_PER_HOUR_DESC = "number of purchases per hour - default 60";
  private static final int NUM_PURCHASE_PER_HOUR_DEFAULT = 60;
  //  private static final String NUM_ITEMS_EACH_PURCHASE_FLAG = "numItemsEachPurchase";
  private static final String NUM_ITEMS_EACH_PURCHASE_DESC = "number of items for each purchase, larger than 0 and default 5";
  private static final int NUM_ITEMS_EACH_PURCHASE_DEFAULT = 5;
  private Map<OptionsFlags, Object> optsToArgs;
  private CommandLine cmdLines;
  private static final String RE_INT_PATTERN = "^\\d+$";

  private static Options genOptionsForPurchaseOrder() {
    Options options = new Options();
    options.addOption(
        Option.builder(OptionsFlags.maxNumStore.toString()).required().hasArg().build());
    options.addOption(Option.builder(OptionsFlags.ip.toString()).required().hasArg().build());
    options.addOption(Option.builder(OptionsFlags.date.toString()).required().hasArg().build());
    options.addOption(
        Option.builder(OptionsFlags.customersIdRange.toString()).optionalArg(true).hasArg()
            .build());
    options.addOption(
        Option.builder(OptionsFlags.maxItemID.toString()).optionalArg(true).hasArg().build());
    options.addOption(
        Option.builder(OptionsFlags.numPurchasesPerHour.toString()).optionalArg(true).hasArg()
            .build());
    options.addOption(
        Option.builder(OptionsFlags.numItemsEachPurchase.toString()).optionalArg(true).hasArg()
            .build());
    return options;
  }

  public Map<OptionsFlags, Object> parse(String[] args)
      throws ParseException, InvalidArgumentException {
    CommandLineParser parser = new DefaultParser();
    this.cmdLines = parser.parse(StoresSimulationCMLParser.genOptionsForPurchaseOrder(), args);
    this.validateMaxNumStore(cmdLines);
    this.validateIP(cmdLines);
    this.validateDate(cmdLines);
    this.validateCustomersIdRange(cmdLines);
    this.validateMaxItemID(cmdLines);
    this.validateNumOfPurchasePerHour(cmdLines);
    this.validateNumOfItemsEachPurchase(cmdLines);
    return this.optsToArgs;
  }

  private void validateMaxNumStore(CommandLine cmdLines) throws InvalidArgumentException {
    String maxStores = cmdLines.getOptionValue(OptionsFlags.maxNumStore.toString());
    if (!maxStores.matches(RE_INT_PATTERN) || !(Integer.valueOf(maxStores) > 0)) {
      throw new InvalidArgumentException("number of max number of stores must be larger than 0");
    } else {
      this.optsToArgs.put(OptionsFlags.maxNumStore, Integer.valueOf(maxStores));
    }
  }

  /**
   * Todo: validate if the provided IP format is valid, raise error otherwise
   */
  private void validateIP(CommandLine cmdLines) {
    String ip = cmdLines.getOptionValue(OptionsFlags.ip.toString());
    this.optsToArgs.put(OptionsFlags.ip, ip);
  }

  private void validateDate(CommandLine cmdLines) throws InvalidArgumentException {
    String date = cmdLines.getOptionValue(OptionsFlags.date.toString());
    try {
      DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
      LocalDate.parse(date, dateFormatter);
      this.optsToArgs.put(OptionsFlags.date, date);
    } catch (DateTimeParseException e) {
      throw new InvalidArgumentException("provided date argument is not valid");
    }
  }


  private void validateCustomersIdRange(CommandLine cmdLines) throws InvalidArgumentException {
    String idRange = cmdLines.getOptionValue(OptionsFlags.customersIdRange.toString());
    if (idRange == null) {
      this.optsToArgs.put(OptionsFlags.customersIdRange, CUSTOMER_ID_RANGE_DEFAULT);
    } else if (!idRange.matches(RE_INT_PATTERN) || !(Integer.valueOf(idRange) > 0)) {
      throw new InvalidArgumentException("customer id range must be positive integer");
    }
  }

  private void validateMaxItemID(CommandLine cmdLines) throws InvalidArgumentException {
    String maxItemId = cmdLines.getOptionValue(OptionsFlags.maxItemID.toString());
    if (maxItemId == null) {
      this.optsToArgs.put(OptionsFlags.customersIdRange, MAX_ITEM_ID_DEFAULT);
    } else if (!maxItemId.matches(RE_INT_PATTERN) || !(Integer.valueOf(maxItemId) > 0)) {
      throw new InvalidArgumentException("MaxItemID must be positive integer");
    }
  }

  private void validateNumOfPurchasePerHour(CommandLine cmdLines) throws InvalidArgumentException {
    String numPurchase = cmdLines.getOptionValue(OptionsFlags.numPurchasesPerHour.toString());
    if (numPurchase == null) {
      this.optsToArgs.put(OptionsFlags.numPurchasesPerHour, NUM_PURCHASE_PER_HOUR_DEFAULT);
    } else if (!numPurchase.matches(RE_INT_PATTERN) || !(Integer.valueOf(numPurchase) > 0)) {
      throw new InvalidArgumentException("Number of purchase per hour must be positive integer");
    }
  }

  private void validateNumOfItemsEachPurchase(CommandLine cmdLines)
      throws InvalidArgumentException {
    String numItems = cmdLines.getOptionValue(OptionsFlags.numItemsEachPurchase.toString());
    if (numItems == null) {
      this.optsToArgs.put(OptionsFlags.numItemsEachPurchase, NUM_ITEMS_EACH_PURCHASE_DEFAULT);
    } else if (!numItems.matches(RE_INT_PATTERN) || !(Integer.valueOf(numItems) > 0)) {
      throw new InvalidArgumentException(
          "Number of items for each purchase must be positive integer");
    }
  }

  public Map<OptionsFlags, Object> getOptsToArgs() {
    return optsToArgs;
  }
}
