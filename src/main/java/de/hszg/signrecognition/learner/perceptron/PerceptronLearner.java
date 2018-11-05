package de.hszg.signrecognition.learner.perceptron;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.entity.*;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@NoArgsConstructor
public class PerceptronLearner implements Learner {

    private Perceptron p1;
    private Perceptron p2;
    private Perceptron p3;

    private String outputFilename;
    private int learningIterations;
    private double learningRate;
    private int initialWeightFactor;

    public PerceptronLearner(String outputFilename, int learningIterations, double learningRate, int initialWeightFactor) {
        this.outputFilename = outputFilename;
        this.learningIterations = learningIterations;
        this.learningRate = learningRate;
        this.initialWeightFactor = initialWeightFactor;
    }

    @Override
    public void learn(List<FeatureVector> trainingSet) {

        p1 = new Perceptron(trainingSet.get(0).getNumberOfFeatures(), 0, learningRate, initialWeightFactor);
        p2 = new Perceptron(trainingSet.get(0).getNumberOfFeatures(), 1, learningRate, initialWeightFactor);
        p3 = new Perceptron(trainingSet.get(0).getNumberOfFeatures(), 2, learningRate, initialWeightFactor);

        //train neural network
        System.out.println("training neural network...");
        for (int i = 0; i < learningIterations; i++) {
//            System.out.println("Iteration " + (i + 1));

            trainingSet.stream().forEach(featureVector -> {
                p1.trainPerceptron(featureVector);
                p2.trainPerceptron(featureVector);
                p3.trainPerceptron(featureVector);
            });
        }

        System.out.println("finished Training!");

//        Perceptron.writeNeuralNetworkIntoFile(OUTPUT_FILENAME, Arrays.asList(p1, p2, p3));

    }

    @Override
    public Sign classify(FeatureVector featureVector) {
        int bit1 = p1.guess(featureVector.getFeatureValues());
        int bit2 = p2.guess(featureVector.getFeatureValues());
        int bit3 = p3.guess(featureVector.getFeatureValues());

        Sign guess = Perceptron.guessSign(bit1, bit2, bit3);

        return guess;
    }

}
