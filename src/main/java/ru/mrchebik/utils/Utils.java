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
import java.net.URL;
import java.nio.file.Files;
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

    public ResponseEntity addFile(URL url) throws IOException, InterruptedException {
        File temp = fileUtils.createTempFile(url);

        String format = Files.probeContentType(temp.toPath()).split("/")[1];

        if (!fileUtils.isSupportedFormat(temp)) {
            temp.delete();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        File sourceFile = fileUtils.createPicture(temp, getKey(), format);
        setOptimization(sourceFile, format);
        sourceFile = new File(sourceFile.getPath());
        DataKeyFile dataKeyFile = new DataKeyFile(sourceFile.getName().substring(0, KEY_LENGTH), getFilename(url), sourceFile.getPath(), format, fileUtils.getSize(sourceFile.length()), fileUtils.getResolution(ImageIO.read(sourceFile)));

        lessInstancesUtils.setLessInstances(dataKeyFile.getKeyFile(), sourceFile.getPath(), sourceFile.getName(), dataKeyFile.getOriginalFilename(), format);

        return new ResponseEntity<>("image/" + dataKeyFileService.add(dataKeyFile), HttpStatus.CREATED);
    }

    public ResponseEntity addFile(MultipartFile file) throws IOException, InterruptedException {
        String format = file.getContentType().split("/")[1];

        if (!fileUtils.isSupportedFormat(file)) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        File sourceFile = fileUtils.createPicture(getKey(), format, file);
        setOptimization(sourceFile, format);
        sourceFile = new File(sourceFile.getPath());
        DataKeyFile dataKeyFile = new DataKeyFile(sourceFile.getName().substring(0, KEY_LENGTH), fileUtils.getFilename(file.getOriginalFilename().split("\\.")), sourceFile.getPath(), format, fileUtils.getSize(sourceFile.length()), fileUtils.getResolution(ImageIO.read(sourceFile)));

        lessInstancesUtils.setLessInstances(dataKeyFile.getKeyFile(), sourceFile.getPath(), sourceFile.getName(), dataKeyFile.getOriginalFilename(), format);

        return new ResponseEntity<>("image/" + dataKeyFileService.add(dataKeyFile), HttpStatus.CREATED);
    }

    public ResponseEntity addFile(MultipartFile file,
                                  String keyFolder) throws IOException, InterruptedException {
        String format = file.getContentType().split("/")[1];

        if (!fileUtils.isSupportedFormat(file)) {
            return new ResponseEntity(HttpStatus.CONTINUE);
        }

        File sourceFile = fileUtils.createPicture(getKey(), keyFolder, format, file);
        setOptimization(sourceFile, format);
        sourceFile = new File(sourceFile.getPath());
        DataKeyFile dataKeyFile = new DataKeyFile(sourceFile.getName().substring(0, KEY_LENGTH), fileUtils.getFilename(file.getOriginalFilename().split("\\.")), sourceFile.getPath(), format, fileUtils.getSize(sourceFile.length()), fileUtils.getResolution(ImageIO.read(sourceFile)));

        dataKeyFile.setMinPath(lessInstancesUtils.setMinInstance(keyFolder, sourceFile.getPath(), sourceFile.getName()));

        lessInstancesUtils.setLessInstances(dataKeyFile.getKeyFile(), keyFolder, sourceFile.getPath(), sourceFile.getName(), dataKeyFile.getOriginalFilename(), format);

        return new ResponseEntity<>("image/" + dataKeyFileService.add(dataKeyFile), HttpStatus.CREATED);
    }

    private void setOptimization(File file,
                                 String format) throws IOException, InterruptedException {
        Process optimization = setTypeOptimization(file, format);
        assert optimization != null;
        optimization.waitFor();
    }

    private Process setTypeOptimization(File file,
                                        String format) throws IOException {
        if ("octet-stream".equals(format) || "png".equals(format)) {
            return new ProcessBuilder("optipng", "-o2", "-strip", "all", file.getPath()).start();
        } else if ("jpeg".equals(format)) {
            return new ProcessBuilder("mozjpeg", "-copy", "none", "-outfile", file.getPath(), file.getPath()).start();
        }

        return null;
    }

    private String getFilename(URL url) {
        String[] slashes = url.getPath().split("\\/");
        String lastSlash = slashes[slashes.length - 1];
        return lastSlash.substring(0, lastSlash.lastIndexOf("."));
    }

    public ResponseEntity<Resource> getDirectImage(String key,
                                                   boolean isMin) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        return new ResponseEntity<>(new FileSystemResource(isMin ? dataKeyFile.getMinPath() : dataKeyFile.getPath()), new HttpHeaders(), HttpStatus.OK);
    }

    public ResponseEntity<Resource> getZipFolder(String key) throws IOException {
        return new ResponseEntity<>(new FileSystemResource(fileUtils.createZipArchive(key)), new HttpHeaders(), HttpStatus.OK);
    }
}
