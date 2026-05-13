package org.author.demo.filemanager.file.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MimeTypeValidator.class)
public @interface AllowedMimeTypes {
    String message() default "Unsupported MIME type";

    String[] value();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
