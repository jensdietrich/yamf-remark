package io.github.yamf.remark;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Tests for the TSV parser.
 * @author jens dietrich
 */
public class TSVParserTests {

    public static Path TEST_DATA = null;
    public static Path TEST_ORACLE = null;

    @BeforeAll
    public static void setUp() {
        TEST_DATA = Path.of(TSVParserTests.class.getResource("/test-data.tsv").getPath());
        assumeTrue(TEST_DATA.toFile().exists());

        TEST_ORACLE = Path.of(TSVParserTests.class.getResource("/test-oracle.tsv").getPath());
        assumeTrue(TEST_ORACLE.toFile().exists());
    }

    @Test
    public void testDataHeader() throws IOException {
        InputTable table = new TSVParser().parse(TEST_DATA);
        List<String> headerRow = table.getHeaders();
        String[] labels = {
            "SID","Q1","Q2","Q3","Q4","Q5","Q6","Q7","Q8","Q9","Q10","Q11","Q12","Q13","Q14","Q15","Q16","Q17","Q18","Q19","Q20"
        };
        for (int i = 0; i < labels.length; i++) {
            assertEquals(labels[i],headerRow.get(i));
        }
    }

    @Test
    public void testDataRow1() throws IOException {
        InputTable table = new TSVParser().parse(TEST_DATA);
        List<InputTable.Row> rows = table.getRows();
        assertTrue(0<rows.size());
        InputTable.Row row = rows.get(0);
        assertEquals(21,row.getCells().size());
        String[] labels = {
            "anonymous1","(a,b,c)","(a,e)","c","c","(a,b,d)","(a,b,d)","(c,d,e)","(b,c)","c","(a,b,c,d)","(b,d)","(b,c,d)","e","b","(b,d)","(b,c)","(b,c)","(b,e)","b","b"
        };
        for (int i = 0; i < labels.length; i++) {
            assertEquals(labels[i],row.getCells().get(i).toString());
        }
    }


}
