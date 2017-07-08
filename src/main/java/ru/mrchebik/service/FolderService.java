package ru.mrchebik.service;

import ru.mrchebik.model.Folder;

import java.util.HashSet;
import java.util.List;

/**
 * Created by mrchebik on 7/5/17.
 */
public interface FolderService {
    void add(Folder folder);

    List<Folder> get(String keyFolder);

    HashSet<Folder> findAllByImage_keyFile(String keyFile);

    void remove(String keyFolder);
}
