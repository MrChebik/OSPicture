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
import ru.mrchebik.model.Image;
import ru.mrchebik.service.ImageService;
import ru.mrchebik.utils.key.KeyUtils;
import ru.mrchebik.utils.optimization.OptimizationUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by mrchebik on 5/31/17.
 */
@Component
public class Utils {
    private final KeyUtils keyUtils;
    private final ZipUtils zipUtils;
    private final FileUtils fileUtils;
    private final ImageService imageService;
    private final ChecksumUtils checksumUtils;
    private final OptimizationUtils optimizationUtils;
    private final LessInstancesUtils lessInstancesUtils;
    @Value("${path.pictures}")
    public String PATH_PICTURES;

    @Autowired
    public Utils(KeyUtils keyUtils,
                 ZipUtils zipUtils,
                 FileUtils fileUtils,
                 ImageService imageService,
                 ChecksumUtils checksumUtils,
                 OptimizationUtils optimizationUtils,
                 LessInstancesUtils lessInstancesUtils) {
        this.keyUtils = keyUtils;
        this.zipUtils = zipUtils;
        this.fileUtils = fileUtils;
        this.imageService = imageService;
        this.checksumUtils = checksumUtils;
        this.optimizationUtils = optimizationUtils;
        this.lessInstancesUtils = lessInstancesUtils;
    }

    public ResponseEntity addFile(URL url) throws IOException, InterruptedException, NoSuchAlgorithmException, DigestException {
        File temp = fileUtils.createTempFile(url);

        String format = Files.probeContentType(temp.toPath()).split("/")[1];

        if (!fileUtils.isSupportedFormat(format)) {
            temp.delete();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        File sourceFile = fileUtils.createPicture(keyUtils.generateKey(), format, temp);
        optimizationUtils.doOptimization(sourceFile.getPath(),
                format);
        File optimizeFile = new File(sourceFile.getPath());

        String checksum = checksumUtils.getChecksumSHA3(optimizeFile.toPath());
        String resolution = fileUtils.getResolution(optimizeFile);
        long size = optimizeFile.length();
        String resultOfChecksum = checksumUtils.findDuplicate(checksum,
                resolution,
                size);

        if (!"none".equals(resultOfChecksum)) {
            return new ResponseEntity<>("image/" + resultOfChecksum, HttpStatus.ACCEPTED);
        } else {
            Image image = new Image(fileUtils.getFilename(sourceFile.getName()),
                    getFilename(url),
                    fileUtils.getSize(size),
                    resolution,
                    format,

                    sourceFile.length(),
                    size,
                    checksumUtils.getChecksumSHA3(sourceFile.toPath()),
                    checksum);

            lessInstancesUtils.setLessInstances(image.getKeyFile(),
                    format,
                    image.getFilename(),
                    sourceFile.getName(),
                    sourceFile.getPath());

            return new ResponseEntity<>("image/" + imageService.add(image), HttpStatus.CREATED);
        }
    }

    public Image addFile(MultipartFile file) throws IOException, InterruptedException, DigestException, NoSuchAlgorithmException {
        String format = file.getContentType().split("/")[1];

        if (!fileUtils.isSupportedFormat(format)) {
            return null;
        }

        File sourceFile = fileUtils.createPicture(keyUtils.generateKey(),
                format,
                file);
        optimizationUtils.doOptimization(sourceFile.getPath(),
                format);
        File optimizeFile = new File(sourceFile.getPath());

        String checksum = checksumUtils.getChecksumSHA3(optimizeFile.toPath());
        String resolution = fileUtils.getResolution(optimizeFile);
        long size = optimizeFile.length();
        String resultOfChecksum = checksumUtils.findDuplicate(checksum,
                resolution,
                size);

        if (!"none".equals(resultOfChecksum)) {
            return imageService.get(resultOfChecksum);
        } else {
            Image image = new Image(fileUtils.getFilename(sourceFile.getName()),
                    fileUtils.getFilename(file.getOriginalFilename()),
                    fileUtils.getSize(size),
                    resolution,
                    format,

                    file.getSize(),
                    size,
                    checksumUtils.getChecksumSHA3(file),
                    checksum);

            lessInstancesUtils.setLessInstances(image.getKeyFile(),
                    format,
                    image.getFilename(),
                    sourceFile.getName(),
                    sourceFile.getPath());

            return imageService.add(image);
        }
    }

    private String getFilename(URL url) {
        String[] slashes = url.getPath().split("\\/");
        String lastSlash = slashes[slashes.length - 1];
        return lastSlash.substring(0, lastSlash.lastIndexOf("."));
    }

    public ResponseEntity<Resource> getDirectImage(String key) throws FileNotFoundException {
        Image image = imageService.get(key);

        return new ResponseEntity<>(new FileSystemResource(fileUtils.getPath(image)), new HttpHeaders(), HttpStatus.OK);
    }

    public ResponseEntity<Resource> getZipFolder(String key) throws IOException {
        return new ResponseEntity<>(new FileSystemResource(zipUtils.createZip(key)), new HttpHeaders(), HttpStatus.OK);
    }
}
