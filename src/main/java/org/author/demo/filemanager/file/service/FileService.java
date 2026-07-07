package org.author.demo.filemanager.file.service;

import org.author.demo.filemanager.file.dto.FileDownloadDto;
import org.author.demo.filemanager.file.dto.FileResponseDto;
import org.author.demo.filemanager.file.model.FileEntity;
import org.author.demo.filemanager.file.repository.FileRepository;
import org.author.demo.filemanager.file.storage.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;


@Service
public class FileService {
    private static final String NO_SUCH_FILE_FOUND_MESSAGE = "No such file found.";
    private static final String FILE_IS_EMPTY_MESSAGE = "File is empty";

    private final FileRepository fileRepository;
    private final StorageService storageService;

    public FileService(FileRepository fileRepository, StorageService storageService) {
        this.fileRepository = fileRepository;
        this.storageService = storageService;
    }

    public FileResponseDto upload(MultipartFile file, List<String> tags) {
        if (isValidFile(file)) {
            List<String> safeTags = (tags == null) ? List.of() : List.copyOf(tags);
            String storedPath = storageService.save(file);
            FileEntity entity = getFileEntity(file, safeTags, storedPath);
            FileEntity saved = fileRepository.save(entity);

            return entityToDto(saved);
        } else {
            throw new IllegalArgumentException(FILE_IS_EMPTY_MESSAGE);
        }
    }

    public List<FileResponseDto> getAll() {
        return fileRepository.findAll().stream()
                .map(this::entityToDto)
                .toList();
    }

    public FileResponseDto findById(UUID id) throws FileNotFoundException {
        FileEntity entity = fileRepository
                .findById(id)
                .orElseThrow(() -> new FileNotFoundException(NO_SUCH_FILE_FOUND_MESSAGE));

        return entityToDto(entity);
    }

    public void deleteById(UUID id) throws FileNotFoundException {
        FileEntity entity = fileRepository
                .findById(id)
                .orElseThrow(() -> new FileNotFoundException(NO_SUCH_FILE_FOUND_MESSAGE));

        storageService.deleteFile(entity.getStoragePath());
        fileRepository.deleteById(entity.getId());
    }

    public FileDownloadDto download(UUID id) throws FileNotFoundException, MalformedURLException {
        FileEntity entity = fileRepository
                .findById(id)
                .orElseThrow(() -> new FileNotFoundException(NO_SUCH_FILE_FOUND_MESSAGE));

        Resource resource = storageService.loadAsResource(entity.getStoragePath());

        FileDownloadDto fileDownloadDto = new FileDownloadDto();
        fileDownloadDto.setResource(resource);
        fileDownloadDto.setFilename(entity.getOriginalName());
        fileDownloadDto.setContentType(entity.getMimeType());

        return fileDownloadDto;
    }

    public UUID saveGeneratedFile(byte[] data, String originalName, String mimeType) {
        if (isValidData(data)) {
            String storedPath = storageService.saveBytes(data, originalName);

            FileEntity entity = new FileEntity();
            entity.setOriginalName(originalName);
            entity.setMimeType(mimeType);
            entity.setSizeBytes(data.length);
            entity.setStoragePath(storedPath);
            entity.setTags(List.of());

            FileEntity saved = fileRepository.save(entity);

            return saved.getId();
        } else {
            throw new IllegalArgumentException(FILE_IS_EMPTY_MESSAGE);
        }
    }

    private boolean isValidData(byte[] data) {
        return data != null && data.length != 0;
    }

    private boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }

    private FileResponseDto entityToDto(FileEntity entity) {
        FileResponseDto dto = new FileResponseDto();
        dto.setId(entity.getId());
        dto.setFilename(entity.getOriginalName());
        dto.setContentType(entity.getMimeType());
        dto.setTags(entity.getTags());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setSizeBytes(entity.getSizeBytes());

        return dto;
    }

    private FileEntity getFileEntity(MultipartFile file, List<String> tags, String storedPath) {
        FileEntity entity = new FileEntity();
        entity.setOriginalName(file.getOriginalFilename());
        entity.setMimeType(file.getContentType());
        entity.setSizeBytes(file.getSize());
        entity.setStoragePath(storedPath);
        entity.setTags(tags);

        return entity;
    }
}
