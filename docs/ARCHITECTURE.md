# Architecture Guide

## Overview

Daily Challenge follows **Clean Architecture** principles with **MVVM (Model-View-ViewModel)** pattern, ensuring separation of concerns, testability, and maintainability across the Kotlin Multiplatform codebase.

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      Presentation Layer                      │
│  ┌────────────────────────────────────────────────────────┐ │
│  │          Jetpack Compose UI (Composables)              │ │
│  │  - Screens   - Components   - Navigation   - Theme    │ │
│  └────────────────────────────────────────────────────────┘ │
│                            ↕                                 │
│  ┌────────────────────────────────────────────────────────┐ │
│  │                    ViewModels                          │ │
│  │  - State Management   - UI Logic   - Event Handling   │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                       Domain Layer                           │
│  ┌────────────────────────────────────────────────────────┐ │
│  │              Repository Interfaces                     │ │
│  │  - ChallengesRepository                                │ │
│  │  - UserPreferencesRepository                           │ │
│  │  - TipsRepository                                      │ │
│  └────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                        Data Layer                            │
│  ┌────────────────────────────────────────────────────────┐ │
│  │           Repository Implementations                   │ │
│  └────────────────────────────────────────────────────────┘ │
│                            ↕                                 │
│  ┌─────────────────────┐         ┌─────────────────────┐   │
│  │   Remote Data       │         │   Local Data        │   │
│  │  - API Service      │         │  - DataStore        │   │
│  │  - Ktor Client      │         │  - Preferences      │   │
│  │  - JSON Parsing     │         │  - Cache            │   │
│  └─────────────────────┘         └─────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↕
┌─────────────────────────────────────────────────────────────┐
│                  Dependency Injection                        │
│                        (Koin)                                │
└─────────────────────────────────────────────────────────────┘
```

## Architectural Layers

### 1. Presentation Layer

The UI layer built with Jetpack Compose Multiplatform, responsible for rendering the interface and handling user interactions.

#### Components

**A. Composables (UI)**
- Location: `composeApp/src/commonMain/kotlin/com/nyinyi/dailychallenge/ui/`
- Responsibility: Render UI based on state
- Key Files:
  - `App.kt` - Root composable
  - `screens/` - Screen-level composables
  - `components/` - Reusable UI components

**B. ViewModels (State Management)**
- Location: Alongside corresponding screens
- Responsibility: Manage UI state, handle business logic
- Pattern: StateFlow for state emission
- Key Files:
  - `AppViewModel.kt` - App-level state
  - `QuestionListViewModel.kt` - List screen state
  - `QuizScreenViewModel.kt` - Quiz game state
  - `MultipleChoiceViewModel.kt` - MCQ game state

#### State Flow Pattern

```kotlin
// ViewModel emits UI state
class QuizScreenViewModel(
    private val repository: ChallengesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadQuestions() {
        viewModelScope.launch {
            repository.getTrueFalseChallenges()
                .collect { result ->
                    _uiState.value = when (result) {
                        is NetworkResult.Success -> UiState.Success(result.data)
                        is NetworkResult.Error -> UiState.Error(result.message)
                    }
                }
        }
    }
}
```

```kotlin
// Composable observes state
@Composable
fun QuizScreen(viewModel: QuizScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is UiState.Loading -> LoadingScreen()
        is UiState.Success -> QuizContent(state.data)
        is UiState.Error -> ErrorScreen(state.message)
    }
}
```

### 2. Domain Layer

Contains business logic and repository interfaces, independent of frameworks and implementation details.

#### Repository Interfaces

```kotlin
interface ChallengesRepository {
    suspend fun getDailyChallenges(): Flow<NetworkResult<List<DailyChallengeObj>>>
    suspend fun getTrueFalseChallenges(category: String): Flow<NetworkResult<List<QuizCard>>>
    suspend fun getMultipleChoiceChallenges(category: String): Flow<NetworkResult<List<MultipleChoiceObj>>>
    suspend fun getMultipleSelectChallenges(category: String): Flow<NetworkResult<List<MultipleSelectObj>>>
    suspend fun getMatchingChallenges(category: String): Flow<NetworkResult<List<MatchingGameObj>>>
}
```

```kotlin
interface UserPreferencesRepository {
    val userProfileFlow: Flow<UserProfile>
    suspend fun saveUserProfile(userProfile: UserProfile)
    suspend fun updateCategory(category: String)
    suspend fun updateTheme(isDarkTheme: Boolean)
}
```

### 3. Data Layer

Handles data operations, API calls, and local storage.

#### A. Repository Implementations

**ChallengesRepositoryImpl**
- Location: `data/repository/ChallengesRepositoryImpl.kt`
- Responsibilities:
  - Fetch data from API
  - Handle errors with fallback data
  - Transform API responses to domain models
  - Cache management

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
        } catch (e: Exception) {
            emit(NetworkResult.Error(e.message ?: "Unknown error"))
            // Emit fallback data
            emit(NetworkResult.Success(getDefaultTrueFalseChallenges()))
        }
    }
}
```

