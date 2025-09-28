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

/**
 * Service implementation for analyzing word frequencies in text.
 * Provides functionality for:
 * - Finding the highest word frequency
 * - Calculating specific word frequencies
 * - Finding N most frequent words
 *
 * Features:
 * - Case-insensitive word matching
 * - Unicode support for multi-language text
 * - Efficient punctuation handling using regex
 * - Thread-safe implementation
 */
@Service
public class SimpleFrequencySearchService implements WordFrequencyAnalyzer {

    private static final int LARGE_TEXT_THRESHOLD = 10_000;
    // Updated pattern to keep Unicode letters and numbers, removing all other characters
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}]+");

    /**
     * Creates a frequency map of words in the given text.
     * Words are processed case-insensitively and punctuation is removed.
     *
     * @param text The input text to analyze
     * @return Map with words as keys and their frequencies as values
     */
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

    /**
     * Finds the highest frequency of any word in the given text.
     * Handles empty or null input by returning 0.
     *
     * @param text The input text to analyze
     * @return The frequency of the most frequent word, or 0 if text is empty/null
     */
    @Override
    public int calculateHighestFrequency(String text) {
        return buildFrequencyMap(text).values().stream()
                .max(Integer::compareTo).orElse(0);
    }

    /**
     * Calculates how many times a specific word appears in the text.
     * The search is case-insensitive and ignores punctuation.
     *
     * @param text The input text to analyze
     * @param word The specific word to search for
     * @return The frequency of the specified word, or 0 if text/word is empty/null
     */
    @Override
    public int calculateFrequencyForWord(String text, String word) {
        if (word == null || word.isBlank()) return 0;
        return buildFrequencyMap(text).getOrDefault(word.toLowerCase(Locale.ROOT), 0);
    }

    /**
     * Alternative implementation using Stream API.
     * Finds the N most frequent words in the text.
     * Results are sorted by frequency (descending) and then alphabetically.
     *
     * Note: This implementation is currently not used as the imperative version
     * was easier to debug the sorting logic, but left it for reference.
     *
     * @param text The input text to analyze
     * @param n The maximum number of results to return
     * @return List of WordFrequency objects containing the top N words
     */
    public List<WordFrequency> calculateMostFrequentNWordsWithStream(String text, int n) {

       return buildFrequencyMap(text).entrySet().stream()
                .map(e -> (WordFrequency)new WordFrequencyDTO(e.getKey(), e.getValue()))
                .sorted(Comparator
                        .comparing(WordFrequency::frequency, Comparator.reverseOrder())
                        .thenComparing(WordFrequency::word))
                .limit(n)
                .toList();
    }

    /**
     * Finds the N most frequent words in the text.
     * Results are sorted by frequency (descending) and then alphabetically.
     * If N is larger than the number of unique words, returns all words.
     *
     * @param text The input text to analyze
     * @param n The maximum number of results to return
     * @return List of WordFrequency objects containing the top N words
     */
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
