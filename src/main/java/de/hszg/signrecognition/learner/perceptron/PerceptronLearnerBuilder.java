package de.hszg.signrecognition.learner.perceptron;

public class PerceptronLearnerBuilder {
    private String outputFilename;
    private int learningIterations;
    private double learningRate;
    private int initialWeightFactor;

    public PerceptronLearnerBuilder setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
        return this;
    }

    public PerceptronLearnerBuilder setLearningIterations(int learningIterations) {
        this.learningIterations = learningIterations;
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
        return new PerceptronLearner(outputFilename, learningIterations, learningRate, initialWeightFactor);
    }
}