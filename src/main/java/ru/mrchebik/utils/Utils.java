package ru.mrchebik.utils;

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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by mrchebik on 5/31/17.
 */
@Component
public class Utils {
    private final Random random = new Random();
    private final DataKeyFileService dataKeyFileService;
    private final LessInstancesUtils lessInstancesUtils;
    private final FileUtils fileUtils;
    @Value("${path.pictures}")
    public String PATH_PICTURES;
    @Value("${key.length}")
    public int KEY_LENGTH;

    @Autowired
    public Utils(DataKeyFileService dataKeyFileService,
                 LessInstancesUtils lessInstancesUtils,
                 FileUtils fileUtils) {
        this.dataKeyFileService = dataKeyFileService;
        this.lessInstancesUtils = lessInstancesUtils;
        this.fileUtils = fileUtils;
    }

    public String getKey() {
        String key = "";

        for (int i = 0; i < KEY_LENGTH; i++) {
            key += (char) (random.nextBoolean() ? (random.nextInt(9) + 48) : (random.nextInt(25) + 97));
        }

        return key;
    }

    public ResponseEntity addFile(boolean isFolder,
                                  MultipartFile file,
                                  String keyFolder) throws IOException, InterruptedException {
        String formats[] = file.getContentType().split("/");

        if (fileUtils.isUnsupportedFormat(formats[0], formats[1])) {
            if (isFolder) {
                return new ResponseEntity(HttpStatus.CONTINUE);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        File sourceFile = fileUtils.createPicture(getKey(), keyFolder, formats[1], file);
        setOptimization(formats[1], sourceFile.getPath());
        sourceFile = new File(sourceFile.getPath());
        DataKeyFile dataKeyFile = new DataKeyFile(sourceFile.getName().substring(0, KEY_LENGTH), fileUtils.getFilename(file.getOriginalFilename().split("\\.")), sourceFile.getPath(), formats[1], fileUtils.getSize(sourceFile.length()), fileUtils.getResolution(ImageIO.read(sourceFile)), new Date());

        if (isFolder) {
            dataKeyFile.setMinPath(lessInstancesUtils.setMinInstance(keyFolder, sourceFile.getPath(), sourceFile.getName()));
        }

        lessInstancesUtils.setLessInstances(isFolder, dataKeyFile.getKeyFile(), keyFolder, sourceFile.getPath(), sourceFile.getName(), dataKeyFile.getOriginalFilename(), formats[1]);

        return new ResponseEntity<>("image/" + dataKeyFileService.add(dataKeyFile), HttpStatus.CREATED);
    }

    private void setOptimization(String format,
                                 String sourcePath) throws IOException, InterruptedException {
        Process optimization = setTypeOptimization(format, sourcePath);
        assert optimization != null;
        optimization.waitFor();
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

    public ResponseEntity<Resource> getDirectImage(String key,
                                                   boolean isMin) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        HttpHeaders headers = new HttpHeaders();
        Resource resource = new FileSystemResource(isMin ? dataKeyFile.getMinPath() : dataKeyFile.getPath());
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
