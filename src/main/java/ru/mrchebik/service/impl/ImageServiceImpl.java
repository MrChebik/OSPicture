package ru.mrchebik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mrchebik.model.Image;
import ru.mrchebik.repository.ImageRepository;
import ru.mrchebik.service.ImageService;

import java.util.List;

/**
 * Created by mrchebik on 21.05.17.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public Image add(Image image) {
        return imageRepository.saveAndFlush(image);
    }

    @Override
    public Image get(String key) {
        return imageRepository.findByKeyFile(key);
    }

    @Override
    public List<Image> findEqualChecksum(String checksum) {
        return imageRepository.findAllByChecksumSHA3(checksum);
    }

    @Override
    public List<Image> findEqualChecksumAfterOptimization(String checksum) {
        return imageRepository.findAllByChecksumSHA3AfterOptimization(checksum);
    }

    @Override
    public long getAllByKeyLength(int length) {
        return imageRepository.findByKeyLength(length);
    }
}
