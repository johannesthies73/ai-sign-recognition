package de.hszg.signrecognition.evaluation;


import de.hszg.signrecognition.evaluation.entity.FeatureVectorSignPair;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.perceptron.entity.Perceptron;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Evaluator {

    public static void main(String[] args) {
        new Evaluator().evaluate(
                FeatureVectorUtil.loadFeatureVectorsFromFile("featureVectors.dat"),
                Perceptron.loadNeuralNetworkFromFile("neuralNetwork.dat")
        );

    }

    private void evaluate(List<FeatureVector> vectorsToClassify, List<Perceptron> trainedNeuralNetwork) { //TODO:LEARNER übergeben um generischer zu macehn??
        List<FeatureVectorSignPair> featureVectorSignPairs = new ArrayList<>();

        System.out.println("calculating ratio of right guesses...");
        vectorsToClassify.stream().forEach(featureVector -> {
            int bit1 = trainedNeuralNetwork.get(0).guess(featureVector);
            int bit2 = trainedNeuralNetwork.get(1).guess(featureVector);
            int bit3 = trainedNeuralNetwork.get(2).guess(featureVector);

            Sign guess = Perceptron.guessSign(bit1, bit2, bit3);

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
