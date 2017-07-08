package ru.mrchebik.pinger;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.mrchebik.service.ImageService;
import ru.mrchebik.utils.key.KeyUtils;


/**
 * Created by mrchebik on 7/4/17.
 */
@Component
public class OverflowKeyPinger extends KeyUtils {
    public OverflowKeyPinger(ImageService imageService) {
        super(imageService);
    }

    @Scheduled(fixedDelay = 86_500_000)
    public void pingOverflowKey() {
        isOverflow();
    }
}
