package io.github.yamf.remark;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.*;

public class IntegrationTests {

    static Path TEST_DATA = null;
    static Path TEST_ORACLE = null;

    static Set<String> ALL_ANSWERS = Set.of("a","b","c","d","e");
    static Set<String> CORRECT_ANSWERS_Q1 = Set.of("b","c");
    static Set<String> CORRECT_ANSWERS_Q2 = Set.of("a","c");
    static Set<String> CORRECT_ANSWERS_Q3 = Set.of("c");

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
    public void testSize() throws IOException {
        assertEquals(10,marks.rows().size());
    }

    @Test
    public void test11() {
        ResultTable.Row row = marks.rows().get(0);
        assertEquals("anonymous1",row.id());
        Mark mark = row.cells().get(0);

        assertEquals(ALL_ANSWERS,mark.allAnswers());
        assertEquals(CORRECT_ANSWERS_Q1,mark.correctAnswers());
        assertEquals(Set.of("b","c"),mark.correctAnswersSelected());
        assertEquals(Set.of("a"),mark.incorrectAnswersSelected());
        assertEquals(0.6666,mark.value(),0.001);   // 2/2 - 1/3 = 2/3
    }

    @Test
    public void test21() {
        ResultTable.Row row = marks.rows().get(1);
        assertEquals("anonymous2",row.id());
        Mark mark = row.cells().get(0);

        assertEquals(ALL_ANSWERS,mark.allAnswers());
        assertEquals(CORRECT_ANSWERS_Q1,mark.correctAnswers());
        assertEquals(Set.of("b"),mark.correctAnswersSelected());
        assertEquals(Set.of(),mark.incorrectAnswersSelected());
        assertEquals(0.5,mark.value(),0.001);   // 1/2 - 0 = 1/2
    }

    @Test
    public void test23() {
        ResultTable.Row row = marks.rows().get(1);
        assertEquals("anonymous2",row.id());
        Mark mark = row.cells().get(2);
        assertEquals(ALL_ANSWERS,mark.allAnswers());
        assertEquals(CORRECT_ANSWERS_Q3,mark.correctAnswers());
        assertEquals(Set.of(),mark.correctAnswersSelected());
        assertEquals(Set.of("a","b"),mark.incorrectAnswersSelected());
        assertEquals(0.0,mark.value(),0.001);   // cannot be negative, so becomes 0
    }

}
