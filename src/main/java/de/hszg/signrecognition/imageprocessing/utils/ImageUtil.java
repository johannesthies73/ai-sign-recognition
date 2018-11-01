package de.hszg.signrecognition.imageprocessing.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtil {
    public static boolean saveImage(BufferedImage image, String path, String fileName) {
        try {
            File outputfile = new File(path + fileName);
            ImageIO.write(image, "png", outputfile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
