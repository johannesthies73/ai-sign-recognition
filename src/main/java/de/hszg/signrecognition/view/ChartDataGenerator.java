package de.hszg.signrecognition.view;

import de.hszg.signrecognition.evaluation.entity.ChartData;
import de.hszg.signrecognition.evaluation.entity.ConfigResult;
import de.hszg.signrecognition.evaluation.entity.Series;
import de.hszg.signrecognition.learner.perceptron.entity.RatioResult;
import javafx.util.Pair;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ChartDataGenerator {


    public ChartData generate(SortedMap<Double, Integer>... avgRatiosAndWeightFactorsMaps) {

        List<Series> seriesList = new ArrayList<>();

        for (int i = 0; i < avgRatiosAndWeightFactorsMaps.length; i++) {
            avgRatiosAndWeightFactorsMaps[i].forEach((avgRatio, weightsFactor) -> {
                seriesList.add(new Series(weightsFactor + "", avgRatio, avgRatio - 5, avgRatio + 5));
            });
        }

        return new ChartData(seriesList);
    }

    public ChartData generate(List<ConfigResult> configResults) {

        List<Series> seriesList = new ArrayList<>();

        for (ConfigResult configResult : configResults) {
            seriesList.add(new Series(
                    configResult.getConfig().getInitialWeightFactor() + "",
                    configResult.getResult().getAvgRatio(),
                    configResult.getResult().getAvgRatio() - 0.1,
                    configResult.getResult().getAvgRatio() + 0.1));
        }

        return new ChartData(seriesList);
    }


    public ChartData generate(SortedMap<List<RatioResult>, Integer> ratioResults) {
        List<Series> seriesList = new ArrayList<>();

        ratioResults.forEach((ratioResultList, initialWeightsFactor) -> {
            double[] ratiosArray = getCorrectRatiosAsArray(ratioResultList, "correct");

            double deviation = new StandardDeviation().evaluate(ratiosArray);

            double avg = calcAvgRatio(ratiosArray);
            Pair<Double, Double> confidenceInterval = calculateConfidenceInterval(deviation, avg, ratiosArray);

            seriesList.add(new Series(initialWeightsFactor + "", avg, confidenceInterval.getKey(), confidenceInterval.getValue()));

        });

        return new ChartData(seriesList);
    }


    private static Pair<Double, Double> calculateConfidenceInterval(double deviation, double avg, double[] ratiosArray) {

        float z = 2.57583f;

        double lowerBound = avg - ((z * deviation) / Math.sqrt(ratiosArray.length));
        double upperBound = avg + ((z * deviation) / Math.sqrt(ratiosArray.length));

        Pair<Double, Double> confidenceIntervall = new Pair<Double, Double>(lowerBound, upperBound);
        return confidenceIntervall;
    }

    private static double[] getCorrectRatiosAsArray(List<RatioResult> l1, String s) {
        double[] values = new double[l1.size()];
        int i2 = 0;

        AtomicInteger i = new AtomicInteger(0);
        l1.stream().forEach(ratioResult -> {
            if (s.equals("correct")) {
                values[i.get()] = ratioResult.getRatioCorrect();
            } else if (s.equals("wrong")) {
                values[i.get()] = ratioResult.getRatioWrong();
            } else values[i.get()] = ratioResult.getRatioUnknown();

            i.getAndIncrement();
        });

        return values;
    }

    public static double calcAvgRatio(List<Double> ratios) {
        double avgRatio = 0;
        for (Double ratio : ratios) {
            avgRatio += ratio;
        }

        avgRatio /= ratios.size();
        return avgRatio;
    }

    public static double calcAvgRatio(double[] ratios) {
        double avg = 0;
        for (double ratio : ratios) {
            avg += ratio;
        }

        return avg / ratios.length;
    }

}
