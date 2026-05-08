package org.author.demo.filemanager.file.controller;

import org.author.demo.filemanager.file.dto.FileDownloadDto;
import org.author.demo.filemanager.file.dto.FileResponseDto;
import org.author.demo.filemanager.file.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {
    protected static final String CONTENT_DISPOSITION = "Content-Disposition";
    protected static final String HEADER_VALUE_TEMPLATE = "attachment; filename=\"%s\"";

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileResponseDto> uploadFile(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "tags", required = false) List<String> tags) {
        FileResponseDto created = fileService.upload(file, tags);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<FileResponseDto>> findAllFiles() {
        List<FileResponseDto> dtos = fileService.getAll();

        return ResponseEntity.ok().body(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileResponseDto> findFileById(@PathVariable UUID id) throws FileNotFoundException {
        FileResponseDto dto = fileService.findById(id);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFileById(@PathVariable UUID id) throws FileNotFoundException {
        fileService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable UUID id) throws FileNotFoundException, MalformedURLException {
        FileDownloadDto dto = fileService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dto.getContentType()))
                .header(CONTENT_DISPOSITION, HEADER_VALUE_TEMPLATE.formatted(dto.getFilename()))
                .body(dto.getResource());
    }
}
