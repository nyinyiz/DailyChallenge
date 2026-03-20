package com.nyinyi.dailychallenge.feature.profile

import com.nyinyi.dailychallenge.data.model.FailedQuestionRecord
import com.nyinyi.dailychallenge.data.model.UserProfile

data class ProfileDashboard(
    val displayName: String,
    val initials: String,
    val levelLabel: String,
    val levelProgress: Float,
    val currentGoal: String,
    val streakDays: Int,
    val completedChallenges: Int,
    val reviewQueueCount: Int,
    val achievementCount: Int,
    val accuracyRate: Int,
    val weeklyActivity: List<ActivitySummary>,
    val achievements: List<AchievementPreview>,
    val focusAreas: List<ProfileInfoPoint>,
)

data class ActivitySummary(
    val dayLabel: String,
    val sessions: Int,
)

data class AchievementPreview(
    val title: String,
    val description: String,
    val unlocked: Boolean,
)

data class ProfileInfoPoint(
    val label: String,
    val value: String,
    val supportingText: String? = null,
)

internal fun buildProfileDashboard(userProfile: UserProfile): ProfileDashboard {
    val reviewQueueCount = userProfile.totalReviewItemCount
    val hasIdentity = userProfile.name.isNotBlank() || userProfile.email.isNotBlank()
    val completedChallenges = 18 + (reviewQueueCount * 2) + if (hasIdentity) 4 else 0
    val streakDays = (4 + (completedChallenges / 8) - (reviewQueueCount / 2)).coerceIn(3, 21)
    val accuracyRate = (92 - (reviewQueueCount * 4)).coerceIn(64, 96)
    val level = 1 + (completedChallenges / 12)
    val levelProgress = ((completedChallenges % 12) / 12f).coerceIn(0.14f, 0.96f)
    val displayName = userProfile.name.takeIf { it.isNotBlank() } ?: "Challenger"
    val goal =
        if (reviewQueueCount > 0) {
            "Clear $reviewQueueCount saved review ${pluralize(reviewQueueCount, "item", "items")} this week"
        } else {
            "Keep momentum with 3 fresh challenges this week"
        }

    val achievements =
        listOf(
            AchievementPreview(
                title = "Consistency Pulse",
                description = "$streakDays-day learning streak",
                unlocked = streakDays >= 5,
            ),
            AchievementPreview(
                title = "Review Zero",
                description = "Clear your saved review queue",
                unlocked = reviewQueueCount == 0,
            ),
            AchievementPreview(
                title = "Profile Ready",
                description = "Set up your learner identity",
                unlocked = userProfile.name.isNotBlank() && userProfile.email.isNotBlank(),
            ),
        )

    return ProfileDashboard(
        displayName = displayName,
        initials = buildInitials(displayName),
        levelLabel = "Level $level",
        levelProgress = levelProgress,
        currentGoal = goal,
        streakDays = streakDays,
        completedChallenges = completedChallenges,
        reviewQueueCount = reviewQueueCount,
        achievementCount = achievements.count { it.unlocked },
        accuracyRate = accuracyRate,
        weeklyActivity = buildWeeklyActivity(userProfile, reviewQueueCount),
        achievements = achievements,
        focusAreas = buildFocusAreas(userProfile, streakDays, accuracyRate),
    )
}

private fun buildWeeklyActivity(
    userProfile: UserProfile,
    reviewQueueCount: Int,
): List<ActivitySummary> {
    val seed =
        (userProfile.name + userProfile.email)
            .ifBlank { "DailyChallenge" }
            .sumOf { it.code }

    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    return dayLabels.mapIndexed { index, label ->
        val base = (seed + (index * 7)) % 4
        val reviewAdjustment = if (index == 1 || index == 4) 1 else 0
        val sessions = (base + reviewAdjustment + if (index < reviewQueueCount.coerceAtMost(3)) 1 else 0).coerceIn(0, 6)
        ActivitySummary(dayLabel = label, sessions = sessions)
    }
}

private fun buildFocusAreas(
    userProfile: UserProfile,
    streakDays: Int,
    accuracyRate: Int,
): List<ProfileInfoPoint> {
    val reviewGroups =
        listOf(
            "True / False" to userProfile.failedQuizCards,
            "Multiple Choice" to userProfile.failedMultipleChoiceQuestions,
            "Multiple Select" to userProfile.failedMultipleSelectQuestions,
            "Matching" to userProfile.failedMatchingGameQuestions,
        ).sortedByDescending { it.second.size }

    val topFocusArea = reviewGroups.firstOrNull { it.second.isNotEmpty() }

    val points =
        mutableListOf(
            ProfileInfoPoint(
                label = "Stability",
                value = "$accuracyRate% answer confidence",
                supportingText = "A healthy baseline for steady daily practice.",
            ),
            ProfileInfoPoint(
                label = "Consistency",
                value = "$streakDays days active",
                supportingText = "Short streaks become durable learning habits.",
            ),
        )

    if (topFocusArea != null) {
        points +=
            ProfileInfoPoint(
                label = "Focus area",
                value = topFocusArea.first,
                supportingText = "${topFocusArea.second.size} saved ${pluralize(topFocusArea.second.size, "question", "questions")} waiting for review.",
            )
    } else {
        points +=
            ProfileInfoPoint(
                label = "Focus area",
                value = "Fresh challenges",
                supportingText = "No review backlog. You can lean into harder practice.",
            )
    }

    return points
}

private fun buildInitials(name: String): String =
    name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString(separator = "") { it.first().uppercase() }
        .ifBlank { "DC" }

private fun pluralize(
    count: Int,
    singular: String,
    plural: String,
): String = if (count == 1) singular else plural

internal val UserProfile.totalReviewItemCount: Int
    get() =
        failedQuizCards.size +
            failedMultipleChoiceQuestions.size +
            failedMultipleSelectQuestions.size +
            failedMatchingGameQuestions.size

internal fun UserProfile.reviewGroups(): List<Pair<String, List<FailedQuestionRecord>>> =
    listOf(
        "True / False" to failedQuizCards,
        "Multiple Choice" to failedMultipleChoiceQuestions,
        "Multiple Select" to failedMultipleSelectQuestions,
        "Matching" to failedMatchingGameQuestions,
    ).filter { it.second.isNotEmpty() }
