package ru.mrchebik.utils.key.switchers.impl;

import ru.mrchebik.utils.key.switchers.Switcher;
import ru.mrchebik.utils.key.switchers.SwitcherService;

/**
 * Created by mrchebik on 7/4/17.
 */
// Near 62^10 (62^10 / 100 * 75) keys will be available
public class HighSwitcher extends Switcher implements SwitcherService {
    public HighSwitcher() {
        super.isShorted = false;
        super.isLowerCase = true;
        super.isUpperCase = true;
        super.isNumberCase = true;
    }
}
