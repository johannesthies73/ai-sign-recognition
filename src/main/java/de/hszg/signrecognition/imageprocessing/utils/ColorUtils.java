package de.hszg.signrecognition.imageprocessing.utils;

import de.hszg.signrecognition.imageprocessing.entity.ColorName;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class ColorUtils {

    public static List<ColorName> colorNameList = Arrays.asList(

            new ColorName("White", 255, 255, 255),

            new ColorName("LightGrey", 204, 204, 204),
            new ColorName("Grey", 153, 153, 153),
            new ColorName("DarkGrey", 102, 102, 102),
            new ColorName("VeryDarkGrey", 51, 51, 51),

            new ColorName("Black", 0, 0, 0),

            new ColorName("VeryLightRed", 255, 102, 102),
            new ColorName("LightRed", 255, 51, 51),
            new ColorName("Red", 255, 0, 0),
            new ColorName("DarkRed", 204, 0, 0),
            new ColorName("VeryDarkRed", 153, 0, 0),

            new ColorName("VeryLightBlue", 51, 204, 255),
            new ColorName("LightBlue", 51, 153, 255),
            new ColorName("Blue", 0, 0, 255),
            new ColorName("DarkBlue", 0, 0, 204),
            new ColorName("VeryDarkBlue", 0, 0, 153),

            new ColorName("VeryDarkBlue2", 102, 102, 200),

            new ColorName("VeryLightGreen", 102, 255, 102),
            new ColorName("LightGreen", 0, 255, 51),
            new ColorName("Green", 0, 204, 0),
            new ColorName("DarkGreen", 0, 153, 0),
            new ColorName("VeryDarkGreen", 0, 102, 0),

            new ColorName("VeryLightYellow", 255, 255, 204),
            new ColorName("LightYellow", 255, 255, 153),
            new ColorName("Yellow", 255, 255, 0),
            new ColorName("DarkYellow", 255, 204, 0),

            new ColorName("LightOrange", 255, 153, 0),
            new ColorName("Orange", 255, 102, 0),

            new ColorName("Gold", 255, 204, 51),
            new ColorName("LightBrown", 153, 102, 0),
            new ColorName("Brown", 102, 51, 0),
            new ColorName("DarkBrown", 51, 0, 0),

            new ColorName("Purple", 102, 0, 153)

    );


    private static List<ColorName> initColorList() {

        return colorNameList;
    }

    public static String getColorNameFromRgb(int r, int g, int b) {

        //List<ColorName> colorNameList = initColorList();

        ColorName closestMatch = null;
        int minMSE = Integer.MAX_VALUE;
        int mse;
        for (ColorName c : colorNameList) {
            mse = c.computeMSE(r, g, b);
            if (mse < minMSE) {
                minMSE = mse;
                closestMatch = c;
            }
        }

        if (closestMatch != null) {
            return closestMatch.getName();
        } else {
            return "No matched color name.";
        }
    }

    public static String getColorNameFromColor(Color color) {
        return getColorNameFromRgb(color.getRed(), color.getGreen(),
                color.getBlue());
    }


}
