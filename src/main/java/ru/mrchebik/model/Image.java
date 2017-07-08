package ru.mrchebik.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by mrchebik on 21.05.17.
 */

/**
 * Min images (200px, 400px, 500px) saves at the DB and does not wires with images
 */
@Entity
@Table(name = "image")
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imgId;

    private String keyFile;
    private String filename;
    private String size;
    private String resolution;
    private String mimeType;
    /*private String path;*/

    private long fullSize;
    private long fullSizeAfterOptimization;
    private String checksumSHA3;
    private String checksumSHA3AfterOptimization;
    private Date startDate;

    @ManyToMany
    @JoinTable(name = "image_folder", joinColumns = @JoinColumn(name = "imgId"), inverseJoinColumns = @JoinColumn(name = "fldrId"))
    private Set<Folder> folders;

    public Image() {
    }

    public Image(String keyFile,
                 String filename,
                 String size,
                 String resolution,
                 String mimeType,
                 /*String path,*/

                 long fullSizeAfterOptimization,
                 String checksumSHA3AfterOptimization) {
        this.keyFile = keyFile;
        this.filename = filename;
        this.size = size;
        this.resolution = resolution;
        this.mimeType = mimeType;
        /*this.path = path;*/

        this.fullSizeAfterOptimization = fullSizeAfterOptimization;
        this.checksumSHA3AfterOptimization = checksumSHA3AfterOptimization;
        this.startDate = new Date();
    }

    public Image(String keyFile,
                 String filename,
                 String size,
                 String resolution,
                 String mimeType,
                 /*String path,*/

                 long fullSize,
                 long fullSizeAfterOptimization,
                 String checksumSHA3,
                 String checksumSHA3AfterOptimization) {
        this.keyFile = keyFile;
        this.filename = filename;
        this.size = size;
        this.resolution = resolution;
        this.mimeType = mimeType;
        /*this.path = path;*/

        this.fullSize = fullSize;
        this.fullSizeAfterOptimization = fullSizeAfterOptimization;
        this.checksumSHA3 = checksumSHA3;
        this.checksumSHA3AfterOptimization = checksumSHA3AfterOptimization;
        this.startDate = new Date();
    }

    public long getImgId() {
        return imgId;
    }

    public void setImgId(long imgId) {
        this.imgId = imgId;
    }

    public String getKeyFile() {
        return keyFile;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /*public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }*/

    public long getFullSize() {
        return fullSize;
    }

    public void setFullSize(long fullSize) {
        this.fullSize = fullSize;
    }

    public long getFullSizeAfterOptimization() {
        return fullSizeAfterOptimization;
    }

    public void setFullSizeAfterOptimization(long fullSizeAfterOptimization) {
        this.fullSizeAfterOptimization = fullSizeAfterOptimization;
    }

    public String getChecksumSHA3() {
        return checksumSHA3;
    }

    public void setChecksumSHA3(String checksumSHA3) {
        this.checksumSHA3 = checksumSHA3;
    }

    public String getChecksumSHA3AfterOptimization() {
        return checksumSHA3AfterOptimization;
    }

    public void setChecksumSHA3AfterOptimization(String checksumSHA3AfterOptimization) {
        this.checksumSHA3AfterOptimization = checksumSHA3AfterOptimization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Set<Folder> getFolders() {
        return folders;
    }

    public void setFolders(Set<Folder> folders) {
        this.folders = folders;
    }
}
