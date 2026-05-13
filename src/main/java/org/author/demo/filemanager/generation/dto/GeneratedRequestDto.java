package org.author.demo.filemanager.generation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record GeneratedRequestDto(
        @NotBlank
        @Size(max = 500, message = "prompt is too long")
        String prompt,

        @NotNull
        List<UUID> sourceFileIds
) {
}