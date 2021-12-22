import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class test2 {
  public static void main(String[]agrs) throws ParseException {
    Option opt = Option.builder("maxScore").required().hasArgs().build();
    Options options = new Options();
    options.addOption(opt);
    String[] args = {"-maxScore","sd"};
    CommandLineParser parser = new DefaultParser();
    CommandLine line = parser.parse(options, args);
    System.out.println(line.getOptionValue("maxScore"));
  }

}
