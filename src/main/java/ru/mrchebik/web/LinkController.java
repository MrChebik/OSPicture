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
import ru.mrchebik.model.FilenameFormat;
import ru.mrchebik.model.Folder;
import ru.mrchebik.model.Image;
import ru.mrchebik.model.InfoImage;
import ru.mrchebik.service.FolderService;
import ru.mrchebik.service.ImageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrchebik on 15.05.17.
 */
@Controller
public class LinkController {
    private final ImageService imageService;
    private final FolderService folderService;

    @Autowired
    public LinkController(ImageService imageService,
                          FolderService folderService) {
        this.imageService = imageService;
        this.folderService = folderService;
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

    @GetMapping("/info_image/{keyFolder}/{keyImg}")
    @ResponseBody
    public ResponseEntity<InfoImage> handleGetInfoImage(@PathVariable String keyImg,
                                                        @PathVariable String keyFolder) throws IOException {
        Image image = imageService.get(keyImg);
        boolean isOctetStream = "octet-steam".equals(image.getMimeType());

        InfoImage infoImage = new InfoImage(image.getKeyFile(), image.getFilename(), image.getSize(), isOctetStream ? "png" : image.getMimeType(), String.valueOf(isOctetStream), image.getResolution());

        List<Folder> folders = folderService.get(keyFolder);

        Map attributesOfFolder = getAttributesOfFolder(image, folders);
        infoImage.setFolderLeft((String) attributesOfFolder.get("folderLeft"));
        infoImage.setFolderRight((String) attributesOfFolder.get("folderRight"));

        infoImage.setPx500Path("500_" + keyImg);
        infoImage.setPx200Path("200_" + keyImg);

        return new ResponseEntity<>(infoImage, HttpStatus.OK);
    }
    
    @GetMapping("/image/{key}")
    public String handleGetImage(Model model,
                                 @PathVariable String key) throws IOException {
        Image image = imageService.get(key);

        if (image == null) {
            throw new ResourceNotFoundException();
        } else {
            model.addAllAttributes(getAttributesOfImage(image));
            model.addAllAttributes(getAttributesOfLessInstances(key, true));

            return "index";
        }
    }

    @GetMapping("/folder/{keyFolder}/image/{keyImg}")
    public String handleGetImage(Model model,
                                 @PathVariable String keyImg,
                                 @PathVariable String keyFolder) throws IOException {
        Image image = imageService.get(keyImg);

        if (image == null) {
            throw new ResourceNotFoundException();
        } else {
            List<Folder> folders = folderService.get(keyFolder);

            model.addAllAttributes(getAttributesOfFolder(image, folders));
            model.addAllAttributes(getAttributesOfImage(image));
            model.addAllAttributes(getAttributesOfLessInstances(keyImg, false));

            return "index";
        }
    }

    @GetMapping("/folder/{key}")
    public String handleGetFolder(Model model,
                                  @PathVariable String key) throws IOException {
        List<Folder> folders = folderService.get(key);

        if (folders.size() != 0) {
            List<FilenameFormat> keyFiles = new ArrayList<>();

            for (Folder folder : folders) {
                Image image = folder.getImage();
                FilenameFormat filenameFormat = new FilenameFormat(image.getKeyFile(), image.getMimeType());
                filenameFormat.setOctetStream("octet-stream".equals(image.getMimeType()));

                keyFiles.add(filenameFormat);
            }

            model.addAttribute("files", keyFiles);
            model.addAttribute("folder", key);

            return "index";
        } else {
            throw new ResourceNotFoundException();
        }
    }

    private Map<String, String> getAttributesOfFolder(Image image,
                                                      List<Folder> folders) {
        Map<String, String> map = new HashMap<>();

        map.put("isFromFolder", folders.get(0).getKeyFolder());

        for (int i = 0; i < folders.size(); i++) {
            if (folders.get(i).getImage().getKeyFile().equals(image.getKeyFile())) {
                map.put("folderLeft", folders.get(i == 0 ? folders.size() - 1 : (i - 1)).getImage().getKeyFile());
                map.put("folderRight", folders.get(i == folders.size() - 1 ? 0 : (i + 1)).getImage().getKeyFile());
            }
        }

        return map;
    }

    private Map<String, java.io.Serializable> getAttributesOfImage(Image image) {
        Map<String, java.io.Serializable> map = new HashMap<>();

        map.put("key", image.getKeyFile());
        map.put("name", image.getFilename());
        map.put("size", image.getSize());
        boolean isOctetStream = "octet-stream".equals(image.getMimeType());
        map.put("format", isOctetStream ? "png" : image.getMimeType());
        map.put("isOctetStream", isOctetStream);
        map.put("resolution", image.getResolution());

        return map;
    }

    private Map<String, java.io.Serializable> getAttributesOfLessInstances(String key,
                                                                           boolean isDirectImage) {
        Map<String, java.io.Serializable> map = new HashMap<>();

        if (isDirectImage) {
            boolean contains500px = key.contains("500_");
            boolean contains200px = key.contains("200_");
            map.put("px500Path", contains500px ? key.substring(4) : ("500_" + (contains200px ? key.substring(4) : key)));
            map.put("px200Path", contains200px ? key.substring(4) : ("200_" + (contains500px ? key.substring(4) : key)));
            map.put("px500TRUE", contains500px ? 1 : 0);
            map.put("px200TRUE", contains200px ? 1 : 0);
        } else {
            map.put("px500Path", "500_" + key);
            map.put("px200Path", "200_" + key);
        }

        return map;
    }
}
