package ru.mrchebik.utils.key.switchers.impl;

import ru.mrchebik.utils.key.switchers.Switcher;
import ru.mrchebik.utils.key.switchers.SwitcherService;

/**
 * Created by mrchebik on 7/4/17.
 */
// Near 1_099_511_627_776 (824_633_720_832) keys will be available
public class MediumSwitcher extends Switcher implements SwitcherService {
    public MediumSwitcher() {
        super.isShorted = true;
        super.isLowerCase = true;
        super.isUpperCase = false;
        super.isNumberCase = true;
    }
}
