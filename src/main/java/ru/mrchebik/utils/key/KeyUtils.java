package ru.mrchebik.utils.key;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mrchebik.service.ImageService;
import ru.mrchebik.utils.key.switchers.Switcher;
import ru.mrchebik.utils.key.switchers.impl.HighSwitcher;
import ru.mrchebik.utils.key.switchers.impl.LowSwitcher;
import ru.mrchebik.utils.key.switchers.impl.MediumSwitcher;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by mrchebik on 7/3/17.
 */
@Component
public class KeyUtils {
    private final Random random;
    private final BigInteger zero;
    private final BigInteger seventyFive;
    private final BigInteger hundred;

    private final ImageService imageService;

    private Switcher switcher;

    @Value("${key.length}")
    public int KEY_LENGTH;

    @Autowired
    public KeyUtils(ImageService imageService) {
        this.imageService = imageService;

        random = new Random();

        switcher = new LowSwitcher();

        zero = new BigInteger("0");
        seventyFive = new BigInteger("75");
        hundred = new BigInteger("100");
    }

    public String generateKey() {
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < KEY_LENGTH; i++) {
            key.append((char) (
                    random.nextBoolean()
                            ?
                            switcher.isUpperCase() && switcher.isLowerCase()
                                    ?
                                    random.nextBoolean()
                                            ?
                                            upperCase()
                                            :
                                            lowerCase()
                                    :
                                    switcher.isUpperCase()
                                            ?
                                            upperCase()
                                            :
                                            switcher.isNumberCase() && !switcher.isLowerCase()
                                                    ?
                                                    numberCase()
                                                    :
                                                    lowerCase()
                            :
                            switcher.isNumberCase()
                                    ?
                                    numberCase()
                                    :
                                    switcher.isUpperCase()
                                            ?
                                            upperCase()
                                            :
                                            lowerCase()));
        }

        if (imageService.get(key.toString()) != null) {
            switcher.setOverflow(true);
            isOverflow();
            return generateKey();
        }

        return key.toString();
    }

    private int upperCase() {
        return random.nextInt(isShorted(25)) + 65;
    }

    private int lowerCase() {
        return random.nextInt(isShorted(25)) + 97;
    }

    private int numberCase() {
        return random.nextInt(isShorted(9)) + 48;
    }

    private int isShorted(int range) {
        return switcher.isShorted() ? (range > 9 ? 6 : range) : range;
    }

    // Overflow when DB has more than 75% of keys with specified length or will be collision
    protected void isOverflow() {
        if (KEY_LENGTH < 1) {
            KEY_LENGTH = 1;
        }

        if (!switcher.isOverflow()) {
            final BigInteger MAX_VALUES = new BigInteger(String.valueOf((switcher.isNumberCase() ? 10 : 0) +
                    (switcher.isUpperCase() ? switcher.isShorted() ? 6 : 25 : 0) +
                    (switcher.isLowerCase() ? switcher.isShorted() ? 6 : 25 : switcher.isNumberCase() || switcher.isUpperCase() ? 0 : switcher.isShorted() ? 6 : 25))
            ).pow(KEY_LENGTH);
            final BigInteger CURRENT_VALUES = new BigInteger(String.valueOf(imageService.getAllByKeyLength(KEY_LENGTH)));

            if (MAX_VALUES
                    .divide(hundred)
                    .multiply(seventyFive)
                    .subtract(CURRENT_VALUES)
                    .compareTo(zero) <= 0) {
                KEY_LENGTH++;
            }
        } else {
            if (switcher instanceof LowSwitcher) {
                switcher = new MediumSwitcher();
            } else if (switcher instanceof MediumSwitcher) {
                switcher = new HighSwitcher();
            } else if (switcher instanceof HighSwitcher) {
                switcher = new LowSwitcher();
                KEY_LENGTH++;
            }
            switcher.setOverflow(false);
        }
    }
}
