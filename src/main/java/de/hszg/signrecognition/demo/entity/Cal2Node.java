package de.hszg.signrecognition.demo.entity;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;

import java.util.ArrayList;
import java.util.List;

public class Cal2Node {

    private Integer searchDepth;
    private Sign sign;
    private List<Double> weights = new ArrayList<>();
    private List<Cal2Node> followingNodes = new ArrayList<>();


    public Cal2Node() {
    }

    public Cal2Node(int searchDepth, Sign sign) {
        this.searchDepth = searchDepth;
        this.sign = sign;
        followingNodes.add(new Cal2Node(0, Sign.LEFT));
    }

    public void update(FeatureVector featureVector) {
        if (sign != null && sign.equals(featureVector.getSign()) && searchDepth < featureVector.getNumberOfFeatures()) {
            return;
        }

        if (sign != null && !sign.equals(featureVector.getSign())) {
            sign = null;
            followingNodes.add(new Cal2Node(searchDepth + 1, featureVector.getSign()));
/*
            weights.add(featureVector.getFeatureValue(searchDepth));
*/
            return;
        }

        if (sign == null) {
            if (weights.contains(featureVector.getFeatureValue(searchDepth))) {
                int i = weights.indexOf(featureVector.getFeatureValue(searchDepth));
                followingNodes.get(i).update(featureVector);
            } else {
                followingNodes.add(new Cal2Node(searchDepth, featureVector.getSign()));
                //TODO: liste zu arrays ändern
/*
                weights.add(featureVector.getFeatureValue(searchDepth));
*/
            }
        }

    }
}
