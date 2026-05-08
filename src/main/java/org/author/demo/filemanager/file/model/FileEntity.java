package org.author.demo.filemanager.file.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "files")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storagePath;

    @Column(nullable = false)
    private String mimeType;

    private long sizeBytes;

    @Column(columnDefinition = "TEXT")
    private String summary;

    private String category;

    @ElementCollection
    @CollectionTable(name = "file_tag", joinColumns = @JoinColumn(name = "file_id"))
    @Column(name = "tag")
    private List<String> tags;

//    @Column(name = "embedding", columnDefinition = "vector(768)")
//    @JdbcTypeCode(SqlTypes.VECTOR)
//    private float[] embedding;

    @Column(nullable = false)
    private boolean aiGenerated = false;

    @Column(nullable = false)
    private boolean isIndexed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public FileEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String name) {
        this.originalName = name;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String type) {
        this.mimeType = type;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean isAiGenerated() {
        return aiGenerated;
    }

    public void setAiGenerated(boolean generated) {
        this.aiGenerated = generated;
    }

    public boolean isIsIndexed() {
        return isIndexed;
    }

    public void setIsIndexed(boolean indexed) {
        this.isIndexed = indexed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
