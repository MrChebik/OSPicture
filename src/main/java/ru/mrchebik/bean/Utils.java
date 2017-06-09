package ru.mrchebik.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.service.DataKeyFileService;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

/**
 * Created by mrchebik on 5/31/17.
 */
@Component
public class Utils {
    public final String PATH = "/home/mrchebik/OSPicture/";
    private final BigDecimal kb = new BigDecimal(1024);
    private final Random random = new Random();
    private final DataKeyFileService dataKeyFileService;
    
    private Process optimization, px500Process, px200Process;

    @Autowired
    public Utils(DataKeyFileService dataKeyFileService) {
        this.dataKeyFileService = dataKeyFileService;
    }

    public String getKey() {
        String key = "";

        for (int i = 0; i < 10; i++) {
            if (random.nextBoolean()) {
                key += (char) (random.nextInt(9) + 48);
            } else {
                key += (char) (random.nextInt(25) + 97);
            }
        }

        return key;
    }

    public String getSize(long length) {
        String originalSize = String.valueOf(length);

        if (originalSize.length() > 6) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Mb";
        } else if (originalSize.length() > 3) {
            return new BigDecimal(originalSize).divide(kb, 1, BigDecimal.ROUND_HALF_UP) + "Kb";
        } else {
            return originalSize + "b";
        }
    }

    public String getFilename(String[] pieces) {
        String fileName = "";

        for (int i = 0; i < pieces.length - 1; i++) {
            fileName += pieces[i];
            if (i != pieces.length - 2) {
                fileName += ".";
            }
        }

        String checkedName = pieces[pieces.length - 1];
        if (!checkedName.equals("png") && !checkedName.equals("jpg") && !checkedName.equals("jpeg") && !checkedName.equals("gif") && !checkedName.equals("webp") && !checkedName.equals("bmp")) {
            fileName += checkedName;
        }

        return fileName;
    }

    public String getResolution(BufferedImage bufferedImage) {
        return bufferedImage.getWidth() + "x" + bufferedImage.getHeight();
    }
    
    public ResponseEntity addFile(boolean isFolder, MultipartFile file, String keyFolder) throws IOException, InterruptedException {
        String formats[] = file.getContentType().split("/");

        if ((!formats[0].equals("image") && !formats[1].equals("octet-stream")) || formats[1].equals("x-portable-pixmap") || formats[1].equals("x-portable-bitmap") || formats[1].equals("x-xpixmap") || formats[1].equals("x-xbitmap") || formats[1].equals("x-pcx") || formats[1].equals("x-tga")) {
            if (isFolder) {
                return new ResponseEntity(HttpStatus.CONTINUE);
            } else {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        String key = getKey();

        File sourceFile = new File(PATH + keyFolder + (!keyFolder.equals("") ? "/" : "") + key + (formats[1].equals("octet-stream") ? "" : ("." + formats[1])));
        sourceFile.createNewFile();
        file.transferTo(sourceFile);

        if (optimization != null) {
            optimization.waitFor();
        }

        if (formats[1].equals("octet-stream") || formats[1].equals("png")) {
            optimization = new ProcessBuilder("optipng", "-o2", "-strip", "all", sourceFile.getPath()).start();
        } else if (formats[1].equals("jpg") || formats[1].equals("jpeg")) {
            optimization = new ProcessBuilder("mozjpeg", "-copy", "none", "-outfile", sourceFile.getPath(), sourceFile.getPath()).start();
        }

        String fileName = getFilename(file.getOriginalFilename().split("\\."));
        
        if (isFolder) {
            new File(PATH + keyFolder + "_500/").mkdir();
            new File(PATH + keyFolder + "_200/").mkdir();
        }
        
        optimization.waitFor();

        File afterOptimization = new File(sourceFile.getPath());

        String resolution = getResolution(ImageIO.read(sourceFile));
        String size = getSize(afterOptimization.length());
        
        if (isFolder) {
            optimization = new ProcessBuilder("convert", afterOptimization.getPath(), "-resize", "400x320^", "\\", "-gravity", "center", "-extent", "400x320", PATH + keyFolder + "_min/" + afterOptimization.getName()).start();
        }

        if (px500Process != null) {
            px500Process.waitFor();
            px200Process.waitFor();
        }

        String px500Quest = PATH + (isFolder ? keyFolder + "_500/" : "500_") + sourceFile.getName();
        String px200Quest = PATH + (isFolder ? keyFolder + "_200/" : "200_") + sourceFile.getName();

        px500Process = new ProcessBuilder("convert", sourceFile.getPath(), "-resize", "500x500^", px500Quest).start();
        px200Process = new ProcessBuilder("convert", sourceFile.getPath(), "-resize", "200x200^", px200Quest).start();

        px500Process.waitFor();
        px200Process.waitFor();

        File px500 = new File(px500Quest);
        File px200 = new File(px200Quest);

        dataKeyFileService.add(new DataKeyFile("500_" + key, fileName, px500.getPath(), formats[1], getSize(px500.length()), getResolution(ImageIO.read(px500)), new Date(), key, "200_" + key));
        dataKeyFileService.add(new DataKeyFile("200_" + key, fileName, px200.getPath(), formats[1], getSize(px200.length()), getResolution(ImageIO.read(px200)), new Date(), "500_" + key, key));

        return new ResponseEntity<>("image/" + dataKeyFileService.add(new DataKeyFile(key, fileName, sourceFile.getPath(), formats[1], size, resolution, new Date(), isFolder ? PATH + keyFolder + "_min/" + afterOptimization.getName() : null, "500_" + key, "200_" + key)), HttpStatus.CREATED);
    }
    
    public ResponseEntity<InputStreamResource> getDirectImage(String key, boolean isMin) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);
        File file = new File(isMin ? dataKeyFile.getMinPath() : dataKeyFile.getPath());

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(new MimetypesFileTypeMap().getContentType(file)))
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
