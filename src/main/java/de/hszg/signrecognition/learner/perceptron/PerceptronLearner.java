package de.hszg.signrecognition.learner.perceptron;

import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.entity.*;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class PerceptronLearner implements Learner {

    private Perceptron p1;
    private Perceptron p2;
    private Perceptron p3;

    private static final String OUTPUT_FILENAME = "neuralNetwork.dat";
    private static final String INPUT_FILENAME = "featureVectors.dat";
    private static final float PICK_RATE = 0.2f; //0.2
    private static final int ITERATIONS = 20; //20
    private static final double LEARNING_RATE = 0.1; //0.1


    @Override
    public void learn(List<FeatureVector> trainingSet) {

        //FeatureVectorUtil.writeFeatureVectorsIntoFile("trainingSet.dat", trainingSet);

        p1 = new Perceptron(trainingSet.get(0).getNumberOfFeatures(), 0, LEARNING_RATE);
        p2 = new Perceptron(trainingSet.get(0).getNumberOfFeatures(), 1, LEARNING_RATE);
        p3 = new Perceptron(trainingSet.get(0).getNumberOfFeatures(), 2, LEARNING_RATE);

        //train neural network
        System.out.println("training neural network...");
        for (int i = 0; i < ITERATIONS; i++) {
            System.out.println("Iteration " + (i + 1));

            trainingSet.stream().forEach(featureVector -> {
                p1.trainPerceptron(featureVector);
                p2.trainPerceptron(featureVector);
                p3.trainPerceptron(featureVector);
            });
        }

        System.out.println("finished Training!");

        Perceptron.writeNeuralNetworkIntoFile(OUTPUT_FILENAME, Arrays.asList(p1, p2, p3));

    }

    public static void main(String[] args) {

        new PerceptronLearner().learn(
                FeatureVectorUtil.getRandomFeatureVectors(INPUT_FILENAME, PICK_RATE)
//                FeatureVectorUtil.loadFeatureVectorsFromFile(INPUT_FILENAME)
        );
    }

}
