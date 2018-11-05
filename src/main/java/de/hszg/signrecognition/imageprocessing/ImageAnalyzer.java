package de.hszg.signrecognition.imageprocessing;

import de.hszg.signrecognition.imageprocessing.entity.ColorName;
import de.hszg.signrecognition.imageprocessing.entity.Sign;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.FeatureVector;
import de.hszg.signrecognition.imageprocessing.entity.featurevector.SignFeatureVector;
import de.hszg.signrecognition.imageprocessing.utils.ColorUtils;
import de.hszg.signrecognition.imageprocessing.utils.ImageUtil;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ImageAnalyzer {


    public FeatureVector extractFeatureVector(File file, Sign sign) {

        BufferedImage bufferedImage = loadAndCutImage(file);

        /*Calculate Color Ratios*/

        try {
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            BufferedImage tl = bufferedImage
                    .getSubimage(0, 0, width / 2, height / 2);
            BufferedImage tr = bufferedImage
                    .getSubimage(width / 2, 0, width / 2, height / 2);
            BufferedImage bl = bufferedImage
                    .getSubimage(0, height / 2, width / 2, height / 2);
            BufferedImage br = bufferedImage
                    .getSubimage(width / 2, height / 2, width / 2, height / 2);

            return new SignFeatureVector(
                    sign,
                    calcColorRatios(bufferedImage),
                    calcColorRatios(tl),
                    calcColorRatios(tr),
                    calcColorRatios(bl),
                    calcColorRatios(br));


        } catch (Exception e) {
            log.error("cutting failed for Image: " + sign.name());
        }

        return null;
    }

    private BufferedImage loadAndCutImage(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);

            if (bufferedImage != null) {
                bufferedImage = ImageCutter.cutOffBezels(bufferedImage);
                //ImageUtil.saveImage(bufferedImage, "C:\\Users\\jthies\\Desktop\\SAVED_IMAGES\\", bufferedImage.toString().substring(14,22) + ".png");
                return bufferedImage;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Integer> calcColorRatios(BufferedImage image) {

        Map<String, Integer> colorRatiosMap = new HashMap<>();

        ColorUtils.colorNameList.stream().forEach(colorName -> {
            colorRatiosMap.put(colorName.getName(), 0);
        });

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                Color color = new Color(image.getRGB(x, y));
                ColorName colorName = new ColorName(ColorUtils.getColorNameFromColor(color),
                        color.getRed(), color.getGreen(), color.getBlue());

                Integer absoluteRatio = colorRatiosMap.get(colorName.getName()) + 1;

                colorRatiosMap.put(colorName.getName(), absoluteRatio);
            }
        }

        int totalNumberOfPixels = image.getWidth() * image.getHeight();
//        calcRelativeRatio(colorRatiosMap, totalNumberOfPixels);


        //push all int ratios into features
        List<Integer> features = new ArrayList<>();
        features.addAll(colorRatiosMap.values());

        return features;
    }

    private void calcRelativeRatio(Map<String, Integer> colorRatiosMap, int totalNumberOfPixels) {

        colorRatiosMap.forEach((colorName, absoluteRatio) -> {
            colorRatiosMap.put(colorName, absoluteRatio * 100 / totalNumberOfPixels);
        });

    }
}
