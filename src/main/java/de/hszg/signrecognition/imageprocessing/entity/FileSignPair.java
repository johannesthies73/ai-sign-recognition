package de.hszg.signrecognition.imageprocessing.entity;

import java.io.File;

public class FileSignPair {

    private File file;
    private Sign sign;

    public FileSignPair(File file, Sign sign) {
        this.file = file;
        this.sign = sign;
    }

    public File getFile() {
        return file;
    }

    public Sign getSign() {
        return sign;
    }

}
