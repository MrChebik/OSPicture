package ru.mrchebik.model;

/**
 * Created by mrchebik on 6/22/17.
 */
public class InfoImage {
    private String key;
    private String name;
    private String size;
    private String format;
    private String isOctetStream;
    private String resolution;

    private String px500Path;
    private String px200Path;

    private String px500TRUE;
    private String px200TRUE;

    private String folderLeft;
    private String folderRight;

    public InfoImage(String key, String name, String size, String format, String isOctetStream, String resolution) {
        this.key = key;
        this.name = name;
        this.size = size;
        this.format = format;
        this.isOctetStream = isOctetStream;
        this.resolution = resolution;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getIsOctetStream() {
        return isOctetStream;
    }

    public void setIsOctetStream(String isOctetStream) {
        this.isOctetStream = isOctetStream;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getPx500Path() {
        return px500Path;
    }

    public void setPx500Path(String px500Path) {
        this.px500Path = px500Path;
    }

    public String getPx200Path() {
        return px200Path;
    }

    public void setPx200Path(String px200Path) {
        this.px200Path = px200Path;
    }

    public String getPx500TRUE() {
        return px500TRUE;
    }

    public void setPx500TRUE(String px500TRUE) {
        this.px500TRUE = px500TRUE;
    }

    public String getPx200TRUE() {
        return px200TRUE;
    }

    public void setPx200TRUE(String px200TRUE) {
        this.px200TRUE = px200TRUE;
    }

    public String getFolderLeft() {
        return folderLeft;
    }

    public void setFolderLeft(String folderLeft) {
        this.folderLeft = folderLeft;
    }

    public String getFolderRight() {
        return folderRight;
    }

    public void setFolderRight(String folderRight) {
        this.folderRight = folderRight;
    }
}
