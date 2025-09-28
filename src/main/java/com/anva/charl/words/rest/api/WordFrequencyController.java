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

@RestController
@RequestMapping("/api/words/")
public class WordFrequencyController {

    private final SimpleFrequencySearchService searchService;

    public WordFrequencyController(SimpleFrequencySearchService simpleFrequencySearchService) {
        this.searchService = simpleFrequencySearchService;
    }

    @PostMapping("/freq/highest")
    public ResponseEntity<HighestFrequencyResponse> calculateHighestFrequency(@Valid @RequestBody HighestFrequencyRequest request) {
        int highestOccurrence = searchService.calculateHighestFrequency(request.text());
        return ResponseEntity.ok(new HighestFrequencyResponse(highestOccurrence));
    }

    @PostMapping("/freq/specific")
    public ResponseEntity<HighestFrequencyResponse> calculateFrequencyForWord(@Valid @RequestBody HighestFrequencySpecificRequest request) {
        int occurrence = searchService.calculateFrequencyForWord(request.text(), request.word());
        return ResponseEntity.ok(new HighestFrequencyResponse(occurrence));
    }

    @PostMapping("/freq/page")
    public ResponseEntity<List<WordFrequency>> calculateFrequencyForWord(@Valid @RequestBody MostFrequentWordsRequest request) {
        var wordList = searchService.calculateMostFrequentNWords(request.text(), request.limit());
        return ResponseEntity.ok(wordList);
    }
}
