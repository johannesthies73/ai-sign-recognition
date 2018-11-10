package de.hszg.signrecognition.evaluation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Config {

    private int initialWeightFactor;
    private float learningRate;

}
