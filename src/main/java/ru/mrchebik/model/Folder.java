package ru.mrchebik.model;

import javax.persistence.*;

/**
 * Created by mrchebik on 6/30/17.
 */
@Entity
@Table(name = "folder")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long fldrId;

    private String keyFolder;

    @ManyToOne
    @JoinColumn(name = "keyFile")
    private Image image;

    public Folder() {
    }

    public Folder(Image image,
                  String keyFolder) {
        this.image = image;
        this.keyFolder = keyFolder;
    }

    public long getFldrId() {
        return fldrId;
    }

    public void setFldrId(long fldrId) {
        this.fldrId = fldrId;
    }

    public String getKeyFolder() {
        return keyFolder;
    }

    public void setKeyFolder(String keyFolder) {
        this.keyFolder = keyFolder;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
