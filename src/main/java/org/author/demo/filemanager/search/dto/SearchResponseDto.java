package org.author.demo.filemanager.search.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class SearchResponseDto {
    private String type;
    private UUID id;
    private String title;
    private LocalDateTime createdAt;

    public SearchResponseDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
