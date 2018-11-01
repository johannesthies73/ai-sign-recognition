package de.hszg.signrecognition.imageprocessing.entity.featurevector;

import de.hszg.signrecognition.imageprocessing.entity.Sign;

import java.io.Serializable;
import java.util.List;

public interface FeatureVector extends Serializable {

    Sign getSign();

    int getNumberOfFeatures();

    int getFeatureValue(int i);

    List<Integer> getFeatureValues();
}
