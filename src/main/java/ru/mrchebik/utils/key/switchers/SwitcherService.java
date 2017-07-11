package ru.mrchebik.utils.key.switchers;

/**
 * Created by mrchebik on 7/4/17.
 */
public interface SwitcherService {
    boolean isShorted();
    boolean isOverflow();
    boolean isLowerCase();
    boolean isUpperCase();
    boolean isNumberCase();

    void setOverflow(boolean isOverflow);
}
