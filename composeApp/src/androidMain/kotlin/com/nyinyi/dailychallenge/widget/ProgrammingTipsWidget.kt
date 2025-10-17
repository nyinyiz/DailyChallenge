package com.nyinyi.dailychallenge.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.nyinyi.dailychallenge.MainActivity
import com.nyinyi.dailychallenge.data.model.ProgrammingTip
import com.nyinyi.dailychallenge.data.remote.ChallengesApiService
import com.nyinyi.dailychallenge.data.repository.TipsRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ProgrammingTipsWidget : GlanceAppWidget() {
    private val httpClient by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    },
                )
            }
        }
    }

    private val apiService by lazy {
        ChallengesApiService(httpClient)
    }

    private val repository by lazy {
        TipsRepository(apiService)
    }

    override suspend fun provideGlance(
        context: Context,
        id: GlanceId,
    ) {
        val tip = repository.getRandomTip()

        provideContent {
            GlanceTheme {
                TipWidgetContent(tip = tip)
            }
        }
    }

    @Composable
    private fun TipWidgetContent(tip: ProgrammingTip) {
        Box(
            modifier =
                GlanceModifier
                    .fillMaxSize()
                    .background(GlanceTheme.colors.background)
                    .clickable(actionStartActivity<MainActivity>())
                    .padding(16.dp),
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        GlanceModifier
                            .background(GlanceTheme.colors.primaryContainer)
                            .cornerRadius(16.dp)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                ) {
                    Text(
                        text = tip.category,
                        style =
                            TextStyle(
                                color = GlanceTheme.colors.onPrimaryContainer,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                    )
                }

                Spacer(modifier = GlanceModifier.height(16.dp))

                Text(
                    text = tip.tip,
                    style =
                        TextStyle(
                            color = GlanceTheme.colors.onBackground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            textAlign = androidx.glance.text.TextAlign.Center,
                        ),
                    modifier =
                        GlanceModifier
                            .fillMaxWidth(),
                )

                Spacer(modifier = GlanceModifier.height(16.dp))

                Row(
                    modifier = GlanceModifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Tap to open app",
                        style =
                            TextStyle(
                                color = GlanceTheme.colors.onSurfaceVariant,
                                fontSize = 10.sp,
                            ),
                    )

                    Spacer(modifier = GlanceModifier.defaultWeight())

                    Text(
                        text = "Updates hourly",
                        style =
                            TextStyle(
                                color = GlanceTheme.colors.onSurfaceVariant,
                                fontSize = 10.sp,
                            ),
                    )
                }
            }
        }
    }
}
