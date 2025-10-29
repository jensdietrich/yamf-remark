package io.github.yamf.remark;

import java.util.Set;

/**
 * Abstraction for how to compute correct marks.
 * @author jens dietrich
 */
public interface Formula {
    Mark compute (String question,Set<String> answersSelected, Set<String> correctAnswers, Set<String> allAnswers);
}
