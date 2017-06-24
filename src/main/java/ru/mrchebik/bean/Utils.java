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
    private Process optimization;
    private Process px500Process;
    private Process px200Process;

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

        if ((!"image".equals(formats[0]) && !"octet-stream".equals(formats[1])) || "x-portable-pixmap".equals(formats[1]) || "x-portable-bitmap".equals(formats[1]) || "x-xpixmap".equals(formats[1]) || "x-xbitmap".equals(formats[1]) || "x-pcx".equals(formats[1]) || "x-tga".equals(formats[1])) {
            if (isFolder) {
                return new ResponseEntity(HttpStatus.CONTINUE);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        String key = getKey();

        File sourceFile = new File(PATH_PICTURES + keyFolder + (!"".equals(keyFolder) ? "/" : "") + key + ("octet-stream".equals(formats[1]) ? "" : ("." + formats[1])));
        sourceFile.createNewFile();
        file.transferTo(sourceFile);

        if (optimization != null) {
            optimization.waitFor();
        }

        optimization = setTypeOptimization(formats[1], sourceFile.getPath());

        String fileName = getFilename(file.getOriginalFilename().split("\\."));

        if (isFolder) {
            new File(PATH_PICTURES + keyFolder + "_500/").mkdir();
            new File(PATH_PICTURES + keyFolder + "_200/").mkdir();
        }

        optimization.waitFor();

        sourceFile = new File(sourceFile.getPath());

        if (isFolder) {
            optimization = new ProcessBuilder("convert", sourceFile.getPath(), "-resize", "400x320^", "\\", "-gravity", "center", "-extent", "400x320", PATH_PICTURES + keyFolder + "_min/" + sourceFile.getName()).start();
        }

        setLessInstances(isFolder, key, keyFolder, sourceFile.getPath(), sourceFile.getName(), fileName, formats[1]);

        DataKeyFile dataKeyFile = new DataKeyFile(key, fileName, sourceFile.getPath(), formats[1], getSize(sourceFile.length()), getResolution(ImageIO.read(sourceFile)), new Date(), "500_" + key, "200_" + key);
        if (isFolder) {
            dataKeyFile.setMinPath(PATH_PICTURES + keyFolder + "_min/" + sourceFile.getName());
        }

        return new ResponseEntity<>("image/" + dataKeyFileService.add(dataKeyFile), HttpStatus.CREATED);
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
        if (px500Process != null) {
            px500Process.waitFor();
            px200Process.waitFor();
        }

        String px500Quest = PATH_PICTURES + (isFolder ? keyFolder + "_500/" : "500_") + sourceName;
        String px200Quest = PATH_PICTURES + (isFolder ? keyFolder + "_200/" : "200_") + sourceName;

        px500Process = new ProcessBuilder("convert", sourcePath, "-resize", "500x500^", px500Quest).start();
        px200Process = new ProcessBuilder("convert", sourcePath, "-resize", "200x200^", px200Quest).start();

        px500Process.waitFor();
        px200Process.waitFor();

        File px500 = new File(px500Quest);
        File px200 = new File(px200Quest);

        dataKeyFileService.add(new DataKeyFile("500_" + key, fileName, px500.getPath(), format, getSize(px500.length()), getResolution(ImageIO.read(px500)), new Date(), key, "200_" + key));
        dataKeyFileService.add(new DataKeyFile("200_" + key, fileName, px200.getPath(), format, getSize(px200.length()), getResolution(ImageIO.read(px200)), new Date(), "500_" + key, key));
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

    public String[] getPX(String path500,
                          String path200,
                          String key) {
        String[] returns = new String[4];
        DataKeyFile px500 = dataKeyFileService.get(path500);
        DataKeyFile px200 = dataKeyFileService.get(path200);

        boolean isEqual500 = key.contains("500_");
        boolean isEqual200 = key.contains("200_");

        returns[0] = isEqual500 ? "image/" + px500.getKeyFile() : "image/500_" + (isEqual200 ? px200.getKeyFile() : key);
        returns[1] = isEqual200 ? "image/" + px200.getKeyFile() : "image/200_" + (isEqual500 ? px500.getKeyFile() : key);

        if (isEqual500) {
            returns[2] = String.valueOf(1);
        } else if (isEqual200) {
            returns[3] = String.valueOf(1);
        }

        return returns;
    }

    public ResponseEntity<Resource> getDirectImage(String key,
                                                   boolean isMin) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        HttpHeaders headers = new HttpHeaders();
        Resource resource = new FileSystemResource(isMin ? dataKeyFile.getMinPath() : dataKeyFile.getPath());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
