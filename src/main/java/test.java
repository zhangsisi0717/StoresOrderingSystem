import org.apache.commons.cli.*;

public class test {
  public static void main(String[] args) throws ParseException {
    //https://commons.apache.org/proper/commons-cli/apidocs/org/apache/commons/cli/Option.Builder.html
    Options options = new Options();
    options.addOption(Option.builder("maxStore").required().hasArg().desc("maximum number of stores to simulate").build());
    options.addOption(Option.builder("numCustomersRange").optionalArg(true).hasArg().desc("This is the range of customerIDs per store -default 1000").build());
    options.addOption(Option.builder("maxItemID").optionalArg(true).hasArg().desc("maximum itemID - default 100000").build());
    options.addOption(Option.builder("numPurchasesPerHour").optionalArg(true).hasArg().desc("number of purchases per hour - default 60").build());
    options.addOption(Option.builder("numItemsEachPurchase").optionalArg(true).hasArg().desc("number of items for each purchase, larger than 0 and default 5").build());
    options.addOption(Option.builder("date").optionalArg(true).hasArg().desc("date  -default to 20210101").build());
    options.addOption(Option.builder("ip").required().hasArg().desc("IP/port address of the server").build());

    String[] arg = {"-maxStore","100","-numCustomersRange","2000","-maxItemID","500000","-ip","localhost:8080"};


//    String[] arg = new String[]{"-m","100"};
    CommandLineParser cmlParser = new DefaultParser();
    CommandLine cml = cmlParser.parse(options,arg);
    System.out.println(cml.getOptionValue("maxStore"));
    System.out.println(cml.getOptionValue("ip"));
    System.out.println(cml.getOptionValue("numItemsEachPurchase"));

  }

}
