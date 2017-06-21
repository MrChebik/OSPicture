package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import ru.mrchebik.bean.Utils;
import ru.mrchebik.exception.ResourceNotFoundException;
import ru.mrchebik.model.DataKeyFile;
import ru.mrchebik.model.FilenameFormat;
import ru.mrchebik.service.DataKeyFileService;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @GetMapping("/imageAnim")
    public ModelAndView handleAnimation(@RequestParam String key,
                                        @RequestParam(required = false) String left,
                                        @RequestParam(required = false) String right,
                                        @ModelAttribute("animation") String animation,
                                        RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = new ModelAndView();
        RedirectView view = new RedirectView("/image/" + key);
        view.setExposeModelAttributes(false);
        modelAndView.setView(view);

        redirectAttributes.addFlashAttribute("animation", left != null ? "left" : "right");

        return modelAndView;
    }
    
    @GetMapping("/image/{key}")
    public String handleGetImage(Model model,
                                 @ModelAttribute("animation") String animation,
                                 @PathVariable String key) throws IOException {
        DataKeyFile dataKeyFile = dataKeyFileService.get(key);

        if (dataKeyFile == null) {
            throw new ResourceNotFoundException();
        } else {
            String folderPath = dataKeyFile.getPath().split(dataKeyFile.getKeyFile())[0];
            if (!folderPath.equals(utils.PATH_PICTURES)) {
                File folder = new File(folderPath);
                model.addAttribute("isFromFolder", folder.getName());
                String[] folderFiles = folder.list();
                for (int i = 0; i < folderFiles.length; i++) {
                    if (folderFiles[i].contains(key)) {
                        model.addAttribute("folderLeft", folderFiles[i == 0 ? folderFiles.length - 1 : (i - 1)].substring(0, utils.KEY_LENGTH));
                        model.addAttribute("folderRight", folderFiles[i == folderFiles.length - 1 ? 0 : (i + 1)].substring(0, utils.KEY_LENGTH));
                        break;
                    }
                }
            }

            model.addAttribute("key", dataKeyFile.getKeyFile());
            model.addAttribute("name", dataKeyFile.getOriginalFilename());
            model.addAttribute("size", dataKeyFile.getSize());
            boolean isOctetStream = dataKeyFile.getMimeType().equals("octet-stream");
            model.addAttribute("format", isOctetStream ? "png" : dataKeyFile.getMimeType());
            model.addAttribute("isOctetStream", isOctetStream);
            model.addAttribute("resolution", dataKeyFile.getScale());

            DataKeyFile px500 = dataKeyFileService.get(dataKeyFile.getPath500px());
            DataKeyFile px200 = dataKeyFileService.get(dataKeyFile.getPath200px());

            boolean isEqual500 = key.contains("500_"), isEqual200 = key.contains("200_");

            model.addAttribute("px500Path", isEqual500 ? "image/" + px500.getKeyFile() : "image/500_" + (isEqual200 ? px200.getKeyFile() : key));
            model.addAttribute("px200Path", isEqual200 ? "image/" + px200.getKeyFile() : "image/200_" + (isEqual500 ? px500.getKeyFile() : key));

            if (isEqual500) {
                model.addAttribute("px500TRUE", 1);
            } else if (isEqual200) {
                model.addAttribute("px200TRUE", 1);
            }

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
                keyFiles.add(dataKeyFile.getMimeType().equals("octet-stream") ? new FilenameFormat(dataKeyFile.getKeyFile(), dataKeyFile.getMimeType(), true) : new FilenameFormat(dataKeyFile.getKeyFile(), dataKeyFile.getMimeType()));
            }

            model.addAttribute("files", keyFiles);
            model.addAttribute("folder", true);

            return "index";
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
