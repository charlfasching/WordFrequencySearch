package com.anva.charl.words.rest.api;

import com.anva.charl.words.data.model.WordFrequency;
import com.anva.charl.words.rest.model.HighestFrequencyRequest;
import com.anva.charl.words.rest.model.HighestFrequencyResponse;
import com.anva.charl.words.rest.model.HighestFrequencySpecificRequest;
import com.anva.charl.words.rest.model.MostFrequentWordsRequest;
import com.anva.charl.words.service.SimpleFrequencySearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Controller for word frequency analysis operations.
 * Provides endpoints for analyzing word frequencies in text content with support for:
 * - Finding the highest frequency word
 * - Calculating frequency of specific words
 * - Finding N most frequent words
 *
 * All endpoints support multi-language text and handle various text processing requirements
 * including punctuation and case-sensitivity.
 */
@RestController
@RequestMapping("/api/words/")
public class WordFrequencyController {

    private final SimpleFrequencySearchService searchService;

    public WordFrequencyController(SimpleFrequencySearchService simpleFrequencySearchService) {
        this.searchService = simpleFrequencySearchService;
    }

    /**
     * Calculates the highest frequency of any word in the provided text.
     *
     * @param request Contains the text to analyze
     * @return ResponseEntity with HighestFrequencyResponse containing the highest frequency count
     */
    @PostMapping("/freq/highest")
    public ResponseEntity<HighestFrequencyResponse> calculateHighestFrequency(@Valid @RequestBody HighestFrequencyRequest request) {
        int highestOccurrence = searchService.calculateHighestFrequency(request.text());
        return ResponseEntity.ok(new HighestFrequencyResponse(highestOccurrence));
    }

    /**
     * Calculates the frequency of a specific word in the provided text.
     * The search is case-insensitive and handles various forms of punctuation.
     *
     * @param request Contains the text to analyze and the specific word to search for
     * @return ResponseEntity with HighestFrequencyResponse containing the frequency count for the specified word
     */
    @PostMapping("/freq/specific")
    public ResponseEntity<HighestFrequencyResponse> calculateFrequencyForWord(@Valid @RequestBody HighestFrequencySpecificRequest request) {
        int occurrence = searchService.calculateFrequencyForWord(request.text(), request.word());
        return ResponseEntity.ok(new HighestFrequencyResponse(occurrence));
    }

    /**
     * Finds the N most frequent words in the provided text.
     * Results are sorted by frequency (descending) and then alphabetically.
     *
     * @param request Contains the text to analyze and the limit (N) for number of results
     * @return ResponseEntity with List of WordFrequency objects containing the top N most frequent words
     */
    @PostMapping("/freq/page")
    public ResponseEntity<List<WordFrequency>> calculateFrequencyForWord(@Valid @RequestBody MostFrequentWordsRequest request) {
        var wordList = searchService.calculateMostFrequentNWords(request.text(), request.limit());
        return ResponseEntity.ok(wordList);
    }
}
