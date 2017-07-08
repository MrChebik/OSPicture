package ru.mrchebik.utils.key.switchers.impl;

import ru.mrchebik.utils.key.switchers.Switcher;
import ru.mrchebik.utils.key.switchers.SwitcherService;

/**
 * Created by mrchebik on 7/4/17.
 */
// Near 1_099_511_627_776 (824_633_720_832) keys will be available
public class MediumSwitcher extends Switcher implements SwitcherService {
    private boolean isShorted;
    private boolean isLowerCase;
    private boolean isUpperCase;
    private boolean isNumberCase;

    public MediumSwitcher() {
        this.isShorted = true;
        this.isLowerCase = true;
        this.isUpperCase = false;
        this.isNumberCase = true;
    }

    @Override
    public void getDefaultSetting() {
        this.isShorted = super.isShorted;
        this.isLowerCase = super.isLowerCase;
        this.isUpperCase = super.isUpperCase;
        this.isNumberCase = super.isNumberCase;
    }

    @Override
    public void setDefaultSetting() {
        super.isShorted = this.isShorted;
        super.isLowerCase = this.isLowerCase;
        super.isUpperCase = this.isUpperCase;
        super.isNumberCase = this.isNumberCase;
    }

    @Override
    public boolean isShorted() {
        return isShorted;
    }

    @Override
    public boolean isLowerCase() {
        return isLowerCase;
    }

    @Override
    public boolean isUpperCase() {
        return isUpperCase;
    }

    @Override
    public boolean isNumberCase() {
        return isNumberCase;
    }
}
