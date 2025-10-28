package io.github.yamf.remark;

import org.apache.commons.cli.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
