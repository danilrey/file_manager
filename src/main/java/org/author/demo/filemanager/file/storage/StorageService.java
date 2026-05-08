package org.author.demo.filemanager.file.storage;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class StorageService {
    protected static final String FILE_IS_EMPTY_MESSAGE = "File is empty";
    protected static final String FAILED_TO_STORE_FILE_MESSAGE = "Failed to store file";
    protected static final String INVALID_STORAGE_PATH_MESSAGE = "Invalid storage path";
    protected static final String FAILED_TO_INIT_STORAGE_DIRECTORY_MESSAGE = "Failed to init storage directory";
    protected static final String FAILED_TO_DELETE_FILE_MESSAGE = "Failed to delete file";
    protected static final char CHAR = '.';
    protected static final String RESOURCE_IS_NOT_READABLE_OR_NOT_EXISTS_MESSAGE = "Resource is not readable or not exists";

    private final Path basePath;

    public StorageService(@Value("${storage.base-path:uploads}") String basePath) {
        this.basePath = Paths.get(basePath).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(basePath);
        } catch (IOException e) {
            throw new IllegalStateException(FAILED_TO_INIT_STORAGE_DIRECTORY_MESSAGE, e);
        }
    }

    public String save(MultipartFile file) {
        if (isValidFile(file)) {
            String originalName = file.getOriginalFilename();
            String filename = UUID.randomUUID() + getExtension(originalName);
            Path target = basePath.resolve(filename).normalize();

            checkPathStartsWithBase(target);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IllegalStateException(FAILED_TO_STORE_FILE_MESSAGE, e);
            }

            return filename;
        } else {
            throw new IllegalArgumentException(FILE_IS_EMPTY_MESSAGE);
        }
    }

    public String saveBytes(byte[] data, String originalName) {
        if (data != null && data.length > 0 && originalName != null && !originalName.isBlank()) {
            String filename = UUID.randomUUID() + getExtension(originalName);
            Path target = basePath.resolve(filename).normalize();

            checkPathStartsWithBase(target);

            try (InputStream inputStream = new ByteArrayInputStream(data)) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IllegalStateException(FAILED_TO_STORE_FILE_MESSAGE, e);
            }

            return filename;
        } else {
            throw new IllegalArgumentException(FILE_IS_EMPTY_MESSAGE);
        }
    }

    public Resource loadAsResource(String storagePath) throws MalformedURLException {
        if (isValidPath(storagePath)) {
            Path target = basePath.resolve(storagePath).normalize();

            checkPathStartsWithBase(target);

            UrlResource resource = new UrlResource(target.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new IllegalArgumentException(RESOURCE_IS_NOT_READABLE_OR_NOT_EXISTS_MESSAGE);
            }

            return resource;
        } else {
            throw new IllegalArgumentException(INVALID_STORAGE_PATH_MESSAGE);
        }
    }

    public void deleteFile(String storagePath) {
        if (isValidPath(storagePath)) {
            Path target = basePath.resolve(storagePath).normalize();

            checkPathStartsWithBase(target);

            try {
                Files.deleteIfExists(target);
            } catch (IOException e) {
                throw new IllegalStateException(FAILED_TO_DELETE_FILE_MESSAGE, e);
            }
        }
    }

    private void checkPathStartsWithBase(Path target) {
        if (!target.startsWith(basePath)) {
            throw new IllegalArgumentException(INVALID_STORAGE_PATH_MESSAGE);
        }
    }

    private boolean isValidPath(String storagePath) {
        return storagePath != null && !storagePath.isBlank();
    }

    private String getExtension(String originalName) {
        if (originalName != null) {
            int dotIndex = originalName.lastIndexOf(CHAR);
            if (dotIndex >= 0) {
                return originalName.substring(dotIndex);
            }
        }

        return "";
    }

    private boolean isValidFile(MultipartFile file) {
        return file != null && !file.isEmpty();
    }
}
