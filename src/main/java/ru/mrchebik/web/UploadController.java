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
import java.io.*;
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
    private Process optimization, px500Process, px200Process;

    private File px500File, px200File;

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

        if (px500Process != null) {
            px500Process.waitFor();
            px200Process.waitFor();
        }

        px500Process = new ProcessBuilder("convert", sourceFile.getPath(), "-resize", "500x500^", utils.getPATH() + "500_" + sourceFile.getName()).start();
        px200Process = new ProcessBuilder("convert", sourceFile.getPath(), "-resize", "200x200^", utils.getPATH() + "200_" + sourceFile.getName()).start();

        px500Process.waitFor();
        px200Process.waitFor();

        File px500 = new File(utils.getPATH() + "500_" + sourceFile.getName());
        File px200 = new File(utils.getPATH() + "200_" + sourceFile.getName());

        dataKeyFileService.add(new DataKeyFile("500_" + key, fileName, px500.getPath(), formats[1], utils.getSize(px500.length()), utils.getResolution(ImageIO.read(px500)), new Date(), key, "200_" + key));
        dataKeyFileService.add(new DataKeyFile("200_" + key, fileName, px200.getPath(), formats[1], utils.getSize(px200.length()), utils.getResolution(ImageIO.read(px200)), new Date(), "500_" + key, key));

        return new ResponseEntity<>("image/" + dataKeyFileService.add(new DataKeyFile(key, fileName, sourceFile.getPath(), formats[1], utils.getSize(new File(sourceFile.getPath()).length()), resolution, new Date(), "500_" + key, "200_" + key)), HttpStatus.CREATED);
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
            } else if (formats[1].equals("jpg") || formats[1].equals("jpeg")) {
                jpegOptimization = new ProcessBuilder("mozjpeg", "-copy", "none", "-outfile", sourceFile.getPath(), sourceFile.getPath());
                optimization = jpegOptimization.start();
            }

            String fileName = utils.getFilename(multipartFiles.get(j).getOriginalFilename().split("\\."));
            px500File = new File(utils.getPATH() + keyFolder + "_500/");
            px200File = new File(utils.getPATH() + keyFolder + "_200/");
            px500File.mkdir();
            px200File.mkdir();

            optimization.waitFor();

            File afterOptimization = new File(sourceFile.getPath());

            String resolution = utils.getResolution(ImageIO.read(sourceFile));
            String size = utils.getSize(afterOptimization.length());
            optimization = new ProcessBuilder("convert", afterOptimization.getPath(), "-resize", "400x320^", "\\", "-gravity", "center", "-extent", "400x320", utils.getPATH() + keyFolder + "_min/" + afterOptimization.getName()).start();

            if (px500Process != null) {
                px500Process.waitFor();
                px200Process.waitFor();
            }

            px500Process = new ProcessBuilder("convert", afterOptimization.getPath(), "-resize", "500x500^", utils.getPATH() + keyFolder + "_500/" + afterOptimization.getName()).start();
            px200Process = new ProcessBuilder("convert", afterOptimization.getPath(), "-resize", "200x200^", utils.getPATH() + keyFolder + "_200/" + afterOptimization.getName()).start();

            px500Process.waitFor();
            px200Process.waitFor();

            File px500 = new File(utils.getPATH() + keyFolder + "_500/" + sourceFile.getName());
            File px200 = new File(utils.getPATH() + keyFolder + "_200/" + sourceFile.getName());

            dataKeyFileService.add(new DataKeyFile("500_" + key, fileName, px500.getPath(), formats[1], utils.getSize(px500.length()), utils.getResolution(ImageIO.read(px500)), new Date(), key, "200_" + key));
            dataKeyFileService.add(new DataKeyFile("200_" + key, fileName, px200.getPath(), formats[1], utils.getSize(px200.length()), utils.getResolution(ImageIO.read(px200)), new Date(), "500_" + key, key));

            dataKeyFileService.add(new DataKeyFile(key, fileName, sourceFile.getPath(), formats[1], size, resolution, new Date(), utils.getPATH() + keyFolder + "_min/" + afterOptimization.getName(), "500_" + key, "500_" + key));
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
