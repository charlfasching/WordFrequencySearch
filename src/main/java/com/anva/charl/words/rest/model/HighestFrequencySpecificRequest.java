package com.anva.charl.words.rest.model;

import jakarta.validation.constraints.NotBlank;

public record HighestFrequencySpecificRequest(
    @NotBlank(message = "Text block to search in is missing")
    String text,

    @NotBlank(message = "A word to search for is missing")
    String word
) {}
