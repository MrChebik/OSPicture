package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mrchebik.bean.Utils;
import ru.mrchebik.exception.ResourceNotFoundException;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.service.DataKeyFileService;

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

    @Autowired
    public LinkController(DataKeyFileService dataKeyFileService,
                          Utils utils) {
        this.dataKeyFileService = dataKeyFileService;
        this.utils = utils;
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

    @GetMapping("/image/{key}")
    public String handleGetImage(Model model,
                                 @PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        if (dataKeyFile == null) {
            throw new ResourceNotFoundException();
        } else {
            model.addAttribute("key", dataKeyFile.getKeyFile());
            model.addAttribute("name", dataKeyFile.getOriginalFilename());
            model.addAttribute("size", dataKeyFile.getSize());
            model.addAttribute("format", dataKeyFile.getMimeType());
            model.addAttribute("resolution", dataKeyFile.getScale());

            return "index";
        }
    }

    @GetMapping("/folder/{key}")
    public String handleGetFolder(Model model,
                                   @PathVariable String key) throws IOException {
        File folder = new File(utils.getPATH() + key);
        if (folder.exists()) {
            File[] files = folder.listFiles();
            ArrayList keyFiles = new ArrayList();

            for (int i = 0; i < files.length; i++) {
                keyFiles.add(dataKeyFileService.get(files[i].getName().substring(0, 10)));
            }

            model.addAttribute("files", keyFiles);
            model.addAttribute("folder", true);

            return "index";
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
