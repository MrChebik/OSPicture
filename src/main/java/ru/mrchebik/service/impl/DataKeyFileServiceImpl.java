package ru.mrchebik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.repository.DataKeyFileRepository;
import ru.mrchebik.service.DataKeyFileService;

/**
 * Created by mrchebik on 21.05.17.
 */
@Service
@Transactional
public class DataKeyFileServiceImpl implements DataKeyFileService {
    private final DataKeyFileRepository dataKeyFileRepository;

    @Autowired
    public DataKeyFileServiceImpl(DataKeyFileRepository dataKeyFileRepository) {
        this.dataKeyFileRepository = dataKeyFileRepository;
    }

    @Override
    public String add(DataKeyFile dataKeyFile) {
        dataKeyFileRepository.saveAndFlush(dataKeyFile);
        return dataKeyFile.getKeyFile();
    }

    @Override
    public DataKeyFile get(String key) {
        return dataKeyFileRepository.findByKeyFile(key);
    }
}
