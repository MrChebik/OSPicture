package ru.mrchebik.utils.key.switchers;

/**
 * Created by mrchebik on 7/4/17.
 */
public abstract class Switcher implements SwitcherService {
    protected boolean isShorted;

    private boolean isOverflow;

    // Default will be true, if all switchers is off
    protected boolean isLowerCase;

    protected boolean isUpperCase;
    protected boolean isNumberCase;

    @Override
    public boolean isShorted() {
        return isShorted;
    }

    @Override
    public boolean isOverflow() {
        return isOverflow;
    }

    @Override
    public void setOverflow(boolean isOverflow) {
        this.isOverflow = isOverflow;
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