**UserPreferencesRepositoryImpl**
- Location: `data/repository/UserPreferencesRepositoryImpl.kt`
- Responsibilities:
  - Persist user preferences to DataStore
  - Provide reactive Flow of user settings
  - Handle serialization/deserialization

#### B. Remote Data Source

**ChallengesApiService**
- Location: `data/remote/ChallengesApiService.kt`
- Responsibilities:
  - Define API endpoints
  - Make HTTP requests using Ktor
  - Parse JSON responses

```kotlin
class ChallengesApiService(
    private val httpClient: HttpClient
) {
    suspend fun getDailyChallenges(): List<DailyChallengeObj> {
        return httpClient.get("${ApiConstants.BASE_URL}daily_challenges.json").body()
    }

    suspend fun getTrueFalseChallenges(category: String): List<QuizCard> {
        return httpClient.get(
            "${ApiConstants.BASE_URL}true_or_false_challenges_$category.json"
        ).body()
    }
}
```

#### C. Local Data Source

**DataStore Preferences**
- Location: Platform-specific implementations
  - `androidMain/kotlin/.../DataStoreHelper.android.kt`
  - `iosMain/kotlin/.../DataStoreHelper.ios.kt`
- Responsibilities:
  - Store user preferences
  - Category selection
  - Theme preference
  - User profile data

### 4. Dependency Injection

Uses **Koin** for dependency management across the multiplatform project.

**AppModule Configuration**
- Location: `di/AppModule.kt`

```kotlin
val appModule = module {
    // HttpClient with platform-specific engine
    single {
        HttpClient(engine) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = HttpLogger
                level = LogLevel.INFO
            }
        }
    }

    // API Service
    single { ChallengesApiService(get()) }

    // Repositories
    single<ChallengesRepository> { ChallengesRepositoryImpl(get()) }
    single<UserPreferencesRepository> {
        UserPreferencesRepositoryImpl(get())
    }
    single { TipsRepository(get()) }

    // ViewModels
    viewModel { AppViewModel(get()) }
    viewModel { QuestionListViewModel(get()) }
    viewModel { QuizScreenViewModel(get(), get()) }
    viewModel { MultipleChoiceViewModel(get(), get()) }
    viewModel { MultipleSelectViewModel(get(), get()) }
    viewModel { MatchingGameViewModel(get(), get()) }
    viewModel { ProfileViewModel(get()) }
}
```

**Initialization**
- Android: `MainActivity.onCreate()`
- iOS: `MainViewController.kt`
- Desktop: `main.kt`

```kotlin
// Android
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataStore = createDataStore(applicationContext)

        startKoin {
            androidContext(applicationContext)
            modules(appModule(dataStore))
        }

        setContent {
            App()
        }
    }
}
```

## Navigation Architecture

### Navigation Graph

Uses Jetpack Navigation Compose for type-safe navigation.

**Route Definitions**
- Location: `navigation/Routes.kt`

```kotlin
object Routes {
    const val QUESTION_LIST = "question_list"
    const val QUESTION_DETAIL = "question_detail/{id}"
    const val QUIZ_SCREEN = "quiz_screen"
    const val MULTIPLE_CHOICE_SCREEN = "multiple_choice_screen"
    const val MULTIPLE_SELECT_SCREEN = "multiple_select_screen"
    const val MATCHING_GAME_SCREEN = "matching_game_screen"
}
```

**Navigation Implementation**
- Location: `navigation/AppNavigation.kt`

```kotlin
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.QUESTION_LIST
    ) {
        composable(Routes.QUESTION_LIST) {
            QuestionsList(navController)
        }

        composable(Routes.QUIZ_SCREEN) {
            QuizScreen(navController)
        }

        // More routes...
    }
}
```

