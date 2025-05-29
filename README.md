# Daily Challenge

A multiplatform app to browse and solve daily coding challenges. Built with Kotlin Multiplatform and
Compose for Android, iOS, Desktop, and Web.

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

## Welcome for Contribution

I welcome contributions! You can add new challenge questions in any area—Android, Kotlin, Compose,
or even platform-specific topics. Help me grow the collection and make learning fun for everyone!

1. **Fork** the repository.
2. **Clone** your fork:
   ```sh
   git clone https://github.com/nyinyiz/DailyChallenge.git
   ```
3. **Create a new branch** for your feature or fix:
   ```sh
   git checkout -b my-new-challenge
   ```
4. **Add your challenge/question** or make your changes.
5. **Commit** and **push** your changes:
   ```sh
   git add .
   git commit -m "Add new challenge: [title]"
   git push origin my-new-challenge
   ```
6. **Open a Pull Request** on GitHub and describe your contribution.

## Contributing Challenge Questions

Thank you for considering contributing to Daily Challenge! Here's how you can add new challenge questions.

### JSON File Structure

Challenge questions are stored in the `composeApp/src/commonMain/composeResources/files` directory. There are three types of challenges:

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

### True/False Challenge Format
```json
{
  "id": "unique_id",
  "question": "Your question text here",
  "correctAnswer": "true|false",
  "explanation": "Explanation text",
  "difficulty": "Easy|Medium|Hard"
}
```

### Multiple Choice Challenge Format
```json
{
   "question": "Your multiple choice question",
   "options": [
      "Option A",
      "Option B",
      "Option C",
      "Option D"
   ],
   "correctAnswer": "A|B|C|D",
   "explanation": "Explanation for the correct answer"
}
```

## 👤 Author

Created by **Nyi Nyi Zaw** (nyinyizaw.dev@gmail.com)