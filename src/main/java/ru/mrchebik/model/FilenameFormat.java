package ru.mrchebik.model;

/**
 * Created by mrchebik on 6/17/17.
 */
public class FilenameFormat {
    private String filename;
    private String format;
    private boolean isOctetStream;

    public FilenameFormat(String filename, String format) {
        this.filename = filename;
        this.format = format;
    }

    public FilenameFormat(String filename, String format, boolean isOctetStream) {
        this.filename = filename;
        this.format = format;
        this.isOctetStream = isOctetStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public boolean isOctetStream() {
        return isOctetStream;
    }

    public void setOctetStream(boolean octetStream) {
        isOctetStream = octetStream;
    }
}