### Navigation Flow

```
App Launch
    ↓
QuestionsList (Home)
    ↓
┌───────────┬───────────────┬────────────────┬──────────────┐
│           │               │                │              │
Quiz      MCQ        MultiSelect    Matching       Daily
Screen    Screen     Screen         Game           Challenge
                                                   Detail
```

## Data Flow Architecture

### Request Flow

```
User Action (Button Click)
    ↓
Composable calls ViewModel method
    ↓
ViewModel dispatches to Repository
    ↓
Repository calls API Service or Local Store
    ↓
Network request via Ktor / DataStore read
    ↓
Response/Data returned to Repository
    ↓
Repository transforms to domain model
    ↓
ViewModel updates StateFlow
    ↓
Composable recomposes with new state
    ↓
UI updates displayed to user
```

### Error Handling Flow

```kotlin
// Sealed class for result types
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String) : NetworkResult<Nothing>()
    data class NetworkError(val exception: Exception) : NetworkResult<Nothing>()
}

// Repository error handling
override suspend fun getDailyChallenges(): Flow<NetworkResult<List<DailyChallengeObj>>> = flow {
    emit(NetworkResult.Loading)
    try {
        val data = apiService.getDailyChallenges()
        emit(NetworkResult.Success(data))
    } catch (e: IOException) {
        emit(NetworkResult.NetworkError(e))
        // Fallback to default data
        emit(NetworkResult.Success(getDefaultChallenges()))
    } catch (e: Exception) {
        emit(NetworkResult.Error(e.message ?: "Unknown error"))
    }
}
```

## Platform-Specific Architecture

### Expect/Actual Pattern

For platform-specific implementations:

```kotlin
// commonMain
expect fun createDataStore(context: Any): DataStore<Preferences>

// androidMain
actual fun createDataStore(context: Any): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create {
        (context as Context).filesDir
            .resolve("user_preferences.preferences_pb")
    }
}

// iosMain
actual fun createDataStore(context: Any): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create {
        NSHomeDirectory() + "/user_preferences.preferences_pb"
    }
}
```

### Platform Modules

**Android-Specific**
- `MainActivity.kt` - Entry point
- `ProgrammingTipsWidget.kt` - Glance widget
- `ProgrammingTipsWidgetReceiver.kt` - Widget receiver
- DataStore Android implementation

**iOS-Specific**
- `iosApp/` - Native iOS wrapper
- DataStore iOS implementation
- iOS-specific resource handling

**Desktop-Specific**
- Desktop window configuration
- JVM-specific optimizations

## State Management Patterns

### ViewModel State Pattern

```kotlin
// UI State sealed class
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<QuizCard>) : UiState()
    data class Error(val message: String) : UiState()
}

// ViewModel
class QuizScreenViewModel(
    private val repository: ChallengesRepository,
    private val userPrefsRepo: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _currentQuestion = MutableStateFlow<QuizCard?>(null)
    val currentQuestion: StateFlow<QuizCard?> = _currentQuestion.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            userPrefsRepo.userProfileFlow
                .flatMapLatest { profile ->
                    repository.getTrueFalseChallenges(profile.selectedCategory)
                }
                .collect { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            _uiState.value = UiState.Success(result.data)
                            _currentQuestion.value = result.data.firstOrNull()
                        }
                        is NetworkResult.Error -> {
                            _uiState.value = UiState.Error(result.message)
                        }
                    }
                }
        }
    }

    fun answerQuestion(answer: Boolean) {
        // Handle answer logic
    }

    fun nextQuestion() {
        // Move to next question
    }
}
```

## Widget Architecture (Android)

### Glance Widget Implementation

```kotlin
// Widget provider
class ProgrammingTipsWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val tip = getTip()
            ProgrammingTipsContent(tip)
        }
    }
}

// Widget receiver for updates
class ProgrammingTipsWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = ProgrammingTipsWidget()
}

// WorkManager scheduling
class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        ProgrammingTipsWidget().updateAll(applicationContext)
        return Result.success()
    }
}
```

## Testing Architecture

### Testing Layers

