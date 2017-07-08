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
import java.util.List;

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

        int count = getCountOfImage(folders, keyImg);

        infoImage.setFolderLeft(folders.get(count == 0 ? folders.size() - 1 : (count - 1)).getImage().getKeyFile());
        infoImage.setFolderRight(folders.get(count == folders.size() - 1 ? 0 : (count + 1)).getImage().getKeyFile());

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
            model.addAttribute("key", image.getKeyFile());
            model.addAttribute("name", image.getFilename());
            model.addAttribute("size", image.getSize());
            boolean isOctetStream = "octet-stream".equals(image.getMimeType());
            model.addAttribute("format", isOctetStream ? "png" : image.getMimeType());
            model.addAttribute("isOctetStream", isOctetStream);
            model.addAttribute("resolution", image.getResolution());

            boolean contains500px = key.contains("500_");
            boolean contains200px = key.contains("200_");
            model.addAttribute("px500Path", contains500px ? key.substring(4) : ("500_" + (contains200px ? key.substring(4) : key)));
            model.addAttribute("px200Path", contains200px ? key.substring(4) : ("200_" + (contains500px ? key.substring(4) : key)));
            model.addAttribute("px500TRUE", contains500px ? 1 : 0);
            model.addAttribute("px200TRUE", contains200px ? 1 : 0);

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

            int count = getCountOfImage(folders, keyImg);

            model.addAttribute("isFromFolder", keyFolder);
            model.addAttribute("folderLeft", folders.get(count == 0 ? folders.size() - 1 : (count - 1)).getImage().getKeyFile());
            model.addAttribute("folderRight", folders.get(count == folders.size() - 1 ? 0 : (count + 1)).getImage().getKeyFile());

            model.addAttribute("key", image.getKeyFile());
            model.addAttribute("name", image.getFilename());
            model.addAttribute("size", image.getSize());
            boolean isOctetStream = "octet-stream".equals(image.getMimeType());
            model.addAttribute("format", isOctetStream ? "png" : image.getMimeType());
            model.addAttribute("isOctetStream", isOctetStream);
            model.addAttribute("resolution", image.getResolution());

            model.addAttribute("px500Path", "500_" + keyImg);
            model.addAttribute("px200Path", "200_" + keyImg);

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

    private int getCountOfImage(List<Folder> folders,
                                String keyImg) {
        for (int i = 0; i < folders.size(); i++) {
            if (folders.get(i).getImage().getKeyFile().equals(keyImg)) {
                return i;
            }
        }

        return -1;
    }
}
