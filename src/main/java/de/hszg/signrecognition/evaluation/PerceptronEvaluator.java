package de.hszg.signrecognition.evaluation;


import de.hszg.signrecognition.evaluation.entity.ChartData;
import de.hszg.signrecognition.evaluation.entity.Config;
import de.hszg.signrecognition.evaluation.entity.ConfigResult;
import de.hszg.signrecognition.evaluation.entity.Result;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import de.hszg.signrecognition.learner.Learner;
import de.hszg.signrecognition.learner.perceptron.PerceptronLearner;
import de.hszg.signrecognition.learner.perceptron.PerceptronLearnerBuilder;
import de.hszg.signrecognition.learner.perceptron.entity.RatioResult;
import de.hszg.signrecognition.view.ChartDataGenerator;
import de.hszg.signrecognition.view.JsonProducer;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;


@Slf4j
public class PerceptronEvaluator {

    private static final String INPUT_FILENAME = "featureVectors.dat";
    private static final float PICK_RATE = 0.1f;//0.1f

    private static final int NUMBER_OF_LEARNING_INSTANCES_PER_CONFIG = 10; //100
    private static final float LEARNING_RATE = 1f;//1f
    private static final float COVERAGE = 0.6f;

    public static void main(String[] args) {


        log.debug("starting calculation...");
        ChartData chartData = new ChartDataGenerator().generate(Arrays.asList(
                calculateAvgRatiosForConfig(0),
                calculateAvgRatiosForConfig(10),
                calculateAvgRatiosForConfig(1000),
                calculateAvgRatiosForConfig(10000),
                calculateAvgRatiosForConfig(100000)
        ));

        String jsonString = JsonProducer.getChartDataAsString(chartData);

        writeStringIntoFile("jsonStringPerceptron.dat", jsonString);

//        new RestClient("http://localhost", 8086, "/chartdata").sendData(jsonString);

    }

    private static ConfigResult calculateAvgRatiosForConfig(int initialWeightsFactor) {
        long t = System.currentTimeMillis();

        int totalNumberOfLearningIterationsPerConfig = 0;
        List<Double> allRatiosPerConfig = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_LEARNING_INSTANCES_PER_CONFIG; i++) {
            Pair<List<FeatureVector>/*trainingSet*/, List<FeatureVector>/*classifySet*/> fvPair = FeatureVectorUtil.getRandomFeatureVectors(INPUT_FILENAME, PICK_RATE);
            List<FeatureVector> trainingSet = fvPair.getKey();
            List<FeatureVector> classifySet = fvPair.getValue();

            PerceptronLearner perceptronLearner = new PerceptronLearnerBuilder()
                    .setNumberOfInputsPerNeuron(trainingSet.get(0).getNumberOfFeatures())
                    .setInitialWeightFactor(initialWeightsFactor)
                    .setLearningRate(LEARNING_RATE)
                    .setCoverage(COVERAGE)
                    .createPerceptronLearner();


            perceptronLearner.learn(trainingSet);

            int numberOfLearningIterationsPerInstance = perceptronLearner.getCount();
            totalNumberOfLearningIterationsPerConfig += numberOfLearningIterationsPerInstance;

            Double ratio = calculateRatio(classifySet, perceptronLearner);
            allRatiosPerConfig.add(ratio);

//            log.debug("Learning Instance " + i + "\t-> correct: " + Math.round(ratio) + " % " + "-> number of learning iterations: " + numberOfLearningIterationsPerInstance);
        }

        double avgRatioPerConfig = calculateAvgRatioForConfig(allRatiosPerConfig);

        double learningRuntime = (0.0 + System.currentTimeMillis() - t) / 1000;

        int avgNumberOfLearningIterationsPerInstance = totalNumberOfLearningIterationsPerConfig / NUMBER_OF_LEARNING_INSTANCES_PER_CONFIG;


        log.debug("Results for Weightfactor " + initialWeightsFactor + ":");
        log.debug("Average Ratio: " + avgRatioPerConfig + " %");
        log.debug("Runtime: " + learningRuntime + " s");
        log.debug("Average number of learning iterations: " + avgNumberOfLearningIterationsPerInstance);
        log.debug("############################");


        return new ConfigResult(
                new Config(initialWeightsFactor, LEARNING_RATE),
                new Result(avgRatioPerConfig, learningRuntime, avgNumberOfLearningIterationsPerInstance)
        );

    }

    private static Double calculateRatio(List<FeatureVector> vectorsToClassify, Learner learner) { //TODO:LEARNER übergeben um generischer zu macehn??
        Map<FeatureVector, Sign> answerGuessMap = new HashMap<>();

        vectorsToClassify.stream().forEach(featureVector -> {

            Sign guess = learner.classify(featureVector);
            answerGuessMap.put(featureVector, guess);
        });


        return RatioResult.calcRatioCorrect(answerGuessMap); //change this method for other ratios (wrong, unknown)

    }

    private static Double calculateAvgRatioForConfig(List<Double> ratiosPerConfig) {
        double avg = 0;

        for (Double value : ratiosPerConfig) {
            avg += value;
        }

        return avg / ratiosPerConfig.size();
    }


    private static boolean writeStringIntoFile(String fileName, String jsonString) {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("xdata/" + fileName)))) {
            out.writeBytes(jsonString);
            return true;
        } catch (Exception e) {
            log.error("Could not create File. [{}]", fileName);
            log.error("Exception: ", e);
        }
        return false;
    }
}