```
┌─────────────────────────────────────┐
│        UI Tests (Compose)           │
│  - Screen tests                     │
│  - Navigation tests                 │
│  - Integration tests                │
└─────────────────────────────────────┘
               ↓
┌─────────────────────────────────────┐
│      ViewModel Tests (Unit)         │
│  - State management tests           │
│  - Business logic tests             │
│  - Mock repositories                │
└─────────────────────────────────────┘
               ↓
┌─────────────────────────────────────┐
│     Repository Tests (Unit)         │
│  - Data transformation tests        │
│  - Error handling tests             │
│  - Mock API service                 │
└─────────────────────────────────────┘
```

## File Organization

```
composeApp/src/
├── commonMain/kotlin/com/nyinyi/dailychallenge/
│   ├── data/
│   │   ├── model/              # Data models
│   │   │   ├── DailyChallengeObj.kt
│   │   │   ├── QuizCard.kt
│   │   │   └── ...
│   │   ├── remote/             # API layer
│   │   │   ├── ChallengesApiService.kt
│   │   │   └── ApiConstants.kt
│   │   └── repository/         # Repository implementations
│   │       ├── ChallengesRepository.kt
│   │       └── ChallengesRepositoryImpl.kt
│   ├── ui/
│   │   ├── screens/            # Screen composables
│   │   │   ├── list/
│   │   │   ├── detail/
│   │   │   └── play/
│   │   ├── components/         # Reusable components
│   │   ├── theme/              # Theme configuration
│   │   ├── App.kt              # Root composable
│   │   └── AppViewModel.kt     # App state
│   ├── navigation/             # Navigation logic
│   │   ├── Routes.kt
│   │   └── AppNavigation.kt
│   └── di/                     # Dependency injection
│       └── AppModule.kt
├── androidMain/kotlin/com/nyinyi/dailychallenge/
│   ├── MainActivity.kt
│   └── widget/
│       ├── ProgrammingTipsWidget.kt
│       └── ProgrammingTipsWidgetReceiver.kt
├── iosMain/kotlin/com/nyinyi/dailychallenge/
│   └── Platform.ios.kt
└── commonTest/kotlin/
    └── ...
```

## Design Patterns

### 1. Repository Pattern
- Abstracts data sources
- Single source of truth
- Easy to swap implementations

### 2. Dependency Injection
- Loose coupling
- Testability
- Configuration flexibility

### 3. Observer Pattern
- StateFlow/SharedFlow for reactive UI
- Automatic UI updates
- Lifecycle awareness

### 4. Factory Pattern
- ViewModel creation
- HttpClient configuration
- Platform-specific implementations

### 5. Strategy Pattern
- Different quiz game modes
- Platform-specific behaviors
- Adaptive UI strategies

## Performance Considerations

### 1. Lazy Loading
- ViewModels created on-demand
- Composables only loaded when visible
- API calls triggered only when needed

### 2. State Hoisting
- State managed at appropriate levels
- Recomposition optimization
- Minimal state sharing

### 3. Coroutine Scoping
- ViewModelScope for automatic cancellation
- Structured concurrency
- Proper error propagation

### 4. Caching
- Default data as fallback
- DataStore for preferences
- In-memory caching for API responses

## Security Considerations

### 1. Data Validation
- JSON parsing with error handling
- Input validation in ViewModels
- Type-safe data models

### 2. Network Security
- HTTPS for all API calls
- Certificate pinning (future enhancement)
- Timeout configurations

### 3. Local Storage
- Encrypted DataStore (when needed)
- No sensitive data in preferences
- Secure credential handling

## Scalability

### Adding New Features

**New Game Mode Example:**

1. **Create Model** (`data/model/NewGameObj.kt`)
2. **Add API Method** (`ChallengesApiService.kt`)
3. **Update Repository** (`ChallengesRepository.kt`)
4. **Create ViewModel** (`NewGameViewModel.kt`)
5. **Build UI** (`screens/play/newgame/NewGameScreen.kt`)
6. **Add Navigation** (`navigation/Routes.kt`)
7. **Register in DI** (`di/AppModule.kt`)

### Adding New Platform

1. Create platform source set (`newPlatformMain`)
2. Implement platform-specific code (expect/actual)
3. Configure build for new target
4. Test platform-specific features

---

## Related Documentation

- [Tech Stack Guide](TECH_STACK.md) - Detailed library information
- [Setup Guide](SETUP.md) - Development environment setup
- [API Reference](API_REFERENCE.md) - API documentation
- [Contributing Guide](CONTRIBUTING.md) - How to contribute

---

**Last Updated**: October 2025
