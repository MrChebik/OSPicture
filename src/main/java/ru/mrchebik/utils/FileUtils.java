package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.mrchebik.model.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrchebik on 6/25/17.
 */
@Component
public class FileUtils {
    private final BigDecimal kb;
    private final List<String> supportedFormats;

    @Value("${path.pictures}")
    public String PATH_PICTURES;
    @Value("${key.length}")
    public int KEY_LENGTH;

    /*@Autowired*/
    public FileUtils(/*ZipUtils zipUtils*/) {
        kb = new BigDecimal(1024);

        supportedFormats = new ArrayList<>(Arrays.asList(
                "octet-stream",
                "webp",
                "jpeg",
                "png",
                "gif"
        ));

        /*File images = new File(zipUtils.PATH_PICTURES + "images");
        images.mkdir();

        String[] listOfImages = images.list();

        if (listOfImages == null || listOfImages.length < 256) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    File creator = new File(images.getPath() + File.separator + Integer.toHexString(i) + Integer.toHexString(j));
                    creator.mkdir();
                }
            }
        }*/
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
        return name.replaceFirst("[.][^.]+$", "");
    }

    protected String getResolution(File file) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(file);
        return bufferedImage.getWidth() + "x" + bufferedImage.getHeight();
    }

    public String getPath(Image image) {
        return PATH_PICTURES + image.getKeyFile() + ("octet-stream".equals(image.getMimeType()) ? "" : ("." + image.getMimeType()));
    }

    protected File createTempFile(URL url) throws IOException {
        File temp = new File(PATH_PICTURES + "temp.tmp");
        Files.copy(url.openStream(), temp.toPath());

        return temp;
    }

    private File createSourceFile(String key,
                                  String format) throws IOException {
        File sourceFile = new File(PATH_PICTURES + key + ("octet-stream".equals(format) ? "" : ("." + format)));
        sourceFile.createNewFile();

        return sourceFile;
    }

    protected File createPicture(String key, String format, File file) throws IOException {
        File newFile = new File(PATH_PICTURES + key + "." + format);
        file.renameTo(newFile);

        return newFile;
    }

    protected File createPicture(String key,
                                 String format,
                                 MultipartFile file) throws IOException {
        File sourceFile = createSourceFile(key, format);
        file.transferTo(sourceFile);

        return sourceFile;
    }

    protected boolean isSupportedFormat(String format) {
        return supportedFormats.contains(format);
    }
}
