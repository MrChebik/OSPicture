package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;

/**
 * Created by mrchebik on 6/25/17.
 */
@Component
public class FileUtils {
    private final ZipUtils zipUtils;
    private final BigDecimal kb = new BigDecimal(1024);
    @Value("${path.pictures}")
    public String PATH_PICTURES;
    @Value("${key.length}")
    public int KEY_LENGTH;

    @Autowired
    public FileUtils(ZipUtils zipUtils) {
        this.zipUtils = zipUtils;
    }

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

    protected File createTempFile(URL url) throws IOException {
        File temp = new File(PATH_PICTURES + "temp.tmp");
        Files.copy(url.openStream(), temp.toPath());

        return temp;
    }

    protected File createPicture(String key,
                                 String keyFolder,
                                 String format,
                                 MultipartFile file) throws IOException {
        File sourceFile = createSourceFile(key, keyFolder, format);
        file.transferTo(sourceFile);

        return sourceFile;
    }

    protected File createPicture(String key,
                                 String format,
                                 MultipartFile file) throws IOException {
        File sourceFile = createSourceFile(key, format);
        file.transferTo(sourceFile);

        return sourceFile;
    }

    protected File createPicture(File file, String key, String format) throws IOException {
        File newFile = new File(PATH_PICTURES + key + "." + format);
        file.renameTo(newFile);

        return newFile;
    }

    private File createSourceFile(String key,
                                  String keyFolder,
                                  String format) throws IOException {
        File sourceFile = new File(PATH_PICTURES + keyFolder + File.separator + key + ("octet-stream".equals(format) ? "" : ("." + format)));
        sourceFile.createNewFile();

        return sourceFile;
    }

    private File createSourceFile(String key,
                                  String format) throws IOException {
        File sourceFile = new File(PATH_PICTURES + key + ("octet-stream".equals(format) ? "" : ("." + format)));
        sourceFile.createNewFile();

        return sourceFile;
    }

    protected String getSize(long length) {
        String originalSize = String.valueOf(length);

        if (originalSize.length() > 6) {
            return new BigDecimal(originalSize)
                    .divide(kb, 1, BigDecimal.ROUND_HALF_UP)
                    .divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Mb";
        } else if (originalSize.length() > 3) {
            return new BigDecimal(originalSize)
                    .divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Kb";
        } else {
            return originalSize + "b";
        }
    }

    protected String getFilename(String name) {
        int lastDot = name.lastIndexOf(".");

        if (isSupportedFormat(name.substring(lastDot + 1, name.length()))) {
            return name.substring(0, lastDot);
        }

        return name;
    }

    protected String getResolution(BufferedImage bufferedImage) {
        return bufferedImage.getWidth() + "x" + bufferedImage.getHeight();
    }

    protected boolean isSupportedFormat(File file) throws IOException {
        String formats[] = Files.probeContentType(file.toPath()).split("/");

        return "image".equals(formats[0]) && isSupportedFormat(formats[1]);
    }

    protected boolean isSupportedFormat(MultipartFile file) throws IOException {
        String formats[] = file.getContentType().split("/");

        return "image".equals(formats[0]) && isSupportedFormat(formats[1]);
    }

    private boolean isSupportedFormat(String format) {
        return "jpg".equalsIgnoreCase(format) || "jpeg".equalsIgnoreCase(format) || "png".equalsIgnoreCase(format) || "webp".equalsIgnoreCase(format) || "bmp".equalsIgnoreCase(format) || "gif".equalsIgnoreCase(format);
    }

    protected String createZipArchive(String key) throws IOException {
        return zipUtils.checkZip(key);
    }
}
