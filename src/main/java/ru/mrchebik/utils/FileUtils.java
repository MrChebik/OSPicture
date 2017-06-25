package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * Created by mrchebik on 6/25/17.
 */
@Component
public class FileUtils {
    private final BigDecimal kb = new BigDecimal(1024);
    @Value("${path.pictures}")
    public String PATH_PICTURES;
    @Value("${key.length}")
    public int KEY_LENGTH;

    public String[] getFolderPaths(String key,
                                   String path) {
        String[] returns = new String[3];
        if (key.length() == KEY_LENGTH) {
            String folderPath = path.split(key)[0];
            if (!folderPath.equals(PATH_PICTURES)) {
                File folder = new File(folderPath);
                returns[0] = folder.getName();
                String[] folderFiles = folder.list();
                assert folderFiles != null;
                for (int i = 0; i < folderFiles.length; i++) {
                    if (folderFiles[i].contains(key)) {
                        returns[1] = folderFiles[i == 0 ? folderFiles.length - 1 : (i - 1)].substring(0, KEY_LENGTH);
                        returns[2] = folderFiles[i == folderFiles.length - 1 ? 0 : (i + 1)].substring(0, KEY_LENGTH);
                        break;
                    }
                }
            }
        }

        return returns;
    }

    protected File createPicture(String key,
                                 String keyFolder,
                                 String format,
                                 MultipartFile file) throws IOException {
        File sourceFile = new File(PATH_PICTURES + keyFolder + (!"".equals(keyFolder) ? "/" : "") + key + ("octet-stream".equals(format) ? "" : ("." + format)));
        sourceFile.createNewFile();
        file.transferTo(sourceFile);

        return sourceFile;
    }

    protected String getSize(long length) {
        String originalSize = String.valueOf(length);

        if (originalSize.length() > 6) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Mb";
        } else if (originalSize.length() > 3) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Kb";
        } else {
            return originalSize + "b";
        }
    }

    protected String getFilename(String[] pieces) {
        String fileName = "";

        for (int i = 0; i < pieces.length - 1; i++) {
            fileName += pieces[i];
            if (i != pieces.length - 2) {
                fileName += ".";
            }
        }

        String checkedName = pieces[pieces.length - 1];
        if (!"png".equalsIgnoreCase(checkedName) && !"jpg".equalsIgnoreCase(checkedName) && !"jpeg".equalsIgnoreCase(checkedName) && !"gif".equalsIgnoreCase(checkedName) && !"webp".equalsIgnoreCase(checkedName) && !"bmp".equalsIgnoreCase(checkedName)) {
            fileName += checkedName;
        }

        return fileName;
    }

    protected String getResolution(BufferedImage bufferedImage) {
        return bufferedImage.getWidth() + "x" + bufferedImage.getHeight();
    }

    protected boolean isUnsupportedFormat(String format0,
                                          String format1) {
        return (!"image".equals(format0) && !"octet-stream".equals(format1)) || "x-portable-pixmap".equals(format1) || "x-portable-bitmap".equals(format1) || "x-xpixmap".equals(format1) || "x-xbitmap".equals(format1) || "x-pcx".equals(format1) || "x-tga".equals(format1);
    }
}
