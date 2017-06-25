package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.mrchebik.exception.ResourceNotFoundException;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.model.FilenameFormat;
import ru.mrchebik.model.InfoImage;
import ru.mrchebik.service.DataKeyFileService;
import ru.mrchebik.utils.FileUtils;
import ru.mrchebik.utils.LessInstancesUtils;
import ru.mrchebik.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by mrchebik on 15.05.17.
 */
@Controller
public class LinkController {
    private final DataKeyFileService dataKeyFileService;
    private final Utils utils;
    private final FileUtils fileUtils;
    private final LessInstancesUtils lessInstancesUtils;

    @Autowired
    public LinkController(DataKeyFileService dataKeyFileService,
                          Utils utils,
                          FileUtils fileUtils,
                          LessInstancesUtils lessInstancesUtils) {
        this.dataKeyFileService = dataKeyFileService;
        this.utils = utils;
        this.fileUtils = fileUtils;
        this.lessInstancesUtils = lessInstancesUtils;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleResourceNotFoundException(Model model) {
        model.addAttribute("notFound", "true");
        return "404";
    }

    @GetMapping("/")
    public String handleAbsolutePath() {
        return "index";
    }

    @GetMapping("/info_image/{key}")
    @ResponseBody
    public ResponseEntity<InfoImage> handleGetInfoImage(@PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);
        boolean isOctetStream = "octet-steam".equals(dataKeyFile.getMimeType());

        InfoImage infoImage = new InfoImage(dataKeyFile.getKeyFile(), dataKeyFile.getOriginalFilename(), dataKeyFile.getSize(), isOctetStream ? "png" : dataKeyFile.getMimeType(), String.valueOf(isOctetStream), dataKeyFile.getScale());

        String[] leftRight = fileUtils.getFolderPaths(dataKeyFile.getKeyFile(), dataKeyFile.getPath());
        if (leftRight[0] != null) {
            infoImage.setFolderLeft(leftRight[1]);
            infoImage.setFolderRight(leftRight[2]);
        }

        String[] pxValues = lessInstancesUtils.getPX(dataKeyFile.getKeyFile());
        infoImage.setPx500Path(pxValues[0]);
        infoImage.setPx200Path(pxValues[1]);
        infoImage.setPx500TRUE(pxValues[2]);
        infoImage.setPx200TRUE(pxValues[3]);

        return new ResponseEntity<>(infoImage, HttpStatus.OK);
    }
    
    @GetMapping("/image/{key}")
    public String handleGetImage(Model model,
                                 @PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        if (dataKeyFile == null) {
            throw new ResourceNotFoundException();
        } else {
            String[] leftRight = fileUtils.getFolderPaths(dataKeyFile.getKeyFile(), dataKeyFile.getPath());
            if (leftRight[0] != null) {
                model.addAttribute("isFromFolder", leftRight[0]);
                model.addAttribute("folderLeft", leftRight[1]);
                model.addAttribute("folderRight", leftRight[2]);
            }

            model.addAttribute("key", dataKeyFile.getKeyFile());
            model.addAttribute("name", dataKeyFile.getOriginalFilename());
            model.addAttribute("size", dataKeyFile.getSize());
            boolean isOctetStream = "octet-stream".equals(dataKeyFile.getMimeType());
            model.addAttribute("format", isOctetStream ? "png" : dataKeyFile.getMimeType());
            model.addAttribute("isOctetStream", isOctetStream);
            model.addAttribute("resolution", dataKeyFile.getScale());

            String[] pxValues = lessInstancesUtils.getPX(dataKeyFile.getKeyFile());
            model.addAttribute("px500Path", pxValues[0]);
            model.addAttribute("px200Path", pxValues[1]);
            model.addAttribute("px500TRUE", pxValues[2]);
            model.addAttribute("px200TRUE", pxValues[3]);

            return "index";
        }
    }

    @GetMapping("/folder/{key}")
    public String handleGetFolder(Model model,
                                  @PathVariable String key) throws IOException {
        File folder = new File(utils.PATH_PICTURES + key);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            ArrayList<FilenameFormat> keyFiles = new ArrayList<>();

            for (File file : files) {
                DataKeyFile dataKeyFile = dataKeyFileService.get(file.getName().substring(0, 10));
                FilenameFormat filenameFormat = new FilenameFormat(dataKeyFile.getKeyFile(), dataKeyFile.getMimeType());
                if ("octet-stream".equals(dataKeyFile.getMimeType())) {
                    filenameFormat.setOctetStream(true);
                }
                keyFiles.add(filenameFormat);
            }

            model.addAttribute("files", keyFiles);
            model.addAttribute("folder", true);

            return "index";
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
