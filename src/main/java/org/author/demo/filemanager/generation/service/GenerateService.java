package org.author.demo.filemanager.generation.service;

import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.dto.GeneratedResponseDto;
import org.author.demo.filemanager.generation.model.DocStatus;
import org.author.demo.filemanager.generation.model.GeneratedDocEntity;
import org.author.demo.filemanager.generation.repository.GeneratedDocRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
public class GenerateService {
    private static final int MIN_PROMPT_LENGTH = 3;
    private static final int MAX_PROMPT_LENGTH = 4000;
    private static final int MAX_SOURCE_FILES = 20;
    private static final String INVALID_PROMPT_MESSAGE = "Prompt is invalid";
    private static final String INVALID_SOURCE_FILES_MESSAGE = "Source file ids are invalid";
    protected static final String NO_SUCH_RESPONSE_FOUND_MESSAGE = "No such response found.";

    private final GeneratedDocRepository docRepository;
    private final GenerateWorker generateWorker;

    public GenerateService(GeneratedDocRepository docRepository, GenerateWorker generateWorker) {
        this.docRepository = docRepository;
        this.generateWorker = generateWorker;
    }

    public GeneratedResponseDto create(GeneratedRequestDto requestDto) {
        validateRequest(requestDto);

        GeneratedDocEntity entity = new GeneratedDocEntity();
        entity.setStatus(DocStatus.PENDING);
        entity.setPrompt(requestDto.prompt());
        entity.setSourceFileIds(requestDto.sourceFileIds());
        entity.setResponseFormat(requestDto.format());

        GeneratedDocEntity saved = docRepository.save(entity);
        generateWorker.generateAsync(saved.getId());

        return entityToDto(saved);
    }

    public GeneratedResponseDto getById(UUID id) {
        GeneratedDocEntity entity = docRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_RESPONSE_FOUND_MESSAGE));

        return entityToDto(entity);
    }

    private GeneratedResponseDto entityToDto(GeneratedDocEntity saved) {
        GeneratedResponseDto responseDto = new GeneratedResponseDto();
        responseDto.setId(saved.getId());
        responseDto.setCreatedAt(saved.getCreatedAt());
        responseDto.setTitle(saved.getTitle());
        responseDto.setContent(saved.getContent());
        responseDto.setStatus(saved.getStatus());
        //todo: reset here content for files
        return responseDto;
    }

    private void validateRequest(GeneratedRequestDto requestDto) {
        if (requestDto == null) {
            throw new IllegalArgumentException(INVALID_PROMPT_MESSAGE);
        }

        validatePrompt(requestDto.prompt());
        validateSourceFileIds(requestDto.sourceFileIds());
    }

    private void validatePrompt(String prompt) {
        if (prompt == null) {
            throw new IllegalArgumentException(INVALID_PROMPT_MESSAGE);
        }

        String trimmedPrompt = prompt.trim();

        if (trimmedPrompt.length() < MIN_PROMPT_LENGTH || trimmedPrompt.length() > MAX_PROMPT_LENGTH) {
            throw new IllegalArgumentException(INVALID_PROMPT_MESSAGE);
        }
    }

    private void validateSourceFileIds(List<UUID> fileSourceIds) {
        if (fileSourceIds == null || fileSourceIds.isEmpty()) {
            throw new IllegalArgumentException(INVALID_SOURCE_FILES_MESSAGE);
        }

        if (fileSourceIds.size() > MAX_SOURCE_FILES) {
            throw new IllegalArgumentException(INVALID_SOURCE_FILES_MESSAGE);
        }

        if (fileSourceIds.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(INVALID_SOURCE_FILES_MESSAGE);
        }
    }
}
