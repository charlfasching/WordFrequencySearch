# Word Frequency Analysis Service

## Purpose
This service provides an API for analyzing word frequencies in text content. It's designed to handle various text analysis requirements with support for:
- Multi-language text processing
- Large text volumes
- Real-time analysis
- Precise word frequency counting

## Tech Stack
- Java 25
- Spring Boot 3.x
- Maven for build automation
- JUnit 5 & AssertJ for testing
- Spring Validation for input validation

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
