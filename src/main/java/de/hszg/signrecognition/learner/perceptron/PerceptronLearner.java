package de.hszg.signrecognition.learner.perceptron;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.entity.*;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Data
public class PerceptronLearner implements Learner {

    private Perceptron p1;
    private Perceptron p2;
    private Perceptron p3;

    private final double LEARNING_RATE;
    private final int INITIAL_WEIGHT_FACTOR;
    private final float COVERAGE;
    private int count;

    public PerceptronLearner(int numberOfInputsPerNeuron, double learningRate, int initialWeightFactor, float coverage) {
        p1 = new Perceptron(numberOfInputsPerNeuron, 0, initialWeightFactor, learningRate);
        p2 = new Perceptron(numberOfInputsPerNeuron, 1, initialWeightFactor, learningRate);
        p3 = new Perceptron(numberOfInputsPerNeuron, 2, initialWeightFactor, learningRate);

        this.LEARNING_RATE = learningRate;
        this.INITIAL_WEIGHT_FACTOR = initialWeightFactor;
        this.COVERAGE = coverage;
        this.count = 0;

    }

    @Override
    public void learn(List<FeatureVector> trainingSet) {
        //train neural network until coverage is reached
        while (!everyTrainingsVectorCorrect(trainingSet, COVERAGE)) {
            count++;

            trainingSet.stream().forEach(featureVector -> {
                p1.trainPerceptron(featureVector);
                p2.trainPerceptron(featureVector);
                p3.trainPerceptron(featureVector);
            });

        }
    }

    @Override
    public Sign classify(FeatureVector featureVector) {
        int bit1 = p1.guess(featureVector.getFeatureValues());
        int bit2 = p2.guess(featureVector.getFeatureValues());
        int bit3 = p3.guess(featureVector.getFeatureValues());

        Sign guess = Perceptron.guessSign(bit1, bit2, bit3);

        return guess;
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

}
