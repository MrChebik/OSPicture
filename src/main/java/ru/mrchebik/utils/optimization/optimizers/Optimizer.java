package ru.mrchebik.utils.optimization.optimizers;

import java.io.IOException;

/**
 * Created by mrchebik on 7/4/17.
 */
public interface Optimizer {
    Process start(String path) throws IOException;
}
