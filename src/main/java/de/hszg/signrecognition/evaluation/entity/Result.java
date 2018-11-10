package de.hszg.signrecognition.evaluation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result {

    private double avgRatio;
    private double runtime;
    private int avgNumberOfLearningIterations;
}
