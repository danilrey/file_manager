package org.author.demo.filemanager.file.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class MimeTypeValidator implements ConstraintValidator<AllowedMimeTypes, MultipartFile> {
    private Set<String> allowed;

    @Override
    public void initialize(AllowedMimeTypes constraintAnnotation) {
        allowed = Set.of(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            String contentType = value.getContentType();

            return contentType != null && allowed.contains(contentType);
        }
    }
}
