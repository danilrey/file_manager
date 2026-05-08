package org.author.demo.filemanager.file.dto;

import org.springframework.core.io.Resource;

public class FileDownloadDto {
    private Resource resource;
    private String filename;
    private String contentType;

    public FileDownloadDto() {
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}