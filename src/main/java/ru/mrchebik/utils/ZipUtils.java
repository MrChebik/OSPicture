package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by mrchebik on 6/28/17.
 */
@Component
public class ZipUtils {
    @Value("${path.pictures}")
    public String PATH_PICTURES;

    protected String checkZip(String key) throws IOException {
        File zip = new File(PATH_PICTURES + "zip" + File.separator + key + ".zip");
        if (zip.createNewFile()) {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zip));
            zout.setLevel(Deflater.BEST_COMPRESSION);
            for (File file : new File(PATH_PICTURES + key).listFiles()) {
                addFileToZip(zout, file);
            }
            zout.close();
        }

        return zip.getPath();
    }

    private void addFileToZip(ZipOutputStream zout, File file) throws IOException {
        int BUFFER = 10_000_000;
        byte[] data = new byte[BUFFER];
        BufferedInputStream origin = new BufferedInputStream(new FileInputStream(file), BUFFER);
        zout.putNextEntry(new ZipEntry(file.getName()));
        int count;
        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            zout.write(data, 0, count);
        }
        origin.close();
    }
}
