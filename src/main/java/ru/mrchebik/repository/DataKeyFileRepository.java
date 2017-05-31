package ru.mrchebik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mrchebik.model.DataKeyFile;

/**
 * Created by mrchebik on 21.05.17.
 */
@Repository
public interface DataKeyFileRepository extends JpaRepository<DataKeyFile, String> {
    DataKeyFile findByKeyFile(String key);
}
