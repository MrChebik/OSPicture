package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mrchebik.bean.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by mrchebik on 21.05.17.
 */
@RestController
public class UploadController {
    private final Utils utils;

    @Autowired
    public UploadController(Utils utils) {
        this.utils = utils;
    }

    @PutMapping("/upload/image")
    public ResponseEntity add(@RequestBody MultipartFile file) throws IOException, InterruptedException {
        return utils.addFile(false, file, "");
    }

    @PutMapping("/upload/images")
    public ResponseEntity add(@RequestBody List<MultipartFile> multipartFiles) throws IOException, InterruptedException {
        String keyFolder = utils.getKey();

        File folder = new File(utils.PATH_PICTURES + keyFolder);
        folder.mkdir();

        File minFolder = new File(utils.PATH_PICTURES + keyFolder + "_min");
        minFolder.mkdir();

        int col = multipartFiles.size();

        for (MultipartFile multipartFile : multipartFiles) {
            if (utils.addFile(true, multipartFile, keyFolder) == new ResponseEntity(HttpStatus.CONTINUE)) {
                col--;
            }
        }

        if (col == 0) {
            folder.delete();
            minFolder.delete();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("folder/" + keyFolder, HttpStatus.CREATED);
        }
    }

    @GetMapping("/img/{key}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadImage(@PathVariable String key) throws FileNotFoundException {
        return utils.getDirectImage(key, false);
    }

    @GetMapping("/img_min/{key}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> downloadImageMin(@PathVariable String key) throws FileNotFoundException {
        return utils.getDirectImage(key, true);
    }
}
