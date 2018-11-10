package de.hszg.signrecognition.evaluation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigResult {

    private Config config;
    private Result result;
}
