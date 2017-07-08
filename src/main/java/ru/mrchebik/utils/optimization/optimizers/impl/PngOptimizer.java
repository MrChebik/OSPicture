package ru.mrchebik.utils.optimization.optimizers.impl;

import ru.mrchebik.utils.optimization.optimizers.Optimizer;

import java.io.IOException;

/**
 * Created by mrchebik on 7/4/17.
 */
public class PngOptimizer implements Optimizer {
    @Override
    public Process start(String path) throws IOException {
        return new ProcessBuilder("optipng", "-o2", "-strip", "all", path).start();
    }
}
