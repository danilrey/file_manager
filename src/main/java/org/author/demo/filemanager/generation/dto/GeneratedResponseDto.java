package org.author.demo.filemanager.generation.dto;

import org.author.demo.filemanager.generation.model.DocStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class GeneratedResponseDto {
    private UUID id;
    private String title;
    private String content;
    private DocStatus status;
    private LocalDateTime createdAt;

    public GeneratedResponseDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DocStatus getStatus() {
        return status;
    }

    public void setStatus(DocStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
