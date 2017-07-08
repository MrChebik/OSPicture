package ru.mrchebik.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.mrchebik.model.Folder;
import ru.mrchebik.service.FolderService;

import java.io.*;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by mrchebik on 6/28/17.
 */
@Component
public class ZipUtils {
    private final FolderService folderService;

    @Value("${path.pictures}")
    public String PATH_PICTURES;

    public ZipUtils(FolderService folderService) {
        this.folderService = folderService;
    }

    protected String createZip(String key) throws IOException {
        File zip = new File(PATH_PICTURES + "zip" + File.separator + key + ".zip");
        if (zip.createNewFile()) {
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zip));
            zout.setLevel(Deflater.BEST_COMPRESSION);
            List<Folder> folderOfPictures = folderService.get(key);
            for (Folder folder : folderOfPictures) {
                addFileToZip(zout, new File(PATH_PICTURES + folder.getImage().getKeyFile() + ("octet-stream".equals(folder.getImage().getMimeType()) ? "" : ("." + folder.getImage().getMimeType()))));
            }
            zout.close();
        }

        return zip.getPath();
    }

    private void addFileToZip(ZipOutputStream zout, File file) throws IOException {
        int BUFFER = 8 * 1024;
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
