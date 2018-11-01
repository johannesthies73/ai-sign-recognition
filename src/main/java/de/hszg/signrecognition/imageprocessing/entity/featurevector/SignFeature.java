package de.hszg.signrecognition.imageprocessing.entity.featurevector;

import java.io.Serializable;

public class SignFeature implements Serializable {

    private String colorString;
    private int colorRatio;

    public SignFeature(String colorString, int colorRatio) {
        this.colorString = colorString;
        this.colorRatio = colorRatio;
    }

    public String getColorString() {
        return colorString;
    }

    public int getColorRatio() {
        return colorRatio;
    }
}
