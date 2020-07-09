package com.multiscreenlimited.smallfilemanager.Models;

import java.io.File;

public class ImageModel {

    String name;
    String url;
    String dateModified;
    String type;
    String parentfolder;
    File imageFile;

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }



    public String getParentfolder() {
        return parentfolder;
    }

    public void setParentfolder(String parentfolder) {
        this.parentfolder = parentfolder;
    }


    public ImageModel() {
    }
    // Getters & Setters here

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}