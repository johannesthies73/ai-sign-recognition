package de.hszg.signrecognition.learner.perceptron;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.entity.*;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@NoArgsConstructor
public class PerceptronLearner implements Learner {

    private Perceptron p1;
    private Perceptron p2;
    private Perceptron p3;

    private double learningRate;
    private int initialWeightFactor;

    public PerceptronLearner(int numberOfInputsPerNeuron, double learningRate, int initialWeightFactor) {
        this.learningRate = learningRate;
        this.initialWeightFactor = initialWeightFactor;

        p1 = new Perceptron(numberOfInputsPerNeuron, 0, initialWeightFactor, learningRate);
        p2 = new Perceptron(numberOfInputsPerNeuron, 1, initialWeightFactor, learningRate);
        p3 = new Perceptron(numberOfInputsPerNeuron, 2, initialWeightFactor, learningRate);
    }

    @Override
    public int learn(List<FeatureVector> trainingSet, float coverage) {
        int numberOfLearningIterations = 0;

        //train neural network until coverage is reached
//        while (!everyTrainingsVectorCorrect(trainingSet, coverage)) {
//            numberOfLearningIterations++;
//
//            trainingSet.stream().forEach(featureVector -> {
//                p1.trainPerceptron(featureVector);
//                p2.trainPerceptron(featureVector);
//                p3.trainPerceptron(featureVector);
//            });
//
//        }
        int z = 0;
        while (z < 500) {
            z++;
            numberOfLearningIterations++;
            trainingSet.stream().forEach(featureVector -> {
                p1.trainPerceptron(featureVector);
                p2.trainPerceptron(featureVector);
                p3.trainPerceptron(featureVector);
            });

        }

        return numberOfLearningIterations;


//        Perceptron.writeNeuralNetworkIntoFile(OUTPUT_FILENAME, Arrays.asList(p1, p2, p3));

    }

    private boolean everyTrainingsVectorCorrect(List<FeatureVector> trainingSet, float coverage) {

        AtomicInteger right = new AtomicInteger();
        trainingSet.parallelStream().forEach(featureVector -> {
            if (featureVector.getSign() == classify(featureVector)) {
                right.getAndIncrement();
            }
        });

        double actualRight = 100 * right.get() / trainingSet.size();
        if (actualRight / 100 >= coverage) {
            return true;
        }
        return false;
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
