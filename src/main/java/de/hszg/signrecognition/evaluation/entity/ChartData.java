package de.hszg.signrecognition.evaluation.entity;

import java.util.List;

public class ChartData {

    List<Series> seriesList;

    public ChartData(List<Series> seriesList) {
        this.seriesList = seriesList;
    }

    public List<Series> getSeriesList() {
        return seriesList;
    }
}
