<div align="center">
  <img src="screenshots/app_icon.png" alt="App Icon" width="120">
  <h1>Daily Challenge</h1>
  <p>
    <a href="https://kotlinlang.org"><img src="https://img.shields.io/badge/Kotlin-2.0.20-purple?style=flat&logo=kotlin" alt="Kotlin"></a>
    <a href="https://www.jetbrains.com/lp/compose-multiplatform/"><img src="https://img.shields.io/badge/Compose%20Multiplatform-1.7.0-blue?style=flat&logo=jetpackcompose" alt="Compose Multiplatform"></a>
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-green?style=flat" alt="License"></a>
  </p>
  <p><strong>Master Mobile Development with Daily Coding Challenges</strong></p>
</div>

## 🚀 Overview

**Daily Challenge** is a Kotlin Multiplatform educational app designed to gamify learning for Android, iOS, and Desktop developers.

| 🤖 Android | 🍎 iOS | 🖥️ Desktop |
| :---: | :---: | :---: |
| Compose Multiplatform | Compose Multiplatform | Compose Multiplatform |

## ✨ Key Features

| Feature | Description |
| :--- | :--- |
| **🎮 Game Modes** | True/False, MCQ, Multi-Select, Matching |
| **📚 Categories** | Android, iOS, Kotlin, Swift, Flutter |
| **🔥 Daily Challenges** | Complex coding problems with solutions |
| **🌙 Theming** | Dark/Light mode support |
| **📶 Offline First** | Learn anywhere, anytime |

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

## 🏗️ Architecture

```mermaid
graph TD
    User[User Action] --> UI[Compose UI]
    UI --> VM[ViewModel]
    VM --> Repo[Repository]
    Repo --> Remote[API Service]
    Repo --> Local[DataStore/Cache]
```

## 📚 Documentation

| Guide | Description |
| :--- | :--- |
| [**Project Intro**](docs/PROJECT_INTRO.md) | Goals, mission, and vision |
| [**Architecture**](docs/ARCHITECTURE.md) | MVVM, Clean Architecture, Koin |
| [**Tech Stack**](docs/TECH_STACK.md) | Libraries and tools used |
| [**Features**](docs/FEATURES.md) | Detailed feature breakdown |
| [**Setup**](docs/SETUP.md) | Installation and build guide |
| [**API Reference**](docs/API_REFERENCE.md) | API endpoints and data formats |

## ⚡ Quick Start

```bash
git clone https://github.com/nyinyiz/DailyChallenge.git
cd DailyChallenge
./gradlew composeApp:run
```

## 🤝 Contributing

Contributions are welcome! See [CONTRIBUTING.md](docs/CONTRIBUTING.md).

## 📄 License

This project is licensed under the [MIT License](LICENSE).
