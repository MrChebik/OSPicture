package ru.mrchebik.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mrchebik.model.Folder;
import ru.mrchebik.repository.FolderRepository;
import ru.mrchebik.service.FolderService;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;

/**
 * Created by mrchebik on 7/5/17.
 */
@Service
@Transactional
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;

    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository) {
        this.folderRepository = folderRepository;
    }

    @Override
    public void add(Folder folder) {
        folderRepository.saveAndFlush(folder);
    }

    @Override
    public List<Folder> get(String keyFolder) {
        return folderRepository.findAllByKeyFolder(keyFolder);
    }

    @Override
    public HashSet<Folder> findAllByImage_keyFile(String keyFile) {
        return folderRepository.findAllByImage_KeyFile(keyFile);
    }

    @Override
    public void remove(String keyFolder) {
        folderRepository.delete(keyFolder);
    }
}
