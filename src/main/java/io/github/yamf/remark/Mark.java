package io.github.yamf.remark;

import com.google.common.base.Preconditions;
import java.util.Set;

/**
 * Data structure representing marks.
 * @param value
 * @param correctAnswersSelected
 * @param incorrectAnswersSelected
 * @param correctAnswers
 * @param allAnswers
 * @param scalingFactorUsed
 */
public record Mark (double value, String question, Set<String> correctAnswersSelected,Set<String> incorrectAnswersSelected, Set<String> correctAnswers,Set<String> allAnswers,int scalingFactorUsed){

    public Mark(double value, String question, Set<String> correctAnswersSelected, Set<String> incorrectAnswersSelected, Set<String> correctAnswers, Set<String> allAnswers,int scalingFactorUsed) {
        this.value = value;
        this.correctAnswersSelected = correctAnswersSelected;
        this.incorrectAnswersSelected = incorrectAnswersSelected;
        this.correctAnswers = correctAnswers;
        this.allAnswers = allAnswers;
        this.question = question;
        this.scalingFactorUsed = scalingFactorUsed;

        // verify
        Preconditions.checkArgument(allAnswers.containsAll(correctAnswers));
        Preconditions.checkArgument(allAnswers.containsAll(incorrectAnswersSelected));
        Preconditions.checkArgument(correctAnswers.containsAll(correctAnswersSelected));
        Preconditions.checkArgument(scalingFactorUsed>0,"Scaling factor used should be greater than 0");
    }
}

