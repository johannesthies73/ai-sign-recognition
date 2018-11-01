package de.hszg.signrecognition.imageprocessing;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private final String BASE_PATH;

    public FileLoader(String basePath) {
        this.BASE_PATH = basePath;
    }


    public List<File> loadFiles(String relativePath) {

        File parentfolder = new File(BASE_PATH + relativePath);
        return (List<File>) FileUtils.listFiles(parentfolder, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

        /*Files.walk(Paths.get(parentfolder.toURI()), 255, FileVisitOption.FOLLOW_LINKS);*/
    }
}
