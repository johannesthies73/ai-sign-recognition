package de.hszg.signrecognition.evaluation.entity;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;

public class FeatureVectorSignPair {

    private FeatureVector featureVector;

    private Sign sign;

    public FeatureVectorSignPair(FeatureVector featureVector, Sign sign) {
        this.featureVector = featureVector;
        this.sign = sign;
    }

    public FeatureVector getFeatureVector() {
        return featureVector;
    }

    public Sign getSign() {
        return sign;
    }
}
