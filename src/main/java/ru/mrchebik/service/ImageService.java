package ru.mrchebik.service;

import ru.mrchebik.model.Image;

import java.util.List;

/**
 * Created by mrchebik on 21.05.17.
 */
public interface ImageService {
    Image add(Image image);

    Image get(String key);

    List<Image> findEqualChecksum(String checksum);

    List<Image> findEqualChecksumAfterOptimization(String checksum);

    long getAllByKeyLength(int length);
}
