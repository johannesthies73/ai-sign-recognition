package de.hszg.signrecognition.evaluation.entity;

public class Series {

    String name;
    double averageValue;
    double lowerBound;
    double upperbound;

    public Series(String name, double averageValue, double lowerBound, double upperbound) {
        this.name = name;
        this.averageValue = averageValue;
        this.lowerBound = lowerBound;
        this.upperbound = upperbound;
    }


    public String getName() {
        return name;
    }

    public double getAverageValue() {
        return averageValue;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperbound() {
        return upperbound;


    }

}


