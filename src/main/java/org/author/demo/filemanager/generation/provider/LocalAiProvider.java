package org.author.demo.filemanager.generation.provider;

import org.author.demo.filemanager.file.model.FileEntity;
import org.author.demo.filemanager.file.repository.FileRepository;
import org.author.demo.filemanager.file.storage.StorageService;
import org.author.demo.filemanager.generation.dto.AiGeneratedRequest;
import org.author.demo.filemanager.generation.dto.AiGeneratedResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.Math.min;

@Service
public class LocalAiProvider implements AiProvider {
    private static final String NO_SUCH_FILE_FOUND_MESSAGE = "No such file found";
    private static final String AI_FILE_READ_FAILED_MESSAGE = "AI file read failed";
    private static final String AI_RESPONSE_IS_EMPTY_MESSAGE = "AI response is empty";
    protected static final String HEADER_CONTEXT_TEMPLATE = "\n-- FILE: %s --\n";
    protected static final String CONTEXT_TEMPLATE = "\n-- CONTEXT --\n";
    protected static final int START = 0;
    private static final int MAX_CONTENT_CHARS = 50_000;
    protected static final String DELIMITER = "\n";

    @Value("${ai.local.base-url}")
    private String baseUrl;
    @Value("${ai.local.model}")
    private String model;
    @Value("${ai.local.stream}")
    private boolean stream;
    private final RestTemplate restTemplate;
    private final FileRepository fileRepository;
    private final StorageService storageService;

    public LocalAiProvider(RestTemplate restTemplate, FileRepository fileRepository, StorageService storageService) {
        this.restTemplate = restTemplate;
        this.fileRepository = fileRepository;
        this.storageService = storageService;
    }

    @Override
    public AiGeneratedResponse generate(String prompt, List<UUID> sourceFileIds) {
        try {
            String fullPrompt = buildPromptWithContext(prompt, sourceFileIds);
            AiGeneratedRequest request = new AiGeneratedRequest(model, fullPrompt, stream);
            AiGeneratedResponse response = restTemplate.postForObject(baseUrl, request, AiGeneratedResponse.class);

            if (response != null) {
                return response;
            } else {
                throw new IllegalStateException(AI_RESPONSE_IS_EMPTY_MESSAGE);
            }
        } catch (MalformedURLException | FileNotFoundException e) {
            throw new IllegalStateException(AI_FILE_READ_FAILED_MESSAGE);
        }
    }

    private String buildPromptWithContext(String prompt, List<UUID> sourceFileIds) throws FileNotFoundException, MalformedURLException {
        StringBuilder context = new StringBuilder();
        context.append(prompt);
        context.append(CONTEXT_TEMPLATE);

        buildContextFromFiles(sourceFileIds, context);

        return context.toString();
    }

    private void buildContextFromFiles(List<UUID> sourceFileIds, StringBuilder context) throws FileNotFoundException, MalformedURLException {
        for (UUID id : sourceFileIds) {
            ExtractedFile result = getExtractedFile(id);
            int limit = MAX_CONTENT_CHARS;

            if (context.length() >= limit) {
                break;
            } else {
                appendContext(context, limit, result);
            }
        }
    }

    private void appendContext(StringBuilder context, int limit, ExtractedFile result) {
        int remaining = limit - context.length();
        String text = result.text();

        context.append(HEADER_CONTEXT_TEMPLATE.formatted(result.fileName()));
        context.append(text, START, min(remaining, text.length()));
    }

    private ExtractedFile getExtractedFile(UUID id) throws FileNotFoundException, MalformedURLException {
        FileEntity entity = fileRepository
                .findById(id)
                .orElseThrow(() -> new FileNotFoundException(NO_SUCH_FILE_FOUND_MESSAGE));
        Resource resource = storageService.loadAsResource(entity.getStoragePath());
        String text = extractTextWithTika(resource);

        return new ExtractedFile(entity.getOriginalName(), text);
    }

    private String extractTextWithTika(Resource resource) {
        TikaDocumentReader documentReader = new TikaDocumentReader(resource);
        List<Document> documents = documentReader.get();

        return documents.stream()
                .map(Document::getText)
                .collect(Collectors.joining(DELIMITER));
    }

    private record ExtractedFile(String fileName, String text) {
    }
}
