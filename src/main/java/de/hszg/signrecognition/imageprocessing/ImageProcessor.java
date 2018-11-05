package de.hszg.signrecognition.imageprocessing;

import de.hszg.signrecognition.imageprocessing.entity.FileSignPair;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.utils.FeatureVectorUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ImageProcessor {

    private static final String OUTPUT_FILENAME = "featureVectors.dat";
    private static final String BASE_PATH = "C:\\Users\\jthies\\Documents\\Studium\\Professoren\\Ringwelski\\7. Semester\\Künstliche instelligenz\\Verkehrsschilder\\english\\ausgedünnt";
//    private static final String BASE_PATH = "C:\\Users\\jthies\\Pictures\\kuenstliche_intelligenz\\bilder_paul\\VERKEHRSZEICHEN";

    private void start() {
        long t = System.nanoTime();

        System.out.println("loading images...");
        List<FileSignPair> fileSignList = loadFiles();

        System.out.println("calculating FeatureVectors...");
        List<FeatureVector> featureVectors = calcFeatureVectors(fileSignList);

        System.out.println("Saving FeatureVectors into File...");
        FeatureVectorUtil.writeFeatureVectorsIntoFile(OUTPUT_FILENAME, featureVectors);
        log.debug("Laufzeit: {}", (System.nanoTime() - t) / 10000000);

    }

    private List<FeatureVector> calcFeatureVectors(List<FileSignPair> fileSignList) {
        ImageAnalyzer imageAnalyzer = new ImageAnalyzer();

        List<FeatureVector> featureVectors = fileSignList.parallelStream().map(fileSignPair ->
                imageAnalyzer.extractFeatureVector(fileSignPair.getFile(), fileSignPair.getSign()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return featureVectors;
    }

    private List<FileSignPair> loadFiles() {
        List<FileSignPair> fileSignPairs = new ArrayList<>();
        FileLoader fileLoader = new FileLoader(BASE_PATH);

        fileLoader.loadFiles("\\left").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.LEFT)));
        fileLoader.loadFiles("\\right").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.RIGHT)));
        fileLoader.loadFiles("\\stop").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.STOP)));
        fileLoader.loadFiles("\\priorityroad").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.PRIORITY_ROAD)));
        fileLoader.loadFiles("\\yield").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.YIELD)));
        fileLoader.loadFiles("\\yieldright").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.YIELD_RIGHT)));

//        fileLoader.loadFiles("\\LINKS").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.LEFT)));
//        fileLoader.loadFiles("\\RECHTS").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.RIGHT)));
//        fileLoader.loadFiles("\\STOP").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.STOP)));
//        fileLoader.loadFiles("\\HAUPTSTRASSE").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.PRIORITY_ROAD)));
//        fileLoader.loadFiles("\\VORFART_GEWAEHREN").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.YIELD)));
//        fileLoader.loadFiles("\\RECHTS_VOR_LINKS").stream().forEach(file -> fileSignPairs.add(new FileSignPair(file, Sign.YIELD_RIGHT)));

        return fileSignPairs;

    }

    public static void main(String[] args) {
        new ImageProcessor().start();
    }
}