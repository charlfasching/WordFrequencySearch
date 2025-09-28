# Word Frequency Analysis Service

## Purpose
This service provides an API for analyzing word frequencies in text content. It's designed to handle various text analysis requirements with support for:
- Multi-language text processing
- Large text volumes
- Real-time analysis
- Precise word frequency counting
- 
## Tech Stack
- Java 25
- Spring Boot 3.x
- Maven for build automation
- JUnit 5 & AssertJ for testing
- Spring Validation for input validation

## Motivation & Considerations

### Pattern Matching Algorithms
During the design phase, several text search algorithms were evaluated for their potential use in this service:

1. **Rabin-Karp Algorithm**
   - Uses hashing to find exact string matches in a text
   - Particularly efficient for multiple pattern searches
   - Best suited for exact substring matching rather than word boundary detection

2. **Knuth-Morris-Pratt (KMP) Algorithm**
   - Advanced pattern matching algorithm that improves efficiency by utilizing information about previous matches
   - Creates a partial match table (also called "failure function") to avoid backing up in the text
   - Achieves O(n + m) time complexity where n is text length and m is pattern length
   - Particularly efficient for patterns with repeating elements

### Implementation Decision
While KMP would have been an excellent choice for pure string pattern matching, it wasn't implemented in the final solution because:

1. The assignment specifically required complete word matching rather than substring searching
2. Word boundaries and punctuation handling were more important than raw pattern matching
3. Java's native text processing capabilities, combined with regex for word boundary detection, provided a more direct solution
4. Seeing as there was no mention of partial or longer multi-word matches, this approach looks to be better suited

### Chosen Approach
The implemented solution uses:
- Regular expressions for accurate word boundary detection
- Java's native string processing for text manipulation
- HashMap for efficient frequency counting
- Custom comparators for sorting results




## Details

### API Endpoints

#### 1. Highest Frequency Analysis
`POST /api/words/freq/highest`

Finds the highest frequency of any word in the provided text.
```json
{
    "text": "Sample text for analysis"
}
```

#### 2. Specific Word Frequency
`POST /api/words/freq/specific`

Analyzes frequency of a specific word in the text.
```json
{
    "text": "Sample text for analysis",
    "word": "sample"
}
```

#### 3. Most Frequent Words
`POST /api/words/freq/page`

Returns the N most frequent words with their counts.
```json
{
    "text": "Sample text for frequency analysis",
    "limit": 5
}
```

### Key Features

1. Text Processing
   - Case-insensitive matching
   - Punctuation handling
   - Multi-language support (including non-Latin scripts)
   - Efficient word boundary detection

2. Input Validation
   - Null and empty text validation
   - Word parameter validation
   - Limit parameter validation
   - Detailed error responses

3. Performance Considerations
   - Large text processing
   - Efficient memory usage
   - Thread-safe implementation

### Error Handling
The service provides structured error responses:
```json
{
    "message": "Input Validation Failed",
    "fieldErrors": [
        {
            "field": "text",
            "message": "Text cannot be empty"
        }
    ]
}
```

### Getting Started

1. Build the project:
```bash
./mvnw clean install
```

2. Run the application:
```bash
./mvnw spring-boot:run
```

3. Run tests:
```bash
./mvnw test
```

### Implementation Notes
- Uses regex pattern matching for accurate word detection
- Implements sorting by frequency and alphabetical order
- Supports Unicode characters for international text
- Validates all input parameters
