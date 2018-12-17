package de.hszg.signrecognition.learner.knearestneighnors;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.knearestneighnors.entity.FvDiffPair;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class KnnLearner implements Learner {

    private final int K;
    private FvDiffPair[] kNearestVectors;

    private List<FeatureVector> trainingSet;

    public KnnLearner(int k) {
        this.K = k;
    }

    @Override
    public void learn(List<FeatureVector> trainingSet) {
        this.trainingSet = trainingSet;
    }

    @Override
    public Sign classify(FeatureVector example) {

        resetkNearestVectors();

        for (FeatureVector featureVector : trainingSet) {
            updateNearestVectors(featureVector, calcDifference(featureVector, example));
        }

        Map<Sign, AtomicInteger> signAmountMap = new HashMap<>();
        initMap(signAmountMap);
        for (FvDiffPair pair : kNearestVectors) {

            try {
                signAmountMap.get(pair.getFeatureVector().getSign()).getAndIncrement();
            } catch (Exception e) {
                log.error("failed to classify Featurevector");
            }
        }

        return bestGuess(signAmountMap);
    }

    private void resetkNearestVectors() {
        kNearestVectors = new FvDiffPair[K];
        for (int i = 0; i < K; i++) {
            kNearestVectors[i] = new FvDiffPair(null, Double.MAX_VALUE);
        }

    }

    private Sign bestGuess(Map<Sign, AtomicInteger> signAmountMap) {
        AtomicReference<Sign> guess = new AtomicReference<>(Sign.UNKNOWN);
        AtomicInteger integer = new AtomicInteger(0);

        signAmountMap.forEach((sign, atomicInteger) -> {
            if (atomicInteger.get() > integer.get()) {
                guess.set(sign);
            }
        });

        return guess.get();
    }

    private void initMap(Map<Sign, AtomicInteger> signAmountMap) {
        signAmountMap.put(Sign.LEFT, new AtomicInteger(0));
        signAmountMap.put(Sign.RIGHT, new AtomicInteger(0));
        signAmountMap.put(Sign.STOP, new AtomicInteger(0));
        signAmountMap.put(Sign.PRIORITY_ROAD, new AtomicInteger(0));
        signAmountMap.put(Sign.YIELD, new AtomicInteger(0));
        signAmountMap.put(Sign.YIELD_RIGHT, new AtomicInteger(0));
    }

    private double calcDifference(FeatureVector featureVector, FeatureVector example) {
        double sumDifference = 0;

        for (int i = 0; i < featureVector.getFeatureValues().size(); i++) {
            sumDifference += biggerValueMinusSmallerValue(featureVector.getFeatureValue(i), example.getFeatureValue(i));
        }

        return sumDifference;
    }

    private void updateNearestVectors(FeatureVector featureVector, double sumDifference) {

        FvDiffPair pair = findBiggestPair(kNearestVectors);

        for (FvDiffPair kNearestVector : kNearestVectors) {
            //TODO: pair is null!!
            if (kNearestVector != null && featureVector != null && pair != null) {
                if (pair.equals(kNearestVector)) {
                    kNearestVector.setFeatureVector(featureVector);
                    kNearestVector.setDifference(sumDifference);
                }
            }
        }

    }

    private FvDiffPair findBiggestPair(FvDiffPair[] kNearestVectors) {
        double currentBiggest = 0;
        FvDiffPair temp = null;
        for (FvDiffPair pair : kNearestVectors) {
            if (pair.getDifference() > currentBiggest) {
                currentBiggest = pair.getDifference();
                temp = pair;
            }
        }
        return temp;
    }

    private double biggerValueMinusSmallerValue(int value1, int value2) {
        if (value1 > value2) {
            return value1 - value2;
        } else return value2 - value1;
    }

}
