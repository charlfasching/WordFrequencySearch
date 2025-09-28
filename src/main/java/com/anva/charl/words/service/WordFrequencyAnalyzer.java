package com.anva.charl.words.service;

import com.anva.charl.words.data.model.WordFrequency;

import java.util.List;

/**
 * Implement the three methods defined in the above interface:
 * • calculateHighestFrequency returns the highest frequency in the text (multiple words may have this frequency);
 * • calculateFrequencyForWord returns the frequency for the specified word in the specified text;
 * • calculateMostFrequentNWords returns a list of the “n” most frequent words in the specified text,
 *   with all words returned in lowercase. If multiple words have the same frequency, they are returned in alphabetical order.
 *
 *   As an example: the text "The cat walks over the staircase"
 */

public interface WordFrequencyAnalyzer {
    int calculateHighestFrequency(String text);
    int calculateFrequencyForWord(String text, String word);
    List<WordFrequency> calculateMostFrequentNWords(String text, int n);
}
