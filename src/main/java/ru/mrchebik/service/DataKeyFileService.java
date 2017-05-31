package ru.mrchebik.service;

import ru.mrchebik.model.DataKeyFile;

/**
 * Created by mrchebik on 21.05.17.
 */
public interface DataKeyFileService {
    String add(DataKeyFile dataKeyFile);
    DataKeyFile get(String key);
}
