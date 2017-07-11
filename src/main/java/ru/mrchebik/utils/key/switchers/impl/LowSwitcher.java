package ru.mrchebik.utils.key.switchers.impl;

import ru.mrchebik.utils.key.switchers.Switcher;
import ru.mrchebik.utils.key.switchers.SwitcherService;

/**
 * Created by mrchebik on 7/4/17.
 */
// Near 46_656 (34_992) keys will be available
public class LowSwitcher extends Switcher implements SwitcherService {
    public LowSwitcher() {
        super.isShorted = true;
        super.isLowerCase = true;
        super.isUpperCase = false;
        super.isNumberCase = false;
    }
}
