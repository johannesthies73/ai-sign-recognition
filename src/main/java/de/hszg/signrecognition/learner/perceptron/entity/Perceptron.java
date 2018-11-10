package de.hszg.signrecognition.learner.perceptron.entity;

import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public class Perceptron implements Serializable {

    private List<Double> weights;
    private Integer bitIndex;
    private final double LEARNING_RATE;

    public Perceptron(int numberOfInputs, int bitIndex, double initialWeightFactor, double learningRate) {
        this.LEARNING_RATE = learningRate;
        this.bitIndex = bitIndex;
        this.weights = new ArrayList<>();

        Random random = new Random();

        //INITIALIZE WEIGHTS WITH RANDOM VALUES
        for (int i = 0; i < numberOfInputs; i++) {
            this.weights.add(/*random.nextDouble() **/ initialWeightFactor); //next double * 50
//            this.weights.add(0.0);
        }
    }

    public int guess(List<Integer> featureValues) {

        int sum = 0;
        for (int i = 0; i < featureValues.size(); i++) {
            sum += featureValues.get(i) * this.weights.get(i);
        }

        return activationFunction(sum);
    }

    private int activationFunction(int sum) {
        if (sum >= 0) {
            return 1;
        } else return 0;
    }

    public void trainPerceptron(FeatureVector featureVector) {
        int rightAnswer = calculateRightAnswer(featureVector.getSign(), this.bitIndex);

        int guess = guess(featureVector.getFeatureValues());
        int error = rightAnswer - guess;

        /*correct weight values*/
        for (int i = 0; i < weights.size(); i++) {
            weights.set(i, weights.get(i) + (error * featureVector.getFeatureValue(i) * LEARNING_RATE));
        }
    }

    private Integer calculateRightAnswer(Sign sign, Integer bitIndex) {
        switch (sign) {
            case LEFT: //for input LEFT only correct digits can be 0 for every of the 3 neurons
                return 0;
            case RIGHT:
                if (bitIndex == 0 || bitIndex == 1) { //only correct digits for input RIGHT are 0 for neuron 0 and 1 AND 1 for neuron 2
                    return 0;
                } else return 1;
            case STOP:
                if (bitIndex == 0 || bitIndex == 2) {
                    return 0;
                } else return 1;
            case PRIORITY_ROAD:
                if (bitIndex == 1 || bitIndex == 2) {
                    return 1;
                } else return 0;
            case YIELD:
                if (bitIndex == 1 || bitIndex == 2) {
                    return 0;
                } else return 1;
            case YIELD_RIGHT:
                if (bitIndex == 0 || bitIndex == 2) {
                    return 1;
                } else return 0;
            default:
                return null;
        }
    }

    public static Sign guessSign(int bit1, int bit2, int bit3) {
        int decimalValue = Integer.parseInt("" + bit1 + bit2 + bit3, 2);

        if (decimalValue == 0) {
            return Sign.LEFT;
        } else if (decimalValue == 1) {
            return Sign.RIGHT;
        } else if (decimalValue == 2) {
            return Sign.STOP;
        } else if (decimalValue == 3) {
            return Sign.PRIORITY_ROAD;
        } else if (decimalValue == 4) {
            return Sign.YIELD;
        } else if (decimalValue == 5) {
            return Sign.YIELD_RIGHT;
        } else {
            return Sign.UNKNOWN;
        }

    }

    public static boolean writeNeuralNetworkIntoFile(String fileName, List<Perceptron> perceptrons) {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("xdata/" + fileName)))) {
            out.writeObject(perceptrons);
            return true;
        } catch (Exception e) {
            log.error("Could not create File. [{}]", fileName);
            log.error("Exception: ", e);
        }
        return false;
    }

    public static List<Perceptron> loadNeuralNetworkFromFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("xdata/" + fileName)))) {
            List<Perceptron> perceptrons = (List<Perceptron>) in.readObject();
            return perceptrons;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
