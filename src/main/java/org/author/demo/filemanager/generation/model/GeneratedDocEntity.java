package org.author.demo.filemanager.generation.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "generated_docs")
public class GeneratedDocEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String prompt;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String title;

    @ElementCollection
    @CollectionTable(name = "generated_doc_sources",
            joinColumns = @JoinColumn(name = "doc_id"))
    @Column(name = "file_id")
    private List<UUID> sourceFileIds;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocStatus status = DocStatus.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResponseFormat responseFormat = ResponseFormat.TEXT;

    private UUID resultFileId;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<UUID> getSourceFileIds() {
        return sourceFileIds;
    }

    public void setSourceFileIds(List<UUID> sourceFileIds) {
        this.sourceFileIds = sourceFileIds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DocStatus getStatus() {
        return status;
    }

    public void setStatus(DocStatus status) {
        this.status = status;
    }

    public UUID getResultFileId() {
        return resultFileId;
    }

    public void setResultFileId(UUID resultFileId) {
        this.resultFileId = resultFileId;
    }

    public ResponseFormat getResponseFormat() {
        return responseFormat;
    }

    public void setResponseFormat(ResponseFormat responseFormat) {
        this.responseFormat = responseFormat;
    }
}
