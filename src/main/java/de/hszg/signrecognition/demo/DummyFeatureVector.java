package de.hszg.signrecognition.demo;

import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.SignFeature;

import java.util.List;

public class DummyFeatureVector implements FeatureVector {

    private Sign sign;
    private int[] feature = new int[3];

    DummyFeatureVector(int a, int b, int c, Sign d) {
        feature[0] = a;
        feature[1] = b;
        feature[2] = c;
        sign = d;
    }


    @Override
    public Sign getSign() {

        return sign;
    }

    @Override
    public int getNumberOfFeatures() {
        return feature.length;
    }

    @Override
    public int getFeatureValue(int i) {
        return feature[i];
    }

    @Override
    public List<Integer> getFeatureValues() {
        return null;
    }

}
