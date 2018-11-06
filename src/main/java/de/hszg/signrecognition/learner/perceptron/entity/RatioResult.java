package de.hszg.signrecognition.learner.perceptron.entity;

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
}
