package org.author.demo.filemanager.generation.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.author.demo.filemanager.generation.dto.AiGeneratedResponse;
import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.provider.AiProvider;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateWorker {

    private static final Logger log = LogManager.getLogger(GenerateWorker.class);
    private final AiProvider aiProvider;
    private final GeneratePersistenceService generatePersistenceService;

    public GenerateWorker(AiProvider aiProvider, GeneratePersistenceService generatePersistenceService) {
        this.aiProvider = aiProvider;
        this.generatePersistenceService = generatePersistenceService;
    }

    @Async("generationExecutor")
    public void generateAsync(UUID id) {

        try {
            generatePersistenceService.markInProgress(id);

            GeneratedRequestDto requestDto = generatePersistenceService.loadRequest(id);
            AiGeneratedResponse response = aiProvider.generate(requestDto.prompt(), requestDto.sourceFileIds());

            generatePersistenceService.saveSuccess(id, response);
        } catch (Exception exception) {
            log.error("Generation failed for id: {}", id, exception);
            generatePersistenceService.saveFailure(id);

        }
    }
}

