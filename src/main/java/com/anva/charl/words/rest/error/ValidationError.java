package com.anva.charl.words.rest.error;

import java.util.List;

public record ValidationError(
    String problem,
    List<FieldError> fieldErrors
) {
    public record FieldError(
        String field,
        String message
    ) {}
}
