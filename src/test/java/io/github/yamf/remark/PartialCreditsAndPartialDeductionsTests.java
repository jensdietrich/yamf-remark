package io.github.yamf.remark;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartialCreditsAndPartialDeductionsTests {

    private Formula formula = new PartialCreditsAndPartialDeductions();

    @Test
    public void test22CorrectOIncorrect() {
        Set<String> allAnswers = Set.of("a","b","c","d","e");
        Set<String> correctAnswers = Set.of("a","b");
        Set<String> selected = Set.of("a","b");
        Mark mark = formula.compute(selected, correctAnswers, allAnswers);

        assertEquals(1.0,mark.value());
        assertEquals(allAnswers,mark.allAnswers());
        assertEquals(correctAnswers,mark.correctAnswers());
        assertEquals(selected,mark.correctAnswersSelected());
        assertEquals(Set.of(),mark.incorrectAnswersSelected());
    }

    @Test
    public void test12CorrectOIncorrect() {
        Set<String> allAnswers = Set.of("a","b","c","d","e");
        Set<String> correctAnswers = Set.of("a","b");
        Set<String> selected = Set.of("a");
        Mark mark = formula.compute(selected, correctAnswers, allAnswers);

        assertEquals(0.5,mark.value());
        assertEquals(allAnswers,mark.allAnswers());
        assertEquals(correctAnswers,mark.correctAnswers());
        assertEquals(selected,mark.correctAnswersSelected());
        assertEquals(Set.of(),mark.incorrectAnswersSelected());
    }

    @Test
    public void test55CorrectOIncorrect() {
        Set<String> allAnswers = Set.of("a","b","c","d","e");
        Set<String> correctAnswers = Set.of("a","b","c","d","e");
        Set<String> selected = Set.of("a","b","c","d","e");
        Mark mark = formula.compute(selected, correctAnswers, allAnswers);

        assertEquals(1.0,mark.value());
        assertEquals(allAnswers,mark.allAnswers());
        assertEquals(correctAnswers,mark.correctAnswers());
        assertEquals(selected,mark.correctAnswersSelected());
        assertEquals(Set.of(),mark.incorrectAnswersSelected());
    }

    @Test
    public void test22Correct13Incorrect() {
        Set<String> allAnswers = Set.of("a","b","c","d","e");
        Set<String> correctAnswers = Set.of("a","b");
        Set<String> selected = Set.of("a","b","c");
        Mark mark = formula.compute(selected, correctAnswers, allAnswers);   // 2/2 - 1/3 = 0.66

        assertEquals(0.66666,mark.value(),0.001);
        assertEquals(allAnswers,mark.allAnswers());
        assertEquals(correctAnswers,mark.correctAnswers());
        assertEquals(Set.of("a","b"),mark.correctAnswersSelected());
        assertEquals(Set.of("c"),mark.incorrectAnswersSelected());
    }

    @Test
    public void test12Correct13Incorrect() {
        Set<String> allAnswers = Set.of("a","b","c","d","e");
        Set<String> correctAnswers = Set.of("a","b");
        Set<String> selected = Set.of("a","c");
        Mark mark = formula.compute(selected, correctAnswers, allAnswers);   // 1/2 - 1/3 = 0.16666

        assertEquals(0.166666,mark.value(),0.001);
        assertEquals(allAnswers,mark.allAnswers());
        assertEquals(correctAnswers,mark.correctAnswers());
        assertEquals(Set.of("a"),mark.correctAnswersSelected());
        assertEquals(Set.of("c"),mark.incorrectAnswersSelected());
    }

    @Test
    public void testEverythingSelected() {
        Set<String> allAnswers = Set.of("a","b","c","d","e");
        Set<String> correctAnswers = Set.of("a","b");
        Set<String> selected = Set.of("a","b","c","d","e");
        Mark mark = formula.compute(selected, correctAnswers, allAnswers);   // 2/2 - 1/3 = 0.66

        assertEquals(0.0,mark.value());
        assertEquals(allAnswers,mark.allAnswers());
        assertEquals(correctAnswers,mark.correctAnswers());
        assertEquals(Set.of("a","b"),mark.correctAnswersSelected());
        assertEquals(Set.of("c","d","e"),mark.incorrectAnswersSelected());
    }


}
