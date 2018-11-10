package de.hszg.signrecognition.imageprocessing;

import de.hszg.signrecognition.imageprocessing.entity.Point;
import de.hszg.signrecognition.imageprocessing.utils.ColorUtils;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.awt.image.BufferedImage;

@Slf4j
public class ImageCutter {

    public static BufferedImage cutOffBezels(BufferedImage image) {

        Point topPoint = cutPixelsLeftRight(image, image.getWidth(), image.getHeight());
        Point leftPoint = cutPixelsTopDown(image, image.getWidth(), image.getHeight());
        Point bottomPoint = cutPixelsRightLeft(image, image.getWidth(), image.getHeight());
        Point rightPoint = cutPixelsBottomUp(image, image.getWidth(), image.getHeight());


        try {
            int width = image.getWidth() - leftPoint.getX() - (image.getWidth() - rightPoint.getX());
            int height = image.getHeight() - topPoint.getY() - (image.getHeight() - bottomPoint.getY());
            image = image.getSubimage(leftPoint.getX(), topPoint.getY(), width, height);
        } catch (Exception e) {
            log.debug("whoops.. cutting image failed");
        }


        return image;
    }


    //TODO: Images mit genaueren Farbwerten -> links / rechts momentan scheinbar darkgrey oder schwarz erkannt statt dunkelblau

    private static Point cutPixelsLeftRight(BufferedImage bufferedImage, int imageWidth, int imageHeight) {
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                Color color = new Color(bufferedImage.getRGB(x, y), false);
                String stringColor = ColorUtils.getColorNameFromColor(color);

                if (!stringColor.equals("DarkGrey") && !stringColor.equals("White")) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static Point cutPixelsTopDown(BufferedImage bufferedImage, int imageWidth, int imageHeight) {
        for (int x = 0; x < imageWidth; x++) {
            for (int y = 0; y < imageHeight; y++) {
                Color color = new Color(bufferedImage.getRGB(x, y), false);
                String stringColor = ColorUtils.getColorNameFromColor(color);

                if (!stringColor.equals("DarkGrey") && !stringColor.equals("White")) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static Point cutPixelsRightLeft(BufferedImage bufferedImage, int imageWidth, int imageHeight) {
        for (int y = imageHeight - 1; y >= 0; y--) {
            for (int x = imageWidth - 1; x >= 0; x--) {
                Color color = new Color(bufferedImage.getRGB(x, y), false);
                String stringColor = ColorUtils.getColorNameFromColor(color);

                if (!stringColor.equals("DarkGrey") && !stringColor.equals("White")) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static Point cutPixelsBottomUp(BufferedImage bufferedImage, int imageWidth, int imageHeight) {
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = imageHeight - 1; y >= 0; y--) {
                Color color = new Color(bufferedImage.getRGB(x, y), false);
                String stringColor = ColorUtils.getColorNameFromColor(color);

                if (!stringColor.equals("DarkGrey") && !stringColor.equals("White")) {
                    return new Point(x, y);
                }
            }
        }
        return null;
    }

    private static BufferedImage cropImage(int x, int y, BufferedImage src, int width, int height) {
        BufferedImage dest = src.getSubimage(x, y, width, height);
        return dest;
    }

}
