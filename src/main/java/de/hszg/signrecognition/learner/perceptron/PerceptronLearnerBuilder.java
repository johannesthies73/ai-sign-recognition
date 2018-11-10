package de.hszg.signrecognition.learner.perceptron;

public class PerceptronLearnerBuilder {
    private int numberOfInputsPerNeuron;
    private double learningRate;
    private int initialWeightFactor;

    public PerceptronLearnerBuilder setNumberOfInputsPerNeuron(int numberOfInputsPerNeuron) {
        this.numberOfInputsPerNeuron = numberOfInputsPerNeuron;
        return this;
    }

    public PerceptronLearnerBuilder setLearningRate(double learningRate) {
        this.learningRate = learningRate;
        return this;
    }

    public PerceptronLearnerBuilder setInitialWeightFactor(int initialWeightFactor) {
        this.initialWeightFactor = initialWeightFactor;
        return this;
    }

    public PerceptronLearner createPerceptronLearner() {
        return new PerceptronLearner(numberOfInputsPerNeuron, learningRate, initialWeightFactor);
    }
}