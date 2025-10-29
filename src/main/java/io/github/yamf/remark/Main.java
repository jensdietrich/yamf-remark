package io.github.yamf.remark;

import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

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
        .longOpt("outout")
        .required(true)
        .hasArg()
        .desc("a file where the results will be written to")
        .build();


    public static void main(String[] args) {

    }

    static void checkHeaders(Table input, Table oracle) {
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

    static void checkRows(Table input, Table.Row correctValues, Table.Row possibleValues) {
        for (int i=0;i<input.getRows().size();i++) {
            checkColumnCount(input.getRows().get(i),possibleValues,"input row "+i,"possible values");
            checkColumnCount(input.getRows().get(i),correctValues,"input row "+i,"correct values");
            checkValues(input.getRows().get(i),possibleValues,"input row "+i,"possible values");
        }
        checkValues(correctValues,possibleValues,"correct values","possible values");
    }

    static void checkColumnCount(Table.Row dataRow, Table.Row referenceRow, String rowName1,String rowName2) {
        if (dataRow.getCells().size()!=referenceRow.getCells().size()) {
            throw new IllegalArgumentException(rowName1 + " does not have the same number of columns as " + rowName2);
        }
    }

    static void checkValues(Table.Row dataRow, Table.Row possibleValues, String rowName1,String rowName2) {
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
