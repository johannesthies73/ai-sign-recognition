package de.hszg.signrecognition.learner.kmeans;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.kmeans.entity.Cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class KmeansLearner implements Learner {

    private final int K;
    private List<Cluster> clusters;

    public KmeansLearner(int k) {
        K = k;
    }

    @Override
    public void learn(List<FeatureVector> trainingSet) {

        initCentroids(trainingSet);

        while (!clusteringFinished(clusters)) {
            assignEveryVectorToNearestCentroid(trainingSet);
        }

    }

    private void assignEveryVectorToNearestCentroid(List<FeatureVector> trainingSet) {
        trainingSet.stream().forEach(featureVector -> {
            assignVectorToClosestCluster(featureVector);
        });
    }

    private void assignVectorToClosestCluster(FeatureVector featureVector) {
        Cluster currentClosest = new Cluster();


    }

    private int calculateDistance(List<Integer> centroid, List<Integer> featureValues) {
        int sumDistance = 0;

        for (int i = 0; i < centroid.size(); i++) {
            sumDistance += biggerValueMinusSmallerValue(centroid.get(i), featureValues.get(i));
        }

        return sumDistance;
    }

    private void initCentroids(List<FeatureVector> trainingSet) {

        AtomicInteger biggestFeature = new AtomicInteger(0);
        trainingSet.stream().forEach(featureVector -> {
            featureVector.getFeatureValues().stream().forEach(integer -> {
                if (biggestFeature.get() < integer) {
                    biggestFeature.set(integer);
                }
            });
        });

        int numberOfFeatures = trainingSet.get(0).getNumberOfFeatures();
        for (int i = 0; i < K; i++) {
            clusters.add(new Cluster("Cluster " + (i + 1), initWithRandowmValues(numberOfFeatures, biggestFeature)));
        }
    }

    private List<Integer> initWithRandowmValues(int numberOfFeatures, AtomicInteger biggestFeature) {
        List<Integer> values = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfFeatures; i++) {
            values.add(random.nextInt(biggestFeature.get()));
        }

        return values;
    }

    private boolean clusteringFinished(List<Cluster> clusters) {
        return false;
    }

    @Override
    public Sign classify(FeatureVector example) {
        return null;
    }

    private double biggerValueMinusSmallerValue(int value1, int value2) {
        if (value1 > value2) {
            return value1 - value2;
        } else return value2 - value1;
    }
}
