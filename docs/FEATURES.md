# Features Guide

Complete documentation of all features available in Daily Challenge.

---

## Table of Contents

1. [Overview](#overview)
2. [Game Modes](#game-modes)
3. [Category System](#category-system)
4. [Daily Challenges](#daily-challenges)
5. [User Interface](#user-interface)
6. [Theme System](#theme-system)
7. [Widget Feature](#widget-feature)
8. [Offline Support](#offline-support)
9. [Progress Tracking](#progress-tracking)
10. [Cross-Platform Features](#cross-platform-features)

---

## Overview

Daily Challenge offers multiple interactive ways to learn and practice mobile development concepts. Each feature is designed to provide an engaging, educational experience.

### Core Features at a Glance

| Feature | Description | Platforms |
|---------|-------------|-----------|
| Multiple Game Modes | 4 different quiz formats | All |
| Category Selection | Focus on specific technologies | All |
| Daily Challenges | Detailed coding problems | All |
| Dark/Light Theme | Customizable appearance | All |
| Programming Tips Widget | Hourly updated tips | Android |
| Offline Support | Works without internet | All |
| Progress Tracking | Track your learning journey | All |

---

## Game Modes

### 1. True or False Quiz

**Description**: Swipe-based quiz with true/false questions.

**User Experience:**
- Tinder-style card interface
- Swipe right for True
- Swipe left for False
- Immediate feedback with explanations
- Progress indicator showing remaining questions

**Technical Implementation:**
- Location: `ui/screens/play/quiz/`
- ViewModel: `QuizScreenViewModel.kt`
- Animation: Card swipe gestures with Compose Animation API
- State: Tracks current question, score, and failed questions

**Example Question:**
```
Question: "In Kotlin, lateinit can only be used with var properties."
Answer: True
Explanation: lateinit modifier is only applicable to var properties...
```

**Features:**
- Smooth card animations
- Gesture-based interaction
- Results summary at the end
- Retry failed questions option
- Category-specific questions

**Code Reference:**
```kotlin
// ui/screens/play/quiz/QuizScreen.kt
@Composable
fun QuizScreen(
    viewModel: QuizScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is UiState.Success -> QuizCardStack(
            questions = state.questions,
            onSwipe = viewModel::answerQuestion
        )
    }
}
```

---

### 2. Multiple Choice Questions

**Description**: Select one correct answer from four options.

**User Experience:**
- Clear question display
- 4 clickable options
- Visual feedback on selection
- Detailed explanation after answering
- Next button to proceed

**Technical Implementation:**
- Location: `ui/screens/play/mcq/`
- ViewModel: `MultipleChoiceViewModel.kt`
- State: Selected answer, correct answer, explanation visibility
- Validation: Ensures one answer selected before submission

**Example Question:**
```
Question: "Which function transforms each element of a collection?"
Options:
  A) filter()
  B) map()       ✓ Correct
  C) reduce()
  D) fold()

Explanation: The map() function transforms each element...
```

**Features:**
- Radio button selection
- Color-coded feedback (green for correct, red for incorrect)
- Explanation dialog
- Score tracking
- Difficulty indicators
- Retry incorrect answers

**UI Components:**
- Question card with Material 3 design
- Option buttons with ripple effect
- Explanation bottom sheet
- Progress bar

**Code Reference:**
```kotlin
// ui/screens/play/mcq/MultipleChoiceScreen.kt
@Composable
fun MultipleChoiceQuestion(
    question: MultipleChoiceObj,
    onAnswerSelected: (String) -> Unit
) {
    Column {
        Text(question.question, style = MaterialTheme.typography.headlineSmall)

        question.options.forEach { option ->
            OptionButton(
                text = option,
                onClick = { onAnswerSelected(option) }
            )
        }
    }
}
```

---

### 3. Multiple Select Questions

**Description**: Choose all correct answers from the given options.

**User Experience:**
- Multiple checkboxes
- Select all applicable answers
- Submit button enabled when at least one selected
- Instant feedback on submission
- Must select ALL correct answers to pass

**Technical Implementation:**
- Location: `ui/screens/play/multiselect/`
- ViewModel: `MultipleSelectViewModel.kt`
- State: Set of selected answers, correct answers set
- Validation: Checks if selected set matches correct answers set

**Example Question:**
```
Question: "Which are valid Kotlin collection types?"
Options:
  ☑ List        ✓
  ☑ Set         ✓
  ☐ Array       ✗ (not selected, but correct)
  ☑ Dictionary  ✗ (incorrect)

Result: Incorrect (missed Array, selected Dictionary)
```

**Features:**
- Checkbox-based multi-selection
- Partial credit not given (must get all correct)
- Detailed explanation of all correct answers
- Visual indication of what was missed
- Score calculation based on exact matches

**Complexity Levels:**
- Easy: 2 correct out of 4 options
- Medium: 2-3 correct out of 4-5 options
- Hard: 3-4 correct out of 5-6 options

**Code Reference:**
```kotlin
// ui/screens/play/multiselect/MultipleSelectScreen.kt
@Composable
fun MultipleSelectQuestion(
    question: MultipleSelectObj,
    selectedAnswers: Set<String>,
    onToggleAnswer: (String) -> Unit
) {
    question.options.forEach { option ->
        Row {
            Checkbox(
                checked = option in selectedAnswers,
                onCheckedChange = { onToggleAnswer(option) }
            )
            Text(option)
        }
    }
}
```

---

### 4. Matching Game

**Description**: Connect related concepts by matching pairs.

**User Experience:**
- Two columns: terms and definitions
- Drag and drop or tap-to-select pairing
- Visual lines connecting matched pairs
- Shuffled options for challenge
- Feedback on completion

**Technical Implementation:**
- Location: `ui/screens/play/matching/`
- ViewModel: `MatchingGameViewModel.kt`
- State: Map of selected pairs, correct pairs, match status
- Algorithm: Shuffle right column, keep left static

**Example Game:**
```
Left Column         Right Column
-----------         ------------
ViewModel     ━━━━━ Stores UI-related data
LiveData      ━━━━━ Observable data holder
Repository    ━━━━━ Mediates between data sources
Room          ━━━━━ SQLite abstraction layer
```

**Features:**
- Drag-and-drop pairing
- Alternative: tap-to-connect interface
- Visual connection lines
- Color-coded feedback (correct/incorrect)
- Shuffle functionality
- Timer option (future)
- Undo last connection

**Game Mechanics:**
1. User selects item from left column
2. User selects corresponding item from right column
3. Line drawn between selections
4. Can modify before submission
5. Submit to check all answers
6. Visual feedback on all pairs

**Code Reference:**
```kotlin
// ui/screens/play/matching/MatchingGameScreen.kt
@Composable
fun MatchingGame(
    pairs: List<MatchingPair>,
    userMatches: Map<String, String>,
    onMatch: (left: String, right: String) -> Unit
) {
    Row {
        // Left column
        Column { /* Terms */ }

        // Connection lines
        Canvas { /* Draw lines */ }

        // Right column (shuffled)
        Column { /* Definitions */ }
    }
}
```

---

## Category System

### Available Categories

Daily Challenge supports five technology categories:

| Category | Icon | Focus Area | Question Count |
|----------|------|------------|----------------|
| Android | 🤖 | Android development | 100+ |
| iOS | 🍎 | iOS development | 80+ |
| Kotlin | 🎯 | Kotlin language | 120+ |
| Swift | 🧡 | Swift language | 80+ |
| Flutter | 💙 | Flutter framework | 60+ |

### Category Selection

**Location**: Home screen / Settings

**Features:**
- Visual category cards
- Icon representation
- Description of focus area
- Question count display
- Persistent selection (saved in DataStore)

**Implementation:**
```kotlin
// data/model/Category.kt
enum class Category(val displayName: String) {
    ANDROID("Android"),
    IOS("iOS"),
    KOTLIN("Kotlin"),
    SWIFT("Swift"),
    FLUTTER("Flutter")
}

// User preference storage
@Serializable
data class UserProfile(
    val selectedCategory: String = "android",
    val isDarkTheme: Boolean = false
)
```

**Category Switching:**
- Tap category card to switch
- Selection persists across app restarts
- All game modes filter by selected category
- Instant content update

---

## Daily Challenges

### Overview

Daily Challenges present detailed coding problems with code snippets and solutions.

**Features:**
- Question with code snippet
- Detailed solution with explanation
- Syntax-highlighted code display
- Copy code functionality
- Difficulty indicators
- Share challenge option

### Challenge Structure

```kotlin
@Serializable
data class DailyChallengeObj(
    val id: String,
    val difficulty: String,      // Easy, Medium, Hard
    val question: String,
    val questionCode: String,    // Code snippet
    val answerCode: String       // Solution + explanation
)
```

### Example Challenge

**Question:**
```
What will be the output of the following Kotlin code?

fun main() {
    val numbers = listOf(1, 2, 3, 4, 5)
    val result = numbers
        .filter { it % 2 == 0 }
        .map { it * it }
    println(result)
}
```

**Answer:**
```
[4, 16]

Explanation:
1. filter { it % 2 == 0 } keeps only even numbers: [2, 4]
2. map { it * it } squares each number: [4, 16]
3. The result is printed: [4, 16]
```

### UI Components

**Location**: `ui/screens/list/` and `ui/screens/detail/`

**Components:**
- `QuestionsList.kt` - List of challenges
- `QuestionDetail.kt` - Detailed view
- `CodeBlock.kt` - Syntax-highlighted code
- `QuestionTab.kt` - Question view
- `SolutionTab.kt` - Solution view

**Features:**
- Tabbed interface (Question / Solution)
- Collapsible code blocks
- Difficulty badges
- Bookmark functionality (planned)
- Share to social media (planned)

---

## User Interface

### Design System

**Framework**: Material 3 Design System
**Theme**: Custom implementation with dark/light modes

### Key UI Elements

#### 1. Navigation

**Type**: Bottom Navigation Bar (Android) / Tab Bar (iOS)

**Tabs:**
- Home (Question List)
- Play (Game Modes)
- Daily Challenge
- Profile (Settings)

**Implementation:**
```kotlin
// navigation/AppNavigation.kt
@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.QUESTION_LIST
    ) {
        composable(Routes.QUESTION_LIST) { QuestionsList() }
        composable(Routes.QUIZ_SCREEN) { QuizScreen() }
        // More routes...
    }
}
```

#### 2. App Bar

**Features:**
- App title/logo
- Back navigation
- Action buttons (settings, theme toggle)
- Search (planned)

#### 3. Cards

**Usage**: Questions, challenges, game mode selection

**Properties:**
- Elevated surface
- Rounded corners (16dp)
- Shadow/elevation
- Ripple effect on click

#### 4. Buttons

**Types:**
- Filled: Primary actions
- Outlined: Secondary actions
- Text: Tertiary actions

**Examples:**
- "Submit Answer" (Filled)
- "Skip Question" (Outlined)
- "Show Explanation" (Text)

#### 5. Dialogs & Bottom Sheets

**Usage:**
- Result summaries
- Explanations
- Settings
- Confirmations

### Adaptive Design

**Responsive Layouts:**
- Phone: Single pane
- Tablet: Two-pane layout (list + detail)
- Desktop: Multi-column layout

**Breakpoints:**
- Compact: < 600dp width
- Medium: 600dp - 840dp
- Expanded: > 840dp

**Implementation:**
```kotlin
@Composable
fun AdaptiveLayout() {
    val windowSize = rememberWindowSize()

    when (windowSize) {
        WindowSize.Compact -> PhoneLayout()
        WindowSize.Medium -> TabletLayout()
        WindowSize.Expanded -> DesktopLayout()
    }
}
```

---

## Theme System

### Dark and Light Themes

**Implementation**: Material 3 Dynamic Color

**Features:**
- System-wide theme toggle
- Persistent preference
- Smooth transition animation
- Dynamic color adaptation (Android 12+)

### Color Scheme

#### Light Theme
```kotlin
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    surface = Color(0xFFF5F5F5),
    background = Color.White
)
```

#### Dark Theme
```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    surface = Color(0xFF1E1E1E),
    background = Color(0xFF121212)
)
```

### Typography

**Font**: System default (San Francisco on iOS, Roboto on Android)

**Scale:**
- Display Large: 57sp
- Headline Large: 32sp
- Title Large: 22sp
- Body Large: 16sp
- Label Large: 14sp

### Theme Toggle

**Location**: Settings / App Bar

**Implementation:**
```kotlin
@Composable
fun ThemeToggle(
    isDarkTheme: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Switch(
        checked = isDarkTheme,
        onCheckedChange = onToggle,
        thumbContent = {
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.DarkMode
                              else Icons.Default.LightMode,
                contentDescription = "Theme"
            )
        }
    )
}
```

---

## Widget Feature

### Programming Tips Widget (Android)

**Platform**: Android only
**Technology**: Glance (Jetpack Compose for Widgets)

**Features:**
- Display random programming tip
- Hourly automatic updates
- Material 3 design
- Tap to open app
- Refresh button

### Widget Configuration

**Location**: `androidMain/kotlin/.../widget/`

**Files:**
- `ProgrammingTipsWidget.kt` - Widget implementation
- `ProgrammingTipsWidgetReceiver.kt` - Broadcast receiver
- `res/xml/programming_tips_widget_info.xml` - Widget metadata

**Manifest Registration:**
```xml
<receiver
    android:name=".widget.ProgrammingTipsWidgetReceiver"
    android:exported="true">
    <intent-filter>
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
    </intent-filter>
    <meta-data
        android:name="android.appwidget.provider"
        android:resource="@xml/programming_tips_widget_info" />
</receiver>
```

### Widget Layout

**Size**: 4x2 cells (min)
**Content:**
- Tip icon/logo
- Programming tip text
- Refresh button
- Category badge

### Update Mechanism

**Technology**: WorkManager

**Schedule**: Hourly updates

**Implementation:**
```kotlin
class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val tip = tipsRepository.getRandomTip()
        ProgrammingTipsWidget().update(applicationContext, tip)
        return Result.success()
    }
}

// Schedule work
val updateRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
    1, TimeUnit.HOURS
).build()

WorkManager.getInstance(context).enqueueUniquePeriodicWork(
    "widget_update",
    ExistingPeriodicWorkPolicy.KEEP,
    updateRequest
)
```

### Tips Data Source

**API**: `/programming_tips.json`

**Example Tip:**
```json
{
  "id": "tip_042",
  "tip": "Use 'val' instead of 'var' whenever possible for immutable variables.",
  "category": "Kotlin"
}
```

---

## Offline Support

### Features

**Core Functionality:**
- App works without internet connection
- Fallback data embedded in app
- Graceful degradation
- Sync when online

### Implementation

**Fallback Data:**
- Location: `data/repository/ChallengesRepositoryImpl.kt`
- Default questions for each game mode
- Hardcoded in repository as backup

**Flow:**
```
1. Try to fetch from API
   ↓ (if fails)
2. Check cache
   ↓ (if empty)
3. Load default data
   ↓
4. Display to user
```

**Code Example:**
```kotlin
override suspend fun getTrueFalseChallenges(
    category: String
): Flow<NetworkResult<List<QuizCard>>> = flow {
    try {
        // Try API first
        val data = apiService.getTrueFalseChallenges(category)
        emit(NetworkResult.Success(data))
    } catch (e: IOException) {
        // Network error - use fallback
        emit(NetworkResult.Success(getDefaultTrueFalseChallenges()))
    }
}

private fun getDefaultTrueFalseChallenges(): List<QuizCard> {
    return listOf(
        QuizCard(
            id = "default_001",
            question = "Kotlin is statically typed.",
            correctAnswer = "true",
            explanation = "...",
            difficulty = "Easy"
        )
        // More default questions...
    )
}
```

### User Experience

**Indicators:**
- Offline indicator banner (when no connection)
- "Using cached data" message
- Limited questions available notice
- Sync button to retry connection

---

## Progress Tracking

### Tracked Metrics

1. **Questions Attempted**: Total count
2. **Correct Answers**: Success rate
3. **Failed Questions**: Stored for review
4. **Category Progress**: Per-category stats
5. **Streak**: Daily usage streak (planned)
6. **Achievements**: Milestone badges (planned)

### Data Storage

**Technology**: DataStore Preferences

**Stored Data:**
```kotlin
@Serializable
data class UserProfile(
    val selectedCategory: String,
    val isDarkTheme: Boolean,
    val failedQuestions: List<String>,  // Question IDs
    val totalAttempted: Int,
    val totalCorrect: Int
)
```

### Statistics Screen (Planned)

**Metrics Display:**
- Total questions answered
- Success rate percentage
- Category breakdown
- Difficulty distribution
- Time spent
- Streak calendar

---

## Cross-Platform Features

### Shared Across Platforms

✅ **Fully Shared:**
- Business logic (ViewModels, Repositories)
- Data models
- API calls and networking
- Navigation structure
- State management

✅ **Mostly Shared:**
- UI components (95% shared)
- Theme configuration
- App structure

❌ **Platform-Specific:**
- Widget (Android only)
- DataStore implementation
- Platform-specific UI tweaks
- Native integrations

### Platform Differences

| Feature | Android | iOS | Desktop |
|---------|---------|-----|---------|
| Navigation | Bottom Nav | Tab Bar | Sidebar |
| Widget | ✅ Yes | ❌ No | ❌ No |
| Share | System Sheet | Share Sheet | Copy |
| Theme | Material 3 | Material 3 | Material 3 |
| Back Button | System Back | Swipe | Browser-style |

### Platform Optimization

**Android:**
- Edge-to-edge display
- Material You dynamic colors
- App widgets
- WorkManager background tasks

**iOS:**
- Native navigation feel
- Swipe gestures
- iOS-style dialogs
- Safe area handling

**Desktop:**
- Keyboard shortcuts
- Menu bar
- Resizable windows
- Multi-window support (planned)

---

## Future Features

### Planned Enhancements

1. **User Accounts & Cloud Sync**
   - Firebase authentication
   - Cross-device sync
   - Cloud backup

2. **Social Features**
   - Leaderboards
   - Challenge friends
   - Share achievements

3. **Advanced Learning**
   - Adaptive difficulty
   - Personalized recommendations
   - Learning paths

4. **More Content**
   - Code challenges with IDE
   - Video tutorials
   - Community contributions

5. **Gamification**
   - Achievements and badges
   - Daily streaks
   - XP and levels
   - Rewards

---

## Related Documentation

- [Architecture Guide](ARCHITECTURE.md) - Technical implementation
- [API Reference](API_REFERENCE.md) - API endpoints
- [Setup Guide](SETUP.md) - Development setup
- [Contributing](CONTRIBUTING.md) - Add new features

---

**Last Updated**: October 2025

**App Version**: 1.0

**Platform Support**: Android 7.0+, iOS 14.0+, Desktop (Windows/macOS/Linux)
