package com.anva.charl.words.rest.model;

import jakarta.validation.constraints.NotBlank;

public record HighestFrequencyRequest(
    @NotBlank(message = "Text input to search in is missing")
    String text
) {}
