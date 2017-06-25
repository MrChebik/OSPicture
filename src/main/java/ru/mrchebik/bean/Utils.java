package ru.mrchebik.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.service.DataKeyFileService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * Created by mrchebik on 5/31/17.
 */
@Component
public class Utils {
    public final int KEY_LENGTH = 10;
    private final BigDecimal kb = new BigDecimal(1024);
    private final Random random = new Random();
    private final DataKeyFileService dataKeyFileService;
    @Value("${path.pictures}")
    public String PATH_PICTURES;

    @Autowired
    public Utils(DataKeyFileService dataKeyFileService) {
        this.dataKeyFileService = dataKeyFileService;
    }

    public String getKey() {
        String key = "";

        for (int i = 0; i < KEY_LENGTH; i++) {
            if (random.nextBoolean()) {
                key += (char) (random.nextInt(9) + 48);
            } else {
                key += (char) (random.nextInt(25) + 97);
            }
        }

        return key;
    }

    private String getSize(long length) {
        String originalSize = String.valueOf(length);

        if (originalSize.length() > 6) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Mb";
        } else if (originalSize.length() > 3) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Kb";
        } else {
            return originalSize + "b";
        }
    }

    private String getFilename(String[] pieces) {
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

    private String getResolution(BufferedImage bufferedImage) {
        return bufferedImage.getWidth() + "x" + bufferedImage.getHeight();
    }

    public ResponseEntity addFile(boolean isFolder,
                                  MultipartFile file,
                                  String keyFolder) throws IOException, InterruptedException {
        String formats[] = file.getContentType().split("/");

        if (isUnsupportedFormat(formats[0], formats[1])) {
            if (isFolder) {
                return new ResponseEntity(HttpStatus.CONTINUE);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        File sourceFile = createPicture(getKey(), keyFolder, formats[1], file);
        setOptimization(formats[1], sourceFile.getPath());
        sourceFile = new File(sourceFile.getPath());
        DataKeyFile dataKeyFile = new DataKeyFile(sourceFile.getName().substring(0, KEY_LENGTH), getFilename(file.getOriginalFilename().split("\\.")), sourceFile.getPath(), formats[1], getSize(sourceFile.length()), getResolution(ImageIO.read(sourceFile)), new Date());

        if (isFolder) {
            dataKeyFile.setMinPath(setMinInstance(keyFolder, sourceFile.getPath(), sourceFile.getName()));
        }

        setLessInstances(isFolder, dataKeyFile.getKeyFile(), keyFolder, sourceFile.getPath(), sourceFile.getName(), dataKeyFile.getOriginalFilename(), formats[1]);

        return new ResponseEntity<>("image/" + dataKeyFileService.add(dataKeyFile), HttpStatus.CREATED);
    }

    private File createPicture(String key,
                               String keyFolder,
                               String format,
                               MultipartFile file) throws IOException {
        File sourceFile = new File(PATH_PICTURES + keyFolder + (!"".equals(keyFolder) ? "/" : "") + key + ("octet-stream".equals(format) ? "" : ("." + format)));
        sourceFile.createNewFile();
        file.transferTo(sourceFile);

        return sourceFile;
    }

    private boolean isUnsupportedFormat(String format0,
                                        String format1) {
        return (!"image".equals(format0) && !"octet-stream".equals(format1)) || "x-portable-pixmap".equals(format1) || "x-portable-bitmap".equals(format1) || "x-xpixmap".equals(format1) || "x-xbitmap".equals(format1) || "x-pcx".equals(format1) || "x-tga".equals(format1);
    }

    private String setMinInstance(String keyFolder,
                                  String sourcePath,
                                  String sourceName) throws IOException {
        new ProcessBuilder("convert", sourcePath, "-resize", "400x320^", "\\", "-gravity", "center", "-extent", "400x320", PATH_PICTURES + keyFolder + "_min/" + sourceName).start();
        return PATH_PICTURES + keyFolder + "_min/" + sourceName;
    }

    private void setOptimization(String format,
                                 String sourcePath) throws IOException, InterruptedException {
        Process optimization = setTypeOptimization(format, sourcePath);
        assert optimization != null;
        optimization.waitFor();
    }

    private void newFolders(String[] types,
                            String keyFolder) {
        for (String type : types) {
            new File(PATH_PICTURES + keyFolder + "_" + type + "/").mkdir();
        }
    }

    private Process setTypeOptimization(String format,
                                        String sourcePath) throws IOException {
        if ("octet-stream".equals(format) || "png".equals(format)) {
            return new ProcessBuilder("optipng", "-o2", "-strip", "all", sourcePath).start();
        } else if ("jpg".equals(format) || "jpeg".equals(format)) {
            return new ProcessBuilder("mozjpeg", "-copy", "none", "-outfile", sourcePath, sourcePath).start();
        }

        return null;
    }

    private void setLessInstances(boolean isFolder,
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
        dataKeyFileService.add(new DataKeyFile(type + "_" + key, fileName, px.getPath(), format, getSize(px.length()), getResolution(ImageIO.read(px)), new Date()));
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

    public ResponseEntity<Resource> getDirectImage(String key,
                                                   boolean isMin) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        HttpHeaders headers = new HttpHeaders();
        Resource resource = new FileSystemResource(isMin ? dataKeyFile.getMinPath() : dataKeyFile.getPath());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
