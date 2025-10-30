# Contributing Guidelines

Thank you for considering contributing to Daily Challenge! We welcome contributions from developers of all skill levels. This guide will help you get started.

---

## Table of Contents

1. [Ways to Contribute](#ways-to-contribute)
2. [Getting Started](#getting-started)
3. [Development Workflow](#development-workflow)
4. [Code Contribution Guidelines](#code-contribution-guidelines)
5. [Contributing Challenge Questions](#contributing-challenge-questions)
6. [Documentation Contributions](#documentation-contributions)
7. [Reporting Issues](#reporting-issues)
8. [Code Review Process](#code-review-process)
9. [Community Guidelines](#community-guidelines)

---

## Ways to Contribute

### 1. Code Contributions
- Fix bugs and issues
- Implement new features
- Improve existing functionality
- Optimize performance
- Write tests

### 2. Challenge Content
- Add new coding challenges
- Create quiz questions
- Develop matching games
- Submit programming tips

### 3. Documentation
- Improve existing docs
- Write tutorials
- Create examples
- Translate documentation

### 4. Design & UX
- Suggest UI improvements
- Create new themes
- Design icons and assets
- Improve user experience

### 5. Testing & Bug Reports
- Test the app on different devices
- Report bugs with detailed information
- Verify bug fixes
- Suggest improvements

---

## Getting Started

### Prerequisites

Before you begin, ensure you have:

- **Android Studio** (Ladybug 2024.2.2 or newer)
- **JDK 17+**
- **Git** for version control
- **Xcode 14+** (for iOS development, macOS only)
- **Kotlin** understanding (basic to intermediate)
- **Compose Multiplatform** familiarity (helpful but not required)

See [SETUP.md](SETUP.md) for detailed installation instructions.

### Fork the Repository

1. Go to [github.com/nyinyiz/DailyChallenge](https://github.com/nyinyiz/DailyChallenge)
2. Click the "Fork" button in the top right
3. Clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/DailyChallenge.git
   cd DailyChallenge
   ```

### Set Up Upstream Remote

```bash
git remote add upstream https://github.com/nyinyiz/DailyChallenge.git
git fetch upstream
```

### Create a Development Branch

```bash
# Always branch from main
git checkout main
git pull upstream main

# Create feature branch
git checkout -b feature/your-feature-name

# Or for bug fixes
git checkout -b fix/bug-description
```

---

## Development Workflow

### 1. Sync with Upstream

Before starting work, sync with the latest changes:

```bash
git checkout main
git pull upstream main
git checkout your-branch
git rebase main
```

### 2. Make Changes

- Write clean, readable code
- Follow Kotlin coding conventions
- Add comments for complex logic
- Update documentation if needed

### 3. Test Your Changes

```bash
# Build the project
./gradlew build

# Run on Android
./gradlew :composeApp:assembleDebug

# Run tests
./gradlew test
```

### 4. Commit Your Changes

```bash
git add .
git commit -m "type: brief description

Detailed explanation if needed.

Closes #issue_number"
```

**Commit Message Format:**
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `style:` Code style changes (formatting, etc.)
- `refactor:` Code refactoring
- `test:` Adding or updating tests
- `chore:` Maintenance tasks

**Examples:**
```bash
git commit -m "feat: add dark mode toggle in settings"
git commit -m "fix: resolve crash when loading empty quiz"
git commit -m "docs: update API reference with new endpoints"
```

### 5. Push to Your Fork

```bash
git push origin your-branch-name
```

### 6. Create Pull Request

1. Go to your fork on GitHub
2. Click "Compare & pull request"
3. Fill in the PR template (see below)
4. Submit the pull request

---

## Code Contribution Guidelines

### Code Style

#### Kotlin Conventions

Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html):

```kotlin
// Good
class UserProfileViewModel(
    private val repository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadUserProfile() {
        viewModelScope.launch {
            repository.getUserProfile()
                .collect { profile ->
                    _uiState.value = UiState.Success(profile)
                }
        }
    }
}
```

#### Compose Best Practices

```kotlin
// Good - State hoisting
@Composable
fun QuizScreen(
    viewModel: QuizScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    QuizContent(
        uiState = uiState,
        onAnswerSelected = viewModel::answerQuestion
    )
}

// Stateless composable
@Composable
fun QuizContent(
    uiState: UiState,
    onAnswerSelected: (Boolean) -> Unit
) {
    when (uiState) {
        is UiState.Success -> QuizCard(uiState.data, onAnswerSelected)
        is UiState.Loading -> LoadingIndicator()
        is UiState.Error -> ErrorMessage(uiState.message)
    }
}
```

### Architecture Guidelines

#### Repository Pattern

```kotlin
// Always define interface first
interface ChallengesRepository {
    suspend fun getChallenges(): Flow<NetworkResult<List<Challenge>>>
}

// Then implement
class ChallengesRepositoryImpl(
    private val apiService: ChallengesApiService
) : ChallengesRepository {
    override suspend fun getChallenges(): Flow<NetworkResult<List<Challenge>>> = flow {
        try {
            val data = apiService.getChallenges()
            emit(NetworkResult.Success(data))
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Unknown error"))
        }
    }
}
```

#### ViewModel Pattern

```kotlin
class FeatureViewModel(
    private val repository: FeatureRepository
) : ViewModel() {

    // Private mutable state
    private val _uiState = MutableStateFlow<UiState>(UiState.Initial)

    // Public immutable state
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.getData()
                .collect { result ->
                    _uiState.value = when (result) {
                        is NetworkResult.Success -> UiState.Success(result.data)
                        is NetworkResult.Error -> UiState.Error(result.message)
                    }
                }
        }
    }

    fun onUserAction() {
        // Handle user interactions
    }
}
```

### Error Handling

```kotlin
// Always handle errors gracefully
suspend fun fetchData(): NetworkResult<Data> {
    return try {
        val response = apiService.getData()
        NetworkResult.Success(response)
    } catch (e: IOException) {
        NetworkResult.NetworkError(e)
    } catch (e: Exception) {
        NetworkResult.Error(e.message ?: "Unknown error")
    }
}
```

### Testing

```kotlin
@Test
fun `loadChallenges updates state to success`() = runTest {
    // Given
    val mockRepository = MockChallengesRepository()
    val viewModel = ChallengesViewModel(mockRepository)

    // When
    viewModel.loadChallenges()

    // Then
    assertTrue(viewModel.uiState.value is UiState.Success)
}
```

### File Organization

Place files in appropriate packages:

```
composeApp/src/commonMain/kotlin/com/nyinyi/dailychallenge/
├── data/
│   ├── model/           # Data models
│   ├── remote/          # API services
│   └── repository/      # Repository implementations
├── ui/
│   ├── screens/         # Screen composables
│   │   └── feature/     # Feature-specific screens
│   │       ├── FeatureScreen.kt
│   │       ├── FeatureViewModel.kt
│   │       └── components/
│   ├── components/      # Reusable components
│   └── theme/           # Theme and styling
├── navigation/          # Navigation logic
└── di/                  # Dependency injection
```

---

## Contributing Challenge Questions

### Challenge Data Repository

Questions are stored in a separate repository:
- **Repository**: [github.com/nyinyiz/daily_challenges_data](https://github.com/nyinyiz/daily_challenges_data)
- **API**: `https://nyinyiz.github.io/daily_challenges_data/`

### Contribution Process

1. **Fork the data repository**
   ```bash
   git clone https://github.com/nyinyiz/daily_challenges_data.git
   ```

2. **Choose challenge type**
   - Daily challenges: `daily_challenges.json`
   - True/False: `true_or_false_challenges_{category}.json`
   - Multiple choice: `multiple_choice_challenges_{category}.json`
   - Multiple select: `multiple_select_challenges_{category}.json`
   - Matching game: `matching_challenges_{category}.json`
   - Programming tips: `programming_tips.json`

3. **Add your challenges**

### Challenge Quality Standards

#### Good Challenge Example (True/False)
```json
{
  "id": "tf_kotlin_042",
  "question": "In Kotlin, lateinit can be used with nullable types.",
  "correctAnswer": "false",
  "explanation": "lateinit cannot be used with nullable types because it guarantees that the property will be initialized before use. Nullable types already allow null as a value, which contradicts lateinit's purpose. Use nullable types with default null value instead.",
  "difficulty": "Medium"
}
```

#### Good Challenge Example (Multiple Choice)
```json
{
  "question": "Which coroutine builder should you use to launch a coroutine that returns a result?",
  "options": [
    "launch",
    "async",
    "runBlocking",
    "withContext"
  ],
  "correctAnswer": "async",
  "difficulty": "Medium",
  "explanation": "async is used when you need a result from a coroutine. It returns a Deferred object that you can await() to get the result. launch is for fire-and-forget operations, runBlocking blocks the thread, and withContext switches context but doesn't launch a new coroutine."
}
```

#### Quality Checklist

- [ ] Technically accurate and verified
- [ ] Clear and unambiguous question
- [ ] Appropriate difficulty level
- [ ] Detailed, educational explanation
- [ ] Proper JSON formatting
- [ ] Unique ID (check existing IDs)
- [ ] Follows category conventions
- [ ] No typos or grammatical errors
- [ ] Code examples are properly formatted

### Categories

| Category | Focus Area | Example Topics |
|----------|-----------|----------------|
| `android` | Android development | Activities, Fragments, ViewModels, Compose |
| `ios` | iOS development | ViewControllers, SwiftUI, UIKit |
| `kotlin` | Kotlin language | Coroutines, collections, sealed classes |
| `swift` | Swift language | Optionals, closures, protocols |
| `flutter` | Flutter framework | Widgets, state management, navigation |

### Validation

Before submitting, validate your JSON:

```bash
# Using Python
python -m json.tool your_file.json

# Using jq
jq . your_file.json

# Or use online validators
# https://jsonlint.com
```

---

## Documentation Contributions

### Documentation Structure

```
docs/
├── PROJECT_INTRO.md      # Project overview
├── ARCHITECTURE.md       # Technical architecture
├── TECH_STACK.md         # Technology details
├── FEATURES.md           # Feature documentation
├── SETUP.md              # Installation guide
├── API_REFERENCE.md      # API documentation
└── CONTRIBUTING.md       # This file
```

### Documentation Style

- Use clear, concise language
- Include code examples where helpful
- Add diagrams for complex concepts
- Keep formatting consistent
- Update table of contents
- Link to related documentation

### Improving Documentation

- Fix typos and grammatical errors
- Clarify confusing sections
- Add missing information
- Update outdated content
- Add examples and tutorials

---

## Reporting Issues

### Before Creating an Issue

1. Search existing issues to avoid duplicates
2. Check if it's already fixed in the main branch
3. Verify you're using the latest version
4. Try to reproduce the issue

### Bug Report Template

```markdown
**Describe the bug**
A clear description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior:
1. Go to '...'
2. Click on '...'
3. See error

**Expected behavior**
What you expected to happen.

**Screenshots**
If applicable, add screenshots.

**Environment:**
- Device: [e.g., Pixel 7]
- OS: [e.g., Android 14]
- App Version: [e.g., 1.0]

**Additional context**
Any other relevant information.
```

### Feature Request Template

```markdown
**Is your feature request related to a problem?**
A clear description of the problem.

**Describe the solution you'd like**
What you want to happen.

**Describe alternatives you've considered**
Other solutions you've thought about.

**Additional context**
Mockups, examples, or references.
```

---

## Code Review Process

### What We Look For

1. **Functionality**: Does it work as intended?
2. **Code Quality**: Is it clean and maintainable?
3. **Architecture**: Does it follow project patterns?
4. **Testing**: Are there appropriate tests?
5. **Documentation**: Is it documented where needed?
6. **Performance**: Are there any performance concerns?

### Review Timeline

- Initial review: Within 3-5 days
- Follow-up reviews: Within 1-2 days
- Merge: After approval from at least one maintainer

### Addressing Review Comments

```bash
# Make requested changes
git add .
git commit -m "fix: address review comments"
git push origin your-branch
```

### Pull Request Template

```markdown
## Description
Brief description of changes.

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Refactoring
- [ ] Other (please describe)

## Testing
- [ ] Tested on Android
- [ ] Tested on iOS (if applicable)
- [ ] Tested on Desktop (if applicable)
- [ ] Added unit tests
- [ ] Manual testing completed

## Screenshots (if applicable)
Add screenshots of UI changes.

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-reviewed my code
- [ ] Commented complex code sections
- [ ] Updated documentation
- [ ] No new warnings
- [ ] Dependent changes merged

## Related Issues
Closes #issue_number
```

---

## Community Guidelines

### Code of Conduct

- Be respectful and inclusive
- Welcome newcomers
- Provide constructive feedback
- Focus on what's best for the community
- Show empathy towards other contributors

### Communication

- **GitHub Issues**: Bug reports and feature requests
- **Pull Requests**: Code discussions
- **Email**: nyinyizaw.dev@gmail.com for private matters

### Getting Help

- Read the documentation first
- Search existing issues
- Ask clear, specific questions
- Provide context and examples
- Be patient and respectful

---

## Recognition

Contributors are recognized in several ways:

- Listed in GitHub contributors
- Mentioned in release notes for significant contributions
- Community shout-outs for helpful contributions

---

## License

By contributing to Daily Challenge, you agree that your contributions will be licensed under the MIT License.

---

## Questions?

If you have questions about contributing, feel free to:

- Open an issue with the `question` label
- Email the maintainer: nyinyizaw.dev@gmail.com
- Check the [PROJECT_INTRO.md](PROJECT_INTRO.md) for project overview

---

## Related Documentation

- [Setup Guide](SETUP.md) - Development environment setup
- [Architecture Guide](ARCHITECTURE.md) - Project architecture
- [API Reference](API_REFERENCE.md) - API documentation
- [Tech Stack](TECH_STACK.md) - Technologies used

---

**Thank you for contributing to Daily Challenge!**

We appreciate your time and effort in making this project better for everyone.

---

**Last Updated**: October 2025
