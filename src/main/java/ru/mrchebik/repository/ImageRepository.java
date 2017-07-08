package ru.mrchebik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.mrchebik.model.Image;

import java.util.List;

/**
 * Created by mrchebik on 21.05.17.
 */
@Repository
public interface ImageRepository extends JpaRepository<Image, String> {
    Image findByKeyFile(String key);

    List<Image> findAllByChecksumSHA3(String checksum);

    List<Image> findAllByChecksumSHA3AfterOptimization(String checksum);

    @Query("SELECT COUNT(*) FROM ru.mrchebik.model.Image image WHERE LENGTH(image.keyFile)=:key_length")
    long findByKeyLength(@Param("key_length") int length);
}
