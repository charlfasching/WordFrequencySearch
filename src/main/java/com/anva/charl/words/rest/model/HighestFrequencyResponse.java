package com.anva.charl.words.rest.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record HighestFrequencyResponse(Integer amount) {}
