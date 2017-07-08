package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mrchebik.model.Image;
import ru.mrchebik.service.ImageService;

import java.io.File;
import java.io.IOException;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mrchebik on 6/25/17.
 */
@Component
public class LessInstancesUtils {
    private final FileUtils fileUtils;
    private final ImageService imageService;
    private final ChecksumUtils checksumUtils;

    @Value("${path.pictures}")
    public String PATH_PICTURES;
    @Value("${key.length}")
    public int KEY_LENGTH;

    @Autowired
    public LessInstancesUtils(FileUtils fileUtils,
                              ImageService imageService,
                              ChecksumUtils checksumUtils) {
        this.fileUtils = fileUtils;
        this.imageService = imageService;
        this.checksumUtils = checksumUtils;
    }

    protected void setLessInstances(String key,
                                    String format,
                                    String fileName,
                                    String sourceName,
                                    String sourcePath) throws InterruptedException, IOException, NoSuchAlgorithmException, DigestException {
        setPxInstance(key,
                "500",
                format,
                fileName,
                sourceName,
                sourcePath);
        setPxInstance(key,
                "400",
                format,
                fileName,
                sourceName,
                sourcePath);
        setPxInstance(key,
                "200",
                format,
                fileName,
                sourceName,
                sourcePath);
    }

    private void setPxInstance(String key,
                               String type,
                               String format,
                               String fileName,
                               String sourceName,
                               String sourcePath) throws IOException, InterruptedException, NoSuchAlgorithmException, DigestException {
        String pxQuest = PATH_PICTURES + type + "_" + sourceName;

        Process pxProcess = "400".equals(type) ?
                new ProcessBuilder("convert", sourcePath, "-resize", "400x320^", "\\", "-gravity", "center", "-extent", "400x320", pxQuest).start()
                :
                new ProcessBuilder("convert", sourcePath, "-resize", type + "x" + type + "^", pxQuest).start();
        pxProcess.waitFor();

        File px = new File(pxQuest);

        String checksum = checksumUtils.getChecksumSHA3(px.toPath());
        String resolution = fileUtils.getResolution(px);
        long size = px.length();
        String resultOfChecksum = checksumUtils.findDuplicate(checksum,
                resolution,
                size);

        if (!"none".equals(resultOfChecksum)) {
            Image image = imageService.get(resultOfChecksum);
            image.setKeyFile(type + "_" + key);
            image.setFilename(fileName);
        } else {
            imageService.add(new Image(type + "_" + key,
                    fileName,
                    fileUtils.getSize(px.length()),
                    fileUtils.getResolution(px),
                    format,

                    px.length(),
                    checksumUtils.getChecksumSHA3(px.toPath())));
        }
    }
}
