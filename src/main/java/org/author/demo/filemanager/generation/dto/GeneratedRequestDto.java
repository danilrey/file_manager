package org.author.demo.filemanager.generation.dto;

import java.util.List;
import java.util.UUID;

public record GeneratedRequestDto(String prompt, List<UUID> sourceFileIds) {
}