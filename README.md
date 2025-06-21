<img src="screenshots/app_icon.png" alt="App Icon" width="80">
# Daily Challenge

A multiplatform app to browse and solve daily coding challenges for mobile developers. Built with Kotlin Multiplatform and
Compose for Android, iOS, Desktop, and Web.

## 🚀 Overview

Daily Challenge is an educational app designed to help developers improve their programming skills through daily coding challenges and quizzes. The app focuses on mobile development topics including Android, iOS, Kotlin, and Swift, offering various interactive game modes to make learning fun and engaging.

## ✨ Features

- **Multiple Game Modes**: Test your knowledge with different quiz formats
  - **True or False**: Quick yes/no questions to test basic knowledge
  - **Multiple Choice**: Select the correct answer from multiple options
  - **Multiple Select**: Choose all correct answers from a set of options
  - **Matching Game**: Match related pairs correctly (coming soon)
- **Category Selection**: Focus on your preferred technology (Android, iOS, Kotlin, Swift)
- **Daily Challenges**: New coding problems delivered regularly
- **Cross-Platform**: Same experience across Android, iOS, and Desktop
- **Dark/Light Theme**: Choose your preferred visual style

## Screenshots

### Android Screenshots

<div align="center">
  <img src="screenshots/Android1.png" alt="Android Screenshot 1" width="200">
  <img src="screenshots/Android2.png" alt="Android Screenshot 2" width="200">
  <img src="screenshots/Android3.png" alt="Android Screenshot 3" width="200">
</div>

### iOS Screenshots

<div align="center">
  <img src="screenshots/ios1.png" alt="iOS Screenshot 1" width="200">
  <img src="screenshots/ios2.png" alt="iOS Screenshot 2" width="200">
  <img src="screenshots/ios3.png" alt="iOS Screenshot 3" width="200">
</div>

### Desktop Screenshots

<div align="center">
  <img src="screenshots/desktop1.png" alt="Desktop Screenshot 1" width="250">
  <img src="screenshots/desktop2.png" alt="Desktop Screenshot 2" width="250">
  <img src="screenshots/desktop3.png" alt="Desktop Screenshot 3" width="250">
</div>

---

## 📱 Download for Testing

Try out the app on your device:

