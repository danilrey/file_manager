package org.author.demo.filemanager.generation.controller;

import org.author.demo.filemanager.generation.dto.GeneratedRequestDto;
import org.author.demo.filemanager.generation.dto.GeneratedResponseDto;
import org.author.demo.filemanager.generation.service.GenerateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/generate")
public class GenerationController {

    private final GenerateService generateService;

    public GenerationController(GenerateService generateService) {
        this.generateService = generateService;
    }

    @PostMapping
    public ResponseEntity<GeneratedResponseDto> generate(@RequestBody GeneratedRequestDto requestDto) throws IOException {
        GeneratedResponseDto responseDto = generateService.create(requestDto);

        return ResponseEntity.accepted().body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneratedResponseDto> checkById(@PathVariable UUID id) {
        GeneratedResponseDto responseDto = generateService.getById(id);

        return ResponseEntity.ok().body(responseDto);
    }
}
