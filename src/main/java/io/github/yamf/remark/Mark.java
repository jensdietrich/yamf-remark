package io.github.yamf.remark;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Data structure representing marks.
 * @param value
 * @param correctAnswersSelected
 * @param incorrectAnswersSelected
 * @param correctAnswers
 * @param allAnswers
 */
public record Mark (double value, String question, Set<String> correctAnswersSelected,Set<String> incorrectAnswersSelected, Set<String> correctAnswers,Set<String> allAnswers){

    public Mark(double value, String question, Set<String> correctAnswersSelected, Set<String> incorrectAnswersSelected, Set<String> correctAnswers, Set<String> allAnswers) {
        this.value = value;
        this.correctAnswersSelected = correctAnswersSelected;
        this.incorrectAnswersSelected = incorrectAnswersSelected;
        this.correctAnswers = correctAnswers;
        this.allAnswers = allAnswers;
        this.question = question;

        // verify
        Preconditions.checkArgument(allAnswers.containsAll(correctAnswers));
        Preconditions.checkArgument(allAnswers.containsAll(incorrectAnswersSelected));
        Preconditions.checkArgument(correctAnswers.containsAll(correctAnswersSelected));
    }
}

