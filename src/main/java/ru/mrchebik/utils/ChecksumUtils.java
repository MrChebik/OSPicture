package ru.mrchebik.utils;

import org.bouncycastle.jcajce.provider.digest.SHA3;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.mrchebik.model.Image;
import ru.mrchebik.service.ImageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by mrchebik on 6/29/17.
 */
@Component
public class ChecksumUtils {
    private final ImageService imageService;

    @Autowired
    public ChecksumUtils(ImageService imageService) {
        this.imageService = imageService;
    }

    private byte[] setChecksumSHA3(Path path) throws IOException, DigestException, NoSuchAlgorithmException {
        return new SHA3.Digest512().digest(Files.readAllBytes(path));
    }

    private byte[] setChecksumSHA3(byte[] bytes) throws IOException, DigestException, NoSuchAlgorithmException {
        return new SHA3.Digest512().digest(bytes);
    }

    public String getChecksumSHA3(Path path) throws IOException, DigestException, NoSuchAlgorithmException {
        return Hex.toHexString(setChecksumSHA3(path));
    }

    public String getChecksumSHA3(MultipartFile file) throws IOException, DigestException, NoSuchAlgorithmException {
        return Hex.toHexString(setChecksumSHA3(file.getBytes()));
    }

    public String findDuplicate(String checksum,
                                String resolution,
                                long size) throws IOException, NoSuchAlgorithmException {
        String output;

        if (!"none".equals(output = findDuplicate(imageService.findEqualChecksum(checksum),
                resolution,
                size))) {
            return output;
        }

        return findDuplicate(imageService.findEqualChecksumAfterOptimization(checksum),
                resolution,
                size);
    }

    private String findDuplicate(List<Image> keyFileList,
                                 String resolution,
                                 long size) {
        for (Image keyFile : keyFileList) {
            if (resolution.equals(keyFile.getResolution()) &&
                    (size == keyFile.getFullSize() ||
                            size == keyFile.getFullSizeAfterOptimization())) {
                return keyFile.getKeyFile();
            }
        }

        return "none";
    }
}
