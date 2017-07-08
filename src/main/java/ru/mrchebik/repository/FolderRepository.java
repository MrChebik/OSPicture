package ru.mrchebik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mrchebik.model.Folder;

import java.util.HashSet;
import java.util.List;

/**
 * Created by mrchebik on 7/5/17.
 */
@Repository
public interface FolderRepository extends JpaRepository<Folder, String> {
    List<Folder> findAllByKeyFolder(String keyFolder);

    HashSet<Folder> findAllByImage_KeyFile(String keyFile);
}
