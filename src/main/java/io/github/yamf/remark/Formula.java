package io.github.yamf.remark;

import java.util.Set;

/**
 * Abstraction for how to compute correct marks.
 * @author jens dietrich
 */
public interface Formula {
    Mark compute (String question,Set<String> answersSelected, Set<String> correctAnswers, Set<String> allAnswers, int scalingFactorUsed);

    default Mark compute (String question,Set<String> answersSelected, Set<String> correctAnswers, Set<String> allAnswers) {
        return compute(question, answersSelected, correctAnswers, allAnswers, 1);
    }
}
