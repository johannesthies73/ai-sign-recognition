package de.hszg.signrecognition.evaluation.entity;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;

public class RightAnswerGuessPair {

    private FeatureVector rightAnswer;

    private Sign guess;

    public RightAnswerGuessPair(FeatureVector rightAnswer, Sign guess) {
        this.rightAnswer = rightAnswer;
        this.guess = guess;
    }

    public FeatureVector getRightAnswer() {
        return rightAnswer;
    }

    public Sign getGuess() {
        return guess;
    }
}
