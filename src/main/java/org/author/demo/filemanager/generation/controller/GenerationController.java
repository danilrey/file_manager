package org.author.demo.filemanager.generation.controller;

import jakarta.validation.Valid;
import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.dto.GeneratedResponseDto;
import org.author.demo.filemanager.generation.service.GenerateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/generate")
public class GenerationController {

    private final GenerateService generateService;

    public GenerationController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping
    public ResponseEntity<GeneratedResponseDto> generate(@Valid @RequestBody GeneratedRequestDto requestDto) {
        GeneratedResponseDto responseDto = generateService.create(requestDto);

        return ResponseEntity.accepted().body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneratedResponseDto> checkById(@PathVariable UUID id) {
        GeneratedResponseDto responseDto = generateService.getById(id);

        return ResponseEntity.ok().body(responseDto);
    }
}
