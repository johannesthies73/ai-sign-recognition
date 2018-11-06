package de.hszg.signrecognition.evaluation;


import de.hszg.signrecognition.evaluation.entity.RightAnswerGuessPair;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.PerceptronLearner;
import de.hszg.signrecognition.learner.perceptron.PerceptronLearnerBuilder;
import de.hszg.signrecognition.learner.perceptron.entity.RatioResult;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
public class Evaluator {

    private static String INPUT_FILENAME = "featureVectors.dat";
    private static float PICK_RATE = 0.1f;
    private static int NUMBER_OF_LEARNING_INSTANCES = 20;
    private static int LEARNING_ITERATIONS = 300;
    private static float LEARNING_RATE = 0.01f;


    public static void main(String[] args) {

        List<RatioResult> l1 = calculateRatios(1);
        List<RatioResult> l2 = calculateRatios(100);
        List<RatioResult> l3 = calculateRatios(10000);
        List<RatioResult> l4 = calculateRatios(1000000);

        double[] correctRatios1 = calulateStandardDeviationCorrect(l1);
        double[] correctRatios2 = calulateStandardDeviationCorrect(l2);
        double[] correctRatios3 = calulateStandardDeviationCorrect(l3);
        double[] correctRatios4 = calulateStandardDeviationCorrect(l4);

        StandardDeviation standardDeviation = new StandardDeviation();
        double deviation1 = standardDeviation.evaluate(correctRatios1);
        double deviation2 = standardDeviation.evaluate(correctRatios2);
        double deviation3 = standardDeviation.evaluate(correctRatios3);
        double deviation4 = standardDeviation.evaluate(correctRatios4);

        Pair<Double, Double> confidenceInterval1 = calculateConfidenceInterval(deviation1, correctRatios1);
        Pair<Double, Double> confidenceInterval2 = calculateConfidenceInterval(deviation2, correctRatios2);
        Pair<Double, Double> confidenceInterval3 = calculateConfidenceInterval(deviation3, correctRatios3);
        Pair<Double, Double> confidenceInterval4 = calculateConfidenceInterval(deviation4, correctRatios4);

        System.out.println();

    }

    private static Pair<Double, Double> calculateConfidenceInterval(double deviation, double[] correctRatios) {
        float sigma = 1.645f;
        double avg = calculateAvg(correctRatios);

        double lowerBound = avg - (sigma * deviation) / Math.sqrt(correctRatios.length);
        double upperBound = avg + (sigma * deviation) / Math.sqrt(correctRatios.length);


        Pair<Double, Double> confidenceIntervall = new Pair<Double, Double>(lowerBound, upperBound);
        return confidenceIntervall;
    }

    private static double calculateAvg(double[] correctRatios) {
        double avg = 0;
        for (double ratio : correctRatios) {
            avg += ratio;
        }

        return avg / correctRatios.length;
    }

    private static double[] calulateStandardDeviationCorrect(List<RatioResult> l1) {
        double[] values = new double[l1.size()];
        int i2 = 0;

        AtomicInteger i = new AtomicInteger(0);
        l1.stream().forEach(ratioResult -> {
            values[i.get()] = ratioResult.getRatioCorrect();
            i.getAndIncrement();
        });

        return values;
    }

    private static List<RatioResult> calculateRatios(int initialWeightsFactor) {
        List<RatioResult> resultRatios = new ArrayList<>();

        Evaluator evaluator = new Evaluator();
        for (int i = 0; i < NUMBER_OF_LEARNING_INSTANCES; i++) {
            Pair<List<FeatureVector>/*trainingSet*/, List<FeatureVector>/*classifySet*/> fvPair = FeatureVectorUtil.getRandomFeatureVectors(INPUT_FILENAME, PICK_RATE);

            PerceptronLearner perceptronLearner = new PerceptronLearnerBuilder()
                    .setLearningIterations(LEARNING_ITERATIONS)
                    .setInitialWeightFactor(initialWeightsFactor)
                    .setLearningRate(LEARNING_RATE)
                    .createPerceptronLearner();

            perceptronLearner.learn(fvPair.getKey());

            resultRatios.add(evaluator.classifyFeatureVectors(fvPair.getValue(), perceptronLearner));


            log.debug("Learning Instance " + i + "\t-> correct: " + Math.round(resultRatios.get(i).getRatioCorrect()) + " %");
        }
        log.debug("############################");
        return resultRatios;
    }

    private RatioResult classifyFeatureVectors(List<FeatureVector> vectorsToClassify, Learner learner) { //TODO:LEARNER übergeben um generischer zu macehn??
        List<RightAnswerGuessPair> rightAnswerGuessPairs = new ArrayList<>();

        vectorsToClassify.stream().forEach(featureVector -> {

            Sign guess = learner.classify(featureVector);
            rightAnswerGuessPairs.add(new RightAnswerGuessPair(featureVector, guess));
        });

        RatioResult result = countCorrectGuesses(rightAnswerGuessPairs);
        return result;

    }

    private RatioResult countCorrectGuesses(List<RightAnswerGuessPair> rightAnswerGuessPairs) {

        AtomicInteger numberOfCorrectGuesses = new AtomicInteger(0);
        AtomicInteger numberOfWrongGuesses = new AtomicInteger(0);
        AtomicInteger numberOfUnknownGuesses = new AtomicInteger(0);

        rightAnswerGuessPairs.parallelStream().forEach((featureVectorSignPair) -> {
            Sign answer = featureVectorSignPair.getRightAnswer().getSign();
            Sign guess = featureVectorSignPair.getGuess();

            if (answer == guess) {
                numberOfCorrectGuesses.getAndIncrement();
            } else if (guess == Sign.UNKNOWN) {
                numberOfWrongGuesses.getAndIncrement();
            } else numberOfUnknownGuesses.getAndIncrement();

        });

        return new RatioResult((double) numberOfCorrectGuesses.get() / rightAnswerGuessPairs.size() * 100,
                (double) numberOfWrongGuesses.get() / rightAnswerGuessPairs.size() * 100,
                (double) numberOfUnknownGuesses.get() / rightAnswerGuessPairs.size() * 100);
    }

    private static double calcAvgRatio(List<Double> ratios) {
        double avgRatio = 0;
        for (Double ratio : ratios) {
            avgRatio += ratio;
        }

        avgRatio /= ratios.size();
        return avgRatio;
    }
}
