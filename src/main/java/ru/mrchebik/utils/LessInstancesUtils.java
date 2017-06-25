package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.service.DataKeyFileService;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by mrchebik on 6/25/17.
 */
@Component
public class LessInstancesUtils {
    private final DataKeyFileService dataKeyFileService;
    private final FileUtils fileUtils;
    @Value("${path.pictures}")
    public String PATH_PICTURES;
    @Value("${key.length}")
    public int KEY_LENGTH;

    @Autowired
    public LessInstancesUtils(DataKeyFileService dataKeyFileService,
                              FileUtils fileUtils) {
        this.dataKeyFileService = dataKeyFileService;
        this.fileUtils = fileUtils;
    }

    protected void setLessInstances(boolean isFolder,
                                    String key,
                                    String keyFolder,
                                    String sourcePath,
                                    String sourceName,
                                    String fileName,
                                    String format) throws InterruptedException, IOException {
        if (isFolder) {
            newFolders(new String[]{"500", "200"}, keyFolder);
        }

        setPxInstance("500", isFolder, key, keyFolder, sourcePath, sourceName, fileName, format);
        setPxInstance("200", isFolder, key, keyFolder, sourcePath, sourceName, fileName, format);
    }

    private void newFolders(String[] types,
                            String keyFolder) {
        for (String type : types) {
            new File(PATH_PICTURES + keyFolder + "_" + type + "/").mkdir();
        }
    }

    private void setPxInstance(String type,
                               boolean isFolder,
                               String key,
                               String keyFolder,
                               String sourcePath,
                               String sourceName,
                               String fileName,
                               String format) throws IOException, InterruptedException {
        String pxQuest = PATH_PICTURES + (isFolder ? (keyFolder + "_" + type + "/") : (type + "_")) + sourceName;

        Process pxProcess = new ProcessBuilder("convert", sourcePath, "-resize", type + "x" + type + "^", pxQuest).start();
        pxProcess.waitFor();

        File px = new File(pxQuest);
        dataKeyFileService.add(new DataKeyFile(type + "_" + key, fileName, px.getPath(), format, fileUtils.getSize(px.length()), fileUtils.getResolution(ImageIO.read(px)), new Date()));
    }

    public String[] getPX(String key) {
        String[] returns = new String[4];

        returns[0] = getPx("500", key);
        returns[1] = getPx("200", key);

        if (key.contains("500_")) {
            returns[2] = String.valueOf(1);
        } else if (key.contains("200_")) {
            returns[3] = String.valueOf(1);
        }

        return returns;
    }

    private String getPx(String type,
                         String key) {
        boolean isBigger = key.length() > KEY_LENGTH;
        String newKey = null;
        if (isBigger) {
            newKey = key.substring(key.length() - KEY_LENGTH, key.length());
        }

        return "image/" + (isBigger ? key.contains(type + "_") ? newKey : (type + "_" + newKey) : (type + "_" + key));
    }

    protected String setMinInstance(String keyFolder,
                                    String sourcePath,
                                    String sourceName) throws IOException {
        new ProcessBuilder("convert", sourcePath, "-resize", "400x320^", "\\", "-gravity", "center", "-extent", "400x320", PATH_PICTURES + keyFolder + "_min/" + sourceName).start();
        return PATH_PICTURES + keyFolder + "_min/" + sourceName;
    }
}
