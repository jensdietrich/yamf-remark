package io.github.yamf.remark;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class TSVExporterTests {

    static Path TEST_DATA = null;
    static Path TEST_ORACLE = null;

    ResultTable marks = null;

    @BeforeAll
    public static void setUp() throws IOException {
        TEST_DATA = Path.of(TSVParserTests.class.getResource("/test-data.tsv").getPath());
        assumeTrue(TEST_DATA.toFile().exists());

        TEST_ORACLE = Path.of(TSVParserTests.class.getResource("/test-oracle.tsv").getPath());
        assumeTrue(TEST_ORACLE.toFile().exists());
    }

    @BeforeEach
    public void init() throws IOException {
        marks = Main.mark(TEST_DATA,TEST_ORACLE);
    }

    @Test
    public void test11() {
        ResultTable.Row row = marks.rows().get(0);
        String id = row.id();
        assumeTrue("anonymous1".equals(id));
        double mark = row.cells().get(0).value();
        assumeTrue(Math.abs(mark-0.6666)<0.001);
        String markAsString = TSVResultTableExporter.DECIMAL_FORMAT.format(mark);

        List<String> lines = TSVResultTableExporter.exportToLines(marks);
        String line = lines.get(1);  // add one because of header line
        String value = line.split("\t")[1]; // add one because of id column
        assertEquals(markAsString, value);
    }

    @Test
    public void test21() {
        ResultTable.Row row = marks.rows().get(1);
        String id = row.id();
        assumeTrue("anonymous2".equals(id));
        double mark = row.cells().get(0).value();
        assumeTrue(Math.abs(mark-0.5)<0.001);
        String markAsString = TSVResultTableExporter.DECIMAL_FORMAT.format(mark);

        List<String> lines = TSVResultTableExporter.exportToLines(marks);
        String line = lines.get(2);  // add one because of header line
        String value = line.split("\t")[1]; // add one because of id column
        assertEquals(markAsString, value);
    }

    @Test
    public void test23() {
        ResultTable.Row row = marks.rows().get(1);
        String id = row.id();
        assumeTrue("anonymous2".equals(id));
        double mark = row.cells().get(2).value();
        assumeTrue(Math.abs(mark-0.0)<0.001);
        String markAsString = TSVResultTableExporter.DECIMAL_FORMAT.format(mark);

        List<String> lines = TSVResultTableExporter.exportToLines(marks);
        String line = lines.get(2);  // add one because of header line
        String value = line.split("\t")[3]; // add one because of id column
        assertEquals(markAsString, value);
    }



}
