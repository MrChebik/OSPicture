package ru.mrchebik.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mrchebik.model.Folder;
import ru.mrchebik.model.Image;
import ru.mrchebik.service.FolderService;
import ru.mrchebik.service.ImageService;
import ru.mrchebik.utils.ChecksumUtils;
import ru.mrchebik.utils.Utils;
import ru.mrchebik.utils.key.KeyUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

/**
 * Created by mrchebik on 21.05.17.
 */
@RestController
public class UploadController {
    private final Utils utils;
    private final KeyUtils keyUtils;
    private final ChecksumUtils checksumUtils;

    private final ImageService imageService;
    private final FolderService folderService;

    @Autowired
    public UploadController(Utils utils,
                            KeyUtils keyUtils,
                            ChecksumUtils checksumUtils,

                            ImageService imageService,
                            FolderService folderService) {
        this.utils = utils;
        this.keyUtils = keyUtils;
        this.checksumUtils = checksumUtils;

        this.imageService = imageService;
        this.folderService = folderService;
    }

    @PutMapping("/upload")
    public ResponseEntity add(@RequestParam String url) throws IOException, InterruptedException, NoSuchAlgorithmException, DigestException {
        return utils.addFile(new URL(url));
    }

    @PutMapping("/upload/image")
    public ResponseEntity add(@RequestBody MultipartFile file) throws IOException, InterruptedException, DigestException, NoSuchAlgorithmException {
        return new ResponseEntity<>("image/" + utils.addFile(file).getKeyFile(), HttpStatus.CREATED);
    }

    @PutMapping("/upload/images")
    public ResponseEntity add(@RequestParam(required = false) String[] keys,
                              @RequestBody(required = false) MultipartFile[] files) throws IOException, InterruptedException, NoSuchAlgorithmException, DigestException {
        String keyFolder = keyUtils.generateKey();

        int col = 0;
        Image firstImg;
        Image secondImg = null;

        if (files.length != 0) {
            col += files.length;

            for (MultipartFile file : files) {
                if ((firstImg = utils.addFile(file)) == null) {
                    col--;
                } else {
                    if (secondImg == null) {
                        secondImg = firstImg;
                    }
                    folderService.add(new Folder(firstImg, keyFolder));
                }
            }
        }

        if (keys != null) {
            col += keys.length;

            if (files.length == 0) {
                ResponseEntity output;
                if ((output = getFolderOfKeys(keys)) != null) {
                    return output;
                }
            }

            for (String key : keys) {
                folderService.add(new Folder(imageService.get(key), keyFolder));
            }
        }

        if (col == 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (col == 1) {
            folderService.remove(keyFolder);
            return new ResponseEntity<>("image/" + secondImg, HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("folder/" + keyFolder, HttpStatus.CREATED);
    }

    private ResponseEntity getFolderOfKeys(String[] keys) {
        int col = keys.length;

        HashSet<HashSet<Folder>> keysList = new HashSet<>();
        for (String key : keys) {
            keysList.add(folderService.findAllByImage_keyFile(key));
        }

        HashSet<Folder> minimal = new HashSet<>();
        for (HashSet<Folder> keysSet : keysList) {
            if (keysSet.size() < minimal.size() || minimal.size() == 0) {
                minimal = keysSet;
            }
        }

        keysList.remove(minimal);

        int colList;

        for (Folder folder : minimal) {
            colList = 0;

            for (HashSet<Folder> otherFolders : keysList) {
                for (Folder otherFolder : otherFolders) {
                    if (otherFolder.getKeyFolder().equals(folder.getKeyFolder())) {
                        colList++;
                    }
                }
            }

            if (colList == col - 1 && folderService.get(folder.getKeyFolder()).size() == col) {
                return new ResponseEntity<>("folder/" + folder.getKeyFolder(), HttpStatus.ACCEPTED);
            }
        }

        return null;
    }

    @GetMapping("/img/{key}")
    @ResponseBody
    public ResponseEntity<Resource> downloadImage(@PathVariable String key) throws FileNotFoundException {
        return utils.getDirectImage(key);
    }

    @GetMapping("/zip_folder/{key}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFolder(@PathVariable String key) throws IOException {
        return utils.getZipFolder(key);
    }

    @PostMapping("/checksum")
    public ResponseEntity checkChecksum(@RequestParam String checksum,
                                        @RequestParam String resolution,
                                        @RequestParam long size) throws IOException, NoSuchAlgorithmException {
        return new ResponseEntity<>(checksumUtils.findDuplicate(checksum, resolution, size), HttpStatus.ACCEPTED);
    }
}
