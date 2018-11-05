package de.hszg.signrecognition.evaluation;


import de.hszg.signrecognition.evaluation.entity.FeatureVectorSignPair;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.PerceptronLearner;
import de.hszg.signrecognition.learner.perceptron.PerceptronLearnerBuilder;
import de.hszg.signrecognition.learner.perceptron.entity.Perceptron;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Evaluator {

    private static int NUMBER_OF_LEARNING_INSTANCES = 100;
    private static String INPUT_FILENAME = "featureVectors.dat";
    private static float PICK_RATE = 0.01f;

    public static void main(String[] args) {

        for (int i = 0; i < NUMBER_OF_LEARNING_INSTANCES; i++) {
            PerceptronLearner perceptronLearner = new PerceptronLearnerBuilder()
                    .setLearningIterations(500)
                    .setInitialWeightFactor(10)
                    .setLearningRate(0.05)
                    .createPerceptronLearner();


            Pair<List<FeatureVector>, List<FeatureVector>> fvPair = FeatureVectorUtil.getRandomFeatureVectors(INPUT_FILENAME, PICK_RATE);
            perceptronLearner.learn(fvPair.getKey());


            new Evaluator().evaluate(fvPair.getValue(), perceptronLearner);
        }
    }

    private void evaluate(List<FeatureVector> vectorsToClassify, Learner learner) { //TODO:LEARNER übergeben um generischer zu macehn??
        List<FeatureVectorSignPair> featureVectorSignPairs = new ArrayList<>();

        System.out.println("calculating ratio of right guesses...");
        vectorsToClassify.stream().forEach(featureVector -> {

            Sign guess = learner.classify(featureVector);
            featureVectorSignPairs.add(new FeatureVectorSignPair(featureVector, guess));
        });

        int ratio = countCorrectGuesses(featureVectorSignPairs);
        System.out.println("Ratio: " + ratio + " %");

    }

    private int countCorrectGuesses(List<FeatureVectorSignPair> featureVectorSignPairs) {

        AtomicInteger numberOfCorrectGuesses = new AtomicInteger();

        featureVectorSignPairs.parallelStream().forEach((featureVectorSignPair) -> {
            Sign sign1 = featureVectorSignPair.getFeatureVector().getSign();
            Sign sign2 = featureVectorSignPair.getSign();

            if (sign1 == sign2) {
                numberOfCorrectGuesses.getAndIncrement();
            }
        });

        return 100 * numberOfCorrectGuesses.get() / featureVectorSignPairs.size();
    }
}
