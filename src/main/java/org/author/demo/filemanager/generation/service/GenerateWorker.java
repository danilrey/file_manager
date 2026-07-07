package org.author.demo.filemanager.generation.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.author.demo.filemanager.ai.dto.AiGeneratedResponse;
import org.author.demo.filemanager.ai.service.AiService;
import org.author.demo.filemanager.file.service.FileService;
import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.model.ResponseFormat;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GenerateWorker {
    private static final String PDF_TEMPLATE = "generated-%s.pdf";
    private static final String DOCX_TEMPLATE = "generated-%s.docx";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final String DOCX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    private static final Logger log = LogManager.getLogger(GenerateWorker.class);

    private final GeneratePersistenceService generatePersistenceService;
    private final GeneratedDocumentRenderer generatedDocumentRenderer;
    private final FileService fileService;
    private final AiService aiService;

    public GenerateWorker(GeneratePersistenceService generatePersistenceService, GeneratedDocumentRenderer generatedDocumentRenderer, FileService fileService, AiService aiService) {
        this.generatePersistenceService = generatePersistenceService;
        this.generatedDocumentRenderer = generatedDocumentRenderer;
        this.fileService = fileService;
        this.aiService = aiService;
    }

    @Async("generationExecutor")
    public void generateAsync(UUID id) {

        try {
            generatePersistenceService.markInProgress(id);

            GeneratedRequestDto requestDto = generatePersistenceService.loadRequest(id);
            AiGeneratedResponse response = aiService.generate(requestDto.prompt(), requestDto.sourceFileIds());

            saveByFormat(requestDto.format(), id, response);
        } catch (Exception exception) {
            log.error("Generation failed for id: {}", id, exception);
            generatePersistenceService.saveFailure(id);

        }
    }

    private void saveByFormat(ResponseFormat format, UUID id, AiGeneratedResponse response) {
        switch (format) {
            case TEXT -> generatePersistenceService.saveSuccess(id, response,ResponseFormat.TEXT);

            case DOCX -> {
                byte[] bytes = generatedDocumentRenderer.renderDocx(response.title(), response.response());
                String fileName = DOCX_TEMPLATE.formatted(id);

                fileService.saveGeneratedFile(bytes, fileName, DOCX_MIME_TYPE);
                generatePersistenceService.saveSuccess(id, response,ResponseFormat.DOCX);
            }

            case PDF -> {
                byte[] bytes = generatedDocumentRenderer.renderPdf(response.title(), response.response());
                String fileName = PDF_TEMPLATE.formatted(id);

                fileService.saveGeneratedFile(bytes, fileName, PDF_MIME_TYPE);
                generatePersistenceService.saveSuccess(id, response,ResponseFormat.PDF);
            }
        }
    }
}