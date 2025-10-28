package io.github.yamf.remark;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data structure to represent TSV data.
 * @author jens dietrich
 */
class TSVTable {

    private List<String> headers;
    private List<Row> rows;

    public TSVTable() {
        this.headers = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<Row> getRows() {
        return rows;
    }

    public int getColumnCount() {
        return headers.size();
    }

    public int getRowCount() {
        return rows.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Headers: ").append(headers).append("\n");
        for (int i = 0; i < rows.size(); i++) {
            sb.append("Row ").append(i).append(": ").append(rows.get(i)).append("\n");
        }
        return sb.toString();
    }

    // Represents a single row with cells
    static class Row {
        private List<Cell> cells;

        public Row() {
            this.cells = new ArrayList<>();
        }

        public void addCell(Cell cell) {
            this.cells.add(cell);
        }

        public List<Cell> getCells() {
            return cells;
        }

        public Cell getCell(int index) {
            return cells.get(index);
        }

        @Override
        public String toString() {
            return cells.toString();
        }
    }

    // Represents a cell containing either a single character or list of values
    static class Cell {
        private List<String> values;

        public Cell() {
            this.values = new ArrayList<>();
        }

        public Cell(String singleChar) {
            this.values = new ArrayList<>();
            this.values.add(singleChar);
        }

        public Cell(List<String> values) {
            this.values = new ArrayList<>(values);
        }

        public boolean isSingle() {
            return values.size() == 1;
        }

        public List<String> getValues() {
            return values;
        }

        @Override
        public String toString() {
            if (isSingle()) {
                return String.valueOf(values.get(0));
            }
            else {
                return values.stream().collect(Collectors.joining(",","(",")"));
            }

        }
    }
}
