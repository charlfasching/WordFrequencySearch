package com.anva.charl.words.rest.api;

import com.anva.charl.words.rest.model.HighestFrequencyRequest;
import com.anva.charl.words.rest.model.HighestFrequencySpecificRequest;
import com.anva.charl.words.rest.model.MostFrequentWordsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableAutoConfiguration
@AutoConfigureMockMvc
class WordSearchApplicationApiTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void seachForHighestFrequencyOverall() throws Exception {

        var frequencyRequest = new HighestFrequencyRequest("The quick brown fox jumps over the lazy dog");
        final ResultActions result = mockMvc.perform(post("/api/words/freq/highest")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(frequencyRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("amount", notNullValue()))
                .andExpect(jsonPath("amount", equalTo(2)));
    }

    @Test
    void seachForHighestFrequencyWithSpecificWords() throws Exception {

        var frequencyRequest = new HighestFrequencySpecificRequest("The quick brown fox jumps over the lazy dog", "fox");
        final ResultActions result = mockMvc.perform(post("/api/words/freq/specific")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(frequencyRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("amount", notNullValue()))
                .andExpect(jsonPath("amount", equalTo(1)));
    }

    @Test
    void seachForHighestFrequencyWithoutSpecificWords() throws Exception {

        var frequencyRequest = new HighestFrequencyRequest("The quick brown fox jumps over the lazy dog");
        final ResultActions result = mockMvc.perform(post("/api/words/freq/highest")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(frequencyRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("amount", notNullValue()))
                .andExpect(jsonPath("amount", equalTo(2)));
    }

    @Test
    void seachForMostFrequentWordsInTextBlock() throws Exception {

        var frequencyRequest = new MostFrequentWordsRequest(
                """
                        This programming assignment is designed to evaluate your technical
                        implementation skills and your ability to reason about scalable system
                        architecture. The problem mirrors real challenges we encounter at ANVA when
                        processing large volumes of text data, and your solution will serve as the
                        foundation for discussing how such systems evolve to meet production demands.
                        We're interested not only in your code quality and testing approach, but also in
                        understanding how you think about building robust, maintainable systems that
                        can grow with our business needs.
                        Interview Process
                        Following your submission, we will conduct a technical interview that uses your
                        implemented solution as the foundation for a broader technical discussion. During
                        this conversation, we will review your code implementation choices and reasoning,
                        then explore how your solution might evolve to address real-world challenges at
                        scale.
                        The discussion will cover foundational technical concepts including performance
                        optimization, data structures and algorithms, concurrent programming patterns,
                        and testing strategies. We will also examine architectural principles for distributed
                        systems, exploring topics such as scalability patterns, fault tolerance mechanisms,
                        data consistency models, and service decomposition strategies.
                        This approach allows us to understand not only your coding abilities, but also your
                        technical reasoning process and how you think about building robust systems that
                        can grow and adapt over time. We encourage you to be prepared to discuss
                        trade-offs in your implementation decisions and to think beyond the immediate
                        requirements toward how your solution might need to evolve in a production
                        environment serving millions of requests.
                        """,
                5
        );
        final ResultActions result = mockMvc.perform(post("/api/words/freq/page")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(frequencyRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].word").value("your"))
                .andExpect(jsonPath("$[0].frequency").value(12))
                .andExpect(jsonPath("$[1].word").value("and"))
                .andExpect(jsonPath("$[1].frequency").value(10))
                .andExpect(jsonPath("$[2].word").value("to"))
                .andExpect(jsonPath("$[2].frequency").value(9))
                .andExpect(jsonPath("$[3].word").value("we"))
                .andExpect(jsonPath("$[3].frequency").value(6))
                .andExpect(jsonPath("$[4].word").value("how"))
                .andExpect(jsonPath("$[4].frequency").value(5));
    }

    @Test
    void validateHighestFrequencyBadRequest_EmptyText() throws Exception {
        var request = new HighestFrequencyRequest("");

        final ResultActions result = mockMvc.perform(post("/api/words/freq/highest")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("problem", equalTo("Input Validation Failed")))
                .andExpect(jsonPath("fieldErrors", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].field", equalTo("text")))
                .andExpect(jsonPath("fieldErrors[0].message", equalTo("Text input to search in is missing")));
    }

    @Test
    void validateSpecificWordBadRequest_MissingWord() throws Exception {
        var request = new HighestFrequencySpecificRequest("Some text", "");

        final ResultActions result = mockMvc.perform(post("/api/words/freq/specific")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("problem", equalTo("Input Validation Failed")))
                .andExpect(jsonPath("fieldErrors", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].field", equalTo("word")))
                .andExpect(jsonPath("fieldErrors[0].message", equalTo("A word to search for is missing")));
    }

    @Test
    void validateMostFrequentWordsBadRequest_InvalidLimit() throws Exception {
        var request = new MostFrequentWordsRequest("Some text", -1);

        final ResultActions result = mockMvc.perform(post("/api/words/freq/page")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andExpect(jsonPath("problem", equalTo("Input Validation Failed")))
                .andExpect(jsonPath("fieldErrors", hasSize(1)))
                .andExpect(jsonPath("fieldErrors[0].field", equalTo("limit")))
                .andExpect(jsonPath("fieldErrors[0].message", equalTo("The Limit number must be a positive integer")));
    }

    @Test
    void validateBadRequest_MalformedJson() throws Exception {
        final ResultActions result = mockMvc.perform(post("/api/words/freq/highest")
                .contentType(APPLICATION_JSON)
                .content("{malformed json}"));

        result.andExpect(status().isBadRequest());
    }
}
