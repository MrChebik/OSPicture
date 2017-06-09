package ru.mrchebik.bean;

import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Random;

/**
 * Created by mrchebik on 5/31/17.
 */
@Component
public class Utils {
    private final BigDecimal kb = new BigDecimal(1024);
    private final Random random = new Random();

    public String getKey() {
        String key = "";

        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                key += (char) (random.nextInt(9) + 48);
            } else {
                key += (char) (random.nextInt(25) + 97);
            }
        }

        return key;
    }

    public boolean checkFormat(String type, String format) {
        return (!type.equals("image") && !format.equals("octet-stream")) || format.equals("x-portable-pixmap") || format.equals("x-portable-bitmap") || format.equals("x-xpixmap") || format.equals("x-xbitmap") || format.equals("x-pcx") || format.equals("x-tga");

    }

    public String getSize(long length) {
        String originalSize = String.valueOf(length);

        if (originalSize.length() > 6) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Mb";
        } else if (originalSize.length() > 3) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Kb";
        } else {
            return originalSize + "b";
        }
    }

    public String getFilename(String[] pieces) {
        String fileName = "";

        for (int i = 0; i < pieces.length - 1; i++) {
            fileName += pieces[i];
            if (i != pieces.length - 2) {
                fileName += ".";
            }
        }

        String checkedName = pieces[pieces.length - 1];
        if (!checkedName.equals("png") && !checkedName.equals("jpg") && !checkedName.equals("jpeg") && !checkedName.equals("gif") && !checkedName.equals("webp") && !checkedName.equals("bmp")) {
            fileName += checkedName;
        }

        return fileName;
    }

    public String getResolution(BufferedImage bufferedImage) {
        return bufferedImage.getWidth() + "x" + bufferedImage.getHeight();
    }

    public String getPATH() {
        return "/home/mrchebik/OSPicture/";
    }
}
