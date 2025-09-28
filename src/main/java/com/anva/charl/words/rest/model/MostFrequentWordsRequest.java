package com.anva.charl.words.rest.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MostFrequentWordsRequest(
    @NotBlank(message = "Text block to search in is missing")
    String text,

    @NotNull(message = "A Limit number must be provided")
    @Positive(message = "The Limit number must be a positive integer")
    Integer limit
) {}
