package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import ru.mrchebik.bean.Utils;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.service.DataKeyFileService;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by mrchebik on 21.05.17.
 */
@RestController
public class UploadController {
    private ProcessBuilder pngOptimization, jpegOptimization;
    private Process optimization;

    private String key;

    private final DataKeyFileService dataKeyFileService;
    private final Utils utils;

    @Autowired
    public UploadController(DataKeyFileService dataKeyFileService,
                            Utils utils) {
        this.dataKeyFileService = dataKeyFileService;
        this.utils = utils;
    }

    @PutMapping("/upload/image")
    public ResponseEntity add(@RequestBody MultipartFile file) throws IOException, InterruptedException {
        String formats[] = file.getContentType().split("/");

        System.out.println(formats[0] + " / " + formats[1]);

        if (utils.checkFormat(formats[0], formats[1])) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        key = utils.getKey();

        File sourceFile = new File(utils.getPATH() + key + (formats[1].equals("octet-stream") ? "" : ("." + formats[1])));
        sourceFile.createNewFile();
        file.transferTo(sourceFile);

        if (formats[1].equals("octet-stream") || formats[1].equals("png")) {
            pngOptimization = new ProcessBuilder("optipng", "-o2", "-strip", "all", sourceFile.getPath());
            optimization = pngOptimization.start();
        } else if (formats[1].equals("jpg") || formats[1].equals("jpeg")) {
            jpegOptimization = new ProcessBuilder("mozjpeg", "-copy", "none", "-outfile", sourceFile.getPath(), sourceFile.getPath());
            optimization = jpegOptimization.start();
        }

        String fileName = utils.getFilename(file.getOriginalFilename().split("\\."));
        optimization.waitFor();
        String resolution = utils.getResolution(ImageIO.read(sourceFile));

        return new ResponseEntity<>(dataKeyFileService.add(new DataKeyFile(key, fileName, sourceFile.getPath(), formats[1], utils.getSize(new File(sourceFile.getPath()).length()), resolution, new Date())), HttpStatus.CREATED);
    }

    @PutMapping("/upload/images")
    public ResponseEntity add(@RequestBody List<MultipartFile> multipartFiles) throws IOException, InterruptedException {
        String keyFolder = utils.getKey();

        File folder = new File(utils.getPATH() + keyFolder);
        folder.mkdir();

        File minFolder = new File(utils.getPATH() + keyFolder + "_min");
        minFolder.mkdir();

        int col = multipartFiles.size();

        for (int j = 0; j < multipartFiles.size(); j++) {
            String formats[] = multipartFiles.get(j).getContentType().split("/");

            System.out.println(formats[0] + " / " + formats[1]);

            if (utils.checkFormat(formats[0], formats[1])) {
                col--;
                continue;
            }

            key = utils.getKey();

            File sourceFile = new File(utils.getPATH() + keyFolder + "/" + key + (formats[1].equals("octet-stream") ? "" : ("." + formats[1])));
            sourceFile.createNewFile();
            multipartFiles.get(j).transferTo(sourceFile);

            if (optimization != null) {
                optimization.waitFor();
            }

            if (formats[1].equals("octet-stream") || formats[1].equals("png")) {
                pngOptimization = new ProcessBuilder("optipng", "-o2", "-strip", "all", sourceFile.getPath());
                optimization = pngOptimization.start();
                System.out.println(2);
            } else if (formats[1].equals("jpg") || formats[1].equals("jpeg")) {
                jpegOptimization = new ProcessBuilder("mozjpeg", "-copy", "none", "-outfile", sourceFile.getPath(), sourceFile.getPath());
                optimization = jpegOptimization.start();
                System.out.println(1);
            }

            String fileName = utils.getFilename(multipartFiles.get(j).getOriginalFilename().split("\\."));

            optimization.waitFor();

            File afterOptimization = new File(sourceFile.getPath());

            String resolution = utils.getResolution(ImageIO.read(sourceFile));
            String size = utils.getSize(afterOptimization.length());
            optimization = new ProcessBuilder("convert", afterOptimization.getPath(), "-resize", "200x130^", "\\", "-gravity", "center", "-extent", "200x130", utils.getPATH() + keyFolder + "_min/" + afterOptimization.getName()).start();

            dataKeyFileService.add(new DataKeyFile(key, fileName, sourceFile.getPath(), formats[1], size, resolution, new Date(), utils.getPATH() + keyFolder + "_min/" + afterOptimization.getName()));
        }

        optimization.waitFor();

        if (col == 0) {
            folder.delete();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("folder/" + keyFolder, HttpStatus.CREATED);
        }
    }

    @GetMapping("/img/{key}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable String key) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);
        File file = new File(dataKeyFile.getPath());

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(new MimetypesFileTypeMap().getContentType(file)))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    @GetMapping("/img_min/{key}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadImageMin(@PathVariable String key) throws FileNotFoundException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);
        File file = new File(dataKeyFile.getMinPath());

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType(new MimetypesFileTypeMap().getContentType(file)))
                .body(new InputStreamResource(new FileInputStream(file)));
    }
}
