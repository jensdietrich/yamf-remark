package io.github.yamf.remark;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Formula that supports partial credits as well as partial deductions.
 * @author jens dietrich
 */
public class PartialCreditsAndPartialDeductions implements Formula {

    @Override
    public Mark compute(String question,Set<String> answersSelected, Set<String> correctAnswers, Set<String> allAnswers, int scalingFactorUsed) {

        Preconditions.checkArgument(!correctAnswers.isEmpty(),"The set of correct answers must not be empty");
        Preconditions.checkArgument(!allAnswers.isEmpty(),"The set of all answers must not be empty");

        Set<String> correctAnswersSelected = Sets.intersection(correctAnswers, answersSelected);
        Set<String> incorrectAnswersSelected = Sets.difference(answersSelected,correctAnswers);
        Set<String> incorrectAnswersAll = Sets.difference(allAnswers,correctAnswers);

        double credit = (double)correctAnswersSelected.size() / (double) correctAnswers.size();

        double deductions = 0.0;
        if (incorrectAnswersAll.size()>0) {
            deductions = (double)incorrectAnswersSelected.size() / (double)incorrectAnswersAll.size();
        }

        double m = credit - deductions;

        // cannot be negative
        if (m<0.0) {
            m = 0.0;
        }

        // scale
        m = scalingFactorUsed*m;

        return new Mark(m,question,correctAnswersSelected,incorrectAnswersSelected,correctAnswers,allAnswers,scalingFactorUsed);
    }

}
