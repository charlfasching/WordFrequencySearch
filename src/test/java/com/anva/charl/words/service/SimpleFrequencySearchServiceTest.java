package com.anva.charl.words.service;

import com.anva.charl.words.data.model.WordFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleFrequencySearchServiceTest {

    private SimpleFrequencySearchService service;

    @BeforeEach
    void setUp() {
        service = new SimpleFrequencySearchService();
    }

    @Test
    void calculateHighestFrequency_WithRepeatedWords_ReturnsHighestCount() {
        // given
        String text = "the quick brown fox jumps over the lazy fox";

        // when
        int result = service.calculateHighestFrequency(text);

        // then
        assertEquals(2, result, "Should find highest frequency of 2 for words 'the' and 'fox'");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void calculateHighestFrequency_WithEmptyInput_ReturnsZero(String input) {
        // when
        int result = service.calculateHighestFrequency(input);

        // then
        assertEquals(0, result, "Should return 0 for empty or null input");
    }

    @Test
    void calculateFrequencyForWord_WithExistingWord_ReturnsCount() {
        // given
        String text = "the quick brown fox jumps over the lazy fox";
        String word = "fox";

        // when
        int result = service.calculateFrequencyForWord(text, word);

        // then
        assertEquals(2, result, "Should find 'fox' exactly 2 times");
    }

    @Test
    void calculateFrequencyForWord_WithNonExistingWord_ReturnsZero() {
        // given
        String text = "the quick brown fox jumps over the lazy fox";
        String word = "cat";

        // when
        int result = service.calculateFrequencyForWord(text, word);

        // then
        assertEquals(0, result, "Should return 0 for non-existing word");
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void calculateFrequencyForWord_WithEmptyWord_ReturnsZero(String word) {
        // given
        String text = "the quick brown fox jumps over the lazy fox";

        // when
        int result = service.calculateFrequencyForWord(text, word);

        // then
        assertEquals(0, result, "Should return 0 for empty or null word");
    }

    @Test
    void calculateMostFrequentNWords_SortsCorrectlyByFrequencyAndAlphabetically() {
        // given
        String text = "the fox and the hound and the cat and dog and another dog";


        int n = 3;

        // when
        List<WordFrequency> result = service.calculateMostFrequentNWords(text, n);

        // then
        assertThat(result)
            .hasSize(3)
            .satisfies(list -> {
                assertEquals("and", list.get(0).word(), "Most frequent word should be 'and'");
                assertEquals(4, list.get(0).frequency(), "'and' should appear 4 times");
                assertEquals("the", list.get(1).word(), "Second most frequent word should be 'the'");
                assertEquals(3, list.get(1).frequency(), "'the' should appear 3 times");
                assertEquals("dog", list.get(2).word(), "Third word should be 'dog' (alphabetically before 'fox')");
                assertEquals(2, list.get(2).frequency(), "'dog' should appear 2 times");
            });
    }

    @Test
    void calculateMostFrequentNWords_WithLargerNThanUniqueWords_ReturnsAllWords() {
        // given
        String text = "the fox jumps into the whole";
        int n = 9;

        // when
        List<WordFrequency> result = service.calculateMostFrequentNWords(text, n);

        // then
        assertThat(result)
            .hasSize(5)
            .extracting(WordFrequency::word)
            .containsExactly("the", "fox", "into", "jumps", "whole");
    }

    @ParameterizedTest
    @MethodSource("providePunctuationCases")
    void calculateFrequencyForWord_IgnoresPunctuation(String text, String word, int expectedFrequency) {
        // when
        int result = service.calculateFrequencyForWord(text, word);

        // then
        assertEquals(expectedFrequency, result,
            String.format("Should find '%s' %d times, ignoring punctuation", word, expectedFrequency));
    }

    private static Stream<Arguments> providePunctuationCases() {
        return Stream.of(
            Arguments.of("Hello, hello! Hello.", "hello", 3),
            Arguments.of("The quick-brown fox; the quick fox!", "quick", 2),
            Arguments.of("Word...word,word;word?word!", "word", 5)
        );
    }

    @Test
    void calculateMostFrequentNWords_CaseInsensitive() {
        // given
        String text = "The THE the tHe THe";
        int n = 1;

        // when
        List<WordFrequency> result = service.calculateMostFrequentNWords(text, n);

        // then
        assertThat(result)
            .hasSize(1)
            .satisfies(list -> {
                assertEquals("the", list.getFirst().word(), "Should match 'the' case-insensitively");
                assertEquals(5, list.getFirst().frequency(), "Should count all variations of 'the'");
            });
    }

    @Test
    void calculateMostFrequentNWords_WithLargeText() {
        // given 800 words
        String text = """
            Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herself on the way. When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then she continued her way. On her way she met a copy. The copy warned the Little Blind Text, that where it came from it would have been rewritten a thousand times and everything that was left from its origin would be the word "and" and the Little Blind Text should turn around and return to its own, safe country. But nothing the copy said could convince her and so it didn’t take long until a few insidious Copy Writers ambushed her, made her drunk with Longe and Parole and dragged her into their agency, where they abused her for their projects again and again. And if she hasn’t been rewritten, then they are still using her. Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herself on the way. When she reached the first hills of the Italic Mountains, she had a last view back on the skyline of her hometown Bookmarksgrove, the headline of Alphabet Village and the subline of her own road, the Line Lane. Pityful a rethoric question ran over her cheek, then she continued her way. On her way she met a copy. The copy warned the Little Blind Text, that where it came from it would have been rewritten a thousand times and everything that was left from its origin would be the word "and" and the Little Blind Text should turn around and return to its own, safe country. But nothing the copy said could convince her and so it didn’t take long until a few insidious Copy Writers ambushed her, made her drunk with Longe and Parole and dragged her into their agency, where they abused her for their projects again and again. And if she hasn’t been rewritten, then they are still using her.Far far away, behind the word mountains, far from the countries Vokalia and Consonantia, there live the blind texts. Separated they live in Bookmarksgrove right at the coast of the Semantics, a large language ocean. A small river named Duden flows by their place and supplies it with the necessary regelialia. It is a paradisematic country, in which roasted parts of sentences fly into your mouth. Even the all-powerful Pointing has no control about the blind texts it is an almost unorthographic life One day however a small line of blind text by the name of Lorem Ipsum decided to leave for the far World of Grammar. The Big Oxmox advised her not to do so, because there were thousands of bad Commas, wild Question Marks and devious Semikoli, but the Little Blind Text didn’t listen. She packed her seven versalia, put her initial into the belt and made herself on the way. When she reached the first hills of the Italic Mountains, she had a last view
            """;
        int n = 5;

        // when
        List<WordFrequency> result = service.calculateMostFrequentNWords(text, n);

        // then
        assertThat(result)
            .hasSize(5)
            .satisfies(list -> {
                assertEquals("the", list.getFirst().word());
                assertEquals(66, list.getFirst().frequency());
                assertEquals("and", list.get(1).word());
                assertEquals(32, list.get(1).frequency());
                assertEquals("her", list.get(2).word());
                assertEquals(31, list.get(2).frequency());
                assertEquals("of", list.get(3).word());
                assertEquals(27, list.get(3).frequency());
                assertEquals("a", list.get(4).word());
                assertEquals(23, list.get(4).frequency());
            });
    }

    @Test
    void calculateMostFrequentNWords_WithNonEnglishText() {
        // given
        String text = """
            Der schnelle braune Fuchs springt über den faulen Hund. 
            Der Fuchs ist schnell und der Hund ist faul. 
            青い空と白い雲が美しいです。空は とても 青く て 雲は とても 白い です。
            안녕하세요 세상아 안녕하세요 여러분 안녕하세요.
            """;
        int n = 3;

        // when
        List<WordFrequency> result = service.calculateMostFrequentNWords(text, n);

        // then
        assertThat(result)
            .hasSize(3)
            .satisfies(list -> {
                assertEquals("der", list.getFirst().word());
                assertEquals(3, list.getFirst().frequency());
                assertEquals("안녕하세요", list.get(1).word());
                assertEquals(3, list.get(1).frequency());
                assertEquals("fuchs", list.get(2).word());
                assertEquals(2, list.get(2).frequency());
            });
    }

    @Test
    void calculateMostFrequentNWords_WithHeavyPunctuation() {
        // given
        String text = """
            Hello!!! How are you???
            I'm doing... well... thank you!!!
            This is a (test) of [punctuation] and {special} characters!!!
            Let's see--how well--it handles... multiple... types... of... punctuation!!!
            Well!!! Well... Well??? How... about... that!!!
            """;
        int n = 3;

        // when
        List<WordFrequency> result = service.calculateMostFrequentNWords(text, n);

        // then
        assertThat(result)
            .hasSize(3)
            .satisfies(list -> {
                assertEquals("well", list.getFirst().word());
                assertEquals(5, list.getFirst().frequency());
                assertEquals("how", list.get(1).word());
                assertEquals(3, list.get(1).frequency());
                assertEquals("of", list.get(2).word());
                assertEquals(2, list.get(2).frequency());
            });
    }
}
