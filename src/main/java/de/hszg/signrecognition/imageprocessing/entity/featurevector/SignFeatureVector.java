package de.hszg.signrecognition.imageprocessing.entity.featurevector;

import de.hszg.signrecognition.imageprocessing.entity.Sign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SignFeatureVector implements FeatureVector {

    private Sign sign;
    private List<SignFeature> features;

    public SignFeatureVector(Sign sign, Map<String, Integer>... colorRatiosMaps) {

        this.sign = sign;
        features = new ArrayList<>();

        Arrays.stream(colorRatiosMaps).forEach(colorRatiosMap -> {
            colorRatiosMap.forEach((colorString, ratio) -> {
                features.add(new SignFeature(colorString, roundRatio(ratio)));
            });
        });

    }

    private int roundRatio(Integer ratio) {
        int newRatio = ratio;

        //TODO: change for clustering
        if (ratio < 91) {
            newRatio = (ratio + 9) / 10 * 10;
        } else newRatio = ratio * 10 / 10;

        return newRatio;
    }

    @Override
    public Sign getSign() {
        return this.sign;
    }

    @Override
    public int getNumberOfFeatures() {
        return features.size();
    }

    @Override
    public int getFeatureValue(int i) {
        return features.get(i).getColorRatio();
    }

    @Override
    public List<Integer> getFeatureValues() {
        return features.stream().map(signFeature -> {
            return signFeature.getColorRatio();
        }).collect(Collectors.toList());
    }
}
