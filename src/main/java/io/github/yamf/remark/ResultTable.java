package io.github.yamf.remark;

import java.util.*;

/**
 * Table structure for result data (marks).
 * @author jens dietrich
 */
public record ResultTable (List<String> headers, List<Row> rows){
    record Row (String id, List<Mark> cells) {}
}
