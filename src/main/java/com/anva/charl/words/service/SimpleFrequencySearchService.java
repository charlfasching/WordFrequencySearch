package com.anva.charl.words.service;

import com.anva.charl.words.data.model.WordFrequency;
import com.anva.charl.words.data.model.WordFrequencyDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class SimpleFrequencySearchService implements WordFrequencyAnalyzer {

    private static final int LARGE_TEXT_THRESHOLD = 10_000;
    // Updated pattern to keep Unicode letters and numbers, removing all other characters
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}]+");

    private Map<String, Integer> buildFrequencyMap(String text) {
        if (text == null || text.isEmpty()) return Collections.emptyMap();

        Map<String, Integer> freqMap = new HashMap<>();
        var matcher = WORD_PATTERN.matcher(text.toLowerCase(Locale.ROOT));

        while (matcher.find()) {
            String word = matcher.group();
            if (!word.isEmpty()) {
                freqMap.put(word, freqMap.getOrDefault(word, 0) + 1);
            }
        }
        return freqMap;
    }

    @Override
    public int calculateHighestFrequency(String text) {
        return buildFrequencyMap(text).values().stream()
                .max(Integer::compareTo).orElse(0);
    }

    @Override
    public int calculateFrequencyForWord(String text, String word) {
        if (word == null || word.isBlank()) return 0;
        return buildFrequencyMap(text).getOrDefault(word.toLowerCase(Locale.ROOT), 0);
    }

    /**
     * It was considered whether this should be a stream API chain.
     * I found that for complex logic that is not streamed in parallel, imperative style is easier debug.
     * @param text
     * @param n
     * @return
     */
//    @Override
    public List<WordFrequency> calculateMostFrequentNWordsWithStream(String text, int n) {

       return buildFrequencyMap(text).entrySet().stream()
                .map(e -> (WordFrequency)new WordFrequencyDTO(e.getKey(), e.getValue()))
                .sorted(Comparator
                        .comparing(WordFrequency::frequency, Comparator.reverseOrder())
                        .thenComparing(WordFrequency::word))
                .limit(n)
                .toList();
    }

    @Override
    public List<WordFrequency> calculateMostFrequentNWords(String text, int n) {

        List<WordFrequency> wordsToSort = new ArrayList<>();
        for (Map.Entry<String, Integer> e : buildFrequencyMap(text).entrySet()) {
            WordFrequency wordFrequency = new WordFrequencyDTO(e.getKey(), e.getValue());
            wordsToSort.add(wordFrequency);
        }
        wordsToSort.sort(Comparator
                .comparing(WordFrequency::frequency, Comparator.reverseOrder())
                .thenComparing(WordFrequency::word));
        List<WordFrequency> list = new ArrayList<>();

        long limit = n;
        for (WordFrequency wordFrequency : wordsToSort) {
            if (limit-- == 0) break;
            list.add(wordFrequency);
        }
        return list;
    }
}
