package com.cgram.prom.tempmail.ncloud.model;

import java.util.List;
import java.util.stream.Collectors;

public class FileUploadResponse {

    private String tempRequestId;
    private List<FileUploadResponseFile> files;

    public String getTempRequestId() {
        return tempRequestId;
    }

    public void setTempRequestId(String tempRequestId) {
        this.tempRequestId = tempRequestId;
    }

    public List<FileUploadResponseFile> getFiles() {
        return files;
    }

    public void setFiles(List<FileUploadResponseFile> files) {
        this.files = files;
    }

    public List<String> getFileIds() {
        return files.stream().map(FileUploadResponseFile::getFileId).collect(Collectors.toList());
    }

}
