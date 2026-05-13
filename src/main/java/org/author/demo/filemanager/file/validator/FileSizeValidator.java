package org.author.demo.filemanager.file.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileSizeValidator implements ConstraintValidator<FileSize, MultipartFile> {
    private long maxBytes;

    @Override
    public void initialize(FileSize constraintAnnotation) {
        this.maxBytes = constraintAnnotation.maxBytes();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value == null || value.getSize() <= maxBytes;
    }
}
