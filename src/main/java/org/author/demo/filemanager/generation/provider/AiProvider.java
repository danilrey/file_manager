package org.author.demo.filemanager.generation.provider;

import org.author.demo.filemanager.generation.dto.AiGeneratedResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface AiProvider {
    AiGeneratedResponse generate(String prompt, List<UUID> sourceFileIds);
}