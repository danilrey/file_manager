package org.author.demo.filemanager.generation.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.author.demo.filemanager.generation.dto.AiGeneratedResponse;
import org.author.demo.filemanager.ai.dto.AiGeneratedResponse;
import org.author.demo.filemanager.ai.service.AiService;
import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.provider.AiProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateWorker {

    private static final Logger log = LogManager.getLogger(GenerateWorker.class);

    private final GeneratePersistenceService generatePersistenceService;
    private final AiService aiService;

    public GenerateWorker(GeneratePersistenceService generatePersistenceService, GeneratedDocumentRenderer generatedDocumentRenderer, FileService fileService, AiService aiService) {
        this.generatePersistenceService = generatePersistenceService;
        this.aiService = aiService;
    }

    @Async("generationExecutor")
    public void generateAsync(UUID id) {

        try {
            generatePersistenceService.markInProgress(id);

            GeneratedRequestDto requestDto = generatePersistenceService.loadRequest(id);
            AiGeneratedResponse response = aiService.generate(requestDto.prompt(), requestDto.sourceFileIds());

            generatePersistenceService.saveSuccess(id, response);
        } catch (Exception exception) {
            log.error("Generation failed for id: {}", id, exception);
            generatePersistenceService.saveFailure(id);

        }
    }
}

