package io.github.yamf.remark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Stream.*;

import static com.google.common.collect.Streams.concat;



public class TSVResultTableExporter implements ResultTableExporter {

    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.0000");
    final static Logger LOG = LoggerFactory.getLogger(Main.class);

    @Override
    public void export(ResultTable table, Path file) throws IOException {
        List<String> lines = exportToLines(table);
        Files.write(file,lines);
        LOG.info("results written to {}", file);
    }

    // testable
    static List<String> exportToLines(ResultTable table) {
        List<String> lines = new ArrayList<>();
        lines.add(
            table.headers().stream().collect(Collectors.joining("\t"))
        );
        lines.addAll(
            table.rows().stream()
                .map( row -> {
                    return row.cells().stream().map(mark -> DECIMAL_FORMAT.format(mark.value())).collect(Collectors.joining("\t",row.id()+'\t',""));
                })
                .collect(Collectors.toUnmodifiableList())
        );
        return lines;
    }


}
