package de.hszg.signrecognition.learner.kmeans.entity;

import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cluster {

    private String id;
    private List<Integer> centroid;
    private List<FeatureVector> featureVectors;

    public Cluster(String id, List<Integer> centroid) {
        this.id = id;
        this.centroid = centroid;
    }
}