- [Download Android APK](https://drive.google.com/file/d/1iVbWyTKYXIWb2e-VXF75y62iZJXG3-3m/view?usp=sharing)
- [Download Mac APP DMG](https://drive.google.com/file/d/1ncy1zSjszQ3zZQ8cXHABDZRgHl4-Jocq/view?usp=sharing)

## 🛠️ Installation & Setup

### Prerequisites
- [Android Studio Arctic Fox](https://developer.android.com/studio) or newer
- [Kotlin](https://kotlinlang.org/docs/getting-started.html) 1.9.0+
- [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) 17+
- [Xcode](https://developer.apple.com/xcode/) 14+ (for iOS development)

### Building the Project

1. Clone the repository:
   ```sh
   git clone https://github.com/nyinyiz/DailyChallenge.git
   cd DailyChallenge
   ```

2. Open the project in Android Studio

3. Build and run for your target platform:
   - **Android**: Select an Android device/emulator and click Run
   - **iOS**: Run the `iosApp` configuration (requires macOS with Xcode)
   - **Desktop**: Run the `desktopApp` configuration

---

## 🔧 Tech Stack

### Core Technologies
| Technology | Description | Version |
|------------|-------------|---------|
| **Kotlin Multiplatform (KMP)** | Cross-platform development | `2.1.21` |
| **Jetpack Compose Multiplatform** | UI toolkit | `1.8.1` |
| **Compose Hot Reload** | Development workflow enhancement | - |

### Architecture & Design Patterns
| Pattern | Description | Version |
|---------|-------------|---------|
| **MVVM Architecture** | Using `ViewModel` and state management | - |
| **Navigation Component** | Jetpack Navigation Compose for routing | - |
| **Koin** | Dependency injection | `4.0.0` |

### UI Framework
| Component | Description |
|-----------|-------------|
| **Compose Material 3** | Modern UI components |
| **Adaptive UI** | Responsive design across platforms |
| **Compose Navigation** | Screen navigation |

### Data Management
| Library | Description | Version |
|---------|-------------|---------|
| **DataStore** | Preferences storage | `1.1.7` |
| **Kotlinx Serialization** | JSON serialization | `1.7.3` |
| **Coil** | Image loading and caching | `3.2.0` |

### Platform Support
| Platform | Details |
|----------|---------|
| **Android** | Min SDK `24`, Target SDK `35` |
| **iOS** | Native support via KMP |
| **Desktop** | JVM-based desktop application |
| **Web** | Configured for WebAssembly *(currently commented out, looking for DataStore solution)* |

### Other Libraries
| Library | Description | Version |
|---------|-------------|---------|
| **Material Icons Extended** | UI icons | - |
| **Coroutines** | Asynchronous programming | `1.10.2` |

---

## 👥 Contributing

Contributions are welcome and appreciated! Help make Daily Challenge better for everyone.

### Ways to Contribute

- **Add New Questions**: Expand our collection of challenges in Android, iOS, Kotlin, Swift, or other mobile development topics
- **Implement Features**: Work on items from our [roadmap](#️-roadmap) or [tasks list](docs/tasks.md)
- **Fix Bugs**: Help squash bugs and improve the app's stability
- **Improve Documentation**: Enhance the documentation to make it more helpful for users and contributors

### Contribution Process

1. **Fork** the repository
2. **Clone** your fork:
   ```sh
   git clone https://github.com/nyinyiz/DailyChallenge.git
   ```
3. **Create a new branch** for your feature or fix:
   ```sh
   git checkout -b my-new-feature
   ```
4. **Make your changes** and test them thoroughly
5. **Commit** and **push** your changes:
   ```sh
   git add .
   git commit -m "Add feature: [description]"
   git push origin my-new-feature
   ```
6. **Open a Pull Request** on GitHub with a clear description of your changes

## Contributing Challenge Questions

Thank you for considering contributing to Daily Challenge! Here's how you can add new challenge
questions.

### JSON File Structure

Challenge questions are stored in the `composeApp/src/commonMain/composeResources/files` directory.
There are three types of challenges:

1. Daily Challenges: `daily_challenges.json`
2. True/False Questions: `true_or_false_challenges_[category].json`
3. Multiple Choice Questions: `multiple_choice_challenges_[category].json`

where `[category]` can be: android, ios, kotlin, swift, or flutter

### JSON Format Examples

#### Daily Challenge Format

```json
{
  "id": "unique_id",
  "difficulty": "Easy|Medium|Hard",
  "question": "Your question text here",
  "questionCode": "Code snippet for the question (optional)",
  "answerCode": "Solution code snippet"
}
```

#### True/False Challenge Format

```json
{
  "id": "unique_id",
  "question": "Your question text here",
  "correctAnswer": "true|false",
  "explanation": "Explanation text",
  "difficulty": "Easy|Medium|Hard"
}
```

#### Multiple Choice Challenge Format

```json
{
  "question": "Your multiple choice question",
  "options": [
    "Option A",
    "Option B",
    "Option C",
    "Option D"
  ],
  "correctAnswer": "Option A",
  "difficulty": "Easy|Medium|Hard",
  "explanation": "Explanation for the correct answer"
}
```

#### Multiple Select Challenge Format

```json
{
  "question": "Your multiple select question",
  "options": [
    "Option A",
    "Option B",
    "Option C",
    "Option D"
  ],
  "correctAnswers": [
    "Option A",
    "Option C"
  ],
  "difficulty": "Easy|Medium|Hard",
  "explanation": "Explanation for the correct answers"
}
```

## 🗺️ Roadmap

The following features and improvements are planned for future releases:

- **Matching Game Mode**: Implementation of the matching pairs game mode
- **Web Support**: Complete WebAssembly support with DataStore integration
- **User Profiles**: Track progress and achievements
- **Leaderboards**: Compete with other users
- **More Categories**: Expand to include Flutter, React Native, and other mobile technologies
- **Offline Mode**: Full functionality without an internet connection
- **Push Notifications**: Daily reminders for new challenges

Check the [tasks.md](docs/tasks.md) file for a comprehensive list of planned improvements.

## 👤 Author

Created by **Nyi Nyi Zaw** (nyinyizaw.dev@gmail.com)

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
