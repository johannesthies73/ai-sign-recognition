package de.hszg.signrecognition.learner.perceptron.entity;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RatioResult {

    private double ratioCorrect;
    private double ratioWrong;
    private double ratioUnknown;

    public RatioResult(double ratioCorrect, double ratioWrong, double ratioUnknown) {
        this.ratioCorrect = ratioCorrect;
        this.ratioWrong = ratioWrong;
        this.ratioUnknown = ratioUnknown;
    }

    public double getRatioCorrect() {
        return ratioCorrect;
    }

    public double getRatioWrong() {
        return ratioWrong;
    }

    public double getRatioUnknown() {
        return ratioUnknown;
    }

    public static Double calcRatioCorrect(Map<FeatureVector, Sign> answerGuessMap) {
        AtomicInteger numberOfCorrectGuesses = new AtomicInteger(0);

        answerGuessMap.forEach((featureVector, sign) -> {
            Sign answer = featureVector.getSign();
            Sign guess = sign;

            if (answer == guess) {
                numberOfCorrectGuesses.getAndIncrement();
            }
        });

        return Double.valueOf(100 * numberOfCorrectGuesses.get() / answerGuessMap.size());
    }

    public static Double calcRatioWrong(Map<FeatureVector, Sign> answerGuessMap) {
        AtomicInteger numberOfWrongGuesses = new AtomicInteger(0);

        answerGuessMap.forEach((featureVector, sign) -> {
            Sign answer = featureVector.getSign();
            Sign guess = sign;

            if (guess != answer && guess != Sign.UNKNOWN) {
                numberOfWrongGuesses.getAndIncrement();
            }
        });

        return Double.valueOf(numberOfWrongGuesses.get());
    }

    public static Double calcRatioUnknown(Map<FeatureVector, Sign> answerGuessMap) {
        AtomicInteger numberOfUnknownGuesses = new AtomicInteger(0);

        answerGuessMap.forEach((featureVector, sign) -> {
            Sign guess = sign;

            if (guess == Sign.UNKNOWN) {
                numberOfUnknownGuesses.getAndIncrement();
            }
        });

        return Double.valueOf(numberOfUnknownGuesses.get());
    }
}
