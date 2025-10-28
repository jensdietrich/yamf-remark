package io.github.yamf.remark;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Simple TSV input parser.
 * @author jens dietrich
 */
public class TSVParser {


    public static TSVTable parse(Path file) throws IOException {
        TSVTable result = new TSVTable();

        try (BufferedReader br = new BufferedReader(new FileReader(file.toFile()))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    // Parse header line
                    String[] headers = line.split("\t");
                    result.setHeaders(Arrays.asList(headers));
                    isFirstLine = false;
                } else {
                    // Parse data line
                    String[] cells = line.split("\t", -1); // -1 to include trailing empty strings
                    TSVTable.Row row = parseRow(cells);
                    result.addRow(row);
                }
            }
        }

        return result;
    }

    private static TSVTable.Row parseRow(String[] cellStrings) {
        TSVTable.Row row = new TSVTable.Row();

        for (String cellStr : cellStrings) {
            TSVTable.Cell cell = parseCell(cellStr);
            row.addCell(cell);
        }

        return row;
    }

    private static TSVTable.Cell parseCell(String cellStr) {
        cellStr = cellStr.trim();

        if (cellStr.startsWith("\"") && cellStr.endsWith("\"")) {
            cellStr = cellStr.substring(1, cellStr.length() - 1);
        }

        // Check if it's a bracketed list: (a,b,c)
        if (cellStr.startsWith("(") && cellStr.endsWith(")")) {
            String content = cellStr.substring(1, cellStr.length() - 1);
            String[] parts = content.split(",");
            List<String> values = new ArrayList<>();

            for (String part : parts) {
                part = part.trim();
                if (!part.isEmpty()) {
                    values.add(part);
                }
            }

            return new TSVTable.Cell(values);
        } else {
            // Single character
            if (!cellStr.isEmpty()) {
                return new TSVTable.Cell(cellStr);
            } else {
                return new TSVTable.Cell(); // Empty cell
            }
        }
    }

}