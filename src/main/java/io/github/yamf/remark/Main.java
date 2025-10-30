package io.github.yamf.remark;

import com.google.common.base.Preconditions;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * Program CLI.
 * @author jens dietrich
 */
public class Main {

    final static Logger LOG = LoggerFactory.getLogger(Main.class);

    final static Option OPT_INPUT = Option.builder()
        .argName("in")
        .longOpt("input")
        .required(true)
        .hasArg()
        .desc("a file in tab-separated format with answers chosen by student for each question")
        .build();

    final static Option OPT_ORACLE= Option.builder()
        .argName("ocl")
        .longOpt("oracle")
        .required(true)
        .hasArg()
        .desc("a file in tab-separated format with the correct answers for each question")
        .build();

    final static Option OPT_OUT= Option.builder()
        .argName("out")
        .longOpt("output")
        .required(true)
        .hasArg()
        .desc("a file where the results will be written to")
        .build();

    final static Option OPT_PROVENANCE = Option.builder()
        .argName("pro")
        .longOpt("provenance")
        .required(false)
        .hasArg()
        .desc("a JSON file where the results with additional provenance information will be written to")
        .build();

    final static Option OPT_SCALING_FACTOR = Option.builder()
        .argName("sca")
        .longOpt("scalingfactor")
        .required(false)
        .hasArg()
        .desc("a positive number by which all marks are multiplied (optional, default is 1)")
        .build();

    final static Options OPTIONS = new Options()
        .addOption(OPT_INPUT)
        .addOption(OPT_ORACLE)
        .addOption(OPT_OUT)
        .addOption(OPT_PROVENANCE)
        .addOption(OPT_SCALING_FACTOR);
        ;


    public static void main(String[] args) throws IOException {

        CommandLineParser cliParser = new DefaultParser();
        InputTableParser parser = new TSVParser();

        try {
            CommandLine cli = cliParser.parse(OPTIONS, args);

            String inputFileName = cli.getOptionValue(OPT_INPUT);
            Path inputFile = Path.of(inputFileName);
            Preconditions.checkState(Files.exists(inputFile));
            Preconditions.checkState(!Files.isDirectory(inputFile));
            LOG.info("Reading input from: {}", inputFile);

            String oracleFileName = cli.getOptionValue(OPT_ORACLE);
            Path oracleFile = Path.of(oracleFileName);
            Preconditions.checkState(Files.exists(oracleFile));
            Preconditions.checkState(!Files.isDirectory(oracleFile));
            LOG.info("Reading oracle (correct and possible answers) from: " + oracleFile);

            int scalingFactor = 1;
            if (cli.hasOption(OPT_SCALING_FACTOR)) {
                scalingFactor = Integer.parseInt(cli.getOptionValue(OPT_SCALING_FACTOR));
                Preconditions.checkArgument(scalingFactor > 0, "scaling factor must be greater than 0");
                LOG.info("Using scaling factor: {}", scalingFactor);
            }

            ResultTable results = mark(inputFile,oracleFile,scalingFactor);
            LOG.info("Marking done, marked {} records",results.rows().size());

            String outputFileName = cli.getOptionValue(OPT_OUT);
            Path outputFile = Path.of(outputFileName);
            new TSVResultTableExporter().export(results,outputFile); // logging in exporter

            if (cli.hasOption(OPT_PROVENANCE)) {
                String provenanceFileName = cli.getOptionValue(OPT_PROVENANCE);
                Path provenanceFile = Path.of(provenanceFileName);
                new JSONResultTableExporter().export(results,provenanceFile);
            }
            else {
                LOG.warn("No provenance information generated, use \"--{}\" option to generate this",OPT_PROVENANCE.getLongOpt());
            }

        }
        catch (ParseException e) {
            showUsage();
        }

    }

    static ResultTable mark(Path inputFile, Path oracleFile,int scalingFactor) throws IOException {

        Formula formula = new PartialCreditsAndPartialDeductions();
        InputTable input = getParser(inputFile).parse(inputFile);
        InputTable oracle = getParser(oracleFile).parse(oracleFile);
        checkHeaders(input, oracle);

        Preconditions.checkState(oracle.getRows().size()==2,"the oracle file must have two rows defining correct and possible ansers (in this order)");
        InputTable.Row correctValues = oracle.getRows().get(0);
        InputTable.Row possibleValues = oracle.getRows().get(1);
        List<String> headers = input.getHeaders();

        List<ResultTable.Row> results = new ArrayList<>();

        for (InputTable.Row row : input.getRows()) {
            String id = row.getCells().get(0).getValues().get(0);
            List<Mark> markList = new ArrayList<>();
            for (int i=1;i<row.getCells().size();i++) {
                String question = input.getHeaders().get(i);
                Mark mark = formula.compute(
                    question,
                    row.getCells().get(i).getValuesAsSet(),
                    correctValues.getCells().get(i).getValuesAsSet(),
                    possibleValues.getCells().get(i).getValuesAsSet(),
                    scalingFactor
                );
                markList.add(mark);
            }
            results.add(new ResultTable.Row(id,markList));
        }
        return new ResultTable(headers,results);
    }

    /**
     * Get the parses, e.g. by file extension.
     * @param file
     * @return
     * @throws IllegalArgumentException
     */
    static InputTableParser getParser(Path file) throws IllegalArgumentException {
        if (file.toString().endsWith(".tsv") || file.toString().endsWith(".csv")) {
            return new TSVParser();
        }
        else {
            throw new IllegalArgumentException("Unrecognized file type: " + file);
        }
    }

    private static void showUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -cp <path-to-built-jar> " + Main.class.getName(), OPTIONS);
    }

    static void checkHeaders(InputTable input, InputTable oracle) {
        List<String> inputHeader =  input.getHeaders();
        List<String> oracleHeader =  input.getHeaders();
        if (inputHeader.size() != oracleHeader.size()) {
            throw new IllegalArgumentException("Input and Oracle headers do not match");
        }

        // ignore first column !
        for (int i = 1; i < inputHeader.size(); i++) {
            if (!inputHeader.get(i).equals(oracleHeader.get(i))) {
                throw new IllegalArgumentException("Input and Oracle values do not match in column " + (i+1));
            }
        }
    }

    static void checkRows(InputTable input, InputTable.Row correctValues, InputTable.Row possibleValues) {
        for (int i=0;i<input.getRows().size();i++) {
            checkColumnCount(input.getRows().get(i),possibleValues,"input row "+i,"possible values");
            checkColumnCount(input.getRows().get(i),correctValues,"input row "+i,"correct values");
            checkValues(input.getRows().get(i),possibleValues,"input row "+i,"possible values");
        }
        checkValues(correctValues,possibleValues,"correct values","possible values");
    }

    static void checkColumnCount(InputTable.Row dataRow, InputTable.Row referenceRow, String rowName1, String rowName2) {
        if (dataRow.getCells().size()!=referenceRow.getCells().size()) {
            throw new IllegalArgumentException(rowName1 + " does not have the same number of columns as " + rowName2);
        }
    }

    static void checkValues(InputTable.Row dataRow, InputTable.Row possibleValues, String rowName1, String rowName2) {
        assert dataRow.getCells().size()==possibleValues.getCells().size();
        for (int i=0;i<dataRow.getCells().size();i++) {
            Set<String> values1 = dataRow.getCells().get(i).getValuesAsSet();
            Set<String> values2 = possibleValues.getCells().get(i).getValuesAsSet();
            if (!values2.containsAll(values1)) {
                throw new IllegalArgumentException(rowName1 + " contains values not defined in " + rowName2);
            }
        }
    }
}
