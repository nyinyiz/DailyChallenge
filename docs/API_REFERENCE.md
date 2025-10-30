# API Reference

## Overview

Daily Challenge fetches educational content from a public API hosted on GitHub Pages. This document provides comprehensive information about available endpoints, data formats, and integration details.

---

## Base Configuration

### API Base URL
```
https://nyinyiz.github.io/daily_challenges_data/
```

### Data Repository
- **GitHub**: [github.com/nyinyiz/daily_challenges_data](https://github.com/nyinyiz/daily_challenges_data)
- **Purpose**: Separate repository for challenge content
- **Update Frequency**: Regular updates with new challenges
- **Contribution**: Open for community contributions

### Protocol
- **Transport**: HTTPS only
- **Format**: JSON
- **Encoding**: UTF-8
- **Content-Type**: `application/json`

---

## Supported Categories

All challenge endpoints support the following categories:

| Category | Description |
|----------|-------------|
| `android` | Android development challenges |
| `ios` | iOS development challenges |
| `kotlin` | Kotlin language challenges |
| `swift` | Swift language challenges |
| `flutter` | Flutter framework challenges |

---

## Endpoints

### 1. Daily Challenges

**Endpoint**: `/daily_challenges.json`

**Method**: GET

**Description**: Retrieves the daily coding challenges with code snippets for detailed problem-solving.

**Request Example**:
```http
GET https://nyinyiz.github.io/daily_challenges_data/daily_challenges.json
```

**Response Format**:
```json
[
  {
    "id": "dc_001",
    "difficulty": "Medium",
    "question": "What will be the output of the following Kotlin code?",
    "questionCode": "fun main() {\n    val list = listOf(1, 2, 3, 4, 5)\n    val result = list.map { it * 2 }.filter { it > 5 }\n    println(result)\n}",
    "answerCode": "[6, 8, 10]\n\nExplanation: The map function doubles each element, then filter keeps only elements greater than 5."
  }
]
```

**Response Fields**:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | String | Yes | Unique identifier for the challenge |
| `difficulty` | String | Yes | Challenge difficulty: "Easy", "Medium", or "Hard" |
| `question` | String | Yes | The challenge question text |
| `questionCode` | String | No | Code snippet for the question (can be empty) |
| `answerCode` | String | Yes | Solution code with explanation |

---

### 2. True or False Challenges

**Endpoint**: `/true_or_false_challenges_{category}.json`

**Method**: GET

**Description**: Retrieves true/false questions for the specified category. Used in the swipeable card quiz mode.

**Request Example**:
```http
GET https://nyinyiz.github.io/daily_challenges_data/true_or_false_challenges_android.json
```

**Available Endpoints**:
- `/true_or_false_challenges_android.json`
- `/true_or_false_challenges_ios.json`
- `/true_or_false_challenges_kotlin.json`
- `/true_or_false_challenges_swift.json`
- `/true_or_false_challenges_flutter.json`

**Response Format**:
```json
[
  {
    "id": "tf_android_001",
    "question": "In Android, ViewModel instances survive configuration changes such as screen rotations.",
    "correctAnswer": "true",
    "explanation": "ViewModels are designed to store and manage UI-related data in a lifecycle-conscious way, surviving configuration changes like screen rotations.",
    "difficulty": "Easy"
  },
  {
    "id": "tf_android_002",
    "question": "LiveData is always better than StateFlow for state management in Kotlin.",
    "correctAnswer": "false",
    "explanation": "While LiveData is useful, StateFlow offers better integration with Kotlin coroutines, cold flow semantics, and is not tied to Android-specific lifecycle components. The choice depends on your specific use case.",
    "difficulty": "Medium"
  }
]
```

**Response Fields**:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | String | Yes | Unique identifier |
| `question` | String | Yes | The true/false question |
| `correctAnswer` | String | Yes | "true" or "false" |
| `explanation` | String | Yes | Detailed explanation of the answer |
| `difficulty` | String | Yes | "Easy", "Medium", or "Hard" |

---

### 3. Multiple Choice Challenges

**Endpoint**: `/multiple_choice_challenges_{category}.json`

**Method**: GET

**Description**: Retrieves multiple-choice questions with exactly one correct answer.

**Request Example**:
```http
GET https://nyinyiz.github.io/daily_challenges_data/multiple_choice_challenges_kotlin.json
```

**Available Endpoints**:
- `/multiple_choice_challenges_android.json`
- `/multiple_choice_challenges_ios.json`
- `/multiple_choice_challenges_kotlin.json`
- `/multiple_choice_challenges_swift.json`
- `/multiple_choice_challenges_flutter.json`

**Response Format**:
```json
[
  {
    "question": "Which Kotlin function is used to transform each element of a collection?",
    "options": [
      "filter()",
      "map()",
      "reduce()",
      "fold()"
    ],
    "correctAnswer": "map()",
    "difficulty": "Easy",
    "explanation": "The map() function transforms each element of a collection by applying a given lambda function to it, returning a new collection with the transformed elements."
  }
]
```

**Response Fields**:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `question` | String | Yes | The multiple-choice question |
| `options` | Array[String] | Yes | Array of 4 answer options |
| `correctAnswer` | String | Yes | The correct option (must match one in `options`) |
| `difficulty` | String | Yes | "Easy", "Medium", or "Hard" |
| `explanation` | String | Yes | Explanation of the correct answer |

**Validation Rules**:
- `options` array must contain exactly 4 strings
- `correctAnswer` must exactly match one of the options
- All options should be unique

---

### 4. Multiple Select Challenges

**Endpoint**: `/multiple_select_challenges_{category}.json`

**Method**: GET

**Description**: Retrieves questions where multiple answers can be correct. Users must select ALL correct answers.

**Request Example**:
```http
GET https://nyinyiz.github.io/daily_challenges_data/multiple_select_challenges_ios.json
```

**Available Endpoints**:
- `/multiple_select_challenges_android.json`
- `/multiple_select_challenges_ios.json`
- `/multiple_select_challenges_kotlin.json`
- `/multiple_select_challenges_swift.json`
- `/multiple_select_challenges_flutter.json`

**Response Format**:
```json
[
  {
    "question": "Which of the following are valid ways to create a String in Kotlin?",
    "options": [
      "val str = \"Hello\"",
      "val str = String(\"Hello\")",
      "val str = 'Hello'",
      "val str = \"\"\"Hello\"\"\""
    ],
    "correctAnswers": [
      "val str = \"Hello\"",
      "val str = \"\"\"Hello\"\"\"",
      "val str = String(\"Hello\")"
    ],
    "difficulty": "Medium",
    "explanation": "Kotlin supports multiple ways to create strings: regular string literals (\"\"), raw string literals (\"\"\"), and the String constructor. Single quotes are used for characters, not strings."
  }
]
```

**Response Fields**:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `question` | String | Yes | The multiple-select question |
| `options` | Array[String] | Yes | Array of answer options (typically 4-6) |
| `correctAnswers` | Array[String] | Yes | Array of all correct options |
| `difficulty` | String | Yes | "Easy", "Medium", or "Hard" |
| `explanation` | String | Yes | Explanation of the correct answers |

**Validation Rules**:
- `correctAnswers` must contain at least 2 items
- All `correctAnswers` must exist in `options`
- All options should be unique

---

### 5. Matching Game Challenges

**Endpoint**: `/matching_challenges_{category}.json`

**Method**: GET

**Description**: Retrieves matching pair challenges where users connect related concepts.

**Request Example**:
```http
GET https://nyinyiz.github.io/daily_challenges_data/matching_challenges_android.json
```

**Available Endpoints**:
- `/matching_challenges_android.json`
- `/matching_challenges_ios.json`
- `/matching_challenges_kotlin.json`
- `/matching_challenges_swift.json`
- `/matching_challenges_flutter.json`

**Response Format**:
```json
[
  {
    "title": "Match Android Architecture Components",
    "pairs": [
      {
        "left": "ViewModel",
        "right": "Stores UI-related data that survives configuration changes"
      },
      {
        "left": "LiveData",
        "right": "Observable data holder class that is lifecycle-aware"
      },
      {
        "left": "Repository",
        "right": "Mediates between different data sources"
      },
      {
        "left": "Room",
        "right": "SQLite abstraction layer for database operations"
      }
    ],
    "difficulty": "Medium"
  }
]
```

**Response Fields**:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `title` | String | Yes | Title/theme of the matching game |
| `pairs` | Array[Pair] | Yes | Array of matching pairs |
| `pairs[].left` | String | Yes | Left side item (typically term/concept) |
| `pairs[].right` | String | Yes | Right side item (typically definition/description) |
| `difficulty` | String | Yes | "Easy", "Medium", or "Hard" |

**Validation Rules**:
- `pairs` array should contain 4-6 pairs
- All `left` values should be unique
- All `right` values should be unique

---

### 6. Programming Tips

**Endpoint**: `/programming_tips.json`

**Method**: GET

**Description**: Retrieves programming tips used for the Android widget feature.

**Request Example**:
```http
GET https://nyinyiz.github.io/daily_challenges_data/programming_tips.json
```

**Response Format**:
```json
[
  {
    "id": "tip_001",
    "tip": "Use meaningful variable names to make your code self-documenting.",
    "category": "Best Practices"
  },
  {
    "id": "tip_002",
    "tip": "In Kotlin, prefer 'val' over 'var' to create immutable variables for safer code.",
    "category": "Kotlin"
  }
]
```

**Response Fields**:

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `id` | String | Yes | Unique identifier for the tip |
| `tip` | String | Yes | The programming tip text |
| `category` | String | Yes | Category of the tip (e.g., "Kotlin", "Android", "Best Practices") |

---

## Integration Guide

### Using Ktor Client

```kotlin
// Define API Service
class ChallengesApiService(private val httpClient: HttpClient) {

    suspend fun getDailyChallenges(): List<DailyChallengeObj> {
        return httpClient.get("${ApiConstants.BASE_URL}daily_challenges.json").body()
    }

    suspend fun getTrueFalseChallenges(category: String): List<QuizCard> {
        return httpClient.get(
            "${ApiConstants.BASE_URL}true_or_false_challenges_$category.json"
        ).body()
    }

    suspend fun getMultipleChoiceChallenges(category: String): List<MultipleChoiceObj> {
        return httpClient.get(
            "${ApiConstants.BASE_URL}multiple_choice_challenges_$category.json"
        ).body()
    }

    suspend fun getMultipleSelectChallenges(category: String): List<MultipleSelectObj> {
        return httpClient.get(
            "${ApiConstants.BASE_URL}multiple_select_challenges_$category.json"
        ).body()
    }

    suspend fun getMatchingChallenges(category: String): List<MatchingGameObj> {
        return httpClient.get(
            "${ApiConstants.BASE_URL}matching_challenges_$category.json"
        ).body()
    }

    suspend fun getProgrammingTips(): List<ProgrammingTip> {
        return httpClient.get("${ApiConstants.BASE_URL}programming_tips.json").body()
    }
}
```

### Repository Pattern Implementation

```kotlin
class ChallengesRepositoryImpl(
    private val apiService: ChallengesApiService
) : ChallengesRepository {

    override suspend fun getTrueFalseChallenges(
        category: String
    ): Flow<NetworkResult<List<QuizCard>>> = flow {
        try {
            val response = apiService.getTrueFalseChallenges(category)
            emit(NetworkResult.Success(response))
        } catch (e: IOException) {
            emit(NetworkResult.NetworkError(e))
            // Fallback to default data
            emit(NetworkResult.Success(getDefaultTrueFalseChallenges()))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Unknown error"))
        }
    }

    // Default/fallback data
    private fun getDefaultTrueFalseChallenges(): List<QuizCard> {
        return listOf(
            QuizCard(
                id = "default_001",
                question = "Kotlin is a statically typed language.",
                correctAnswer = "true",
                explanation = "Kotlin is indeed statically typed...",
                difficulty = "Easy"
            )
        )
    }
}
```

### Error Handling

```kotlin
// Sealed class for API results
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    data class NetworkError(val exception: Exception) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()
}

// Usage in ViewModel
class QuizScreenViewModel(
    private val repository: ChallengesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadQuestions(category: String) {
        viewModelScope.launch {
            repository.getTrueFalseChallenges(category)
                .collect { result ->
                    _uiState.value = when (result) {
                        is NetworkResult.Success -> UiState.Success(result.data)
                        is NetworkResult.Error -> UiState.Error(result.message)
                        is NetworkResult.NetworkError -> UiState.Error(
                            "Network error: ${result.exception.message}"
                        )
                        is NetworkResult.Loading -> UiState.Loading
                    }
                }
        }
    }
}
```

---

## Data Models

### Kotlin Data Classes

```kotlin
// Daily Challenge
@Serializable
data class DailyChallengeObj(
    val id: String,
    val difficulty: String,
    val question: String,
    val questionCode: String = "",
    val answerCode: String
)

// True/False Quiz
@Serializable
data class QuizCard(
    val id: String,
    val question: String,
    val correctAnswer: String,
    val explanation: String,
    val difficulty: String
)

// Multiple Choice
@Serializable
data class MultipleChoiceObj(
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val difficulty: String,
    val explanation: String
)

// Multiple Select
@Serializable
data class MultipleSelectObj(
    val question: String,
    val options: List<String>,
    val correctAnswers: List<String>,
    val difficulty: String,
    val explanation: String
)

// Matching Game
@Serializable
data class MatchingGameObj(
    val title: String,
    val pairs: List<MatchingPair>,
    val difficulty: String
)

@Serializable
data class MatchingPair(
    val left: String,
    val right: String
)

// Programming Tip
@Serializable
data class ProgrammingTip(
    val id: String,
    val tip: String,
    val category: String
)
```

---

## Rate Limiting & Caching

### GitHub Pages Limits
- **Bandwidth**: 100GB/month (soft limit)
- **Build**: 10 builds per hour
- **Files**: 1GB repository size limit

### Recommended Practices
1. **Cache responses** locally to reduce API calls
2. **Implement fallback data** for offline support
3. **Batch requests** when possible
4. **Use ETags** for conditional requests (future)

### Caching Implementation

```kotlin
class CachedChallengesRepository(
    private val apiService: ChallengesApiService,
    private val cache: MutableMap<String, Any> = mutableMapOf()
) : ChallengesRepository {

    override suspend fun getTrueFalseChallenges(
        category: String
    ): Flow<NetworkResult<List<QuizCard>>> = flow {
        val cacheKey = "tf_$category"

        // Check cache first
        cache[cacheKey]?.let { cached ->
            emit(NetworkResult.Success(cached as List<QuizCard>))
        }

        // Fetch from network
        try {
            val response = apiService.getTrueFalseChallenges(category)
            cache[cacheKey] = response
            emit(NetworkResult.Success(response))
        } catch (e: Exception) {
            // If cache exists, use it; otherwise use fallback
            if (cache[cacheKey] == null) {
                emit(NetworkResult.Success(getDefaultData()))
            }
        }
    }
}
```

---

## Testing API Integration

### Mock API Service

```kotlin
class MockChallengesApiService : ChallengesApiService {
    override suspend fun getDailyChallenges(): List<DailyChallengeObj> {
        return listOf(
            DailyChallengeObj(
                id = "test_001",
                difficulty = "Easy",
                question = "Test question",
                questionCode = "val x = 1",
                answerCode = "1"
            )
        )
    }

    // Implement other methods...
}
```

### Testing Repository

```kotlin
@Test
fun `getTrueFalseChallenges returns success`() = runTest {
    val mockApiService = MockChallengesApiService()
    val repository = ChallengesRepositoryImpl(mockApiService)

    repository.getTrueFalseChallenges("kotlin").collect { result ->
        assertTrue(result is NetworkResult.Success)
        assertFalse(result.data.isEmpty())
    }
}
```

---

## Contributing Challenge Data

### How to Add New Challenges

1. **Fork the data repository**
   ```bash
   git clone https://github.com/nyinyiz/daily_challenges_data.git
   ```

2. **Choose the appropriate file**
   - Daily challenges: `daily_challenges.json`
   - True/False: `true_or_false_challenges_{category}.json`
   - Multiple choice: `multiple_choice_challenges_{category}.json`
   - Multiple select: `multiple_select_challenges_{category}.json`
   - Matching: `matching_challenges_{category}.json`
   - Tips: `programming_tips.json`

3. **Follow the JSON format** (see examples above)

4. **Validate your JSON**
   ```bash
   # Use any JSON validator
   cat your_file.json | python -m json.tool
   ```

5. **Submit a Pull Request** with:
   - Clear description of added challenges
   - Category and difficulty level
   - Number of questions added

### Quality Guidelines

- **Accuracy**: Ensure technical correctness
- **Clarity**: Questions should be clear and unambiguous
- **Difficulty**: Accurately categorize as Easy/Medium/Hard
- **Explanations**: Provide detailed explanations
- **Uniqueness**: Avoid duplicate questions
- **Code Formatting**: Use proper indentation for code snippets

---

## API Constants Reference

```kotlin
object ApiConstants {
    const val BASE_URL = "https://nyinyiz.github.io/daily_challenges_data/"

    // Endpoints
    const val DAILY_CHALLENGES = "daily_challenges.json"
    const val PROGRAMMING_TIPS = "programming_tips.json"

    // Category-based endpoints (format with category)
    const val TRUE_FALSE_FORMAT = "true_or_false_challenges_%s.json"
    const val MULTIPLE_CHOICE_FORMAT = "multiple_choice_challenges_%s.json"
    const val MULTIPLE_SELECT_FORMAT = "multiple_select_challenges_%s.json"
    const val MATCHING_FORMAT = "matching_challenges_%s.json"

    // Categories
    const val CATEGORY_ANDROID = "android"
    const val CATEGORY_IOS = "ios"
    const val CATEGORY_KOTLIN = "kotlin"
    const val CATEGORY_SWIFT = "swift"
    const val CATEGORY_FLUTTER = "flutter"
}
```

---

## Troubleshooting

### Common Issues

**Issue**: 404 Not Found
- **Cause**: Invalid category or endpoint
- **Solution**: Verify category name and endpoint format

**Issue**: JSON Parsing Error
- **Cause**: Invalid JSON format or unexpected fields
- **Solution**: Check JSON structure matches data models

**Issue**: Network Timeout
- **Cause**: Slow connection or GitHub Pages down
- **Solution**: Implement retry logic and fallback data

**Issue**: CORS Error (Web)
- **Cause**: Browser CORS restrictions
- **Solution**: GitHub Pages supports CORS, ensure request headers are correct

---

## Related Documentation

- [Architecture Guide](ARCHITECTURE.md) - How API integration fits into architecture
- [Tech Stack Guide](TECH_STACK.md) - Ktor and networking libraries
- [Contributing Guide](CONTRIBUTING.md) - How to contribute challenges

---

**Last Updated**: October 2025
**API Version**: 1.0
**Data Repository**: [github.com/nyinyiz/daily_challenges_data](https://github.com/nyinyiz/daily_challenges_data)
