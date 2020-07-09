package com.multiscreenlimited.smallfilemanager.Models;

public class FilesModel {

    public String fileName;
    public Boolean isDirectory;
    public Long dateCreated;
    public Boolean isAPK;
    public Boolean isZip;
    public Boolean isImage;

    public Boolean getAPK() {
        return isAPK;
    }

    public void setAPK(Boolean APK) {
        isAPK = APK;
    }

    public Boolean getZip() {
        return isZip;
    }

    public void setZip(Boolean zip) {
        isZip = zip;
    }

    public Boolean getImage() {
        return isImage;
    }

    public void setImage(Boolean image) {
        isImage = image;
    }

    public Long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Long dateCreated) {
        this.dateCreated = dateCreated;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String filePath;

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
