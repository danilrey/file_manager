package org.author.demo.filemanager.generation.service;

import org.author.demo.filemanager.generation.dto.AiGeneratedResponse;
import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.model.DocStatus;
import org.author.demo.filemanager.generation.model.GeneratedDocEntity;
import org.author.demo.filemanager.generation.repository.GeneratedDocRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GeneratePersistenceService {
    protected static final String GENERATED_DOC_NOT_FOUND_MESSAGE = "GeneratedDoc not found";
    private final GeneratedDocRepository generatedDocRepository;

    public GeneratePersistenceService(GeneratedDocRepository generatedDocRepository) {
        this.generatedDocRepository = generatedDocRepository;
    }

    @Transactional
    public void markInProgress(UUID id) {
        GeneratedDocEntity entity = generatedDocRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(GENERATED_DOC_NOT_FOUND_MESSAGE));

        entity.setStatus(DocStatus.IN_PROGRESS);
    }

    @Transactional
    public void saveSuccess(UUID id, AiGeneratedResponse response) {
        GeneratedDocEntity entity = generatedDocRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(GENERATED_DOC_NOT_FOUND_MESSAGE));

        entity.setTitle(response.title());
        entity.setContent(response.response());
        entity.setStatus(DocStatus.DONE);
    }

    @Transactional
    public void saveFailure(UUID id) {
        GeneratedDocEntity entity = generatedDocRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(GENERATED_DOC_NOT_FOUND_MESSAGE));

        entity.setStatus(DocStatus.FAILED);
    }

    @Transactional
    public GeneratedRequestDto loadRequest(UUID id) {
        GeneratedDocEntity entity = generatedDocRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException(GENERATED_DOC_NOT_FOUND_MESSAGE));

        return new GeneratedRequestDto(
                entity.getPrompt(),
                List.copyOf(entity.getSourceFileIds())
        );
    }
}
