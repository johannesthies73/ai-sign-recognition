package de.hszg.signrecognition.learner.knearestneighnors.entity;

import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;

public class FvDiffPair {

    private FeatureVector featureVector;
    private double difference;

    public FvDiffPair(FeatureVector featureVector, double difference) {
        this.featureVector = featureVector;
        this.difference = difference;
    }

    public FeatureVector getFeatureVector() {
        return featureVector;
    }

    public double getDifference() {
        return difference;
    }

    public void setFeatureVector(FeatureVector featureVector) {
        this.featureVector = featureVector;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }
}
