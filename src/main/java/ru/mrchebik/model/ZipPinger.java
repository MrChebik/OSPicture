package ru.mrchebik.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.mrchebik.utils.Utils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by mrchebik on 6/28/17.
 */
@Component
public class ZipPinger {
    private final File zipFolder;

    @Autowired
    public ZipPinger(Utils utils) {
        this.zipFolder = new File(utils.PATH_PICTURES + "zip");
        zipFolder.mkdir();
    }

    @Scheduled(fixedDelay = 86_400_000)
    public void pingZip() {
        for (File zip : zipFolder.listFiles()) {
            if (LocalDateTime.now().minusDays(1)
                    .isAfter(LocalDateTime.from(new Date(zip.lastModified()).toInstant().atZone(ZoneId.systemDefault())))) {
                zip.delete();
            }
        }
    }
}
