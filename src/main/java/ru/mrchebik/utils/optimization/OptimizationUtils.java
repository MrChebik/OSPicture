package ru.mrchebik.utils.optimization;

import org.springframework.stereotype.Component;
import ru.mrchebik.utils.optimization.optimizers.impl.JpegOptimizer;
import ru.mrchebik.utils.optimization.optimizers.impl.PngOptimizer;

import java.io.IOException;

/**
 * Created by mrchebik on 7/3/17.
 */
@Component
public class OptimizationUtils {
    public void doOptimization(String pathToFile,
                               String format) throws IOException, InterruptedException {
        Process optimization = setTypeOptimization(pathToFile, format);

        if (optimization != null) {
            optimization.waitFor();
        }
    }

    private Process setTypeOptimization(String pathToFile,
                                        String format) throws IOException {
        if ("jpeg".equals(format)) {
            return new JpegOptimizer().start(pathToFile);
        }
        if ("octet-stream".equals(format) || "png".equals(format)) {
            return new PngOptimizer().start(pathToFile);
        }

        return null;
    }
}
