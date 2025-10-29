package io.github.yamf.remark;

import java.util.*;

/**
 * Table structure for result data (marks).
 * @author jens dietrich
 */
public class OutputTable {

    private List<String> headers = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();


    public OutputTable(List<String> headers, List<Row> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    static class Row {
        public Row(String label, List<Mark> cells) {
            this.label = label;
            this.cells = cells;
        }

        private String label = null; // first column value, such as student id
        private List<Mark> cells = new ArrayList<>();
        public String getLabel() {
            return label;
        }
        public List<Mark> getCells() {
            return cells;
        }
    }

}
