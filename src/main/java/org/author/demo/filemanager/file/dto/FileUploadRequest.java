package org.author.demo.filemanager.file.dto;

import jakarta.validation.constraints.NotNull;
import org.author.demo.filemanager.file.validator.AllowedMimeTypes;
import org.author.demo.filemanager.file.validator.FileSize;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileUploadRequest {

    @NotNull(message = "File is required")
    @FileSize(maxBytes = 50L * 1024 * 1024, message = "Max size is 50MB")
    @AllowedMimeTypes({"application/pdf"})
    private MultipartFile file;

    private List<String> tags;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
