package de.hszg.signrecognition.imageprocessing.utils;

import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.*;

@Slf4j
public class FeatureVectorUtil {

    public static boolean printFeatureVectors(List<FeatureVector> featureVectors) {
        for (FeatureVector featureVector : featureVectors) {
            System.out.println(featureVector.getFeatureValue(0) + ", " +
                    featureVector.getFeatureValue(1) + ", " +
                    featureVector.getFeatureValue(2) + ", " +
                    featureVector.getSign());
        }

        return true;
    }

    public static Pair<List<FeatureVector>, List<FeatureVector>> getRandomFeatureVectors(String fileName, float pickRate) {

        List<FeatureVector> allFeatureVectors = loadFeatureVectorsFromFile(fileName);

        int trainingsDataEndIndex = Math.round(allFeatureVectors.size() * pickRate);

        Collections.shuffle(allFeatureVectors);

        Pair<List<FeatureVector>, List<FeatureVector>> learnAndClassifyData = new Pair<>(
                allFeatureVectors.subList(0, trainingsDataEndIndex),
                allFeatureVectors.subList(trainingsDataEndIndex, allFeatureVectors.size()));

        return learnAndClassifyData;
    }


    public static List<FeatureVector> loadFeatureVectorsFromFile(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("xdata/" + fileName)))) {
            List<FeatureVector> featureVectors = (List<FeatureVector>) in.readObject();
            return featureVectors;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean writeFeatureVectorsIntoFile(String fileName, List<FeatureVector> featureVectors) {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("xdata/" + fileName)))) {
            out.writeObject(featureVectors);
            return true;
        } catch (Exception e) {
            log.error("ColorFeatureDataCreator: Could not create File. [{}]", fileName);
            log.error("Exception: ", e);
        }
        return false;
    }
}
